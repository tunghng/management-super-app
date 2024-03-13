package com.im.notification.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationUserResponseDto {
    private String componentName;
    private String entityType;
    private UUID entityId;
    private UUID userId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
