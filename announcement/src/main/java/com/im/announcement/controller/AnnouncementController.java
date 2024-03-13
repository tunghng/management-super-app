package com.im.announcement.controller;

import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.dto.model.AnnouncementSaveDto;
import com.im.announcement.dto.model.AppUserDto;
import com.im.announcement.dto.response.CountResponse;
import com.im.announcement.dto.response.Response;
import com.im.announcement.dto.response.page.PageData;
import com.im.announcement.dto.response.page.PageLink;
import com.im.announcement.model.enums.PriorityType;
import com.im.announcement.model.enums.RoleType;
import com.im.announcement.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

import static com.im.announcement.controller.ControllerConstants.*;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/announcement")
@AllArgsConstructor
public class AnnouncementController extends BaseController {

    @Autowired
    private final AnnouncementService announcementService;

    @GetMapping
    @Operation(summary = "Get Announcements (getAnnouncements)",
            description = "Returns a page of announcement. " + PAGE_DATA_PARAMETERS)
    public ResponseEntity<PageData<AnnouncementDto>> getAnnouncements(
            @Parameter(description = PAGE_NUMBER_DESCRIPTION)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = PAGE_SIZE_DESCRIPTION)
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = ANNOUNCEMENT_TEXT_SEARCH_DESCRIPTION)
            @RequestParam(required = false) String searchText,
            @Parameter(description = ANNOUNCEMENT_IS_DELETED_PARAM_DESCRIPTION)
            @RequestParam(required = false) Boolean isDeleted,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText);
        AppUserDto currentUser = getCurrentUser(request);
        if (isUserRole(currentUser, RoleType.CUSTOMER)) {
            isDeleted = false;
        }
        return ResponseEntity.ok(announcementService.findAnnouncements(
                pageLink,
                isDeleted,
                getCurrentUser(request),
                isSearchMatchCase
        ));
    }

    @GetMapping("count")
    @Operation(summary = "Count Announcement (countTickets)")
    public ResponseEntity<CountResponse> countUnreadTickets(
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(required = false) PriorityType type,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(
                new CountResponse(
                        announcementService.countUnreadAnnouncements(
                                getCurrentUser(request),
                                isRead,
                                type
                        )
                )
        );
    }

    @GetMapping("{announcementId}")
    @Operation(summary = "Get Announcement By Id (getAnnouncementById)",
            description = "Get the Announcement object based on the provided Announcement Id.")
    public ResponseEntity<AnnouncementDto> getAnnouncementById(
            @Parameter(description = ANNOUNCEMENT_ID_PARAM_DESCRIPTION)
            @PathVariable UUID announcementId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return ResponseEntity.ok(announcementService.findById(
                announcementId,
                currentUser.getTenantId()
        ));
    }

    @PostMapping
    @Operation(summary = "Save Announcement (saveAnnouncement",
            description = "Creates or Updates the Announcement. " +
                    "The newly created Announcement Id will be present in the response. " +
                    "Specify existing Announcement Id to update the Announcement. " +
                    "Remove 'id', 'tenantId' from the request body example (below) to create new Announcement entity. ")
    public ResponseEntity<AnnouncementDto> saveAnnouncement(
            @Valid @RequestBody AnnouncementSaveDto announcementSaveDto,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return ResponseEntity.ok(
                announcementService.save(
                        announcementSaveDto, currentUser.getId(), currentUser.getTenantId()
                )
        );
    }

    @PutMapping("{announcementId}/unread")
    @Operation(summary = "Mark as Unread Announcement by Id (markAsUnreadAnnouncementById)",
            description = "Mark the Announcement is unread based on provided Announcement Id.")
    public Response markAsUnreadAnnouncement(
            @Parameter(description = ANNOUNCEMENT_ID_PARAM_DESCRIPTION)
            @PathVariable UUID announcementId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(announcementService.markAsUnread(announcementId, currentUser.getTenantId()));
    }

    @DeleteMapping("{announcementId}")
    @Transactional
    @Operation(summary = "Delete Announcement by Id (deleteAnnouncement)",
            description = "Delete the Announcement based on provided Announcement Id")
    public Response deleteAnnouncement(
            @Parameter(description = ANNOUNCEMENT_ID_PARAM_DESCRIPTION)
            @PathVariable UUID announcementId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(announcementService.deleteAnnouncement(announcementId, currentUser));
    }

    @PutMapping("{announcementId}/restore")
    @Transactional
    @Operation(summary = "Restore Announcement by Id (restoreAnnouncement)")
    public Response restoreAnnouncement(
            @Parameter(description = ANNOUNCEMENT_ID_PARAM_DESCRIPTION)
            @PathVariable UUID announcementId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(announcementService.restoreAnnouncement(announcementId, currentUser));
    }
}