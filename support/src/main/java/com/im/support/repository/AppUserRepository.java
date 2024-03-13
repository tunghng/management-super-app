package com.im.support.repository;


import com.im.support.model.AppUser;
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

    List<AppUser> findByTenantId(UUID tenantId);

    List<AppUser> findByRoleAndTenantId(String role, UUID tenantId);

    List<AppUser> findByTenantIdAndContactId(UUID tenantId, UUID contactId);

    List<AppUser> findByRoleAndTenantIdAndContactId(String role, UUID tenantId, UUID contactId);

}
