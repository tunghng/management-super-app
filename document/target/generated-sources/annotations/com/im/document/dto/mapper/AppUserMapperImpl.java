package com.im.document.dto.mapper;

import com.im.document.dto.model.AppUserDto;
import com.im.document.model.AppUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T10:40:28+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser toModel(AppUserDto appUserDto) {
        if ( appUserDto == null ) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setId( appUserDto.getId() );
        appUser.setEmail( appUserDto.getEmail() );
        appUser.setFirstName( appUserDto.getFirstName() );
        appUser.setLastName( appUserDto.getLastName() );
        appUser.setPhone( appUserDto.getPhone() );
        appUser.setAuthority( appUserDto.getAuthority() );
        appUser.setRole( appUserDto.getRole() );
        appUser.setAvatar( appUserDto.getAvatar() );
        appUser.setTenantId( appUserDto.getTenantId() );
        appUser.setContactId( appUserDto.getContactId() );
        appUser.setCreatedAt( appUserDto.getCreatedAt() );
        appUser.setUpdatedAt( appUserDto.getUpdatedAt() );
        appUser.setCreatedBy( appUserDto.getCreatedBy() );
        appUser.setUpdatedBy( appUserDto.getUpdatedBy() );

        return appUser;
    }

    @Override
    public AppUserDto toDto(AppUser appUser) {
        if ( appUser == null ) {
            return null;
        }

        AppUserDto appUserDto = new AppUserDto();

        appUserDto.setId( appUser.getId() );
        appUserDto.setEmail( appUser.getEmail() );
        appUserDto.setFirstName( appUser.getFirstName() );
        appUserDto.setLastName( appUser.getLastName() );
        appUserDto.setPhone( appUser.getPhone() );
        appUserDto.setAuthority( appUser.getAuthority() );
        appUserDto.setRole( appUser.getRole() );
        appUserDto.setAvatar( appUser.getAvatar() );
        appUserDto.setTenantId( appUser.getTenantId() );
        appUserDto.setContactId( appUser.getContactId() );
        appUserDto.setCreatedAt( appUser.getCreatedAt() );
        appUserDto.setUpdatedAt( appUser.getUpdatedAt() );
        appUserDto.setCreatedBy( appUser.getCreatedBy() );
        appUserDto.setUpdatedBy( appUser.getUpdatedBy() );

        return appUserDto;
    }
}
