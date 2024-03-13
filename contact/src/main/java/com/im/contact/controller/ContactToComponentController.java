package com.im.contact.controller;

import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.response.page.PageData;
import com.im.contact.dto.response.page.PageLink;
import com.im.contact.service.ContactUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/contact/{contactId}")
public class ContactToComponentController extends BaseController {

    @Autowired
    ContactUserService contactUserService;

    @GetMapping("user")
    @Operation(summary = "Get User Accounts belongs to Contact Id (getUsersByContactId)")
    public PageData<?> getUsersByContactId(
            @PathVariable UUID contactId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        checkContactId(contactId, currentUser.getTenantId());
        PageLink pageLink = createPageLink(page, pageSize, searchText);
        return contactUserService.findUsersByContactId(pageLink, contactId, isSearchMatchCase);
    }
}
