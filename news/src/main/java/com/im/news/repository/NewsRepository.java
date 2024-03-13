package com.im.news.repository;

import com.im.news.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {

    @Query("SELECT n FROM News n " +
            "WHERE n.tenantId = :tenantId " +
            "AND (n.updatedAt BETWEEN COALESCE(:updatedAtStartTs, n.updatedAt) AND COALESCE(:updatedAtEndTs, n.updatedAt)) " +
            "AND (:isDeleted IS NULL OR n.isDeleted = :isDeleted) " +
            "AND (cast(:categoryId as org.hibernate.type.PostgresUUIDType) IS NULL OR n.category.id = :categoryId) " +
            "AND (:categoryName IS NULL OR n.category.name = :categoryName) " +
            "AND (convertToNonSigned(n.headline, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(n.code, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(n.category.name, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) "
    )
    Page<News> findNews(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("isDeleted") Boolean isDeleted,
            @Param("categoryId") UUID categoryId,
            @Param("categoryName") String categoryName,
            @Param("updatedAtStartTs") LocalDateTime updatedAtStartTs,
            @Param("updatedAtEndTs") LocalDateTime updatedAtEndTs,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    News findByIdAndTenantId(UUID id, UUID tenantId);
}
