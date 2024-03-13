package com.im.notification.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.notification.dto.model.NotificationDto;
import com.im.notification.dto.model.NotificationUserDto;
import com.im.notification.model.Notification;
import com.im.notification.model.NotificationComponent;
import com.im.notification.model.NotificationUser;
import com.im.notification.repository.NotificationComponentRepository;
import com.im.notification.repository.NotificationRepository;
import com.im.notification.repository.NotificationUserRepository;
import com.im.notification.socket.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Component
@Slf4j
public class NotificationConsumer {
    @Autowired
    SocketService socketService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationUserRepository notificationUserRepository;

    @Autowired
    NotificationComponentRepository notificationComponentRepository;

    @Transactional
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveNotificationTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receiveNotification(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        NotificationDto notificationDto = mapper.readValue(message, NotificationDto.class);
        log.info("** Saving notification to repository payload: '{}'", notificationDto.toString());
        NotificationComponent notificationComponent = notificationComponentRepository
                .findByEntityId(notificationDto.getComponent().getEntityId());

        if (notificationComponent == null) {
            notificationComponent = new NotificationComponent();
            BeanUtils.copyProperties(notificationDto.getComponent(), notificationComponent);
            Notification notification = new Notification();
            BeanUtils.copyProperties(notificationDto, notification, "component");
            notification.setComponent(notificationComponent);
            notificationRepository.save(notification);
            Collection<UUID> userIds = notificationDto.getToUserIds();
            userIds.remove(notificationDto.getCreatedBy());

            log.info(userIds.toString());
            for (UUID userId : userIds) {
                NotificationUser notificationUser = NotificationUser.builder()
                        .notification(notification)
                        .userId(userId)
                        .isRead(false)
                        .build();
                notificationUserRepository.save(notificationUser);
                socketService.sendMessage(notificationUser);
            }
        }
    }

    @Transactional
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveNotificationUserTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receiveNotificationUser(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        NotificationUserDto notificationUserDto = mapper.readValue(message, NotificationUserDto.class);
        log.info("** Saving notification to repository payload: '{}'", notificationUserDto.toString());
        NotificationComponent notificationComponent = notificationComponentRepository
                .findByEntityId(notificationUserDto.getComponent().getEntityId());
        Notification notification = notificationRepository.findByComponent(notificationComponent);
        NotificationUser notificationUser = notificationUserRepository.findByNotificationAndUserId(notification, notificationUserDto.getUserId());
        notificationUser.setIsRead(notificationUserDto.getIsRead());
        notificationUserRepository.save(notificationUser);
        socketService.sendMessage(notificationUser);
    }
}
