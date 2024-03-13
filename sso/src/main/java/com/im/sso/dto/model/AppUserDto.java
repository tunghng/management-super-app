package com.im.sso.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private UUID id;
    @Email
    @NotEmpty(message = "Email may not be empty")
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    @NotEmpty(message = "Authority may not be empty")
    private String authority;
    @NotEmpty(message = "Role may not be empty")
    private String role;
    private UUID tenantId;
    private UUID contactId;
    private String avatar;
    private Date createdAt;
    private Date updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
