package com.im.billing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod extends BaseEntity implements HasTenantId {
    private String name;
    @Column(length = 10485760)
    private String data;
    private UUID tenantId;
}
