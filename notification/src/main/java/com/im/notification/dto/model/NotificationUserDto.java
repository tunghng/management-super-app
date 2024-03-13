package com.im.notification.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationUserDto {
    private NotificationComponentDto component;
    private UUID userId;
    private Boolean isRead;
}
