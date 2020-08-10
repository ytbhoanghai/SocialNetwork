package com.nguyenhai.demo.Service;

import com.nguyenhai.demo.Entity.Comment;
import com.nguyenhai.demo.Entity.Notification;
import com.nguyenhai.demo.Entity.Post;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.NotificationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface NotificationService {

    @Async("notificationExecutor")
    void notificationNewFriendRequest(String idFriendRequest, String email);

    @Async("notificationExecutor")
    void notificationDeleteFriendRequest(String idFriendRequest, String email);

    @Async("notificationExecutor")
    void notificationUserOnline(BasicUserInfoResponse response, List<String> emails);

    @Async("notificationExecutor")
    void notificationUserOffline(BasicUserInfoResponse response, List<String> emails);

    @Async("notificationExecutor")
    void notificationAboutPost(String idNotification, String email);

    void save(Notification notification);

    void deleteByTypeAndIdInteractiveAndPost(Notification.Type type, String idInteracted, Post post);

    void deleteByTypeAndIdInteractiveAndPostAndComment(Notification.Type type, String idInteracted, Post post, Comment comment);

    void deleteByIdAuthorAndPostAndComment(String idAuthor, Post post, Comment comment);

    void deleteByPost(Post post);

    void deleteByIdInteractiveAndPostAndComment(String idInteractive, Post post, Comment comment);

    long updateViewedNotifications(List<String> ids, String email);

    long getNumberNotificationsUnViewed(String email);

    Notification findById(String id);

    List<Notification> findByIdInteractiveAndTypeIsNotIn(String idInteractive, List<Notification.Type> types);

    List<Notification> findByIdInteractiveAndTypeIsNotIn(String idInteractive, List<Notification.Type> types, Pageable pageable);

    NotificationResponse.NotificationResponseWrapper getNotifications(Integer page, Integer number, String email);
}
