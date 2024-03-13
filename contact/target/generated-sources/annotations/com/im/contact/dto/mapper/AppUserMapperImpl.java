package com.im.contact.dto.mapper;

import com.im.contact.dto.model.AppUserDetailDto;
import com.im.contact.dto.model.AppUserDto;
import com.im.contact.model.AppUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-09T16:40:28+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser toModel(AppUserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setId( userDto.getId() );
        appUser.setEmail( userDto.getEmail() );
        appUser.setFirstName( userDto.getFirstName() );
        appUser.setLastName( userDto.getLastName() );
        appUser.setPhone( userDto.getPhone() );
        appUser.setAuthority( userDto.getAuthority() );
        appUser.setRole( userDto.getRole() );
        appUser.setAvatar( userDto.getAvatar() );
        appUser.setTenantId( userDto.getTenantId() );
        appUser.setCreatedAt( userDto.getCreatedAt() );
        appUser.setUpdatedAt( userDto.getUpdatedAt() );
        appUser.setCreatedBy( userDto.getCreatedBy() );
        appUser.setUpdatedBy( userDto.getUpdatedBy() );

        return appUser;
    }

    @Override
    public AppUserDto toDto(AppUser user) {
        if ( user == null ) {
            return null;
        }

        AppUserDto appUserDto = new AppUserDto();

        appUserDto.setId( user.getId() );
        appUserDto.setEmail( user.getEmail() );
        appUserDto.setFirstName( user.getFirstName() );
        appUserDto.setLastName( user.getLastName() );
        appUserDto.setPhone( user.getPhone() );
        appUserDto.setAuthority( user.getAuthority() );
        appUserDto.setRole( user.getRole() );
        appUserDto.setAvatar( user.getAvatar() );
        appUserDto.setTenantId( user.getTenantId() );
        appUserDto.setCreatedAt( user.getCreatedAt() );
        appUserDto.setUpdatedAt( user.getUpdatedAt() );
        appUserDto.setCreatedBy( user.getCreatedBy() );
        appUserDto.setUpdatedBy( user.getUpdatedBy() );

        return appUserDto;
    }

    @Override
    public AppUserDetailDto toBaseDto(AppUser user) {
        if ( user == null ) {
            return null;
        }

        AppUserDetailDto appUserDetailDto = new AppUserDetailDto();

        appUserDetailDto.setId( user.getId() );
        appUserDetailDto.setEmail( user.getEmail() );
        appUserDetailDto.setFirstName( user.getFirstName() );
        appUserDetailDto.setLastName( user.getLastName() );
        appUserDetailDto.setPhone( user.getPhone() );
        appUserDetailDto.setAuthority( user.getAuthority() );
        appUserDetailDto.setRole( user.getRole() );
        appUserDetailDto.setAvatar( user.getAvatar() );
        appUserDetailDto.setTenantId( user.getTenantId() );
        appUserDetailDto.setCreatedAt( user.getCreatedAt() );
        appUserDetailDto.setUpdatedAt( user.getUpdatedAt() );

        return appUserDetailDto;
    }
}
