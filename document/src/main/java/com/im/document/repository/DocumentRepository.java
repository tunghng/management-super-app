package com.im.document.repository;

import com.im.document.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    @Query("SELECT d FROM Document d " +
            "WHERE d.tenantId = :tenantId " +
            "AND (COALESCE(:typeIds) IS NULL OR d.type.id IN (:typeIds)) " +
            "AND (COALESCE(:contactIds) IS NULL OR d.contact.id IN (:contactIds)) " +
            "AND (:isDeleted IS NULL OR d.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(d.title, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(d.code, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(d.description, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(d.type.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) " +
            "ORDER BY d.createdAt DESC "
    )
    Page<Document> findDocuments(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("isDeleted") Boolean isDeleted,
            @Param("typeIds") List<UUID> typeIds,
            @Param("contactIds") List<UUID> contactIds,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    @Query("SELECT d FROM Document d " +
            "WHERE d.contact.id = :contactId " +
            "AND (:isDeleted IS NULL OR d.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(d.title, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(d.code, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(d.description, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(d.type.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) " +
            "ORDER BY d.createdAt DESC "
    )
    Page<Document> findDocumentsByContactId(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("isDeleted") Boolean isDeleted,
            @Param("contactId") UUID contactId,
            Pageable pageable
    );

    Document findByIdAndTenantId(UUID id, UUID tenantId);
}
