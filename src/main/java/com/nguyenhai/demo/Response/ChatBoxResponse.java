package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.ChatBox;
import com.nguyenhai.demo.Entity.InfoUser;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBoxResponse {

    private String id;
    private BasicDetailsUserInfoResponse basicInfo;
    private Date dateCreate;
    private String latestContent;
    private Integer numberUnSeen;
    private Boolean isBlocked;

    public static ChatBoxResponse build(ChatBox chatBox, InfoUser infoUser, Boolean isOnline, Boolean isBlocked) {
        return ChatBoxResponse.builder()
                .id(chatBox.getId())
                .dateCreate(chatBox.getDateCreated())
                .latestContent(chatBox.getLatestContent())
                .numberUnSeen(chatBox.getUnSeen())
                .basicInfo(BasicDetailsUserInfoResponse.build(infoUser, isOnline))
                .isBlocked(isBlocked).build();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class BasicDetailsUserInfoResponse extends BasicUserInfoResponse {

        private String favoriteQuotes;
        private String mobile;
        private Date birthDay;
        private Boolean gender;

        public static BasicDetailsUserInfoResponse build(InfoUser infoUser, Boolean isOnline) {
            BasicDetailsUserInfoResponse infoResponse =
                    new BasicDetailsUserInfoResponse(
                            infoUser.getFavoriteQuotes() != null ? infoUser.getFavoriteQuotes() : "",
                            infoUser.getMobile(),
                            infoUser.getBirthDay(),
                            infoUser.getGender());
            infoResponse.setId(infoUser.getId());
            infoResponse.setFirstName(infoUser.getFirstName());
            infoResponse.setLastName(infoUser.getLastName());
            infoResponse.setUrlAvatar(infoUser.getUrlAvatar());
            infoResponse.setIsOnline(isOnline);

            return infoResponse;
        }
    }
}
