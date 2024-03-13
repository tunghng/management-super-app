package com.im.form.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.request.FileInfoRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class FileProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(FileInfoRequestDto fileInfoRequestDto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send("editFileInfoTopic", mapper.writeValueAsString(fileInfoRequestDto))
                    .addCallback(
                            result -> log.info("Message sent to topic: {}", fileInfoRequestDto.toString()),
                            ex -> log.error("Failed to send message", ex)
                    );
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

//    public void removeFile(UUID fileId) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        kafkaTemplate.send("removeFileTopic", mapper.writeValueAsString(fileId))
//                .addCallback(
//                        result -> log.info("Message sent to topic: {}", fileId.toString()),
//                        ex -> log.error("Failed to send message", ex)
//                );
//    }
}
