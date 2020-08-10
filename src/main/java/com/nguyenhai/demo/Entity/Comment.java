package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@TypeAlias("comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    private String id;
    private String idAuthor;
    private String idPost;
    private String idComment;
    private String idCommentReply;
    private String content;
    private List<String> likes; // id user
    private List<String> replies; // id comment
    private Integer level; // 0 = first comment | 1 = reply comment
    private Date dateCreated;

}
