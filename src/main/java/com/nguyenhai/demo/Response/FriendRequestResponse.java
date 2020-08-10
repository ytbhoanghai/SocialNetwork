package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.FriendRequest;
import com.nguyenhai.demo.Entity.InfoUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestResponse {

    private String idUserRequest;
    private String picture;
    private String fullName;
    private Integer numberFriends;
    private Boolean viewed;

    public static FriendRequestResponse build(InfoUser infoUser, Boolean viewed) {
        return FriendRequestResponse.builder()
                .idUserRequest(infoUser.getId())
                .picture(infoUser.getUrlAvatar())
                .fullName(infoUser.getFullName())
                .numberFriends(infoUser.getCurrentListFriendInfo().size())
                .viewed(viewed).build();

    }
}
