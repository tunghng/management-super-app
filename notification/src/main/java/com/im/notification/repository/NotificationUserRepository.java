package com.im.notification.repository;

import com.im.notification.model.Notification;
import com.im.notification.model.NotificationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, UUID> {
    NotificationUser findByNotificationAndUserId(Notification notification, UUID userId);

    @Query("SELECT n FROM NotificationUser n " +
            "WHERE n.userId = :userId " +
            "AND (:isRead IS NULL OR n.isRead = :isRead) " +
            "ORDER BY n.notification.createdAt DESC")
    Page<NotificationUser> findByUserIdAndIsRead(
            @Param("userId") UUID userId,
            @Param("isRead") Boolean isRead,
            Pageable pageable);
}
