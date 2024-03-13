package com.im.document.service;

import com.im.document.dto.model.ContactDto;

import java.util.UUID;

public interface ContactService {
    ContactDto findById(UUID contactId);
}
