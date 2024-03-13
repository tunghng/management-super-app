package com.im.contact.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.contact.dto.mapper.AppUserMapper;
import com.im.contact.dto.mapper.ContactMapper;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.model.AppUser;
import com.im.contact.model.Contact;
import com.im.contact.repository.AppUserRepository;
import com.im.contact.repository.ContactRepository;
import com.im.contact.service.ContactUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserConsumer {
    @Autowired
    AppUserRepository userRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    ContactUserServiceImpl contactUserService;
    @Autowired
    AppUserMapper userMapper;
    @Autowired
    ContactMapper contactMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveUserTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AppUserDto userDto = mapper.readValue(message, AppUserDto.class);
        log.info("** Saving user to repository payload: '{}'", userDto.toString());
        AppUser user = userMapper.toModel(userDto);
        userRepository.save(user);
        if (userDto.getContactId() != null) {
            Contact contact = contactRepository.findById(userDto.getContactId()).orElse(null);
            AppUser appUser = userRepository.findById(userDto.getId()).orElse(null);
            if (contact != null && appUser != null) {
                ContactDto contactDto = contactMapper.toDto(contact);
                contactUserService.save(contactDto, userDto);
            }
        }
    }

}
