package com.im.sso.controller;

import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.LogDto;
import com.im.sso.dto.response.page.PageData;
import com.im.sso.dto.response.page.PageLink;
import com.im.sso.model.enums.ActionStatus;
import com.im.sso.model.enums.ActionType;
import com.im.sso.model.enums.EntityType;
import com.im.sso.service.LogService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/sso/log")
public class LogController extends BaseController {
    @Autowired
    LogService logService;

    @GetMapping
    public PageData<LogDto> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) EntityType entityType,
            @RequestParam(required = false) UUID entityId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) ActionStatus actionStatus,
            @RequestParam(required = false) ActionType actionType,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Long createdAtStartTs,
            @RequestParam(required = false) Long createdAtEndTs,
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase
    ) {
        PageLink pageLink = createPageLink(
                page, pageSize, searchText, sortProperty, sortOrder
        );
        AppUserDto currentUser = getCurrentUser();
        return logService.findLogs(
                pageLink,
                entityType,
                entityId,
                userId,
                actionStatus,
                actionType,
                createdAtStartTs,
                createdAtEndTs,
                currentUser.getTenantId(),
                isSearchMatchCase
        );
    }
}
