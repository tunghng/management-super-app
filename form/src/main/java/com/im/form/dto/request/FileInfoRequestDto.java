package com.im.form.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInfoRequestDto {
    private UUID id;
    private UUID userId;
    private UUID tenantId;
    private Boolean isPublished;
}
