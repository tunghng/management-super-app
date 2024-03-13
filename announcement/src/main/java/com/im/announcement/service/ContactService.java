package com.im.announcement.service;

import com.im.announcement.dto.response.ContactResponse;
import com.im.announcement.model.AnnouncementContact;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ContactService {
    ContactResponse findById(UUID contactId);

    List<ContactResponse> collectionToListContactDto(Collection<AnnouncementContact> collection);

    LocalDateTime toLocalDateTime(long timestamp);
}
