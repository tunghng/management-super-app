package com.im.support.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketFeedbackDto implements Serializable {
    UUID ticketId;
    UUID feedbackId;
}
