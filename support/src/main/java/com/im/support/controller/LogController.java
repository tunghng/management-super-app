package com.im.support.controller;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;
import com.im.support.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/support/log")
public class LogController extends BaseController {
    @Autowired
    private LogService logService;

    @GetMapping
    @Operation(summary = "Get Support Ticket Logs (getTicketLogs)")
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
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        AppUserDto currentUser = getCurrentUser(request);
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
