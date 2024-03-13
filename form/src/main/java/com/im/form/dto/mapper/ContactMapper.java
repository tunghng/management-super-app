package com.im.form.dto.mapper;

import com.im.form.dto.model.ContactDto;
import com.im.form.dto.response.ContactInfoDto;
import com.im.form.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring")
public interface ContactMapper {
    Contact toModel(ContactDto contactDto);

    ContactDto toDto(Contact contact);

    ContactInfoDto toInfoDto(Contact contact);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }
}
