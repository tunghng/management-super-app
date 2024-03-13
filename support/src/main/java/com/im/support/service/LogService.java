package com.im.support.service;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;

import java.util.UUID;

public interface LogService {
    LogDto save(LogDto logDto, AppUserDto currentUser);

    LogDto save(LogDto logDto, UUID currentUserId, UUID tenantId);

    PageData<LogDto> findLogs(
            PageLink pageLink,
            EntityType entityType,
            UUID entityId,
            UUID userId,
            ActionStatus actionStatus,
            ActionType actionType,
            Long createdAtStartTs,
            Long createdAtEndTs,
            UUID tenantId,
            Boolean isSearchMatchCase
    );

}
