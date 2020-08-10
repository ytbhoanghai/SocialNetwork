package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@TypeAlias("photo-group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoGroup {

    @Id
    private String id;
    private String postId;
    private String idAuthor;
    private List<String> photos;
    private Date dateCreated;

}
