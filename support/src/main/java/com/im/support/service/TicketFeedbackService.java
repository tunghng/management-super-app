package com.im.support.service;

import com.im.support.dto.model.FeedbackDto;
import com.im.support.dto.model.TicketDto;

public interface TicketFeedbackService {
    void save(TicketDto ticketDto, FeedbackDto feedbackDto);
}
