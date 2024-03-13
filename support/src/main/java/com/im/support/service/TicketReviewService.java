package com.im.support.service;

import com.im.support.dto.model.ReviewDto;
import com.im.support.dto.model.TicketDto;

public interface TicketReviewService {
    void save(TicketDto ticketDto, ReviewDto reviewDto);
}
