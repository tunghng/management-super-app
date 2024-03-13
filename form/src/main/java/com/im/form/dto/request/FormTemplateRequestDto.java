package com.im.form.dto.request;

import com.im.form.model.Addition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormTemplateRequestDto {
    private UUID id;
    @NotNull(message = "Name is required")
    private String name;
    @Column(length = 1000)
    private String description;
    private String logo;
    @Valid
    private Collection<Addition> additions;
}
