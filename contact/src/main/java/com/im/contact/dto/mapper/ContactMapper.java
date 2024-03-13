package com.im.contact.dto.mapper;

import com.im.contact.dto.model.ContactDto;
import com.im.contact.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface ContactMapper {

    @Mapping(source = "id", target = "avatar", qualifiedByName = "getAvatarUrl")
    ContactDto toDto(Contact contact);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/contact/" + id + "/avatar";
    }
}
