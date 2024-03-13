package com.im.contact.dto.mapper;

import com.im.contact.dto.model.ContactUserDto;
import com.im.contact.model.ContactUser;
import org.mapstruct.Mapper;

@Mapper
public interface ContactUserMapper {

    ContactUser toModel(ContactUserDto contactUserDto);

    ContactUserDto toDto(ContactUser contactUser);

}
