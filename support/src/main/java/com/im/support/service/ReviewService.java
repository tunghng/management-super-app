package com.im.support.service;

import com.im.support.dto.model.AppUserDto;
import com.im.support.dto.model.ReviewDto;
import com.im.support.dto.model.TicketDto;

public interface ReviewService {

    ReviewDto findReviewByTicketId(TicketDto ticketDto, AppUserDto currentUser);

    ReviewDto save(ReviewDto reviewDto, TicketDto ticketDto, AppUserDto userDto);
}
