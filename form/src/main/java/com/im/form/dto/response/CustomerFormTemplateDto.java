package com.im.form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerFormTemplateDto {
    private UUID id;
    private String code;
    private String name;
    private Boolean isPublic;
    private Integer count;
    private Integer countUnread;
    private String pathUrl;
    private LocalDateTime latestUpdatedAt;
    private ContactInfoDto contact;
    private Date updatedAt;
}
