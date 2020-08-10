package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("workPlace")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPlace {

    @Id
    private String id;
    private String name;
    private String urlAvatar;
    private String website;

}
