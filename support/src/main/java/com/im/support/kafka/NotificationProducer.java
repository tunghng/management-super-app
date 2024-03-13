package com.im.support.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.request.Notification;
import com.im.support.dto.request.NotificationUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotificationMessage(Notification notification) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send("saveNotificationTopic", mapper.writeValueAsString(notification))
                    .addCallback(
                            result -> log.info("Message sent to topic: {}", notification.toString()),
                            ex -> log.error("Failed to send message", ex)
                    );
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }

    public void sendNotificationUserMessage(NotificationUser notificationUser) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send("saveNotificationUserTopic", mapper.writeValueAsString(notificationUser))
                    .addCallback(
                            result -> log.info("Message sent to topic: {}", notificationUser.toString()),
                            ex -> log.error("Failed to send message", ex)
                    );
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }
}
