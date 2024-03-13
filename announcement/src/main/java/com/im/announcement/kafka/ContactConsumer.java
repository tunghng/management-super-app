package com.im.announcement.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.im.announcement.dto.mapper.ContactMapper;
import com.im.announcement.dto.model.ContactDto;
import com.im.announcement.dto.response.ContactJsonResponse;
import com.im.announcement.model.Contact;
import com.im.announcement.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContactConsumer {
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    ContactMapper contactMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveContactTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ContactJsonResponse contactJsonResponse = mapper.readValue(message, ContactJsonResponse.class);
        ContactDto contactDto = contactMapper.fromJsonToDto(contactJsonResponse);
        log.info("** Saving contact to repository payload: '{}'", contactDto.toString());
        Contact contact = contactMapper.toModel(contactDto);
        contactRepository.save(contact);
    }

}
