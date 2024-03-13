package com.im.sso.dto.mapper;

import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.ContactDto;
import com.im.sso.dto.response.UserProfileResponse;
import com.im.sso.model.AppUser;
import com.im.sso.model.Contact;
import com.im.sso.model.UserComponent;
import com.im.sso.model.enums.AuthorityType;
import com.im.sso.model.enums.RoleType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:12+0700",
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
        appUser.setCreatedAt( userDto.getCreatedAt() );
        appUser.setUpdatedAt( userDto.getUpdatedAt() );
        appUser.setCreatedBy( userDto.getCreatedBy() );
        appUser.setUpdatedBy( userDto.getUpdatedBy() );
        appUser.setEmail( userDto.getEmail() );
        appUser.setFirstName( userDto.getFirstName() );
        appUser.setLastName( userDto.getLastName() );
        appUser.setPhone( userDto.getPhone() );
        appUser.setAvatar( userDto.getAvatar() );
        if ( userDto.getAuthority() != null ) {
            appUser.setAuthority( Enum.valueOf( AuthorityType.class, userDto.getAuthority() ) );
        }
        if ( userDto.getRole() != null ) {
            appUser.setRole( Enum.valueOf( RoleType.class, userDto.getRole() ) );
        }
        appUser.setTenantId( userDto.getTenantId() );

        return appUser;
    }

    @Override
    public UserProfileResponse toUserProfile(AppUser user) {
        if ( user == null ) {
            return null;
        }

        UserProfileResponse userProfileResponse = new UserProfileResponse();

        userProfileResponse.setAvatar( AppUserMapper.getAvatarUrl( user.getId() ) );
        userProfileResponse.setId( user.getId() );
        userProfileResponse.setEmail( user.getEmail() );
        userProfileResponse.setFirstName( user.getFirstName() );
        userProfileResponse.setLastName( user.getLastName() );
        userProfileResponse.setPhone( user.getPhone() );
        if ( user.getAuthority() != null ) {
            userProfileResponse.setAuthority( user.getAuthority().name() );
        }
        if ( user.getRole() != null ) {
            userProfileResponse.setRole( user.getRole().name() );
        }
        userProfileResponse.setTenantId( user.getTenantId() );
        userProfileResponse.setCreatedAt( user.getCreatedAt() );
        userProfileResponse.setUpdatedAt( user.getUpdatedAt() );
        userProfileResponse.setContact( contactToContactDto( user.getContact() ) );
        userProfileResponse.setComponents( userComponentCollectionToAppComponentDtoCollection( user.getComponents() ) );

        return userProfileResponse;
    }

    @Override
    public AppUserDto toDto(AppUser user) {
        if ( user == null ) {
            return null;
        }

        AppUserDto appUserDto = new AppUserDto();

        appUserDto.setAvatar( AppUserMapper.getAvatarUrl( user.getId() ) );
        appUserDto.setContactId( contactToUUID( user.getContact() ) );
        appUserDto.setId( user.getId() );
        appUserDto.setEmail( user.getEmail() );
        appUserDto.setFirstName( user.getFirstName() );
        appUserDto.setLastName( user.getLastName() );
        appUserDto.setPhone( user.getPhone() );
        if ( user.getAuthority() != null ) {
            appUserDto.setAuthority( user.getAuthority().name() );
        }
        if ( user.getRole() != null ) {
            appUserDto.setRole( user.getRole().name() );
        }
        appUserDto.setTenantId( user.getTenantId() );
        appUserDto.setCreatedAt( user.getCreatedAt() );
        appUserDto.setUpdatedAt( user.getUpdatedAt() );
        appUserDto.setCreatedBy( user.getCreatedBy() );
        appUserDto.setUpdatedBy( user.getUpdatedBy() );

        return appUserDto;
    }

    protected ContactDto contactToContactDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDto contactDto = new ContactDto();

        contactDto.setId( contact.getId() );
        contactDto.setName( contact.getName() );
        contactDto.setTaxNumber( contact.getTaxNumber() );
        contactDto.setEmail( contact.getEmail() );
        contactDto.setPhone( contact.getPhone() );
        contactDto.setField( contact.getField() );
        contactDto.setDescription( contact.getDescription() );
        contactDto.setAvatar( contact.getAvatar() );
        contactDto.setIsDeleted( contact.getIsDeleted() );
        contactDto.setTenantId( contact.getTenantId() );
        contactDto.setCreatedAt( contact.getCreatedAt() );
        contactDto.setUpdatedAt( contact.getUpdatedAt() );

        return contactDto;
    }

    protected AppComponentDto userComponentToAppComponentDto(UserComponent userComponent) {
        if ( userComponent == null ) {
            return null;
        }

        AppComponentDto appComponentDto = new AppComponentDto();

        appComponentDto.setId( userComponent.getId() );
        List<String> list = userComponent.getPermissions();
        if ( list != null ) {
            appComponentDto.setPermissions( new ArrayList<String>( list ) );
        }

        return appComponentDto;
    }

    protected Collection<AppComponentDto> userComponentCollectionToAppComponentDtoCollection(Collection<UserComponent> collection) {
        if ( collection == null ) {
            return null;
        }

        Collection<AppComponentDto> collection1 = new ArrayList<AppComponentDto>( collection.size() );
        for ( UserComponent userComponent : collection ) {
            collection1.add( userComponentToAppComponentDto( userComponent ) );
        }

        return collection1;
    }
}
