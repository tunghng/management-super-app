package com.im.news.dto.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsSaveDto {
    Collection<String> attachedFile;
    private UUID id;
    private String code;
    private String cover;
    private String headline;
    private String body;
    private String category;
    private UUID categoryId;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}