package com.nguyenhai.demo.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class RedisSessionCreatedEventListener implements ApplicationListener<SessionCreatedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisSessionCreatedEventListener.class);

    @Override
    public void onApplicationEvent(SessionCreatedEvent sessionCreatedEvent) {
        LOGGER.info("created session with id " + sessionCreatedEvent.getSessionId());
    }
}
