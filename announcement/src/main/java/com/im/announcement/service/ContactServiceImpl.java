package com.im.announcement.service;

import com.im.announcement.dto.mapper.ContactMapper;
import com.im.announcement.dto.response.ContactResponse;
import com.im.announcement.model.AnnouncementContact;
import com.im.announcement.model.Contact;
import com.im.announcement.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ContactMapper contactMapper;

    @Override
    public ContactResponse findById(UUID contactId) {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        return contact != null ? contactMapper.toResponse(contact) : null;
    }

    @Override
    public List<ContactResponse> collectionToListContactDto(Collection<AnnouncementContact> collection) {
        if (collection != null) {
            return collection.stream().map(item ->
                            contactMapper.toResponse(item.getContact()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /*
    If we use ZoneId.systemDefault, the time that received from kafka will be wrong
    For example, we created a contact at 2023-06-01 16:35:32.015962.
    Kafka will send 1685637332015, we received it and convert it with ZoneId.systemDefault.
    The result in database will be: 2023-06-01 23:35:32.015000 (+ 7 hours).
     */
    @Override
    public LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }
}
