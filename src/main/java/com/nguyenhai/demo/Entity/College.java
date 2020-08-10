package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("college")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class College {

    @Id
    private String id;
    private String urlAvatar;
    private String name;
    private String location;
    private String website;
}
