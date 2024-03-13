package com.im.document.service;

import com.im.document.dto.model.DocumentTypeDto;
import com.im.document.dto.response.page.PageData;
import com.im.document.dto.response.page.PageLink;
import com.im.document.model.DocumentType;

import java.util.UUID;

public interface DocumentTypeService {

    PageData<DocumentTypeDto> findDocumentTypes(
            PageLink pageLink,
            UUID tenantId,
            Boolean isSearchMatchCase
    );

    DocumentTypeDto save(
            DocumentTypeDto documentTypeDto,
            UUID tenantId
    );

    DocumentType toModel(String type);

    String toString(DocumentType type);
}
