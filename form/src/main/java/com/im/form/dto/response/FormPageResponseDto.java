package com.im.form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormPageResponseDto {
    private UUID id;
    private String code;
    private String pathUrl;
    private Boolean isRead;
    private ContactInfoDto contact;
    private Date createdAt;
    private Boolean isApproved;
}
