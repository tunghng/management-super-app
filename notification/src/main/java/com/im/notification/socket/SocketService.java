package com.im.notification.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.im.notification.dto.model.NotificationUserResponseDto;
import com.im.notification.model.NotificationUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class SocketService {
    private final SocketIOServer server;

    public void sendMessage(NotificationUser notificationUser) {
        for (SocketIOClient client : server.getAllClients()) {
            if (client.get("userId").equals(notificationUser.getUserId().toString())) {
                client.sendEvent("add-notification", NotificationUserResponseDto.builder()
                        .componentName(notificationUser.getNotification().getComponent().getComponentName())
                        .entityType(notificationUser.getNotification().getComponent().getEntityType())
                        .entityId(notificationUser.getNotification().getComponent().getEntityId())
                        .userId(notificationUser.getUserId())
                        .isRead(notificationUser.getIsRead())
                        .createdAt(notificationUser.getNotification().getCreatedAt())
                        .build());
            }
        }
    }
}
