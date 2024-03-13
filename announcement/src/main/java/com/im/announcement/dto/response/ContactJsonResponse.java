package com.im.announcement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactJsonResponse {
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
    private long createdAt;
    private long updatedAt;
}
