package com.im.support.dto.model;

import com.im.support.model.TicketType;
import com.im.support.model.enums.TicketState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSaveDto {
    @Schema(description = "Ticket Id (only update)")
    private UUID id;
    @NotEmpty
    @Schema(description = "Ticket Title",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "Ticket Description")
    private String description;
    @Schema(description = "Ticket Priority. Default MEDIUM")
    private String priority;
    @Schema(description = "Ticket State. Default PROCESSING")
    private TicketState state;
    @NotEmpty
    @Schema(description = "Ticket Type Id",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID typeId;
    @Schema(description = "Ticket Contact Id. Role CUSTOMER auto get contact id.")
    private UUID contactId;
    @Schema(description = "Ticket File Attached")
    private Collection<String> attachedFile;
}
