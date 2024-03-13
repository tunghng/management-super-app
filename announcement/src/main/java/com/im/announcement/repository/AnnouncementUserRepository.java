package com.im.announcement.repository;

import com.im.announcement.model.Announcement;
import com.im.announcement.model.AnnouncementUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnouncementUserRepository extends JpaRepository<AnnouncementUser, UUID> {
    Optional<AnnouncementUser> findByAnnouncementAndUserId(Announcement announcement, UUID id);

    Optional<AnnouncementUser> findByAnnouncementIdAndUserId(UUID announcementId, UUID userId);

    @Query("SELECT COUNT(au) FROM AnnouncementUser au " +
            "WHERE au.isRead = :isRead " +
            "AND au.userId = :userId " +
            "AND au.announcement.isDeleted = false")
    Long countByUserIdAndIsRead(UUID userId, Boolean isRead);
}