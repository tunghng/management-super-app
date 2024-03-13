package com.im.filestorage.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileStorageDto {
    private UUID id;
    private String title;
    private LocalDateTime createAt;
    private LocalDateTime uploadAt;
    private UUID createdBy;
    private UUID updatedBy;
    private boolean isPublished;
    private String extension;
}
