package com.im.support.repository;

import com.im.support.model.Ticket;
import com.im.support.model.TicketReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketReviewRepository extends JpaRepository<TicketReview, UUID> {

    TicketReview findByTicket(Ticket ticket);

}
