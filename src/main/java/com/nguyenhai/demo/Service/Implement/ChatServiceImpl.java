package com.nguyenhai.demo.Service.Implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenhai.demo.Entity.ChatBox;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Message;
import com.nguyenhai.demo.Exception.ChatBoxNotFoundException;
import com.nguyenhai.demo.Exception.MessageNotFoundException;
import com.nguyenhai.demo.Repository.ChatBoxRepository;
import com.nguyenhai.demo.Repository.MessageRepository;
import com.nguyenhai.demo.Response.ChatBoxResponse;
import com.nguyenhai.demo.Response.ListChatBoxResponse;
import com.nguyenhai.demo.Response.MessageResponse;
import com.nguyenhai.demo.Service.ChatService;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Util.MonitorUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "chatService")
public class ChatServiceImpl implements ChatService {

    private InfoUserService infoUserService;
    private ChatBoxRepository chatBoxRepository;
    private MessageRepository messageRepository;
    private MonitorUserStatus monitorUserStatus;
    private SimpMessageSendingOperations messageSendingOperations;

    @Autowired
    public ChatServiceImpl(InfoUserService infoUserService,
                           ChatBoxRepository chatBoxRepository,
                           MessageRepository messageRepository,
                           MonitorUserStatus monitorUserStatus,
                           SimpMessageSendingOperations messageSendingOperations) {

        this.infoUserService = infoUserService;
        this.chatBoxRepository = chatBoxRepository;
        this.messageRepository = messageRepository;
        this.monitorUserStatus = monitorUserStatus;
        this.messageSendingOperations = messageSendingOperations;
    }

    @Override
    public ChatBoxResponse openChatBox(String idUser, String email) {
        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(idUser);

        Boolean isBlocked = me.getBlockedListFriendInfo().containsKey(user.getId());

        // get status online | offline of user
        MonitorUserStatus.Info info = monitorUserStatus.getInfoById(user.getId());
        boolean isOnline = info != null && info.isOnline();
        if (user.getBlockedListFriendInfo().containsKey(me.getId())) {
            isOnline = false;
        }

        Optional<ChatBox> optionalChatBox = chatBoxRepository
                .findByIdAuthorAndIdUser(me.getId(), user.getId());
        if (optionalChatBox.isPresent()) {

            ChatBox chatBox = optionalChatBox.get();
            return ChatBoxResponse.build(chatBox, user, isOnline, isBlocked);
        }

        ChatBox chatBox = chatBoxRepository.save(ChatBox.createNewChatBox(me.getId(), user.getId()));

        return ChatBoxResponse.build(chatBox, user, isOnline, isBlocked);
    }

    @Override
    public List<ChatBoxResponse> getAllChatBox(String email) {
        InfoUser user = infoUserService.findByEmail(email);
        return chatBoxRepository.findAllByIdAuthor(
                user.getId(), Sort.by(Sort.Direction.DESC, "lastAccess")).stream()
                .map(chatBox -> {
                    MonitorUserStatus.Info info = monitorUserStatus.getInfoById(chatBox.getIdUser());
                    boolean isOnline = info != null && info.isOnline();

                    InfoUser infoUser = infoUserService.findById(chatBox.getIdUser());

                    Boolean isBlocked = user.getBlockedListFriendInfo().containsKey(infoUser.getId());

                    if (infoUser.getBlockedListFriendInfo().containsKey(user.getId())) {
                        isOnline = false;
                    }


                    return ChatBoxResponse.build(chatBox, infoUser, isOnline, isBlocked);
                })/*.sorted(Comparator.comparing(ChatBoxResponse::getNumberUnSeen).reversed())*/
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getMessages(String idChatBox, String fromMessage, Integer number, String email) {
        ChatBox chatBox = chatBoxRepository.findById(idChatBox)
                .orElseThrow(() -> new ChatBoxNotFoundException(idChatBox));

        InfoUser authorChatBox = infoUserService.findByEmail(email);
        Assert.isTrue(chatBox.getIdAuthor().equals(authorChatBox.getId()), "Access Denied !!!");

        InfoUser userChatBox = infoUserService.findById(chatBox.getIdUser());

        List<Message> messages = messageRepository
                .findAllByIdChatBox(chatBox.getId(), Sort.by(Sort.Direction.DESC, "dateCreated"));

        int start = 0;
        if (!fromMessage.equals("ytb")) {
            Message from = messageRepository.findById(fromMessage)
                    .orElseThrow(() -> new MessageNotFoundException(fromMessage));

            start = messages.indexOf(from) + 1;
        }

        List<MessageResponse> messageResponses = new ArrayList<>();
        for (int i = start; i < messages.size() && i < start + number; i++) {
            Message temp = messages.get(i);
            Boolean ofMe = temp.getIdAuthor().equals(authorChatBox.getId());
            if (temp.getIdAuthor().equals(authorChatBox.getId())) {
                messageResponses.add(MessageResponse.build(temp, authorChatBox, ofMe));
            } else {
                messageResponses.add(MessageResponse.build(temp, userChatBox, ofMe));
            }
        }

        chatBox.setUnSeen(0);
        chatBoxRepository.save(chatBox);

        return messageResponses;
    }

    @Override
    public MessageResponse createMessage(String idChatBox, String content, String email) throws JsonProcessingException {
        ChatBox chatBox = chatBoxRepository.findById(idChatBox)
                .orElseThrow(() -> new ChatBoxNotFoundException(idChatBox));

        InfoUser me = infoUserService.findByEmail(email);
        InfoUser user = infoUserService.findById(chatBox.getIdUser());

        Assert.isTrue(chatBox.getIdAuthor().equals(me.getId()), "Access Denied !!!");
        Assert.isTrue(!user.getBlockedListFriendInfo().containsKey(me.getId()), "info user not found by id " + user.getId());
        Assert.isTrue(!me.getBlockedListFriendInfo().containsKey(user.getId()), "you have blocked this user");

        ChatBoxResponse chatBoxResponse = openChatBox(me.getId(), user.getEmail());
        ChatBox chatBox1 = chatBoxRepository.findById(chatBoxResponse.getId())
                .orElseThrow(() -> new ChatBoxNotFoundException(chatBoxResponse.getId()));

        updateChatBox(chatBox1, 1, new Date(), content);
        updateChatBox(chatBox, 0, chatBox.getLastAccess(), content);

        Date now = new Date();
        Message message = pushMessageToChatBox(chatBox.getId(), now, content, me.getId());
        Message message1 = pushMessageToChatBox(chatBox1.getId(), now, content, me.getId());

        sendNewMessageToUser(MessageResponse.build(message1, me, false), user.getEmail());

        return MessageResponse.build(message, me, true);
    }

    @Override
    public Boolean seen(String idChatBox, Integer number, String email) {
        ChatBox chatBox = chatBoxRepository.findById(idChatBox)
                .orElseThrow(() -> new ChatBoxNotFoundException(idChatBox));
        InfoUser infoAuthor = infoUserService.findByEmail(email);

        Assert.isTrue(chatBox.getIdAuthor().equals(infoAuthor.getId()), "Access Denied !!!");

        if (chatBox.getUnSeen() - number < 0) {
            return false;
        }
        chatBox.addUnSeen(number * -1);
        chatBoxRepository.save(chatBox);
        return true;
    }

    // have hard code
    @Override
    public ListChatBoxResponse getNextListChatBox(String idChatBox, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        List<ChatBox> chatBoxes = chatBoxRepository
                .findAllByIdAuthorAndUnSeenGreaterThan(user.getId(), 0, Sort.by(Sort.Direction.DESC, "lastAccess"));

        int index = 0;
        if (!idChatBox.equals("ytb")) {
            ChatBox chatBox = chatBoxRepository.findById(idChatBox)
                    .orElseThrow(() -> new ChatBoxNotFoundException(idChatBox));
            index = chatBoxes.indexOf(chatBox) + 1;
        }

        List<ChatBoxResponse> chatBoxResponses = new ArrayList<>();
        int countingUnSeen = 0;
        for (int i = 0; i < chatBoxes.size(); i++) {
            ChatBox chatBox = chatBoxes.get(i);
            if (i >= index && i < index + number) {
                MonitorUserStatus.Info info = monitorUserStatus.getInfoById(chatBox.getIdUser());
                boolean isOnline = info != null && info.isOnline();

                InfoUser infoUser = infoUserService.findById(chatBox.getIdUser());

                Boolean isBlocked = user.getBlockedListFriendInfo().containsKey(infoUser.getId());

                if (infoUser.getBlockedListFriendInfo().containsKey(user.getId())) {
                    isOnline = false;
                }

                chatBoxResponses.add(ChatBoxResponse.build(chatBox, infoUser, isOnline, isBlocked));
            }
            if (chatBox.getUnSeen() > 0) {
                countingUnSeen += 1;
            }
        }

        return new ListChatBoxResponse(chatBoxResponses, countingUnSeen);
    }

    @Override
    public Integer getNumberMessagesUnSeen(String email) {
        InfoUser user = infoUserService.findByEmail(email);
        List<ChatBox> chatBoxes = chatBoxRepository
                .findAllByIdAuthorAndUnSeenGreaterThan(user.getId(), 0, Sort.by(Sort.Direction.DESC, "lastAccess"));

        return chatBoxes.stream().mapToInt(ChatBox::getUnSeen).sum();
    }

    @Override
    public void deleteChatBox(String idUser, String email) {
        InfoUser user1 = infoUserService.findByEmail(email);
        InfoUser user2 = infoUserService.findById(idUser);

        chatBoxRepository.findByIdAuthorAndIdUser(user1.getId(), user2.getId())
                .ifPresent(chatBox -> {
                    messageRepository.deleteByIdChatBox(chatBox.getId());
                    chatBoxRepository.delete(chatBox);
                });
    }

    private void updateChatBox(ChatBox chatBox, Integer seen, Date lastAccess, String latestContent) {
        chatBox.addUnSeen(seen);
        chatBox.setLastAccess(lastAccess);
        chatBox.setLatestContent(latestContent);
        chatBoxRepository.save(chatBox);
    }

    private Message pushMessageToChatBox(String idChatBox, Date dateCreated, String content, String idAuthor) {
        Message message = Message.builder()
                .id(UUID.randomUUID().toString())
                .idChatBox(idChatBox)
                .content(content)
                .idAuthor(idAuthor)
                .dateCreated(dateCreated).build();

        return messageRepository.save(message);
    }

    @Async("notificationExecutor")
    public void sendNewMessageToUser(MessageResponse messageResponse, String email) throws JsonProcessingException {
        messageSendingOperations
                .convertAndSendToUser(
                        email,
                        "/queue/notification/messages",
                        new ObjectMapper().writeValueAsString(messageResponse));
    }
}
