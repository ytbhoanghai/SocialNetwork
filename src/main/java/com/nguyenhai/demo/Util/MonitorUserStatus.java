package com.nguyenhai.demo.Util;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Service.InfoUserService;
import com.nguyenhai.demo.Service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sound.sampled.DataLine.Info;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class MonitorUserStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorUserStatus.class);
    private static final Integer TIME_KEEP_ALIVE = 180000; // 3p

//    private Map<String, Info> userMap;
    private RedisTemplate<Object, Object> redisTemplate;
    private NotificationService notificationService;
    private InfoUserService infoUserService;

    @Autowired
    public MonitorUserStatus(RedisTemplate<Object, Object> redisTemplate, NotificationService notificationService, InfoUserService infoUserService) {
        // this.userMap = new HashMap<>();
        this.redisTemplate = redisTemplate;
        this.notificationService = notificationService;
        this.infoUserService = infoUserService;
    }

    @Data
    @AllArgsConstructor
    public static class Info implements Serializable {
        private boolean isOnline;
        private Date lastAccess;
    }

    public enum Type {
        EMAIL, ID;
    }

    @Synchronized
    public void setIsOnline(String email) {
        InfoUser user = infoUserService.findByEmail(email);
        Date now = new Date(System.currentTimeMillis());
        if (redisTemplate.opsForHash().hasKey("userMap", user.getId())) {
            Info info = (Info) redisTemplate.opsForHash().get("userMap", user.getId());
            if (!Objects.requireNonNull(info).isOnline()) {
                notificationService.notificationUserOnline(
                        buildBasicUserInfoResponse(user, true, now),
                        getListEmailsFriends(user));
                LOGGER.info("user " + user.getId() + " online");
            }
        } else {
            notificationService.notificationUserOnline(
                    buildBasicUserInfoResponse(user, true, now),
                    getListEmailsFriends(user));
            LOGGER.info("user " + user.getId() + " online");
        }
        redisTemplate.opsForHash().put("userMap", user.getId(), build(true, now));
    }

    @Synchronized
    public void setIsOffline(String value, Type type) {
        InfoUser user = null;
        switch (type) {
            case EMAIL:
                user = infoUserService.findByEmail(value);
                break;
            case ID:
                user = infoUserService.findById(value);
        }

        Date dateTime = new Date(System.currentTimeMillis());
        if (redisTemplate.opsForHash().hasKey("userMap", user.getId())) {
            Info info = (Info) redisTemplate.opsForHash().get("userMap", user.getId());
            if (Objects.requireNonNull(info).isOnline()) {
                dateTime = info.getLastAccess();
                notificationService.notificationUserOffline(
                        buildBasicUserInfoResponse(user, false, dateTime),
                        getListEmailsFriends(user));
                LOGGER.info("user " + user.getId() + " offline");
            }
        } else {
            notificationService.notificationUserOffline(
                    buildBasicUserInfoResponse(user, false, dateTime),
                    getListEmailsFriends(user));
            LOGGER.info("user " + user.getId() + " offline");
        }
        redisTemplate.opsForHash().put("userMap", user.getId(), build(false, dateTime));
    }

    public Info getInfoById(String id) {
        if (redisTemplate.opsForHash().hasKey("userMap", id)) {
            return (Info) redisTemplate.opsForHash().get("userMap", id);
        }
        return null;
    }

    @Synchronized
    @Scheduled(fixedDelay = 3 * 60 * 1000, initialDelay = 10 * 1000)
    private void writeOfflineStatus() {
        redisTemplate.opsForHash().entries("userMap")
                .forEach((k, v) -> {
                    Info info = (Info) v;
                    if (info.isOnline()) {
                        long diff = System.currentTimeMillis() - info.getLastAccess().getTime();
                        if (diff > TIME_KEEP_ALIVE) {
                            setIsOffline((String) k, Type.ID);
                        }
                    } else {
                        InfoUser user = infoUserService.findById((String) k);
                        notificationService.notificationUserOffline(
                                buildBasicUserInfoResponse(user, false, info.getLastAccess()),
                                getListEmailsFriends(user));
                    }
                });
    }

    private Info build(boolean isOnline, Date date) {
        return new Info(isOnline, date);
    }

    private List<String> getListEmailsFriends(InfoUser infoUser) {
        return infoUserService.findByIdIsIn(
                new ArrayList<>(infoUser.getCurrentListFriendInfo().keySet()))
                .stream()
                .map(InfoUser::getEmail)
                .collect(Collectors.toList());
    }

    private BasicUserInfoResponse buildBasicUserInfoResponse(InfoUser infoUser, boolean isOnline, Date lastAccess) {
        BasicUserInfoResponse response = BasicUserInfoResponse.build(infoUser);
        response.setIsOnline(isOnline);
        response.setLastAccess(lastAccess);
        return response;
    }
}
