package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.Feeling;
import com.nguyenhai.demo.Entity.PlaceLived;
import com.nguyenhai.demo.Entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {

    private String id;
    private BasicUserInfoResponse infoAuthor;
    private String content;
    private Post.Type type;
    private Date dateCreated;
    private List<String> photos;
    private List<BasicUserInfoResponse> tagFriends;
    private Feeling feeling;
    private PlaceLived checkin;
    private Post.Action myAction;
    private Boolean ofMe;
    private HashMap<String, String> othersInfo; // arg1, numberShares, numberComments, numberFeelings

}
