package com.im.notification.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationComponentDto {
    private String entityType;
    private UUID entityId;
    private String componentName;
}
