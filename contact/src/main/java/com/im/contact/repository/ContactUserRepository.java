package com.im.contact.repository;

import com.im.contact.model.ContactUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactUserRepository extends JpaRepository<ContactUser, UUID> {
    @Query("SELECT cu FROM ContactUser cu " +
            "WHERE cu.contact.id = :contactId " +
            "AND (convertToNonSigned(cu.user.email, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(cu.user.phone, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(cu.user.firstName, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(cu.user.lastName, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) ")
    Page<ContactUser> findByContactId(String searchText, Boolean isSearchMatchCase, UUID contactId, Pageable pageable);

    @Query("SELECT cu FROM ContactUser cu " +
            "WHERE cu.user.tenantId=:tenantId " +
            "AND cu.contact.tenantId=:tenantId " +
            "AND (convertToNonSigned(cu.user.email, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(cu.user.phone, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(cu.user.firstName, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(cu.user.lastName, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) " +
            "ORDER BY cu.createdAt DESC "
    )
    Page<ContactUser> findByTenantId(String searchText, Boolean isSearchMatchCase, UUID tenantId, Pageable pageable);

    ContactUser findByContactIdAndUserId(UUID contactId, UUID userId);

}
