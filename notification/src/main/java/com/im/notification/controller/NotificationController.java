package com.im.notification.controller;

import com.im.notification.dto.model.AppUserDto;
import com.im.notification.dto.model.NotificationUserResponseDto;
import com.im.notification.dto.page.PageData;
import com.im.notification.dto.page.PageLink;
import com.im.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/notification")
@AllArgsConstructor
public class NotificationController extends BaseController {
    private final NotificationService notificationService;

    @Operation(summary = "Get All Notifications for a user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<PageData<NotificationUserResponseDto>> getNotificationUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Boolean isRead,
            HttpServletRequest request) {
        AppUserDto appUserDto = getCurrentUser(request);
        PageLink pageLink = createPageLink(page, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getNotificationUsers(
                        pageLink,
                        isRead,
                        appUserDto
                ));
    }
}
