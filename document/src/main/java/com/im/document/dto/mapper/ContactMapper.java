package com.im.document.dto.mapper;

import com.im.document.dto.model.ContactDto;
import com.im.document.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface ContactMapper {

    Contact toModel(ContactDto contactDto);

    ContactDto toDto(Contact contact);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }

}
