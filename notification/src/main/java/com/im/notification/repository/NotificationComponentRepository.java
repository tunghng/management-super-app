package com.im.notification.repository;

import com.im.notification.model.NotificationComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationComponentRepository extends JpaRepository<NotificationComponent, UUID> {
    NotificationComponent findByEntityId(UUID entityId);
}
