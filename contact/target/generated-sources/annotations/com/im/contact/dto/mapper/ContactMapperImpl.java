package com.im.contact.dto.mapper;

import com.im.contact.dto.model.ContactDto;
import com.im.contact.model.Contact;
import java.time.ZoneOffset;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-09T16:40:28+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public ContactDto toDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDto contactDto = new ContactDto();

        contactDto.setAvatar( ContactMapper.getAvatarUrl( contact.getId() ) );
        contactDto.setId( contact.getId() );
        contactDto.setName( contact.getName() );
        contactDto.setTaxNumber( contact.getTaxNumber() );
        contactDto.setEmail( contact.getEmail() );
        contactDto.setPhone( contact.getPhone() );
        contactDto.setField( contact.getField() );
        contactDto.setDescription( contact.getDescription() );
        contactDto.setTenantId( contact.getTenantId() );
        if ( contact.getCreatedAt() != null ) {
            contactDto.setCreatedAt( Date.from( contact.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( contact.getUpdatedAt() != null ) {
            contactDto.setUpdatedAt( Date.from( contact.getUpdatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        contactDto.setIsDeleted( contact.getIsDeleted() );

        return contactDto;
    }
}
