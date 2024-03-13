package com.im.document.service;

import com.im.document.dto.model.AppUserDto;
import com.im.document.dto.model.DocumentDto;
import com.im.document.dto.model.DocumentSaveDto;
import com.im.document.dto.response.page.PageData;
import com.im.document.dto.response.page.PageLink;

import java.util.List;
import java.util.UUID;

public interface DocumentService {

    PageData<DocumentDto> findDocuments(
            PageLink pageLink,
            boolean isDeleted,
            List<UUID> typeIdList,
            List<UUID> contactIdList,
            AppUserDto currentUser,
            Boolean isSearchMatchCase
    );

    DocumentDto findById(
            UUID documentId,
            UUID tenantId
    );

    DocumentDto save(
            DocumentSaveDto documentSaveDto,
            AppUserDto currentUser
    );

    String deleteDocumentById(UUID documentId);

    String restoreDocumentById(UUID documentId);

}
