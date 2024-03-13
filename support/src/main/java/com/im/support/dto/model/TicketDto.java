package com.im.support.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private UUID id;
    private String title;
    private String code;
    private String description;
    private String priority;
    private String state;
    private Boolean isRead;
    private Boolean isDeleted;
    private UUID tenantId;
    private AppUserDto createdBy;
    private AppUserDto updatedBy;
    private AppUserDto closedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private Collection<String> attachedFile;
    private ContactDto contact;
    private ReviewShortDto review;
    private TicketTypeDto type;
}
