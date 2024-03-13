package com.im.contact.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.contact.dto.model.ContactDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class ContactProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ContactDto contactDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send("saveContactTopic", mapper.writeValueAsString(contactDto))
                    .addCallback(
                            result -> log.info("Message sent to topic: {}", contactDto.toString()),
                            ex -> log.error("Failed to send message", ex)
                    );
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
