package com.im.announcement.dto.mapper;

import com.im.announcement.dto.model.AnnouncementDto;
import com.im.announcement.model.Announcement;
import com.im.announcement.service.AnnouncementContactService;
import com.im.announcement.service.ContactService;
import com.im.announcement.service.UserService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserService.class, ContactService.class, AnnouncementContactService.class})
public interface AnnouncementMapper {
    AnnouncementDto toDto(Announcement announcement);

}
