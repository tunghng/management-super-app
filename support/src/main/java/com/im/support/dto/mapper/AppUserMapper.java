package com.im.support.dto.mapper;

import com.im.support.dto.model.AppUserDto;
import com.im.support.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    @Mapping(source = "id", target = "avatar", qualifiedByName = "getAvatarUrl")
    AppUserDto toDto(AppUser user);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }
}
