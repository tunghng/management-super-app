package com.im.support.controller;


import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.model.TicketSaveDto;
import com.im.support.dto.model.TicketTypeDto;
import com.im.support.dto.response.CountResponse;
import com.im.support.dto.response.Response;
import com.im.support.dto.response.page.PageData;
import com.im.support.dto.response.page.PageLink;
import com.im.support.model.enums.RoleType;
import com.im.support.model.enums.TicketState;
import com.im.support.service.TicketService;
import com.im.support.service.TicketTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/support/ticket")
@Slf4j
public class TicketController extends BaseController {

    private final String API_HAS_DEPRECATED = "Route has been deprecated";
    private final List<String> TICKET_KEYS = List.of(
            "code", "title", "contactName", "state", "type", "createdAt",
            "closedAt", "closedBy", "reviewTitle", "reviewBody"
    );

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketTypeService ticketTypeService;

    @GetMapping
    @Operation(summary = "Get Support Tickets (getTickets)")
    public PageData<TicketDto> getSupports(
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Search: title, code")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "Filter by state")
            @RequestParam(required = false) TicketState state,
            @Parameter(description = "Filter by multi-typeId")
            @RequestParam(required = false) List<UUID> typeId,
            @Parameter(description = "Filter by multi-contactId")
            @RequestParam(required = false) List<UUID> contactId,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(required = false) String sortProperty,
            @Parameter(description = "Filter by deleted state")
            @RequestParam(required = false) Boolean isDeleted,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Filter column: createdAt. `createdAtEndTs` is required.")
            @RequestParam(required = false) Long createdAtStartTs,
            @Parameter(description = "Filter column: createdAt. `createdAtStartTs` is required.")
            @RequestParam(required = false) Long createdAtEndTs,
            @Parameter(description = "Filter column: updatedAt. `updatedAtEndTs` is required.")
            @RequestParam(required = false) Long updatedAtStartTs,
            @Parameter(description = "Filter column: updatedAt. `updatedAtStartTs` is required.")
            @RequestParam(required = false) Long updatedAtEndTs,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        AppUserDto currentUser = getCurrentUser(request);
        if (isUserRole(currentUser, RoleType.CUSTOMER)) {
            isDeleted = false;
        }
        return ticketService.findTickets(
                pageLink,
                state,
                typeId,
                contactId,
                isDeleted,
                createdAtStartTs,
                createdAtEndTs,
                updatedAtStartTs,
                updatedAtEndTs,
                currentUser,
                isSearchMatchCase
        );
    }

    @GetMapping("count")
    @Operation(summary = "Count Tickets (countTickets)")
    public ResponseEntity<CountResponse> countUnreadTickets(
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) TicketState state,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                new CountResponse(
                        ticketService.countUnreadTickets(
                                getCurrentUser(request),
                                isRead,
                                state
                        )
                )
        );
    }


    @GetMapping("export")
    @Operation(summary = "Export Support Tickets (exportToExcel)",
            description = "Support the following attributes: code, title, contactName, state, type, createdAt, closedAt, closedBy, reviewTitle, reviewBody")
    public ResponseEntity<Object> exportToExcel(
            @RequestParam(value = "attributes", required = false) String[] attributes,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(required = false) List<UUID> typeId,
            @RequestParam(required = false) List<UUID> contactId,
            @RequestParam(value = "createdAtStartTs", required = false) Long createdAtStartTs,
            @RequestParam(value = "createdAtEndTs", required = false) Long createdAtEndTs,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        String language = "vi-VN";
        if (request.getHeader("Content-Language") != null) {
            language = request.getHeader("Content-Language");
        }
        List<String> attributeList = (attributes == null) ? TICKET_KEYS : Arrays.asList(attributes);


        return ticketService.exportData(
                attributeList,
                fileName,
                language,
                createdAtStartTs,
                createdAtEndTs,
                currentUser,
                contactId,
                typeId
        );
    }

    @GetMapping("{ticketId}")
    @Operation(summary = "Get Support Ticket by Id (getTicketById)")
    public ResponseEntity<TicketDto> getSupportById(
            @PathVariable UUID ticketId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return ResponseEntity.ok(ticketService.findDetailById(
                ticketId,
                currentUser
        ));
    }

    @PostMapping
    @Operation(summary = "Raise Support Ticket (saveTicket)")
    public TicketDto saveTicket(
            @Valid @RequestBody TicketSaveDto newTicketDto,
            HttpServletRequest request
    ) {
        return ticketService.save(newTicketDto, getCurrentUser(request));
    }

    @PutMapping("{ticketId}/closed")
    @Operation(summary = "Close Support Ticket (closeTicket)")
    public TicketDto closeTicket(
            @PathVariable UUID ticketId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        TicketDto ticketDto = checkTicketId(ticketId, currentUser.getTenantId());
        return ticketService.closed(ticketDto, currentUser);
    }

    @PutMapping("{ticketId}/read")
    @Operation(summary = "Mark as Read Ticket by Id (markAsReadTicketById)", deprecated = true)
    public ResponseEntity<Response> markAsReadTicketById() {
        return ResponseEntity.ok(
                new Response(403, API_HAS_DEPRECATED)
        );
    }

    @PutMapping("{ticketId}/unread")
    @Operation(summary = "Mark as Unread Ticket by Id (markAsUnreadTicketById)", deprecated = true)
    public ResponseEntity<Response> markAsUnreadTicketById() {
        return ResponseEntity.ok(
                new Response(403, API_HAS_DEPRECATED)
        );
    }

    @GetMapping("type")
    @Operation(summary = "Get Support Ticket Types (getTicketType)")
    public PageData<?> getTicketTypes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchText", required = false) String searchText,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, null, null);
        return ticketTypeService.findTicketTypes(
                pageLink,
                getCurrentUser(request).getTenantId()
        );
    }

    @PostMapping("type")
    @Operation(summary = "Save Ticket Type (saveTicketType)")
    public TicketTypeDto saveTicketType(
            @Valid @RequestBody TicketTypeDto ticketTypeDto,
            HttpServletRequest request
    ) {
        return ticketTypeService.save(ticketTypeDto, getCurrentUser(request));
    }

    @DeleteMapping("{ticketId}")
    @Transactional
    @Operation(summary = "Delete ticket (deleteTicket)")
    public Response deleteTicket(
            @PathVariable UUID ticketId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(200, ticketService.deleteTicket(ticketId, currentUser));
    }

    @PutMapping("{ticketId}/restore")
    @Transactional
    @Operation(summary = "Restore ticket (restoreTicket)")
    public Response restoreTicket(
            @PathVariable UUID ticketId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(200, ticketService.restoreTicket(ticketId, currentUser));
    }
}
