package com.im.announcement.service;

import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.dto.model.AnnouncementSaveDto;
import com.im.announcement.dto.model.AppUserDto;
import com.im.announcement.dto.response.page.PageData;
import com.im.announcement.dto.response.page.PageLink;
import com.im.announcement.model.enums.PriorityType;

import java.util.UUID;

public interface AnnouncementService {
    PageData<AnnouncementDto> findAnnouncements(
            PageLink pageLink,
            Boolean isDeleted,
            AppUserDto currentUser,
            Boolean isSearchMatchCase
    );

    AnnouncementDto save(AnnouncementSaveDto announcementDto, UUID userId, UUID tenantId);

    Long countUnreadAnnouncements(AppUserDto currentUser, Boolean isRead, PriorityType type);

    AnnouncementDto findById(UUID announcementId, UUID tenantId);

    String markAsUnread(UUID announcementId, UUID tenantId);

    String deleteAnnouncement(UUID announcementId, AppUserDto currentUser);

    String restoreAnnouncement(UUID announcementId, AppUserDto currentUser);
}
