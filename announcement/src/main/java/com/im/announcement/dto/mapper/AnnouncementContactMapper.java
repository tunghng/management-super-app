package com.im.announcement.dto.mapper;

import com.im.announcement.dto.model.AnnouncementContactDto;
import com.im.announcement.model.Announcement;
import com.im.announcement.model.AnnouncementContact;
import com.im.announcement.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface AnnouncementContactMapper {

    @Mapping(source = "announcement", target = "announcementId", qualifiedByName = "announcementToId")
    @Mapping(source = "contact", target = "contactId", qualifiedByName = "contactToId")
    AnnouncementContactDto toDto(AnnouncementContact announcementContact);

    @Named("contactToId")
    default UUID contactToId(Contact contact) {
        return contact.getId();
    }

    @Named("announcementToId")
    default UUID announcementToId(Announcement announcement) {
        return announcement.getId();
    }
}
