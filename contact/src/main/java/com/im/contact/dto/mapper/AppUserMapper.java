package com.im.contact.dto.mapper;

import com.im.contact.dto.model.AppUserDetailDto;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);

    AppUserDetailDto toBaseDto(AppUser user);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }

}
