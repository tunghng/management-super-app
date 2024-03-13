package com.im.sso.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.sso.dto.mapper.ContactMapper;
import com.im.sso.dto.model.ContactDto;
import com.im.sso.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @KafkaListener(
            topicPartitions = {
                    @TopicPartition(
                            topic = "saveContactTopic",
                            partitionOffsets = @PartitionOffset(
                                    partition = "0", initialOffset = "0"
                            )
                    )
            },
            autoStartup = "${listen.auto.start:false}"
    )
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ContactDto contactDto = mapper.readValue(message, ContactDto.class);
        log.info("** Saving contact to repository payload: '{}'", contactDto.toString());
        contactRepository.save(contactMapper.toModel(contactDto));
    }

}
