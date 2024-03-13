package com.im.support.repository;

import com.im.support.model.Ticket;
import com.im.support.model.enums.TicketState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(value = "SELECT t FROM Ticket t " +
            "WHERE t.tenantId = :tenantId " +
            "AND (t.createdAt BETWEEN COALESCE(:createdAtStartTs, t.createdAt) AND COALESCE(:createdAtEndTs, t.createdAt)) " +
            "AND (t.updatedAt BETWEEN COALESCE(:updatedAtStartTs, t.updatedAt) AND COALESCE(:updatedAtEndTs, t.updatedAt)) " +
            "AND (COALESCE(:typeIds) IS NULL OR t.type.id IN (:typeIds)) " +
            "AND (COALESCE(:contactIds) IS NULL OR t.contact.id IN (:contactIds)) " +
            "AND (:state IS NULL OR t.state = :state) " +
            "AND (:isDeleted IS NULL OR t.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(t.title, :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%') " +
            "OR convertToNonSigned(t.code, :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%')) "
    )
    Page<Ticket> findTickets(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("state") TicketState state,
            @Param("tenantId") UUID tenantId,
            @Param("contactIds") List<UUID> contactIds,
            @Param("typeIds") List<UUID> typeIds,
            @Param("isDeleted") Boolean isDeleted,
            @Param("createdAtStartTs") LocalDateTime createdAtStartTs,
            @Param("createdAtEndTs") LocalDateTime createdAtEndTs,
            @Param("updatedAtStartTs") LocalDateTime updatedAtStartTs,
            @Param("updatedAtEndTs") LocalDateTime updatedAtEndTs,
            Pageable pageable
    );

    @Query(value = "SELECT t FROM Ticket t " +
            "WHERE t.contact.id = :contactId " +
            "AND (t.createdAt BETWEEN COALESCE(:createAtStartTs, t.createdAt) AND COALESCE(:createdAtEndTs, t.createdAt)) " +
            "AND (t.updatedAt BETWEEN COALESCE(:updatedAtStartTs, t.updatedAt) AND COALESCE(:updatedAtEndTs, t.updatedAt)) " +
            "AND (COALESCE(:typeIds) IS NULL OR t.type.id IN (:typeIds)) " +
            "AND (:state IS NULL OR t.state = :state) " +
            "AND (:isDeleted IS NULL OR t.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(t.title, :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%') " +
            "OR convertToNonSigned(t.code, :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%')) "
    )
    Page<Ticket> findTicketsByContactId(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("state") TicketState state,
            @Param("contactId") UUID contactId,
            @Param("typeIds") List<UUID> typeIds,
            @Param("isDeleted") Boolean isDeleted,
            @Param("createAtStartTs") LocalDateTime createAtStartTs,
            @Param("createdAtEndTs") LocalDateTime createdAtEndTs,
            @Param("updatedAtStartTs") LocalDateTime updatedAtStartTs,
            @Param("updatedAtEndTs") LocalDateTime updatedAtEndTs,
            Pageable pageable
    );

    @Query(value = "SELECT t FROM Ticket t " +
            "WHERE t.tenantId = :tenantId " +
            "AND (COALESCE(:typeIds) IS NULL OR t.type.id IN (:typeIds)) " +
            "AND (COALESCE(:contactIds) IS NULL OR t.contact.id IN (:contactIds)) " +
            "AND t.createdAt BETWEEN :startTs AND :endTs ORDER BY t.createdAt ASC")
    List<Ticket> findTicketsFromStartToEndOrderByCreatedAtAsc(
            @Param("startTs") LocalDateTime startTs,
            @Param("endTs") LocalDateTime endTs,
            @Param("tenantId") UUID tenantId,
            @Param("contactIds") List<UUID> contactIds,
            @Param("typeIds") List<UUID> typeIds
    );

    @Query(value = "SELECT t FROM Ticket t " +
            "WHERE t.tenantId = :tenantId " +
            "AND (COALESCE(:typeIds) IS NULL OR t.type.id IN (:typeIds)) " +
            "AND (COALESCE(:contactIds) IS NULL OR t.contact.id IN (:contactIds)) " +
            "ORDER BY t.createdAt ASC ")
    List<Ticket> findTicketsOrderByCreatedAtAsc(
            @Param("tenantId") UUID tenantId,
            @Param("contactIds") List<UUID> contactIds,
            @Param("typeIds") List<UUID> typeIds
    );

    Optional<Ticket> findByIdAndTenantId(UUID id, UUID tenantId);
}
