package com.im.form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormTemplateInfoDto {
    private UUID id;
    private String code;
    private String name;
    private Boolean isPublic;
    private Boolean isDeleted;
    private String description;
    private String pathUrl;
    private Integer count;
    private LocalDateTime latestUpdatedAt;
    private UUID contactId;
}
