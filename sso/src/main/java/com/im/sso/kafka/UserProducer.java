package com.im.sso.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.sso.dto.model.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class UserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(AppUserDto userDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send("saveUserTopic", mapper.writeValueAsString(userDto))
                    .addCallback(
                            result -> log.info("Message sent to topic: {}", userDto.toString()),
                            ex -> log.error("Failed to send message", ex)
                    );
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
