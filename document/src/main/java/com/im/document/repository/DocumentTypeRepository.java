package com.im.document.repository;

import com.im.document.model.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {

    @Query("SELECT dt FROM DocumentType dt " +
            "WHERE dt.tenantId=:tenantId " +
            "AND convertToNonSigned(dt.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "ORDER BY dt.createdAt DESC "
    )
    Page<DocumentType> findDocumentTypes(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    DocumentType findByNameAndTenantId(String name, UUID tenantId);

    DocumentType findByIdAndTenantId(UUID id, UUID tenantId);

    DocumentType findByName(String name);
}
