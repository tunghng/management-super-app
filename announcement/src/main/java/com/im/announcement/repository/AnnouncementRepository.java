package com.im.announcement.repository;

import com.im.announcement.model.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {

    @Query("SELECT n FROM Announcement n " +
            "WHERE n.tenantId = :tenantId " +
            "AND (:isDeleted IS NULL OR n.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(n.headline, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(n.code, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) " +
            "ORDER BY n.createdAt DESC "
    )
    Page<Announcement> findAnnouncements(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("isDeleted") Boolean isDeleted,
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    Optional<Announcement> findByIdAndTenantId(UUID id, UUID tenantId);
}
