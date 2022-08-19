package com.saltlux.deepsignal.web.api.websocket;

import com.saltlux.deepsignal.web.api.websocket.dto.NotificationDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@Log4j2
public class WebsocketController implements ApplicationListener<SessionDisconnectEvent> {

    private final SimpMessageSendingOperations messagingTemplate;

    public WebsocketController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    //    @MessageMapping("/topic/notification")
    //    public NotificationDTO sendActivity(
    //        @Payload NotificationDTO notificationDTO,
    //        StompHeaderAccessor stompHeaderAccessor,
    //        Principal principal
    //    ) {
    //        notificationDTO.setSessionId(stompHeaderAccessor.getSessionId());
    //        log.debug("Sending notification tracking data {}", notificationDTO);
    //        return notificationDTO;
    //    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setSessionId(sessionDisconnectEvent.getSessionId());
        notificationDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/notification", notificationDTO);
    }
}
