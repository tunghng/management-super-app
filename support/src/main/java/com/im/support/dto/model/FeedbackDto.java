package com.im.support.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto implements Serializable {

    @JsonIgnore
    private UUID id;

    private String title;

    private String body;

    @JsonIgnore
    private UUID ticketId;

    private LocalDateTime updatedAt;

    private AppUserDto updatedBy;

    private Collection<String> attachedFile;
}
