package com.im.billing.repository;

import com.im.billing.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    @Query(value = "SELECT u FROM AppUser u WHERE u.email=:email")
    AppUser findByEmail(String email);
    List<AppUser> findByRole(String role);
    List<AppUser> findByTenantIdAndContactId(UUID tenantId, UUID contactId);
}
