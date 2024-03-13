package com.im.support.dto.mapper;

import com.im.support.dto.model.ContactDto;
import com.im.support.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface ContactMapper {

    Contact toModel(ContactDto contactDto);

    @Mapping(source = "id", target = "avatar", qualifiedByName = "getAvatarUrl")
    ContactDto toDto(Contact contact);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/contact/" + id + "/avatar";
    }

}
