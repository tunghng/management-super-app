package com.im.billing.repository;

import com.im.billing.model.BillType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillTypeRepository extends JpaRepository<BillType, UUID> {
    @Query("SELECT b FROM BillType b "
            + "WHERE b.tenantId=:tenantId "
            + "AND convertToNonSigned(b.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') "
    )
    Page<BillType> findBillTypes(@Param("searchText") String searchText,
                                 @Param("isSearchMatchCase") Boolean isSearchMatchCase,
                                 @Param("tenantId") UUID tenantId,
                                 Pageable pageable);

    Optional<BillType> findByNameAndTenantId(String name, UUID tenantId);

    Optional<BillType> findByIdAndTenantId(UUID id, UUID tenantId);

    BillType findByName(String name);
}
