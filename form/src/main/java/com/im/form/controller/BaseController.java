package com.im.form.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.form.dto.model.AppUserDto;
import com.im.form.dto.response.page.PageLink;
import com.im.form.dto.response.page.SortOrder;
import com.im.form.exception.UnAuthorizedException;
import com.im.form.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public abstract class BaseController {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    public PageLink createPageLink(int page, int pageSize, String searchText, String sortProperty, String sortOrder) {
        if (!StringUtils.isEmpty(sortProperty)) {
            SortOrder.Direction direction = SortOrder.Direction.DESC;
            if (!StringUtils.isEmpty(sortOrder)) {
                direction = SortOrder.Direction.lookup(sortOrder.toUpperCase());
            }
            SortOrder sort = new SortOrder(sortProperty, direction);
            return new PageLink(page, pageSize, searchText, sort);
        } else {
            return new PageLink(page, pageSize, searchText);
        }
    }

    protected AppUserDto getCurrentUser(HttpServletRequest request) {
        try {
            Map<String, Object> jwt = parseJwt(request.getHeader("Authorization"));
            AppUserDto currentUser = userService.findByUserId(UUID.fromString((String) jwt.get("userId")));
            if (currentUser == null)
                throw new UnAuthorizedException("You aren't authorized to perform this operation.");
            else return currentUser;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected AppUserDto getCurrentUserOrNull(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && !token.isEmpty()) ? getCurrentUser(request) : null;
    }

    Map<String, Object> parseJwt(String token) throws JsonProcessingException {
        if (token == null || token.isEmpty()) {
            throw new UnAuthorizedException("Lost token");
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, Object> map = mapper.readValue(payload, Map.class);
        return map;
    }
}
