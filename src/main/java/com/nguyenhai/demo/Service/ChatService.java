package com.nguyenhai.demo.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nguyenhai.demo.Response.ChatBoxResponse;
import com.nguyenhai.demo.Response.ListChatBoxResponse;
import com.nguyenhai.demo.Response.MessageResponse;

import java.util.List;

public interface ChatService {

    ChatBoxResponse openChatBox(String idUser, String email);

    @Deprecated // nên dùng theo phân trang
    List<ChatBoxResponse> getAllChatBox(String email);

    List<MessageResponse> getMessages(String idChatBox, String fromMessage, Integer number, String email);

    MessageResponse createMessage(String idChatBox, String content, String email) throws JsonProcessingException;

    Boolean seen(String idChatBox, Integer number, String email);

    @Deprecated
    ListChatBoxResponse getNextListChatBox(String idChatBox, Integer number, String email);

    Integer getNumberMessagesUnSeen(String email);

    void deleteChatBox(String idUser, String email);
}
