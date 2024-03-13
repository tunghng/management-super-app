package com.im.document.dto.mapper;

import com.im.document.dto.model.ContactDto;
import com.im.document.dto.model.DocumentDto;
import com.im.document.dto.model.DocumentTypeDto;
import com.im.document.model.Contact;
import com.im.document.model.Document;
import com.im.document.model.DocumentType;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-07T13:31:38+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class DocumentMapperImpl implements DocumentMapper {

    @Override
    public DocumentDto toDto(Document document) {
        if ( document == null ) {
            return null;
        }

        DocumentDto documentDto = new DocumentDto();

        Collection<String> collection = document.getAttachedFile();
        if ( collection != null ) {
            documentDto.setAttachedFile( new ArrayList<String>( collection ) );
        }
        documentDto.setId( document.getId() );
        documentDto.setCode( document.getCode() );
        documentDto.setTitle( document.getTitle() );
        documentDto.setDescription( document.getDescription() );
        documentDto.setContact( contactToContactDto( document.getContact() ) );
        documentDto.setType( documentTypeToDocumentTypeDto( document.getType() ) );
        documentDto.setIsDeleted( document.getIsDeleted() );
        documentDto.setTenantId( document.getTenantId() );
        documentDto.setCreatedBy( document.getCreatedBy() );
        documentDto.setUpdatedBy( document.getUpdatedBy() );
        if ( document.getCreatedAt() != null ) {
            documentDto.setCreatedAt( Date.from( document.getCreatedAt().toInstant( ZoneOffset.UTC ) ) );
        }
        if ( document.getUpdatedAt() != null ) {
            documentDto.setUpdatedAt( Date.from( document.getUpdatedAt().toInstant( ZoneOffset.UTC ) ) );
        }

        return documentDto;
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

    protected DocumentTypeDto documentTypeToDocumentTypeDto(DocumentType documentType) {
        if ( documentType == null ) {
            return null;
        }

        DocumentTypeDto documentTypeDto = new DocumentTypeDto();

        documentTypeDto.setId( documentType.getId() );
        documentTypeDto.setName( documentType.getName() );
        documentTypeDto.setTenantId( documentType.getTenantId() );

        return documentTypeDto;
    }
}
