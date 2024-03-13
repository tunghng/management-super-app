package com.im.contact.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private NotificationComponent component;
    private String message;
    private String description;
    private Date createdAt;
    private Collection<UUID> toUserIds;

    private UUID createdBy;
}