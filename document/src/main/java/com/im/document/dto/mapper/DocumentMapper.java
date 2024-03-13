package com.im.document.dto.mapper;

import com.im.document.dto.model.DocumentDto;
import com.im.document.model.Document;
import com.im.document.service.ContactService;
import com.im.document.service.DocumentTypeService;
import org.mapstruct.Mapper;

@Mapper(uses = {ContactService.class, DocumentTypeService.class})
public interface DocumentMapper {

    DocumentDto toDto(Document document);

}
