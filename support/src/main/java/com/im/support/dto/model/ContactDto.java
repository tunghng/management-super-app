package com.im.support.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    private UUID id;
    private String name;
    private String taxNumber;
    private String email;
    private String phone;
    private String field;
    private String description;
    private String avatar;
    private Boolean isDeleted;
    private UUID tenantId;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
}
