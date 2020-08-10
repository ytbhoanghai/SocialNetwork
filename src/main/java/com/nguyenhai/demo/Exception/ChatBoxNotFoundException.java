package com.nguyenhai.demo.Exception;

public class ChatBoxNotFoundException extends RuntimeException {
    public ChatBoxNotFoundException(String idChatBox) {
        super("chat box not found by id " + idChatBox);
    }
}
