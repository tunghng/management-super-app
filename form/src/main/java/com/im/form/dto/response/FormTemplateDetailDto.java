package com.im.form.dto.response;

import com.im.form.dto.model.AdditionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormTemplateDetailDto {
    private UUID id;
    private String name;
    private String description;
    private String logo;
    private Collection<AdditionDto> additions;
}
