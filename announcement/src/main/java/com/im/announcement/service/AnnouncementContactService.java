package com.im.announcement.service;

import com.im.announcement.dto.model.AnnouncementContactDto;
import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.dto.model.ContactDto;
import com.im.announcement.model.AnnouncementContact;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AnnouncementContactService {
    AnnouncementContactDto save(AnnouncementDto baseAnnouncementDto, ContactDto contactDto);

    List<UUID> convertToListContactUUID(Collection<AnnouncementContact> collection);
}
