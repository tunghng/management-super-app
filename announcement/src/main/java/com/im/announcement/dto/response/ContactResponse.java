package com.im.announcement.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.im.announcement.dto.model.ContactDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JSON Object of contact")
public class ContactResponse extends ContactDto {
    @JsonIgnore
    private UUID tenantId;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
}
