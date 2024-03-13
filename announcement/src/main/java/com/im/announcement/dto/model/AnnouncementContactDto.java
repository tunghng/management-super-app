package com.im.announcement.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementContactDto {
    UUID announcementId;
    UUID contactId;
}
