package com.im.notification.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.notification.dto.model.AppUserDto;
import com.im.notification.exception.UnauthorizedException;
import com.im.notification.service.UserService;
import com.im.notification.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class SocketModule {
    private final SocketIOServer server;
    private final UserService userService;

    public SocketModule(
            SocketIOServer server,
            UserService userService) {
        this.server = server;
        this.userService = userService;
        this.server.addConnectListener(onConnected());
        this.server.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            try {
                Map<String, String> jwt = parseJwt(client.getHandshakeData().getHttpHeaders().get("Authorization"));
                AppUserDto currentUser = userService.findByUserId(UUID.fromString(jwt.get("userId")));
                client.set("userId", currentUser.getId().toString());
            } catch (Exception exc) {
                client.set("userId", "");
            }
            log.info((userService.findByUserId(UUID.fromString("2455d54d-c280-46a9-ac8d-e7875078fda5"))).toString());
            log.info("A client connected to server with session ID {}", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("A client Disconnected from server with session ID {}", client.getSessionId().toString());
        };
    }

    private Map<String, String> parseJwt(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("Lost token");
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, String> map = mapper.readValue(payload, Map.class);
        return map;


    }
}
