package com.im.announcement.dto.model;

import com.im.announcement.dto.response.ContactResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementSaveDto {
    @Schema(example = "784f394c-42b6-435a-983c-b7beff2784f9")
    private UUID id;
    @Schema(description = "Headline of the announcement")
    private String headline;
    @Schema(description = "Body of the announcement")
    @Column(length = 10485760)
    private String body;
    @Schema(description = "Priority of the announcement. Default MEDIUM",
            example = "MEDIUM",
            allowableValues = {"HIGH", "MEDIUM", "LOW"})
    private String priority;
    private Boolean isDeleted;
    private Boolean isRead;
    private List<ContactResponse> contacts;
    @Schema(description = "List of file links attached to the announcement")
    private Collection<String> attachedFile;
}