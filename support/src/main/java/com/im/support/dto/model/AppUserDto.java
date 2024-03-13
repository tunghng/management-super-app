package com.im.support.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AppUserDto implements Serializable {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String authority;
    private String role;
    private String avatar;
    private UUID tenantId;
    private UUID contactId;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
