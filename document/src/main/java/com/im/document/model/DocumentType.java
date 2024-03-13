package com.im.document.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentType extends BaseEntity implements HasTenantId {
    private String name;
    private UUID tenantId;
}
