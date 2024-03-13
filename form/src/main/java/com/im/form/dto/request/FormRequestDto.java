package com.im.form.dto.request;

import com.im.form.dto.model.AdditionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormRequestDto {
    @NotNull(message = "Form template ID is required")
    private UUID formTemplateId;
    @Valid
    private Collection<AdditionDto> data;
}
