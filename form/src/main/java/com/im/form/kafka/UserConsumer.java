package com.im.form.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.mapper.AppUserMapper;
import com.im.form.dto.mapper.ContactMapper;
import com.im.form.dto.model.AppUserDto;
import com.im.form.model.AppUser;
import com.im.form.repository.AppUserRepository;
import com.im.form.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    AppUserMapper userMapper;
    @Autowired
    ContactMapper contactMapper;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveUserTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AppUserDto userDto = mapper.readValue(message, AppUserDto.class);
        log.info("** Saving user to repository payload: '{}'", userDto.toString());
        AppUser user = userMapper.toModel(userDto);
        userRepository.save(user);
    }
}
