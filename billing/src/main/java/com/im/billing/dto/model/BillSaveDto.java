package com.im.billing.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillSaveDto {
    @Schema(description = "Bill Id (only update)")
    private UUID id;
    @NotEmpty
    @Schema(description = "Bill Title",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(description = "Bill Description")
    private String description;
    @NotEmpty
    @Min(1)
    @Schema(description = "Bill Price. Min is 1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private long price;
    @Schema(description = "Bill State. Default is UNPAID")
    private String state;
    @Schema(description = "Bill Type Name. Deprecated.", deprecated = true) // will delete
    private String type;
    @NotEmpty
    @Schema(description = "Bill Type Id",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID typeId;
    @NotEmpty
    @Schema(description = "Bill Contact Id",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID contactId;
    @NotEmpty
    @Schema(description = "Bill due date to pay, timestamp type",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private long dueDateTs;
    @Schema(description = "Bill File Attached")
    private Collection<String> attachedFile;
}
