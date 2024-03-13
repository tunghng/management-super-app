package com.im.billing.service;

import com.im.billing.dto.mapper.ContactMapper;
import com.im.billing.dto.model.ContactDto;
import com.im.billing.exception.BadRequestException;
import com.im.billing.model.Contact;
import com.im.billing.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactMapper contactMapper;

    @Override
    public ContactDto findById(UUID contactId) {
        if (contactId == null) return null;
        Contact contact = contactRepository.findById(contactId).orElseThrow(
                () -> new BadRequestException(String.format("Contact with id [%s] is not found", contactId))
        );
        return contact != null ? contactMapper.toDto(contact) : null;
    }
}
