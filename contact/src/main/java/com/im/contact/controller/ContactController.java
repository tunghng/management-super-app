package com.im.contact.controller;

import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.dto.response.Response;
import com.im.contact.dto.response.page.PageData;
import com.im.contact.dto.response.page.PageLink;
import com.im.contact.service.ContactService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/contact")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class ContactController extends BaseController {

    @Autowired
    ContactService contactService;

    @GetMapping
    @Operation(summary = "Get Contacts (getContacts)")
    public PageData<?> getContacts(
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Parameter(description = "Search columns: name, phone, field, email, taxNumber")
            @RequestParam(value = "searchText", required = false) String searchText,
            @Parameter(description = "A boolean value representing the contacts isDeleted flag.")
            @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
            @Parameter(description = "Name of the property to sort by")
            @RequestParam(value = "sortProperty", required = false) String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(value = "sortOrder", required = false) String sortOrder,
            @Parameter(description = "Filter column: createdAt. `endTs` is required.")
            @RequestParam(value = "createdAtStartTs", required = false) Long createdAtStartTs,
            @Parameter(description = "Filter column: createdAt. `startTs` is required.")
            @RequestParam(value = "createdAtEndTs", required = false) Long createdAtEndTs,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        return contactService.findContacts(
                pageLink,
                isDeleted,
                createdAtStartTs,
                createdAtEndTs,
                getCurrentUser(request).getTenantId(),
                isSearchMatchCase
        );
    }

    @GetMapping("user")
    @Operation(summary = "Get Users belong to Contacts App (getContactUsers)")
    public PageData<?> getContactUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,

            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText);
        return contactService.findContactUsers(
                pageLink,
                getCurrentUser(request).getTenantId(),
                isSearchMatchCase
        );
    }

    @GetMapping("{contactId}")
    @Operation(summary = "Get Contact by id (getContactById)")
    public ContactDto getContactById(
            @PathVariable UUID contactId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        checkContactId(contactId, currentUser.getTenantId());
        return contactService.findById(
                contactId,
                currentUser.getTenantId()
        );
    }

    @GetMapping("export")
    @Operation(summary = "Export Contacts (exportContacts)",
            description = "Support the following attributes: code, title, contactName, description, state, type, createdAt, closedAt, closedBy, reviewTitle, reviewBody")
    public ResponseEntity<Object> exportContacts(
            @RequestHeader(value = "Content-Language", defaultValue = "vi-VN") String language,
            @RequestParam(required = false) List<String> attributes,
            @RequestParam(required = false) Long startTs,
            @RequestParam(required = false) Long endTs,
            @RequestParam(required = false) String filename,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return contactService.exportContacts(
                attributes,
                startTs,
                endTs,
                language,
                filename,
                currentUser
        );
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Save Contact (saveContact)")
    public ContactDto saveContact(
            @Valid @RequestBody ContactDto contactDto,
            HttpServletRequest request
    ) {
        return contactService.save(
                contactDto,
                getCurrentUser(request).getTenantId()
        );
    }

    @DeleteMapping("{contactId}")
    @Transactional
    @Operation(summary = "Delete Contact By Id (deleteContactById)")
    public Response deleteContact(
            @PathVariable UUID contactId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        checkContactId(contactId, currentUser.getTenantId());
        contactService.delete(contactId);
        return new Response(200, String.format("Contact with id [%s] move to trash bin", contactId));
    }

    @PutMapping("{contactId}/restore")
    @Transactional
    @Operation(summary = "Restore Contact By Id (restoreContactById)")
    public Response restoreContact(
            @PathVariable UUID contactId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        checkContactId(contactId, currentUser.getTenantId());
        contactService.restore(contactId);
        return new Response(200, String.format("Contact with id [%s] restore from trash bin", contactId));
    }

    @GetMapping("sync")
    @Operation(summary = "Sync Contact Database (syncContacts)", hidden = true)
    public Response syncContacts() {
        return new Response(200, contactService.syncContacts());
    }

}
