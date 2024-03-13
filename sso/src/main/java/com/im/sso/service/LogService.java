package com.im.sso.service;

import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.LogDto;
import com.im.sso.dto.response.page.PageData;
import com.im.sso.dto.response.page.PageLink;
import com.im.sso.model.enums.ActionStatus;
import com.im.sso.model.enums.ActionType;
import com.im.sso.model.enums.EntityType;

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
