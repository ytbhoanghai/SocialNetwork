package com.nguyenhai.demo.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private String id;
    private BasicUserInfoResponse infoAuthor;
    private BasicUserInfoResponse infoUserOrigin;
    private String content;
    private Date dateCreated;
    private Boolean iLiked;
    private Boolean ofMe;
    private Integer numberCommentsInside;
    private Integer numberLiked;
    private List<BasicUserInfoResponse> someUserLiked;

}
