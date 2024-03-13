package com.im.form.dto.response;

import com.im.form.dto.model.AdditionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormResponseDto {
    private String code;
    private Collection<AdditionDto> data;
    private Boolean isApproved;
    private Date createdAt;
    private UUID createdBy;
    private FormTemplateShortInfo formTemplateInfo;
}
