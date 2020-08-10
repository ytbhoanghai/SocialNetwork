package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Notification;
import com.nguyenhai.demo.Entity.Post;
import com.nguyenhai.demo.Exception.PostNotFoundException;
import com.nguyenhai.demo.Repository.PostRepository;
import com.nguyenhai.demo.Response.CardHistoryResponse;
import com.nguyenhai.demo.Service.HistoryService;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.NotificationService;
import com.nguyenhai.demo.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.nguyenhai.demo.Entity.Notification.Type.*;
import static com.nguyenhai.demo.Entity.Notification.Type.COMMENT_COMMENT;

@Service(value = "historyService")
public class HistoryServiceImpl implements HistoryService {

    private InfoUserService infoUserService;
    private NotificationService notificationService;
    private PostRepository postRepository;


    @Autowired
    public void setInfoUserService(InfoUserService infoUserService) {
        this.infoUserService = infoUserService;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService, PostRepository postRepository) {
        this.notificationService = notificationService;
        this.postRepository = postRepository;
    }

    public static final Map<Notification.Type, String> MAP_TYPE_NOTIFY_AND_MESSAGE = new HashMap<>();
    static {
        MAP_TYPE_NOTIFY_AND_MESSAGE.put(POST_READY, "You have created a post");
        MAP_TYPE_NOTIFY_AND_MESSAGE.put(REACTIVE_POST, "You were reacted on a post by %s");
        MAP_TYPE_NOTIFY_AND_MESSAGE.put(SHARE_POST, "You shared a post by %s");
        MAP_TYPE_NOTIFY_AND_MESSAGE.put(COMMENT_POST, "You have commented on a post by %s");
        MAP_TYPE_NOTIFY_AND_MESSAGE.put(REACTIVE_COMMENT, "You reacted about a comment from %s");
        MAP_TYPE_NOTIFY_AND_MESSAGE.put(COMMENT_COMMENT, "You answered a comment by %s");
    }

    @Override
    public List<CardHistoryResponse> getHistory(Integer page, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);

        Pageable pageable = PageRequest.of(page, number, Sort.by(Sort.Direction.DESC,"dateCreated"));
        return notificationService.findByIdInteractiveAndTypeIsNotIn(user.getId(), Arrays.asList(TAG, COMMENT_TAG), pageable)
                .stream()
                .map(notification -> new CardHistoryResponse(
                        notification.getId(),
                        getMessageForCardHistory(notification),
                        getLink(notification),
                        notification.getDateCreated()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CardHistoryResponse> getHistoryNext(String id, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        Notification notification = notificationService.findById(id);

        List<Notification> notificationList = notificationService.findByIdInteractiveAndTypeIsNotIn(user.getId(), Arrays.asList(TAG, COMMENT_TAG))
                .stream()
                .sorted((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()))
                .collect(Collectors.toList());

        List<CardHistoryResponse> result = new ArrayList<>();
        int index = notificationList.indexOf(notification);
        for (int i = index + 1; i < notificationList.size() && i < index + 1 + number; i++) {
            Notification n = notificationList.get(i);
            result.add(new CardHistoryResponse(n.getId(), getMessageForCardHistory(n), getLink(n), n.getDateCreated()));
        }

        return result;
    }

    private String getMessageForCardHistory(Notification notification) {
        String message = MAP_TYPE_NOTIFY_AND_MESSAGE.get(notification.getType());
        if (notification.getType() != POST_READY) {
            InfoUser user = infoUserService.findById(notification.getIdAuthor());
            return String.format(message, user.getFullName());
        }
        return message;
    }

    private String getLink(Notification notification) {
        if (notification.getType() == SHARE_POST) {
            String idOriginPost = notification.getPost().getIdSharedPost();
            Post post = postRepository.findById(idOriginPost)
                    .orElseThrow(() -> new PostNotFoundException(idOriginPost));

            return post.getLink();
        }
        return notification.getLink();
    }
}
