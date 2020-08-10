package com.nguyenhai.demo.Service.Implement;

import com.nguyenhai.demo.Entity.Comment;
import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Entity.Notification;
import com.nguyenhai.demo.Entity.Post;
import com.nguyenhai.demo.Exception.InfoUserNotFoundException;
import com.nguyenhai.demo.Exception.NotificationNotFoundException;
import com.nguyenhai.demo.Repository.NotificationRepository;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.NotificationResponse;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "notificationService")
public class NotificationServiceImpl implements NotificationService {

    private SimpMessageSendingOperations messageSendingOperations;
    private InfoUserService infoUserService;
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(SimpMessageSendingOperations messageSendingOperations,
                                   InfoUserService infoUserService,
                                   NotificationRepository notificationRepository) {

        this.messageSendingOperations = messageSendingOperations;
        this.infoUserService = infoUserService;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void notificationNewFriendRequest(String idFriendRequest, String email) {
        messageSendingOperations
                .convertAndSendToUser(email, "/queue/notification.new-friend-request", idFriendRequest);
    }

    @Override
    public void notificationDeleteFriendRequest(String idFriendRequest, String email) {
        messageSendingOperations
                .convertAndSendToUser(email, "/queue/notification.delete-friend-request", idFriendRequest);
    }

    @Override
    public void notificationUserOnline(BasicUserInfoResponse response, List<String> emails) {
        emails.forEach(s -> {
            messageSendingOperations
                    .convertAndSendToUser(s, "/queue/notification.friend/online", response);
        });
    }

    @Override
    public void notificationUserOffline(BasicUserInfoResponse response, List<String> emails) {
        emails.forEach(s -> {
            messageSendingOperations
                    .convertAndSendToUser(s, "/queue/notification.friend/offline", response);
        });
    }

    @Override
    public void notificationAboutPost(String idNotification, String email) {
        messageSendingOperations
                .convertAndSendToUser(email, "/queue/notification.post", idNotification);
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public NotificationResponse.NotificationResponseWrapper getNotifications(Integer page, Integer number, String email) {
        InfoUser user = infoUserService.findByEmail(email);
        List<Notification> notifications = notificationRepository.findByIdAuthor(user.getId());
        notifications.sort((o1, o2) -> o2.getDateCreated().compareTo(o1.getDateCreated()));

        HashMap<String, Notification.NotificationWrapper> groupNotifications = new HashMap<>();
        notifications.forEach(notification -> {
            String key = notification.getKeyNotificationResponse();

            if (!groupNotifications.containsKey(key)) {
                InfoUser userInteractive = null;
                try {
                    userInteractive = infoUserService.findById(notification.getIdInteractive());
                } catch (InfoUserNotFoundException ignore) { }

                Set<String> idsInteractive = new HashSet<>();
                idsInteractive.add(notification.getIdInteractive());
                Notification.NotificationWrapper wrapper = Notification.NotificationWrapper.builder()
                        .notification(notification)
                        .idsInteractive(idsInteractive)
                        .recentDay(notification.getDateCreated())
                        .viewed(notification.getViewed())
                        .infoAuthorHolder(user)
                        .infoInteractiveHolder(userInteractive).build();

                groupNotifications.put(key, wrapper);
            } else {
                Notification.NotificationWrapper wrapper = groupNotifications.get(key);
                wrapper.getIdsInteractive().add(notification.getIdInteractive());
                if (!notification.getViewed()) {
                    wrapper.setViewed(false);
                }
            }
        });

        List<NotificationResponse> notificationResponses = groupNotifications.values().stream()
                .map(this::buildNotificationResponse)
                .sorted((o1, o2) -> o2.getDateCreated().compareTo(Objects.requireNonNull(o1).getDateCreated()))
                .skip(number * page).limit(number)
                .collect(Collectors.toList());

        Integer numberUnViewed = Long.valueOf(groupNotifications.values().stream()
                .filter(notificationWrapper -> !notificationWrapper.getViewed())
                .count()).intValue();

        return new NotificationResponse.NotificationResponseWrapper(notificationResponses, numberUnViewed);
    }

    @Override
    public List<Notification> findByIdInteractiveAndTypeIsNotIn(String idInteractive, List<Notification.Type> types, Pageable pageable) {
        return notificationRepository.findByIdInteractiveAndTypeIsNotIn(idInteractive, types, pageable);
    }

    @Override
    public void deleteByTypeAndIdInteractiveAndPost(Notification.Type type, String idInteracted, Post post) {
        notificationRepository.deleteByTypeAndIdInteractiveAndPost(type, idInteracted, post);
    }

    @Override
    public void deleteByTypeAndIdInteractiveAndPostAndComment(Notification.Type type, String idInteracted, Post post, Comment comment) {
        notificationRepository.deleteByTypeAndIdInteractiveAndPostAndComment(type, idInteracted, post, comment);
    }

    @Override
    public void deleteByIdAuthorAndPostAndComment(String idAuthor, Post post, Comment comment) {
        notificationRepository.deleteByIdAuthorAndPostAndComment(idAuthor, post, comment);
    }

    @Override
    public void deleteByPost(Post post) {
        notificationRepository.deleteByPost(post);
    }

    @Override
    public void deleteByIdInteractiveAndPostAndComment(String idInteractive, Post post, Comment comment) {
        notificationRepository.deleteByIdInteractiveAndPostAndComment(idInteractive, post, comment);
    }

    @Override
    public long updateViewedNotifications(List<String> ids, String email) {
        List<Notification> temp = new ArrayList<>();
        ids.forEach(s -> {
            Notification temp1 = notificationRepository.findById(s)
                    .orElseThrow(() -> new NotificationNotFoundException(s));
            temp.addAll(
                    notificationRepository.findByTypeAndIdAuthorAndPost(
                            temp1.getType(),
                            temp1.getIdAuthor(),
                            temp1.getPost()
                    )
            );

        });
        temp.forEach(notification -> notification.setViewed(true));

        notificationRepository.saveAll(temp);

        return getNotifications(0, 999, email).getNumberUnViewed();
    }

    @Override
    public long getNumberNotificationsUnViewed(String email) {
        return getNotifications(0, 999, email).getNumberUnViewed();
    }

    @Override
    public Notification findById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @Override
    public List<Notification> findByIdInteractiveAndTypeIsNotIn(String idInteractive, List<Notification.Type> types) {
        return notificationRepository.findByIdInteractiveAndTypeIsNotIn(idInteractive, types);
    }

    private NotificationResponse buildNotificationResponse(Notification.NotificationWrapper notificationWrapper) {
        Notification notification = notificationWrapper.getNotification();
        switch (notification.getType()) {
            case POST_READY:
                return NotificationResponse.build_POST_READY(notificationWrapper);
            case TAG:
                return NotificationResponse.build_TAG(notificationWrapper);
            case REACTIVE_POST:
                return NotificationResponse.build_REACTIVE_POST(notificationWrapper);
            case SHARE_POST:
                return NotificationResponse.build_SHARE_POST(notificationWrapper);
            case COMMENT_POST:
                return NotificationResponse.build_COMMENT_POST(notificationWrapper);
            case COMMENT_TAG:
                return NotificationResponse.build_COMMENT_TAG(notificationWrapper);
            case REACTIVE_COMMENT:
                return NotificationResponse.build_REACTIVE_COMMENT(notificationWrapper);
            case COMMENT_COMMENT:
                return NotificationResponse.build_COMMENT_COMMENT(notificationWrapper);
        }
        return null;
    }
}
