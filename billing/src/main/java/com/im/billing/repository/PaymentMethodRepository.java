package com.im.billing.repository;

import com.im.billing.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    @Query("SELECT p FROM PaymentMethod p "
            + "WHERE p.tenantId=:tenantId "
            + "AND convertToNonSigned(p.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') ")
    Page<PaymentMethod> findPaymentMethods(@Param("searchText") String searchText,
                                           @Param("isSearchMatchCase") Boolean isSearchMatchCase,
                                           @Param("tenantId") UUID tenantId,
                                           Pageable pageable);

    Optional<PaymentMethod> findByNameAndTenantId(String name, UUID tenantId);

    Optional<PaymentMethod> findByIdAndTenantId(UUID id, UUID tenantId);
}
