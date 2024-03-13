package com.im.form.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.mapper.ContactMapper;
import com.im.form.dto.model.ContactDto;
import com.im.form.model.Contact;
import com.im.form.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContactConsumer {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactConsumer(
            ContactRepository contactRepository,
            ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveContactTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ContactDto contactDto = mapper.readValue(message, ContactDto.class);
        log.info("** Saving contact to repository payload: '{}'", contactDto.toString());
        Contact contact = contactMapper.toModel(contactDto);
        contactRepository.save(contact);
    }
}
