package com.im.form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormTemplateShortInfo {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String logo;
    private UUID contactId;
}
