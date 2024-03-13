package com.im.billing.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillTypeDto {
    private UUID id;
    @NotNull
    @NotBlank
    private String name;
    private UUID tenantId;
    private Date createdAt;
    private Date updatedAt;
}
