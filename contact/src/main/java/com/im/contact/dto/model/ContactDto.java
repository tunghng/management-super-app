package com.im.contact.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private UUID tenantId;
    private Date createdAt;
    private Date updatedAt;
    private String avatar;
    private Boolean isDeleted;
}
