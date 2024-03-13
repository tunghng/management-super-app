package com.im.notification.dto.model;

import com.im.notification.model.enums.Authority;
import com.im.notification.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AppUserDto implements Serializable {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String avatar;
    private UUID tenantId;
    private UUID contactId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdBy;
    private LocalDateTime updatedBy;
}
