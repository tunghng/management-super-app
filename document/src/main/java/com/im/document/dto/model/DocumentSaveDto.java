package com.im.document.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSaveDto {
    @Schema(description = "Document Id (only update)")
    private UUID id;
    @Schema(description = "Document Title",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "Document Description")
    private String description;
    @Schema(description = "Document Contact Id. Role CUSTOMER auto get contact id.")
    private UUID contactId;
    @Schema(description = "Document Type Name. Deprecated.", deprecated = true)
    private String type; // will delete
    @Schema(description = "Document Type Id",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID typeId;
    @Schema(description = "Document File Attached")
    Collection<String> attachedFile;
}
