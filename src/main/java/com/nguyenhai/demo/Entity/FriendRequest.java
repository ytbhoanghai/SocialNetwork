package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Document
@TypeAlias("friendRequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @Id
    private String id;
    private HashMap<String, Request> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String id;
        private Boolean viewed;
        private Date dateRequest;

    }

}
