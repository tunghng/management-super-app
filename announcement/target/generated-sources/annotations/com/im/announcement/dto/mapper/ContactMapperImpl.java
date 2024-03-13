package com.im.announcement.dto.mapper;

import com.im.announcement.dto.model.ContactDto;
import com.im.announcement.dto.response.ContactJsonResponse;
import com.im.announcement.dto.response.ContactResponse;
import com.im.announcement.model.Contact;
import com.im.announcement.service.ContactService;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-09T16:39:26+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Autowired
    private ContactService contactService;

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
    public ContactDto fromJsonToDto(ContactJsonResponse jsonDto) {
        if ( jsonDto == null ) {
            return null;
        }

        ContactDto contactDto = new ContactDto();

        contactDto.setId( jsonDto.getId() );
        contactDto.setName( jsonDto.getName() );
        contactDto.setTaxNumber( jsonDto.getTaxNumber() );
        contactDto.setEmail( jsonDto.getEmail() );
        contactDto.setPhone( jsonDto.getPhone() );
        contactDto.setField( jsonDto.getField() );
        contactDto.setDescription( jsonDto.getDescription() );
        contactDto.setAvatar( jsonDto.getAvatar() );
        contactDto.setIsDeleted( jsonDto.getIsDeleted() );
        contactDto.setTenantId( jsonDto.getTenantId() );
        contactDto.setCreatedAt( contactService.toLocalDateTime( jsonDto.getCreatedAt() ) );
        contactDto.setUpdatedAt( contactService.toLocalDateTime( jsonDto.getUpdatedAt() ) );

        return contactDto;
    }

    @Override
    public ContactResponse toResponse(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactResponse contactResponse = new ContactResponse();

        contactResponse.setId( contact.getId() );
        contactResponse.setName( contact.getName() );
        contactResponse.setTaxNumber( contact.getTaxNumber() );
        contactResponse.setEmail( contact.getEmail() );
        contactResponse.setPhone( contact.getPhone() );
        contactResponse.setField( contact.getField() );
        contactResponse.setDescription( contact.getDescription() );
        contactResponse.setAvatar( contact.getAvatar() );
        contactResponse.setIsDeleted( contact.getIsDeleted() );
        contactResponse.setTenantId( contact.getTenantId() );
        contactResponse.setCreatedAt( contact.getCreatedAt() );
        contactResponse.setUpdatedAt( contact.getUpdatedAt() );

        return contactResponse;
    }
}
