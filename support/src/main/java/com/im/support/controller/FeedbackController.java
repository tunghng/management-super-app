package com.im.support.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.FeedbackDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.exception.UnAuthorizedException;
import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;
import com.im.support.model.enums.RoleType;
import com.im.support.service.FeedbackService;
import com.im.support.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/support/ticket/{ticketId}/feedback")
public class FeedbackController extends BaseController {

    @Autowired
    FeedbackService feedbackService;
    @Autowired
    LogService logService;

    @GetMapping
    @Operation(summary = "Get Feedbacks of Ticket by Id (getFeedbacksByTicketId")
    public PageData<?> getFeedbacks(
            @PathVariable UUID ticketId,
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, null, null, null);
        AppUserDto currentUser = getCurrentUser(request);
        TicketDto ticketDto = checkTicketId(ticketId, currentUser.getTenantId());
        return feedbackService.findFeedbacksByTicketId(pageLink, ticketDto, currentUser);
    }

    @PostMapping
    @Operation(summary = "Feedback for A Ticket by Ticket Id (saveFeedbackTicketId)")
    public FeedbackDto saveFeedback(@PathVariable UUID ticketId,
                                    @Valid @RequestBody FeedbackDto feedbackDto,
                                    HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        TicketDto ticketDto = checkTicketId(ticketId, currentUser.getTenantId());
        if (RoleType.CUSTOMER.equals(RoleType.lookup(currentUser.getRole()))
                && !ticketDto.getContact().getId().equals(currentUser.getContactId())) {
            ObjectMapper objectMapper = new ObjectMapper();
            logService.save(LogDto.builder()
                    .actionStatus(ActionStatus.FAILURE)
                    .actionType(ActionType.CREATED)
                    .actionFailureDetails("You do not have permission to do this action")
                    .actionData(objectMapper.valueToTree(feedbackDto))
                    .entityType(EntityType.FEEDBACK)
                    .build(), currentUser);
            throw new UnAuthorizedException("You do not have permission to do this action");
        }
        return feedbackService.save(feedbackDto, ticketDto, currentUser);
    }

}
