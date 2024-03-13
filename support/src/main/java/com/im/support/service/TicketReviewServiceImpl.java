package com.im.support.service;

import com.im.support.dto.model.ReviewDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.model.Review;
import com.im.support.model.Ticket;
import com.im.support.model.TicketReview;
import com.im.support.repository.ReviewRepository;
import com.im.support.repository.TicketRepository;
import com.im.support.repository.TicketReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketReviewServiceImpl implements TicketReviewService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TicketReviewRepository ticketReviewRepository;

    @Override
    public void save(TicketDto ticketDto, ReviewDto reviewDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId()).orElse(null);
        Review review = reviewRepository.findById(reviewDto.getId()).orElse(null);
        TicketReview ticketReview = new TicketReview(ticket, review);
        ticketReviewRepository.save(ticketReview);
    }
}
