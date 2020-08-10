package com.nguyenhai.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document
@TypeAlias("notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;
    private String idAuthor;
    private String idInteractive;
    private Type type;
    private Date dateCreated;
    private Boolean viewed;
    @DBRef private Post post;
    @DBRef private Comment comment;

    public enum Type {
        POST_READY, TAG, REACTIVE_POST, SHARE_POST, COMMENT_POST, COMMENT_TAG, REACTIVE_COMMENT, COMMENT_COMMENT
    }

    public static Notification build(Post post, Comment comment, String idAuthor, String idInteractive, Type type) {
        return Notification.builder()
                .id("N-" + UUID.randomUUID())
                .idAuthor(idAuthor)
                .idInteractive(idInteractive)
                .type(type)
                .post(post)
                .comment(comment)
                .dateCreated(new Date(System.currentTimeMillis()))
                .viewed(false).build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NotificationWrapper {

        private Notification notification;
        private Set<String> idsInteractive;
        private Date recentDay;
        private Boolean viewed;

        private InfoUser infoAuthorHolder;
        private InfoUser infoInteractiveHolder;
    }

    public String getLink() {
        String link = "post?id=" + post.getId();
        if (comment != null) {
            link += "&c=" + comment.getId();
        }
        return link;
    }

    public String getKeyNotificationResponse() {
        String key = type + post.getId();
        switch (type) {
            case COMMENT_POST:
                return key;
            case SHARE_POST:
                return type + post.getIdSharedPost();
        }
        if (comment != null && comment.getLevel() == 1) {
            return key + comment.getIdComment();
        }
        return comment != null ? key + comment.getId() : key;
    }
}
