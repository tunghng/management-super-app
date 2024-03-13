package com.im.document.dto.mapper;

import com.im.document.dto.model.ContactDto;
import com.im.document.model.Contact;
import com.im.document.model.Contact.ContactBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T10:40:28+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public Contact toModel(ContactDto contactDto) {
        if ( contactDto == null ) {
            return null;
        }

        ContactBuilder contact = Contact.builder();

        contact.id( contactDto.getId() );
        contact.name( contactDto.getName() );
        contact.taxNumber( contactDto.getTaxNumber() );
        contact.email( contactDto.getEmail() );
        contact.phone( contactDto.getPhone() );
        contact.field( contactDto.getField() );
        contact.avatar( contactDto.getAvatar() );
        contact.description( contactDto.getDescription() );
        contact.isDeleted( contactDto.getIsDeleted() );
        contact.tenantId( contactDto.getTenantId() );
        contact.createdAt( contactDto.getCreatedAt() );
        contact.updatedAt( contactDto.getUpdatedAt() );

        return contact.build();
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
}
