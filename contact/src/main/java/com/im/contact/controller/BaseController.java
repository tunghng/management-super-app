package com.im.contact.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.dto.model.ContactDto;
import com.im.contact.dto.response.page.PageLink;
import com.im.contact.dto.response.page.SortOrder;
import com.im.contact.exception.ForbiddenException;
import com.im.contact.exception.NotFoundException;
import com.im.contact.service.ContactServiceImpl;
import com.im.contact.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class BaseController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    ContactServiceImpl contactService;

    ObjectMapper mapper = new ObjectMapper();

    public PageLink createPageLink(int page, int pageSize, String searchText) {
        return new PageLink(page, pageSize, searchText);
    }

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

    ContactDto checkContactId(UUID contactId, UUID tenantId) {
        ContactDto contact = contactService.findById(contactId, tenantId);
        checkNotNull(contact, String.format("Contact with id [%s] is not found", contactId));
        return contact;
    }

    <T> T checkNotNull(T reference, String notFoundMessage) {
        if (reference == null) {
            throw new NotFoundException(notFoundMessage);
        }
        return reference;
    }
}
