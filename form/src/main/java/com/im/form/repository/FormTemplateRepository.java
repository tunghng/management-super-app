package com.im.form.repository;

import com.im.form.model.FormTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormTemplateRepository extends JpaRepository<FormTemplate, UUID> {
    @Query("SELECT f FROM FormTemplate f " +
            "WHERE f.tenantId=:tenantId " +
            "AND f.contactId IS NULL " +
            "AND (:isPublic IS NULL OR f.isPublic=:isPublic) " +
            "AND (:isDeleted IS NULL OR f.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(f.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(f.code.id, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) "
    )
    Page<FormTemplate> findGeneralTemplates(
            @Param("tenantId") UUID tenantId,
            @Param("isPublic") Boolean isPublic,
            @Param("isDeleted") Boolean isDeleted,
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            Pageable pageable);

    @Query("SELECT f FROM FormTemplate f " +
            "WHERE f.tenantId=:tenantId " +
            "AND f.contactId IS NOT NULL " +
            "AND (COALESCE(:contactIds) IS NULL OR f.contactId IN (:contactIds)) " +
            "AND (:isPublic IS NULL OR f.isPublic=:isPublic) " +
            "AND (:isDeleted IS NULL OR f.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(f.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(f.code.id, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) "
    )
    Page<FormTemplate> findCustomerTemplates(
            @Param("tenantId") UUID tenantId,
            @Param("contactIds") List<UUID> contactIds,
            @Param("isPublic") Boolean isPublic,
            @Param("isDeleted") Boolean isDeleted,
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            Pageable pageable);

}
