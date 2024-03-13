package com.im.document.dto.mapper;

import com.im.document.dto.model.DocumentTypeDto;
import com.im.document.model.DocumentType;
import org.mapstruct.Mapper;

@Mapper
public interface DocumentTypeMapper {

    DocumentTypeDto toDto(DocumentType documentType);

    DocumentType toModel(DocumentTypeDto documentTypeDto);
}
