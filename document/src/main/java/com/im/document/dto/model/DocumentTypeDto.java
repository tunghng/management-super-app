package com.im.document.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentTypeDto {

    private UUID id;

    private String name;

    @JsonIgnore
    private UUID tenantId;
}
