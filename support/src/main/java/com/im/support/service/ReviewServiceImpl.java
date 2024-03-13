package com.im.support.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.mapper.ReviewMapper;
import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.model.ReviewDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.request.Notification;
import com.im.support.dto.request.NotificationComponent;
import com.im.support.dto.request.NotificationUser;
import com.im.support.kafka.NotificationProducer;
import com.im.support.model.AppUser;
import com.im.support.model.Review;
import com.im.support.model.Ticket;
import com.im.support.model.TicketReview;
import com.im.support.model.enums.*;
import com.im.support.repository.AppUserRepository;
import com.im.support.repository.ReviewRepository;
import com.im.support.repository.TicketRepository;
import com.im.support.repository.TicketReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final static String TICKET_ALREADY_REVIEW = "Ticket with id [%s] is already reviewed";

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TicketReviewRepository ticketReviewRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    LogService logService;

    @Autowired
    ReviewMapper reviewMapper;

    @Autowired
    TicketReviewService ticketReviewService;

    @Autowired
    NotificationProducer notificationProducer;

    @Override
    public ReviewDto findReviewByTicketId(TicketDto ticketDto, AppUserDto currentUser) {
        Review review = reviewRepository.findByTicketId(ticketDto.getId()).orElse(new Review());
        if (review.getId() != null) {
            notificationProducer.sendNotificationUserMessage(
                    generateNotificationUser(review, currentUser.getId(), true)
            );
        }
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto save(ReviewDto reviewDto, TicketDto ticketDto, AppUserDto currentUser) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId()).get();
        TicketReview ticketReview = ticketReviewRepository.findByTicket(ticket);
        if (ticketReview != null) {
            logService.save(LogDto.builder()
                    .entityType(EntityType.REVIEW)
                    .actionType(ActionType.CREATED)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionFailureDetails(String.format(TICKET_ALREADY_REVIEW, ticketDto.getId()))
                    .build(), currentUser);
            throw new RuntimeException(String.format(TICKET_ALREADY_REVIEW, ticketDto.getId()));
        }
        Review review = new Review();
        BeanUtils.copyProperties(reviewDto, review, "createdBy", "updatedBy");
        ReviewTitle title = ReviewTitle.lookup(reviewDto.getTitle());
        review.setTitle(title);
        review.setTicket(ticket);
        review.setCreatedBy(currentUser.getId());
        review.setUpdatedBy(currentUser.getId());
        Review savedReview = reviewRepository.save(review);
        ReviewDto savedReviewDto = reviewMapper.toDto(savedReview);
        ticketReviewService.save(ticketDto, savedReviewDto);

        notificationProducer.sendNotificationMessage(
                generateNotification(savedReview)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        logService.save(LogDto.builder()
                .entityType(EntityType.REVIEW)
                .entityId(savedReviewDto.getId())
                .actionType(ActionType.CREATED)
                .actionStatus(ActionStatus.SUCCESS)
                .actionData(objectMapper.valueToTree(ticketDto))
                .build(), currentUser);

        return savedReviewDto;
    }

    private Notification generateNotification(Review review) {
        Collection<UUID> toUserIds = new ArrayList<>();
        toUserIds.addAll(appUserRepository.findByTenantIdAndContactId(
                review.getTicket().getTenantId(),
                review.getTicket().getContact().getId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                RoleType.TENANT.name(),
                review.getTicket().getTenantId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        toUserIds.addAll(appUserRepository.findByRoleAndTenantId(
                RoleType.MANAGER.name(),
                review.getTicket().getTenantId()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));

        return Notification.builder()
                .component(new NotificationComponent(
                        "support",
                        "review",
                        review.getId()
                ))
                .message("New review")
                .description(String.format("Review created by %s", review.getCreatedBy()))
                .toUserIds(toUserIds)
                .build();
    }

    private NotificationUser generateNotificationUser(Review review, UUID currentUserId, Boolean isRead) {
        return NotificationUser.builder()
                .component(new NotificationComponent(
                        "support",
                        "review",
                        review.getId()
                ))
                .userId(currentUserId)
                .isRead(isRead)
                .build();
    }
}
