package com.im.support.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.mapper.FeedbackMapper;
import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.FeedbackDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.request.Notification;
import com.im.support.dto.request.NotificationComponent;
import com.im.support.dto.request.NotificationUser;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.kafka.NotificationProducer;
import com.im.support.model.AppUser;
import com.im.support.model.Feedback;
import com.im.support.model.Ticket;
import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;
import com.im.support.model.enums.RoleType;
import com.im.support.repository.AppUserRepository;
import com.im.support.repository.FeedbackRepository;
import com.im.support.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    TicketFeedbackService ticketFeedbackService;

    @Autowired
    LogService logService;

    @Autowired
    FeedbackMapper feedbackMapper;

    @Autowired
    NotificationProducer notificationProducer;

    @Override
    public PageData<FeedbackDto> findFeedbacksByTicketId(PageLink pageLink, TicketDto ticketDto, AppUserDto currentUser) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        Page<Feedback> feedbacks = feedbackRepository.findAllByTicketIdOrderByUpdatedAtDesc(
                ticketDto.getId(),
                pageable
        );
        Page<FeedbackDto> feedbackDtoPage = feedbacks.map(feedback ->
                feedbackMapper.toDto(feedback));
        for (Feedback feedback : feedbacks.getContent()) {
            notificationProducer.sendNotificationUserMessage(
                    generateNotificationUser(feedback, currentUser.getId(), true)
            );
        }
        return new PageData<FeedbackDto>(feedbackDtoPage);
    }

    @Override
    public FeedbackDto save(FeedbackDto feedbackDto, TicketDto ticketDto, AppUserDto currentUser) {
        Feedback feedback = new Feedback();
        Ticket ticket = ticketRepository.findById(ticketDto.getId()).orElse(null);
        BeanUtils.copyProperties(feedbackDto, feedback, "createdBy", "updatedBy");
        feedback.setTicket(ticket);
        feedback.setCreatedBy(currentUser.getId());
        feedback.setUpdatedBy(currentUser.getId());
        Feedback savedFeedback = feedbackRepository.save(feedback);
        FeedbackDto savedFeedbackDto = feedbackMapper.toDto(savedFeedback);
        ticketFeedbackService.save(ticketDto, savedFeedbackDto);
        notificationProducer.sendNotificationMessage(
                generateNotification(savedFeedback)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityType(EntityType.FEEDBACK)
                .entityId(savedFeedbackDto.getId())
                .actionData(objectMapper.valueToTree(feedbackDto))
                .actionStatus(ActionStatus.SUCCESS)
                .actionType(ActionType.CREATED).build(), currentUser);
        return savedFeedbackDto;
    }

    private Notification generateNotification(Feedback feedback) {
        Collection<UUID> toUserIds = new ArrayList<>();
        toUserIds.addAll(appUserRepository.findByTenantIdAndContactId(
                feedback.getTicket().getTenantId(),
                feedback.getTicket().getContact().getId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                RoleType.TENANT.name(),
                feedback.getTicket().getTenantId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                RoleType.MANAGER.name(),
                feedback.getTicket().getTenantId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));

        return Notification.builder()
                .component(new NotificationComponent(
                        "support",
                        "feedback",
                        feedback.getId()
                ))
                .message("New feedback")
                .description(String.format("Feedback created by %s", feedback.getCreatedBy()))
                .toUserIds(toUserIds)
                .createdBy(feedback.getCreatedBy())
                .build();
    }

    private NotificationUser generateNotificationUser(Feedback feedback, UUID currentUserId, Boolean isRead) {
        return NotificationUser.builder()
                .component(new NotificationComponent(
                        "support",
                        "feedback",
                        feedback.getId()
                ))
                .userId(currentUserId)
                .isRead(isRead)
                .build();
    }
}
