package com.im.announcement.dto.mapper;

import com.im.announcement.dto.model.AnnouncementContactDto;
import com.im.announcement.model.AnnouncementContact;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-09T16:39:26+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AnnouncementContactMapperImpl implements AnnouncementContactMapper {

    @Override
    public AnnouncementContactDto toDto(AnnouncementContact announcementContact) {
        if ( announcementContact == null ) {
            return null;
        }

        AnnouncementContactDto announcementContactDto = new AnnouncementContactDto();

        announcementContactDto.setAnnouncementId( announcementToId( announcementContact.getAnnouncement() ) );
        announcementContactDto.setContactId( contactToId( announcementContact.getContact() ) );

        return announcementContactDto;
    }
}
