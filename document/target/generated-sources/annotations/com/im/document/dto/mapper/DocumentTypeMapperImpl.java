package com.im.document.dto.mapper;

import com.im.document.dto.model.DocumentTypeDto;
import com.im.document.model.DocumentType;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-01T15:18:53+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class DocumentTypeMapperImpl implements DocumentTypeMapper {

    @Override
    public DocumentTypeDto toDto(DocumentType documentType) {
        if ( documentType == null ) {
            return null;
        }

        DocumentTypeDto documentTypeDto = new DocumentTypeDto();

        documentTypeDto.setId( documentType.getId() );
        documentTypeDto.setName( documentType.getName() );
        documentTypeDto.setTenantId( documentType.getTenantId() );

        return documentTypeDto;
    }

    @Override
    public DocumentType toModel(DocumentTypeDto documentTypeDto) {
        if ( documentTypeDto == null ) {
            return null;
        }

        DocumentType documentType = new DocumentType();

        documentType.setId( documentTypeDto.getId() );
        documentType.setName( documentTypeDto.getName() );
        documentType.setTenantId( documentTypeDto.getTenantId() );

        return documentType;
    }
}
