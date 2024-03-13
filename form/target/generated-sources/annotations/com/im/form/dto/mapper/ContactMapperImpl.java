package com.im.form.dto.mapper;

import com.im.form.dto.model.ContactDto;
import com.im.form.dto.response.ContactInfoDto;
import com.im.form.model.Contact;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T10:40:27+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public Contact toModel(ContactDto contactDto) {
        if ( contactDto == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setId( contactDto.getId() );
        contact.setName( contactDto.getName() );
        contact.setTaxNumber( contactDto.getTaxNumber() );
        contact.setEmail( contactDto.getEmail() );
        contact.setPhone( contactDto.getPhone() );
        contact.setField( contactDto.getField() );
        contact.setAvatar( contactDto.getAvatar() );
        contact.setDescription( contactDto.getDescription() );
        contact.setIsDeleted( contactDto.getIsDeleted() );
        contact.setTenantId( contactDto.getTenantId() );
        contact.setCreatedAt( contactDto.getCreatedAt() );
        contact.setUpdatedAt( contactDto.getUpdatedAt() );

        return contact;
    }

    @Override
    public ContactDto toDto(Contact contact) {
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

    @Override
    public ContactInfoDto toInfoDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactInfoDto contactInfoDto = new ContactInfoDto();

        contactInfoDto.setId( contact.getId() );
        contactInfoDto.setName( contact.getName() );

        return contactInfoDto;
    }
}
