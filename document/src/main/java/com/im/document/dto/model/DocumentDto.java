package com.im.document.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    Collection<String> attachedFile;
    private UUID id;
    private String code;
    private String title;
    private String description;
    private ContactDto contact;
    private DocumentTypeDto type;
    private Boolean isDeleted;
    private UUID tenantId;
    private UUID createdBy;
    private UUID updatedBy;
    private Date createdAt;
    private Date updatedAt;
}
