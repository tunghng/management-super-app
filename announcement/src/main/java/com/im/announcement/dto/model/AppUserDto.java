package com.im.announcement.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@Schema(description = "JSON Object of user")
public class AppUserDto implements Serializable {
    @Schema(example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID id;
    @Schema(description = "Email of the user", example = "example@person.com")
    private String email;
    @Schema(description = "First name of the user", example = "John")
    private String firstName;
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;
    @Schema(description = "Phone number of the user", example = "0987654321")
    private String phone;
    @Schema(description = "Authority of the user",
            example = "SYS_ADMIN",
            allowableValues = {"SYS_ADMIN", "TENANT_ADMIN", "CUSTOMER_USER"})
    private String authority;
    @Schema(description = "Role of the user", example = "SYS_ADMIN",
            allowableValues = {"SYS_ADMIN", "TENANT", "MANAGER", "CUSTOMER"})
    private String role;
    @Schema(description = "Link file avatar of the user")
    private String avatar;
    @Schema(description = "Tenant Id of the user", example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID tenantId;
    @Schema(description = "Contact Id of the user", example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID contactId;
    @Schema(description = "Time of user creation")
    private Date createdAt;
    @Schema(description = "Time of user update")
    private Date updatedAt;
    @Schema(description = "User Id of user creation")
    private UUID createdBy;
    @Schema(description = "User Id of user update")
    private UUID updatedBy;
}