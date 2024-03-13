package com.im.support.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeDto {
    private String name;
    private UUID id;
    private UUID tenantId;
}
