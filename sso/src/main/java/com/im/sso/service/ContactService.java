package com.im.sso.service;

import com.im.sso.dto.model.ContactDto;

import java.util.UUID;

public interface ContactService {
    ContactDto findById(UUID contactId);
}
