package com.im.support.repository;

import com.im.support.model.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {

    @Query("SELECT t FROM TicketType t "
            + "WHERE t.tenantId=:tenantId "
            + "AND LOWER(t.name) LIKE CONCAT('%',LOWER(:searchText),'%')"
    )
    Page<TicketType> findTicketTypes(
            @Param("searchText") String searchText,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    TicketType findByNameAndTenantId(String name, UUID tenantId);

    TicketType findByIdAndTenantId(UUID id, UUID tenantId);

    TicketType findByName(String name);
}
