package com.im.support.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact implements HasTenantId {
    @Id
    private UUID id;
    private String name;
    private String taxNumber;
    private String email;
    private String phone;
    private String field;
    @Column(length = 10485760)
    private String avatar;
    @Column(length = 10485760)
    private String description;
    private Boolean isDeleted;
    private UUID tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
