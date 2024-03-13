package com.im.filestorage.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.filestorage.dto.request.FileInfoRequestDto;
import com.im.filestorage.exception.NotFoundException;
import com.im.filestorage.model.FileStorage;
import com.im.filestorage.repository.FileStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileConsumer {
    @Autowired
    FileStorageRepository fileStorageRepository;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "editFileInfoTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        FileInfoRequestDto fileInfoRequestDto = mapper.readValue(message, FileInfoRequestDto.class);
        log.info("** Editing file info to repository payload: '{}'", fileInfoRequestDto.toString());
        FileStorage fileStorage = fileStorageRepository
                .findById(fileInfoRequestDto.getId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("File with id [%s] is not found", fileInfoRequestDto.getId())
                ));
        fileStorage.setTenantId(fileInfoRequestDto.getTenantId());
        fileStorage.setUserId(fileInfoRequestDto.getUserId());
        fileStorage.setPublished(fileInfoRequestDto.getIsPublished());
        fileStorageRepository.save(fileStorage);
    }
}
