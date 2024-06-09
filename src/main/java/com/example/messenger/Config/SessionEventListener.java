package com.example.messenger.Config;

import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener {

    @EventListener
    public void processSessionCreatedEvent(SessionCreatedEvent event) {
        // do the necessary work
    }

    @EventListener
    public void processSessionDeletedEvent(SessionDeletedEvent event) {
        // do the necessary work
    }

    @EventListener
    public void processSessionDestroyedEvent(SessionDestroyedEvent event) {
        // do the necessary work
    }

    @EventListener
    public void processSessionExpiredEvent(SessionExpiredEvent event) {
        // do the necessary work
    }

}