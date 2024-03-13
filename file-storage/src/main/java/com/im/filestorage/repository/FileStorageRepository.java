package com.im.filestorage.repository;

import com.im.filestorage.model.FileStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorage, UUID> {
    Page<FileStorage> findByUserId(Pageable pageable, UUID userId);

    Page<FileStorage> findByTenantId(Pageable pageable, UUID tenantId);
}
