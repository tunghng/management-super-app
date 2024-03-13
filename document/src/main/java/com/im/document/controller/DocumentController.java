package com.im.document.controller;


import com.im.document.dto.mapper.DocumentMapper;
import com.im.document.dto.model.AppUserDto;
import com.im.document.dto.model.DocumentDto;
import com.im.document.dto.model.DocumentSaveDto;
import com.im.document.dto.model.DocumentTypeDto;
import com.im.document.dto.response.Response;
import com.im.document.dto.response.page.PageData;
import com.im.document.dto.response.page.PageLink;
import com.im.document.service.DocumentService;
import com.im.document.service.DocumentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/document")
public class DocumentController extends BaseController {

    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentTypeService documentTypeService;

    @Autowired
    DocumentMapper documentMapper;

    @GetMapping
    @Operation(summary = "Get Documents (getDocuments)")
    public PageData<DocumentDto> getDocuments(
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "false", required = false) boolean isDeleted,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(required = false) String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Filter by multi-typeId")
            @RequestParam(required = false) List<UUID> typeId,
            @Parameter(description = "Filter by multi-contactId")
            @RequestParam(required = false) List<UUID> contactId,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        AppUserDto currentUser = getCurrentUser(request);
        return documentService.findDocuments(
                pageLink,
                isDeleted,
                typeId,
                contactId,
                currentUser,
                isSearchMatchCase
        );
    }

    @GetMapping("{documentId}")
    @Operation(summary = "Get Document By Id (getDocumentById")
    public DocumentDto getDocumentById(
            @PathVariable UUID documentId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return checkDocumentId(
                documentId,
                currentUser.getTenantId()
        );
    }

    @PostMapping
    @Operation(summary = "Save Document (saveDocument)")
    public DocumentDto saveDocument(
            @Valid @RequestBody DocumentSaveDto documentSaveDto,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return documentService.save(
                documentSaveDto,
                currentUser
        );
    }

    @DeleteMapping("{documentId}")
    @Operation(summary = "Delete Document by Id (restoreDocumentById")
    public Response deleteDocument(
            @PathVariable UUID documentId,
            HttpServletRequest request
    ) {
        checkDocumentId(documentId, getCurrentUser(request).getTenantId());
        return new Response(200, documentService.deleteDocumentById(documentId));
    }

    @PutMapping("{documentId}/restore")
    @Operation(summary = "Restore Document by Id (restoreDocumentById")
    public Response restoreDocument(
            @PathVariable UUID documentId,
            HttpServletRequest request
    ) {
        checkDocumentId(documentId, getCurrentUser(request).getTenantId());
        return new Response(200, documentService.restoreDocumentById(documentId));
    }

    @GetMapping("type")
    @Operation(summary = "Get Document Type (getDocumentType)")
    public PageData<?> getDocumentCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        return documentTypeService.findDocumentTypes(
                pageLink,
                getCurrentUser(request).getTenantId(),
                isSearchMatchCase
        );
    }

    @PostMapping("type")
    @Operation(summary = "Save Document Type (saveDocumentType)")
    public DocumentTypeDto saveDocumentCategory(
            @Valid @RequestBody DocumentTypeDto categoryDto,
            HttpServletRequest request
    ) {
        return documentTypeService.save(categoryDto, getCurrentUser(request).getTenantId());
    }

}
