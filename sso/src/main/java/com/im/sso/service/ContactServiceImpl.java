package com.im.sso.service;

import com.im.sso.dto.mapper.ContactMapper;
import com.im.sso.dto.model.ContactDto;
import com.im.sso.model.Contact;
import com.im.sso.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    @Override
    public ContactDto findById(UUID contactId) {
        Contact contact = contactId != null ?
                contactRepository.findById(contactId).orElse(null)
                : null;
        return contact != null ? contactMapper.toDto(contact) : null;
    }
}
