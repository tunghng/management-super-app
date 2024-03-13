package com.im.announcement.service;

import com.im.announcement.dto.mapper.AnnouncementMapper;
import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.dto.model.AnnouncementSaveDto;
import com.im.announcement.dto.model.AppUserDto;
import com.im.announcement.dto.request.Notification;
import com.im.announcement.dto.request.NotificationComponent;
import com.im.announcement.dto.response.page.PageData;
import com.im.announcement.dto.response.page.PageLink;
import com.im.announcement.exception.BadRequestException;
import com.im.announcement.exception.NotFoundException;
import com.im.announcement.kafka.NotificationProducer;
import com.im.announcement.model.*;
import com.im.announcement.model.enums.PriorityType;
import com.im.announcement.model.enums.RoleType;
import com.im.announcement.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.ZoneId;
import java.util.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import java.util.stream.Collectors;


@Service
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

    private final static String ANNOUNCEMENT_NOT_FOUND = "Announcement with id [%s] is not found";

    @Autowired
    AnnouncementContactService announcementContactService;

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    AnnouncementContactRepository announcementContactRepository;

    @Autowired
    AnnouncementCodeRepository announcementCodeRepository;

    @Autowired
    AnnouncementMapper announcementMapper;

    @Autowired
    ContactService contactService;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    NotificationProducer notificationProducer;

    @Autowired
    AnnouncementUserRepository announcementUserRepository;
    @Override
    public PageData<AnnouncementDto> findAnnouncements(
            PageLink pageLink,
            Boolean isDeleted,
            AppUserDto currentUser,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());

        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<Announcement> announcements = !RoleType.CUSTOMER.equals(RoleType.lookup(currentUser.getRole()))
                ? announcementRepository.findAnnouncements(
                searchText,
                isSearchMatchCase,
                isDeleted,
                currentUser.getTenantId(),
                pageable
        ) : announcementContactRepository.findAllByContactId(
                searchText,
                isSearchMatchCase,
                currentUser.getContactId(),
                isDeleted,
                pageable
        ).map(AnnouncementContact::getAnnouncement);
        Page<AnnouncementDto> announcementDtoList = announcements.map(
                announcement -> announcementMapper.toDto(announcement)
        );
        return new PageData<AnnouncementDto>(announcementDtoList);
    }

    @Override
    @Transactional
    public AnnouncementDto save(AnnouncementSaveDto announcementDto, UUID userId, UUID tenantId) {
        Announcement announcement = new Announcement();
        if (announcementDto.getId() != null)
            announcement = announcementRepository.findById(announcementDto.getId()).get();
        BeanUtils.copyProperties(announcementDto, announcement, "code", "createdBy", "updatedBy");
        if (announcementDto.getId() == null) {
            announcement.setCreatedBy(userId);
            String code = announcementCodeRepository.save(new AnnouncementCode()).getId();
            announcement.setCode(code);
        }
        announcement.setPriority(announcementDto.getPriority() != null ?
                PriorityType.lookup(announcementDto.getPriority())
                : PriorityType.MEDIUM);
        announcement.setUpdatedBy(userId);
        announcement.setTenantId(tenantId);
        announcement.setIsDeleted(false);
        Announcement savedAnnouncement = announcementRepository.saveAndFlush(announcement);
        Notification notification = generateNotification(savedAnnouncement);
        notificationProducer.sendNotificationMessage(notification);

        for (UUID id : notification.getToUserIds()) {
            AnnouncementUser announcementUser = announcementUserRepository
                .findByAnnouncementAndUserId(announcement, id)
                .orElse(AnnouncementUser.builder()
                    .announcement(announcement)
                    .userId(id)
                    .isRead(false)
                    .build());
            announcementUserRepository.save(announcementUser);
        }

        List<UUID> contactIdList = new ArrayList<>();
        if (!announcementDto.getContacts().isEmpty()) {
            contactIdList = announcementDto.getContacts().stream().map(contactDto ->
                    announcementContactService.save(
                                    announcementMapper.toDto(savedAnnouncement),
                                    contactService.findById(contactDto.getId()))
                            .getContactId()
            ).collect(Collectors.toList());
        }

        AnnouncementDto savedAnnouncementDto = announcementMapper.toDto(savedAnnouncement);
        savedAnnouncementDto.setContacts(contactIdList.stream().map(
                contactId -> contactService.findById(contactId)
        ).collect(Collectors.toList()));
        return savedAnnouncementDto;
    }

    @Override
    public Long countUnreadAnnouncements(AppUserDto currentUser, Boolean isRead, PriorityType type) {
        return announcementUserRepository.countByUserIdAndIsRead(
                currentUser.getId(), isRead
        );
    }

    public Notification generateNotification(Announcement announcement) {
        Collection<UUID> toUsersId = new ArrayList<>();
        toUsersId.addAll(appUserRepository.findByTenantIdAndContactId(
                announcement.getTenantId(),
                announcement.getCreatedBy()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));
        Date date = Date.from(announcement.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());


        return Notification.builder()
                .component(new NotificationComponent(
                        "Announcement",
                        "Announcement",
                        announcement.getId()
                ))
                .message("New announcement")
                .description(String.format("Announcement created by %s", announcement.getCreatedBy()))
                .createdAt(date)
                .toUserIds(toUsersId)
                .createdBy(announcement.getCreatedBy())
                .build();
    }

    @Override
    public AnnouncementDto findById(UUID announcementId, UUID tenantId) {
        Announcement announcement = announcementRepository.findByIdAndTenantId(announcementId, tenantId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ANNOUNCEMENT_NOT_FOUND, announcementId)
                ));
        announcementRepository.save(announcement);

        AnnouncementUser announcementUser = announcementUserRepository.findByAnnouncementIdAndUserId(announcementId, tenantId).get();
        announcementUser.setIsRead(true);
        announcementUserRepository.save(announcementUser);

        return announcementMapper.toDto(announcement);
    }

    @Override
    public String markAsUnread(UUID announcementId, UUID tenantId) {
        AnnouncementUser announcementUser = announcementUserRepository.findByAnnouncementIdAndUserId(announcementId, tenantId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(ANNOUNCEMENT_NOT_FOUND, announcementId)
                ));
        announcementUser.setIsRead(false);
        announcementUserRepository.save(announcementUser);
        return String.format("Announcement with id [%s] is unread", announcementId);
    }

    @Override
    public String deleteAnnouncement(UUID announcementId, AppUserDto currentUser) {
        Announcement announcement = announcementRepository.findByIdAndTenantId(announcementId, currentUser.getTenantId())
                .orElseThrow(() -> new NotFoundException(
                        String.format(ANNOUNCEMENT_NOT_FOUND, announcementId)
                ));
        if (announcement.getIsDeleted()) {
            throw new BadRequestException(
                    String.format("Announcement with id [%s] has already deleted", announcementId)
            );
        }
        announcement.setIsDeleted(true);
        announcement.setUpdatedBy(currentUser.getId());
        announcementRepository.save(announcement);
        return String.format("Announcement with id [%s] move to trash bin", announcementId);
    }

    @Override
    public String restoreAnnouncement(UUID announcementId, AppUserDto currentUser) {
        Announcement announcement = announcementRepository.findByIdAndTenantId(announcementId, currentUser.getTenantId())
                .orElseThrow(() -> new NotFoundException(
                        String.format(ANNOUNCEMENT_NOT_FOUND, announcementId)
                ));
        if (!announcement.getIsDeleted()) {
            throw new BadRequestException(
                    String.format("Announcement with id [%s] has already displayed", announcementId)
            );
        }
        announcement.setIsDeleted(false);
        announcement.setUpdatedBy(currentUser.getId());
        announcementRepository.save(announcement);
        return String.format("Announcement with id [%s] restore from trash bin", announcementId);
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
