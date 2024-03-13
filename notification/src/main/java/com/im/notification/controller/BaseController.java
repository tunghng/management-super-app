package com.im.notification.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.notification.dto.model.AppUserDto;
import com.im.notification.dto.page.PageLink;
import com.im.notification.exception.UnauthorizedException;
import com.im.notification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public abstract class BaseController {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    public PageLink createPageLink(int page, int pageSize) {
        return new PageLink(page, pageSize);
    }

    protected AppUserDto getCurrentUser(HttpServletRequest request) {
        try {
            Map<String, Object> jwt = parseJwt(request.getHeader("Authorization"));
            AppUserDto currentUser = userService.findByUserId(UUID.fromString((String) jwt.get("userId")));
            if (currentUser == null)
                throw new UnauthorizedException("You aren't authorized to perform this operation.");
            else return currentUser;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    Map<String, Object> parseJwt(String token) throws JsonProcessingException {
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("Lost token");
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, Object> map = mapper.readValue(payload, Map.class);
        return map;
    }
}
