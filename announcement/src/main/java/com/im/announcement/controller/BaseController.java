package com.im.announcement.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.dto.model.AppUserDto;
import com.im.announcement.dto.response.page.PageLink;
import com.im.announcement.exception.ForbiddenException;
import com.im.announcement.exception.NotFoundException;
import com.im.announcement.model.enums.RoleType;
import com.im.announcement.service.AnnouncementService;
import com.im.announcement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class BaseController {

    private final static String ANNOUNCEMENT_NOT_FOUND = "Announcement with id [%s] is not found";

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    @Autowired
    AnnouncementService announcementService;

    public PageLink createPageLink(int page, int pageSize, String searchText) {
        return new PageLink(page, pageSize, searchText);
    }

    protected AppUserDto getCurrentUser(HttpServletRequest request) {
        try {
            Map<String, String> jwt = parseJwt(request.getHeader("Authorization"));
            AppUserDto currentUser = userService.findByUserId(UUID.fromString(jwt.get("userId")));
            if (currentUser == null)
                throw new ForbiddenException("You aren't authorized to perform this operation.");
            else return currentUser;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    Map<String, String> parseJwt(String token) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, String> map = mapper.readValue(payload, Map.class);
        return map;
    }

    protected boolean isUserRole(AppUserDto user, RoleType role) {
        return user.getRole().equals(role.name());
    }

    AnnouncementDto checkAnnouncementId(UUID announcementId, UUID tenantId) {
        AnnouncementDto announcementDto = announcementService.findById(announcementId, tenantId);
        checkNotNull(announcementDto, String.format(ANNOUNCEMENT_NOT_FOUND, announcementId));
        return announcementDto;
    }

    <T> T checkNotNull(T reference, String notFoundMessage) {
        if (reference == null) {
            throw new NotFoundException(notFoundMessage);
        }
        return reference;
    }
}
