package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.InfoUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicUserInfoResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String urlAvatar;
    private String urlBackground;
    private String statusForMe;
    private Integer numberFriends;
    private Boolean isValidEmail;
    private Boolean isFollowing;
    private Boolean isOnline;
    private Date lastAccess;

    public static BasicUserInfoResponse build(InfoUser infoUser) {
        return BasicUserInfoResponse.builder()
                .id(infoUser.getId())
                .firstName(infoUser.getFirstName())
                .lastName(infoUser.getLastName())
                .urlAvatar(infoUser.getUrlAvatar())
                .urlBackground(infoUser.getUrlBackground())
                .numberFriends(infoUser.getCurrentListFriendInfo().size())
                .isValidEmail(infoUser.getIsValidEmail()).build();
    }

}
