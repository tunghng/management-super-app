package com.im.document.dto.mapper;

import com.im.document.dto.model.AppUserDto;
import com.im.document.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface AppUserMapper {

    AppUser toModel(AppUserDto appUserDto);

    AppUserDto toDto(AppUser appUser);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }
}
