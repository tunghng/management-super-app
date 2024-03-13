package com.im.contact.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends BaseEntity implements HasTenantId {
    private String name;
    private String taxNumber;
    private String email;
    private String phone;
    private String field;
    @Column(length = 10485760)
    private String avatar;
    @Column(length = 10485760)
    private String description;
    @Column(
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT FALSE"
    )
    private Boolean isDeleted;
    private UUID tenantId;
}
