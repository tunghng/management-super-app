package com.im.contact.repository;

import com.im.contact.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {

    @Query("SELECT c FROM Contact c " +
            "WHERE c.tenantId=:tenantId " +
            "AND (c.createdAt BETWEEN COALESCE(:createdAtStartTs, c.createdAt) AND COALESCE(:createdAtEndTs, c.createdAt)) " +
            "AND (:isDeleted IS NULL OR c.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(c.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(c.phone, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(c.field, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(c.email, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(c.taxNumber, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) "
    )
    Page<Contact> findContacts(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("isDeleted") Boolean isDeleted,
            @Param("createdAtStartTs") LocalDateTime createdAtStartTs,
            @Param("createdAtEndTs") LocalDateTime createdAtEndTs,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    Optional<Contact> findByIdAndTenantId(UUID id, UUID tenantId);

    @Query("SELECT MIN(c.createdAt) FROM Contact c WHERE c.tenantId = :tenantId")
    LocalDateTime findMinCreatedAtByTenantId(
            @Param("tenantId") UUID tenantId
    );

    @Query("SELECT MAX(c.createdAt) FROM Contact c WHERE c.tenantId = :tenantId")
    LocalDateTime findMaxCreatedAtByTenantId(
            @Param("tenantId") UUID tenantId
    );
}
