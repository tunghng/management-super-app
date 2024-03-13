package com.im.support.service;

import com.im.support.dto.model.FeedbackDto;
import com.im.support.dto.model.TicketDto;
import com.im.support.model.Feedback;
import com.im.support.model.Ticket;
import com.im.support.model.TicketFeedback;
import com.im.support.repository.FeedbackRepository;
import com.im.support.repository.TicketFeedbackRepository;
import com.im.support.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketFeedbackServiceImpl implements TicketFeedbackService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    TicketFeedbackRepository ticketFeedbackRepository;

    @Override
    public void save(TicketDto ticketDto, FeedbackDto feedbackDto) {
        Ticket ticket = ticketRepository.findById(ticketDto.getId()).orElse(null);
        Feedback feedback = feedbackRepository.findById(feedbackDto.getId()).orElse(null);
        TicketFeedback ticketFeedback = new TicketFeedback(ticket, feedback);
        ticketFeedbackRepository.save(ticketFeedback);
    }

}
