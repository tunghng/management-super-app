package com.im.support.repository;

import com.im.support.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    Page<Feedback> findAllByTicketIdOrderByUpdatedAtDesc(
            UUID ticketId,
            Pageable pageable
    );
}
