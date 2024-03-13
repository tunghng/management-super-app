package com.im.form.dto.response;

import com.im.form.dto.model.AdditionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormEmptyDto {
    private String name;
    private String description;
    private String logo;
    private Collection<AdditionDto> additions;
    private Boolean isDeleted;
}
