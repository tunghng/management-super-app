package com.im.support.repository;

import com.im.support.model.Ticket;
import com.im.support.model.TicketUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketUserRepository extends JpaRepository<TicketUser, UUID> {
    Optional<TicketUser> findByTicketAndUserId(Ticket ticket, UUID id);

    Optional<TicketUser> findByTicketIdAndUserId(UUID ticketId, UUID userId);

    @Query("SELECT COUNT(tu) FROM TicketUser tu " +
            "WHERE tu.isRead = :isRead " +
            "AND tu.userId = :userId " +
            "AND tu.ticket.isDeleted = false")
    Long countByUserIdAndIsRead(UUID userId, Boolean isRead);
}
