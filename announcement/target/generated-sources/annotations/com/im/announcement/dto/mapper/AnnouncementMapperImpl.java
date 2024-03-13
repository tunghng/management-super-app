package com.im.announcement.dto.mapper;

import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.model.Announcement;
import com.im.announcement.service.ContactService;
import com.im.announcement.service.UserService;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-09T16:39:26+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AnnouncementMapperImpl implements AnnouncementMapper {

    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    @Override
    public AnnouncementDto toDto(Announcement announcement) {
        if ( announcement == null ) {
            return null;
        }

        AnnouncementDto announcementDto = new AnnouncementDto();

        announcementDto.setId( announcement.getId() );
        announcementDto.setHeadline( announcement.getHeadline() );
        announcementDto.setBody( announcement.getBody() );
        if ( announcement.getPriority() != null ) {
            announcementDto.setPriority( announcement.getPriority().name() );
        }
        announcementDto.setCode( announcement.getCode() );
        announcementDto.setIsDeleted( announcement.getIsDeleted() );
        announcementDto.setUpdatedBy( userService.findByUserId( announcement.getUpdatedBy() ) );
        announcementDto.setTenantId( announcement.getTenantId() );
        if ( announcement.getCreatedAt() != null ) {
            announcementDto.setCreatedAt( Date.from( announcement.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( announcement.getUpdatedAt() != null ) {
            announcementDto.setUpdatedAt( Date.from( announcement.getUpdatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        announcementDto.setContacts( contactService.collectionToListContactDto( announcement.getContacts() ) );
        Collection<String> collection = announcement.getAttachedFile();
        if ( collection != null ) {
            announcementDto.setAttachedFile( new ArrayList<String>( collection ) );
        }

        return announcementDto;
    }
}
