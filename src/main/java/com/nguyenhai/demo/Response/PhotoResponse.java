package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoResponse {

    private String url;
    private String postLink;
    private Integer numberReacted;
    private Integer numberComment;
    private Integer numberShared;

    public static PhotoResponse build(String url, String postLink, HashMap<String, String> othersInfo) {
        return PhotoResponse.builder()
                .url(url)
                .postLink(postLink)
                .numberReacted(Integer.valueOf(othersInfo.get("numberFeelings")))
                .numberComment(Integer.valueOf(othersInfo.get("numberComments")))
                .numberShared(Integer.valueOf(othersInfo.get("numberShares"))).build();
    }

}
