package com.im.announcement.repository;

import com.im.announcement.model.AnnouncementCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementCodeRepository extends JpaRepository<AnnouncementCode, String> {
}
