package com.im.support.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketType extends BaseEntity implements HasTenantId {
    private String name;
    private UUID tenantId;
}
