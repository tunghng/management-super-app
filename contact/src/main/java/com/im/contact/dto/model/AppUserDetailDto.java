package com.im.contact.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDetailDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String authority;
    private String role;
    private String avatar;
    private UUID tenantId;
    private ContactDto contact;
    private Date createdAt;
    private Date updatedAt;
}
