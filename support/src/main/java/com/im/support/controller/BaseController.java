package com.im.support.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.dto.model.TicketTypeDto;
import com.im.support.dto.response.page.PageLink;
import com.im.support.dto.response.page.SortOrder;
import com.im.support.exception.ForbiddenException;
import com.im.support.exception.NotFoundException;
import com.im.support.model.enums.RoleType;
import com.im.support.service.TicketService;
import com.im.support.service.TicketTypeService;
import com.im.support.service.UserService;
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
    UserService userService;

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketTypeService ticketTypeService;

    ObjectMapper mapper = new ObjectMapper();

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

    TicketDto checkTicketId(UUID ticketId, UUID tenantId) {
        TicketDto ticket = ticketService.findById(ticketId, tenantId);
        checkNotNull(ticket, String.format("Ticket with id [%s] is not found", ticketId));
        return ticket;
    }

    TicketTypeDto checkTicketType(String type, UUID tenantId) {
        TicketTypeDto typeDto = ticketTypeService.findByNameAndTenantId(type, tenantId);
        checkNotNull(typeDto, String.format("Type with name [%s] is not found", type));
        return typeDto;
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

    protected boolean isUserRole(AppUserDto user, RoleType role) {
        return user.getRole().equals(role.name());
    }

    Map<String, String> parseJwt(String token) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, String> map = mapper.readValue(payload, Map.class);
        return map;
    }

    <T> T checkNotNull(T reference, String notFoundMessage) {
        if (reference == null) {
            throw new NotFoundException(notFoundMessage);
        }
        return reference;
    }
}
