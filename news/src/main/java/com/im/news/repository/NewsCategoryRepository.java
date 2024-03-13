package com.im.news.repository;

import com.im.news.model.NewsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, UUID> {

    @Query("SELECT nc FROM NewsCategory nc "
            + "WHERE nc.tenantId=:tenantId "
            + "AND convertToNonSigned(nc.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') "
    )
    Page<NewsCategory> findNewsCategories(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    NewsCategory findByNameAndTenantId(String name, UUID tenantId);

    NewsCategory findByName(String name);

    NewsCategory findByIdAndTenantId(UUID id, UUID tenantId);
}
