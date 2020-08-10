package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Message;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse extends Message {

    private BasicUserInfoResponse basicInfo;
    private Boolean ofMe;

    public static MessageResponse build(Message message, InfoUser infoUser, Boolean ofMe) {
        MessageResponse messageResponse = new MessageResponse(BasicUserInfoResponse.build(infoUser), ofMe);
        messageResponse.setId(message.getId());
        messageResponse.setContent(message.getContent());
        messageResponse.setDateCreated(message.getDateCreated());
        messageResponse.setIdChatBox(message.getIdChatBox());
        messageResponse.setIdAuthor(message.getIdAuthor());

        return messageResponse;
    }

}
