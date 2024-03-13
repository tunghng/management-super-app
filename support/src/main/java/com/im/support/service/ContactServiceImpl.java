package com.im.support.service;

import com.im.support.dto.mapper.ContactMapper;
import com.im.support.dto.model.ContactDto;
import com.im.support.exception.BadRequestException;
import com.im.support.model.Contact;
import com.im.support.repository.ContactRepository;
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
        if (contactId == null) {
            throw new BadRequestException("Contact id is null");
        }
        Contact contact = contactRepository.findById(contactId).orElseThrow(
                () -> new BadRequestException(String.format("Contact with id [%s] is not found", contactId))
        );
        return contact != null ? contactMapper.toDto(contact) : null;
    }
}
