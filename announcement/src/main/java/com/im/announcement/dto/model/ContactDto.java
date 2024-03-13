package com.im.announcement.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JSON Object of contact")
public class ContactDto implements Serializable {
    @Schema(example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID id;
    @Schema(description = "Name of the contact", example = "Company A")
    private String name;
    @Schema(description = "Tax number of the contact", example = "0987654321")
    private String taxNumber;
    @Schema(description = "Email of the contact", example = "example@company.com")
    private String email;
    @Schema(description = "Phone number of the contact", example = "0123456789")
    private String phone;
    @Schema(description = "Field of the contact", example = "Technology")
    private String field;
    @Schema(description = "Description of the contact")
    private String description;
    @Schema(description = "File link avatar of the contact")
    private String avatar;
    @Schema(description = "Boolean value represent the contact state is deleted or not")
    private Boolean isDeleted;
    private UUID tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
