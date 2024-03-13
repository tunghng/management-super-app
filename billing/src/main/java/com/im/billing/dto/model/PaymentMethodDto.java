package com.im.billing.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethodDto {
    private UUID id;
    private String name;
    private String data;
    private UUID tenantId;
    private Date createdAt;
    private Date updatedAt;
}
