package com.im.support.repository;

import com.im.support.model.TicketFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketFeedbackRepository extends JpaRepository<TicketFeedback, UUID> {
}
