package com.im.support.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.LogDto;
import com.im.support.dto.model.ReviewDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.exception.UnAuthorizedException;
import com.im.support.model.enums.ActionStatus;
import com.im.support.model.enums.ActionType;
import com.im.support.model.enums.EntityType;
import com.im.support.model.enums.RoleType;
import com.im.support.service.LogService;
import com.im.support.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/support/ticket/{ticketId}/review")
public class ReviewController extends BaseController {

    @Autowired
    ReviewService reviewService;
    @Autowired
    LogService logService;

    @GetMapping
    @Operation(summary = "Get Review of Ticket Id (getReviewByTicketId")
    public ReviewDto getReview(
            @PathVariable UUID ticketId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        TicketDto ticketDto = checkTicketId(ticketId, currentUser.getTenantId());
        return reviewService.findReviewByTicketId(ticketDto, currentUser);
    }

    @PostMapping
    @Operation(summary = "Save Review for Ticket Id (saveReviewTicketId")
    public ReviewDto saveReview(
            @PathVariable UUID ticketId,
            @Valid @RequestBody ReviewDto reviewDto,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        TicketDto ticketDto = checkTicketId(ticketId, currentUser.getTenantId());
        if (RoleType.CUSTOMER.equals(RoleType.lookup(currentUser.getRole()))
                && !ticketDto.getContact().getId().equals(currentUser.getContactId())) {
            ObjectMapper objectMapper = new ObjectMapper();
            logService.save(LogDto.builder()
                    .entityType(EntityType.REVIEW)
                    .actionType(ActionType.CREATED)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionData(objectMapper.valueToTree(reviewDto))
                    .actionFailureDetails("You do not have permission to do this action")
                    .build(), currentUser);
            throw new UnAuthorizedException("You do not have permission to do this action");
        }
        return reviewService.save(reviewDto, ticketDto, currentUser);
    }

}
