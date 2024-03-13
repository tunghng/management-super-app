package com.im.billing.service;

import com.im.billing.dto.model.ContactDto;

import java.util.UUID;

public interface ContactService {
    ContactDto findById(UUID contactId);
}
