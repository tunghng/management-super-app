package com.im.announcement.dto.model;

import com.im.announcement.dto.response.ContactResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDto {
    @Schema(example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID id;
    @Schema(description = "Headline of the announcement")
    private String headline;
    @Schema(description = "Body of the announcement")
    @Column(length = 10485760)
    private String body;
    @Schema(description = "Priority of the announcement",
            example = "MEDIUM",
            allowableValues = {"HIGH", "MEDIUM", "LOW"})
    private String priority;
    @Schema(description = "Code of the announcement. Code in the format of 0000001, 0000002, 0000003,... ",
            example = "0000001",
            format = "^[0-9]{7}")
    private String code;
    private Boolean isDeleted;
    private Boolean isRead;
    @Schema(description = "JSON Object of user updated the announcement")
    private AppUserDto updatedBy;
    @Schema(description = "Tenant Id of the announcement", example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID tenantId;
    @Schema(description = "Time of announcement creation")
    private Date createdAt;
    @Schema(description = "Time of announcement update")
    private Date updatedAt;
    @Schema(description = "List of contact objects associated with the announcement")
    private List<ContactResponse> contacts;
    @Schema(description = "List of file links attached to the announcement")
    private Collection<String> attachedFile;
}