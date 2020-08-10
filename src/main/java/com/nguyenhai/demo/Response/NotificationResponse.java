package com.nguyenhai.demo.Response;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Notification;
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
public class NotificationResponse {

    private String id;
    private String content;
    private String link;
    private String objectName;
    private String objectId;
    private String picture;
    private Date dateCreated;
    private Boolean viewed;

    public static NotificationResponse build_POST_READY(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoAuthorHolder();
        String content = "Your post is ready, click here to view";
        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_TAG(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();
        Notification notification = notificationWrapper.getNotification();

        String content = "tagged you in a post";
        if (notification.getPost().getTagFriends().size() > 1) {
            Integer arg2 = notification.getPost().getTagFriends().size() - 1;
            content = String.format("tagged you and %d others in a post", arg2);
        }
        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_REACTIVE_POST(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();

        String content = "has reactive one of your posts";
        if (notificationWrapper.getIdsInteractive().size() > 1) {
            Integer arg2 = notificationWrapper.getIdsInteractive().size() - 1;
            content = String.format("and %d others have reactive one of your posts", arg2);
        }

        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_SHARE_POST(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();

        String content = "shared a post of yours";
        if (notificationWrapper.getIdsInteractive().size() > 1) {
            Integer arg2 = notificationWrapper.getIdsInteractive().size() - 1;
            content = String.format("and %s others shared a post of yours", arg2);
        }

        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_COMMENT_POST(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();

        String content = "commented on a post of you";
        if (notificationWrapper.getIdsInteractive().size() > 1) {
            Integer arg2 = notificationWrapper.getIdsInteractive().size() - 1;
            content = String.format("and %d others commented on one of your posts", arg2);
        }
        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_COMMENT_TAG(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();

        String content = "commented on a post you are tagged in";
        if (notificationWrapper.getIdsInteractive().size() > 1) {
            Integer arg2 = notificationWrapper.getIdsInteractive().size() - 1;
            content = String.format("and %d others commented on a post you are tagged in", arg2);
        }
        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_REACTIVE_COMMENT(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();

        String content = "has reactive one of your comments";
        if (notificationWrapper.getIdsInteractive().size() > 1) {
            Integer arg2 = notificationWrapper.getIdsInteractive().size() - 1;
            content = String.format("and %d others have reactive one of your comments", arg2);
        }

        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }

    public static NotificationResponse build_COMMENT_COMMENT(Notification.NotificationWrapper notificationWrapper) {
        InfoUser object = notificationWrapper.getInfoInteractiveHolder();

        String content = "replied to your comment in a post";
        if (notificationWrapper.getIdsInteractive().size() > 1) {
            Integer arg2 = notificationWrapper.getIdsInteractive().size() - 1;
            content = String.format("and %d others replied to your comment in a post", arg2);
        }

        return build(notificationWrapper, content, object.getFullName(), object.getId(), object.getUrlAvatar());
    }


    private static NotificationResponse build(Notification.NotificationWrapper notificationWrapper, String content, String objectName, String objectId, String urlAvatar) {
        Notification notification = notificationWrapper.getNotification();
        return NotificationResponse.builder()
                .id(notification.getId())
                .content(content)
                .link(notification.getLink())
                .objectName(objectName)
                .objectId(objectId)
                .picture(urlAvatar)
                .dateCreated(notificationWrapper.getRecentDay())
                .viewed(notificationWrapper.getViewed()).build();
    }

    @Data
    @AllArgsConstructor
    public static class NotificationResponseWrapper {
        private List<NotificationResponse> notificationResponses;
        private Integer numberUnViewed;
    }

}
