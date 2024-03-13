package com.im.announcement.dto.mapper;

import com.im.announcement.dto.model.ContactDto;
import com.im.announcement.dto.response.ContactJsonResponse;
import com.im.announcement.dto.response.ContactResponse;
import com.im.announcement.model.Contact;
import com.im.announcement.service.ContactService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(uses = {ContactService.class})
public interface ContactMapper {

    Contact toModel(ContactDto contactDto);

    ContactDto toDto(Contact contact);

    ContactDto fromJsonToDto(ContactJsonResponse jsonDto);

    ContactResponse toResponse(Contact contact);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }
}
