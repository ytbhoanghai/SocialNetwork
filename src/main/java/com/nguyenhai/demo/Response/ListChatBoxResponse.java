package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListChatBoxResponse {

    private List<ChatBoxResponse> chatBoxResponses;
    private Integer numberUnSeen;

}
