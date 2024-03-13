package com.im.support.service;

import com.im.support.dto.model.ContactDto;

import java.util.UUID;

public interface ContactService {
    ContactDto findById(UUID contactId);
}
