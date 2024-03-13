package com.im.announcement.repository;

import com.im.announcement.model.AnnouncementContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnnouncementContactRepository extends JpaRepository<AnnouncementContact, UUID> {

    AnnouncementContact findByAnnouncementIdAndContactId(UUID announcementId, UUID contactId);

    @Query("SELECT n FROM AnnouncementContact n " +
            "WHERE n.contact.id = :contactId " +
            "AND (:isDeleted IS NULL OR n.announcement.isDeleted = :isDeleted) " +
            "AND (convertToNonSigned(n.announcement.headline, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%') " +
            "OR convertToNonSigned(n.announcement.code, :isSearchMatchCase) LIKE CONCAT('%',:searchText,'%')) " +
            "ORDER BY n.createdAt DESC "
    )
    Page<AnnouncementContact> findAllByContactId(
            @Param("searchText") String searchText,
            @Param("isSearchMatchCase") Boolean isSearchMatchCase,
            @Param("contactId") UUID contactId,
            @Param("isDeleted") Boolean isDeleted,
            Pageable pageable);
}
