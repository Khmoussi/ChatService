package com.example.messenger.Config;

import com.example.messenger.POJO.Enums.MessageType;
import com.example.messenger.POJO.TestMessage;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@AllArgsConstructor
public class ClientDisconnectEventListener {
private final SimpMessageSendingOperations simpMessageSendingOperations;
@EventListener
public void handleWebsocketDisconnectListener(SessionDisconnectEvent event){
StompHeaderAccessor headerAccessor =StompHeaderAccessor.wrap(event.getMessage());
String username=(String)headerAccessor.getSessionAttributes().get("username");
        TestMessage msg=TestMessage.builder()
                .content(username+" has left")
                .type(MessageType.Leave)
                .sender(username)
                .build();
        simpMessageSendingOperations.convertAndSend("/topic/public",msg);


    }
}
