package com.im.filestorage.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.filestorage.dto.model.AppUserDto;
import com.im.filestorage.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public abstract class BaseController {

    ObjectMapper mapper = new ObjectMapper();

    protected AppUserDto getCurrentUser(HttpServletRequest request) {
        try {
            Map<String, String> jwt = parseJwt(request.getHeader("Authorization"));
            AppUserDto userDto = new AppUserDto();
            userDto.setUserId(UUID.fromString(jwt.get("userId")));
            userDto.setTenantId(UUID.fromString(jwt.get("tenantId")));
            return userDto;
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

    <T> T checkNotNull(T reference, String notFoundMessage) {
        if (reference == null) {
            throw new NotFoundException(notFoundMessage);
        }
        return reference;
    }
}
