package com.nguyenhai.demo.Listener;

import com.nguyenhai.demo.Util.MonitorUserStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.Session;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class RedisSessionDestroyedEventListener implements ApplicationListener<SessionDestroyedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionDestroyedEventListener.class);

    private SpringSessionBackedSessionRegistry<Session> sessionRegistry;
    private MonitorUserStatus monitorUserStatus;

    @Autowired
    public RedisSessionDestroyedEventListener(SpringSessionBackedSessionRegistry<Session> sessionRegistry, MonitorUserStatus monitorUserStatus) {
        this.sessionRegistry = sessionRegistry;
        this.monitorUserStatus = monitorUserStatus;
    }

    @Override
    public void onApplicationEvent(@NotNull SessionDestroyedEvent sessionDestroyedEvent) {
        String subEvent = sessionDestroyedEvent instanceof SessionExpiredEvent
                ? SessionExpiredEvent.class.getName()
                : SessionDeletedEvent.class.getName();

        UserDetails principal = (UserDetails) sessionRegistry
                .getSessionInformation(sessionDestroyedEvent.getSessionId()).getPrincipal();
        String email = principal.getUsername();

        //monitorUserStatus.setIsOffline(email, MonitorUserStatus.Type.EMAIL);

        LOGGER.info("destroyed session " + sessionDestroyedEvent.getSessionId() + " - " + subEvent);
    }
}
