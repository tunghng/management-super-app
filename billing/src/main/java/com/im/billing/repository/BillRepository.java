package com.im.billing.repository;

import com.im.billing.model.Bill;
import com.im.billing.model.enums.BillState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    @Query(value = "SELECT b FROM Bill b " +
            "WHERE b.tenantId = :tenantId " +
            "AND (b.createdAt BETWEEN COALESCE(:createdAtStartTs, b.createdAt) AND COALESCE(:createdAtEndTs, b.createdAt)) " +
            "AND (b.dueDate BETWEEN COALESCE(:dueDateStartTs, b.dueDate) AND COALESCE(:dueDateEndTs, b.dueDate)) " +
            "AND (b.updatedAt BETWEEN COALESCE(:updatedAtStartTs, b.updatedAt) AND COALESCE(:updatedAtEndTs, b.updatedAt)) " +
            "AND (COALESCE(:typeIds) IS NULL OR b.type.id IN (:typeIds)) " +
            "AND (COALESCE(:contactIds) IS NULL OR b.contact.id IN (:contactIds)) " +
            "AND (COALESCE(:states) IS NULL OR b.state IN (:states)) " +
            "AND (:isDeleted IS NULL OR b.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(b.title, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(b.code, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(b.type.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) "
    )
    Page<Bill> findBills(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("states") List<BillState> stateList,
            @Param("tenantId") UUID tenantId,
            @Param("contactIds") List<UUID> contactIdList,
            @Param("typeIds") List<UUID> typeIdList,
            @Param("isDeleted") Boolean isDeleted,
            @Param("createdAtStartTs") LocalDateTime createdAtStartTs,
            @Param("createdAtEndTs") LocalDateTime createdAtEndTs,
            @Param("dueDateStartTs") LocalDateTime dueDateStartTs,
            @Param("dueDateEndTs") LocalDateTime dueDateEndTs,
            @Param("updatedAtStartTs") LocalDateTime updatedAtStartTs,
            @Param("updatedAtEndTs") LocalDateTime updatedAtEndTs,
            Pageable pageable
    );

    @Query(value = "SELECT b FROM Bill b " +
            "WHERE b.contact.id = :contactId " +
            "AND (b.createdAt BETWEEN COALESCE(:createdAtStartTs, b.createdAt) AND COALESCE(:createdAtEndTs, b.createdAt)) " +
            "AND (b.dueDate BETWEEN COALESCE(:dueDateStartTs, b.dueDate) AND COALESCE(:dueDateEndTs, b.dueDate)) " +
            "AND (b.updatedAt BETWEEN COALESCE(:updatedAtStartTs, b.updatedAt) AND COALESCE(:updatedAtEndTs, b.updatedAt)) " +
            "AND (COALESCE(:typeIds) IS NULL OR b.type.id IN (:typeIds)) " +
            "AND (COALESCE(:states) IS NULL OR b.state IN (:states)) " +
            "AND (:isDeleted IS NULL OR b.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(LOWER(b.title), :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%') " +
            "OR convertToNonSigned(LOWER(b.code), :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%') " +
            "OR convertToNonSigned(LOWER(b.type.name), :isSearchMatchCase) LIKE CONCAT('%',LOWER(:searchText),'%')) "
    )
    Page<Bill> findBillsByContactId(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("states") List<BillState> state,
            @Param("contactId") UUID contactId,
            @Param("typeIds") List<UUID> typeIdList,
            @Param("isDeleted") Boolean isDeleted,
            @Param("createdAtStartTs") LocalDateTime createdAtStartTs,
            @Param("createdAtEndTs") LocalDateTime createdAtEndTs,
            @Param("dueDateStartTs") LocalDateTime dueDateStartTs,
            @Param("dueDateEndTs") LocalDateTime dueDateEndTs,
            @Param("updatedAtStartTs") LocalDateTime updatedAtStartTs,
            @Param("updatedAtEndTs") LocalDateTime updatedAtEndTs,
            Pageable pageable
    );

    @Query(value = "SELECT b FROM Bill b " +
            "WHERE b.tenantId = :tenantId " +
            "AND (cast(:contactId as org.hibernate.type.PostgresUUIDType) IS NULL OR b.contact.id = :contactId) " +
            "AND (:state IS NULL OR b.state = :state) " +
            "AND (cast(:fromContactId as org.hibernate.type.PostgresUUIDType) IS NULL OR b.contact.id = :fromContactId) " +
            "AND b.isDeleted = false " +
            "AND (cast(:typeId as org.hibernate.type.PostgresUUIDType) IS NULL OR b.type.id = :typeId) " +
            "AND b.createdAt BETWEEN :startTs AND :endTs ORDER BY b.createdAt ASC")
    List<Bill> findBillsFromStartToEndOrderByCreatedAtAsc(
            @Param("startTs") LocalDateTime startTs,
            @Param("endTs") LocalDateTime endTs,
            @Param("tenantId") UUID tenantId,
            @Param("contactId") UUID contactId,
            @Param("state") BillState state,
            @Param("typeId") UUID typeId,
            @Param("fromContactId") UUID fromContactId
    );

    @Query(value = "SELECT b FROM Bill b " +
            "WHERE b.tenantId = :tenantId " +
            "AND (cast(:contactId as org.hibernate.type.PostgresUUIDType) IS NULL OR b.contact.id = :contactId) " +
            "AND (:state IS NULL OR b.state = :state) " +
            "AND (cast(:typeId as org.hibernate.type.PostgresUUIDType) IS NULL OR b.type.id = :typeId) " +
            "AND b.isDeleted = false " +
            "AND (cast(:fromContactId as org.hibernate.type.PostgresUUIDType) IS NULL OR b.contact.id = :fromContactId) " +
            "ORDER BY b.createdAt ASC ")
    List<Bill> findBillsOrderByCreatedAtAsc(
            @Param("tenantId") UUID tenantId,
            @Param("contactId") UUID contactId,
            @Param("state") BillState state,
            @Param("typeId") UUID typeId,
            @Param("fromContactId") UUID fromContactId
    );

    @Query(value = "SELECT b from Bill b WHERE b.isDeleted = :isDeleted")
    Page<Bill> findBillsWithIsDeleted(@Param("isDeleted") Boolean isDeleted, Pageable pageable);

    Optional<Bill> findByIdAndTenantId(UUID billId, UUID tenantId);
}
