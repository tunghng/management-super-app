package com.im.document.service;

import com.im.document.dto.mapper.ContactMapper;
import com.im.document.dto.model.ContactDto;
import com.im.document.model.Contact;
import com.im.document.repository.ContactRepository;
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
        Contact contact = contactRepository.findById(contactId).orElse(null);
        return contact != null ? contactMapper.toDto(contact) : null;
    }
}
