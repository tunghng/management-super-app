package com.im.filestorage.service;

import com.im.filestorage.dto.model.FileStorageDto;
import com.im.filestorage.dto.request.UpdateRequest;
import com.im.filestorage.dto.response.MultiFileResponse;
import com.im.filestorage.dto.response.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileStorageService {
    FileStorageDto saveFile(MultipartFile file, String title, UUID currentUserId, UUID tenantId);

    String deleteFile(UUID fileId, UUID userId);

    String publishFile(UUID fileId, UUID userId, UUID tenantId);

    String updateFileInfo(UpdateRequest updateRequest, UUID userId, UUID tenantId);

    FileStorageDto getFileInfo(UUID fileId, UUID tenantId);

    List<FileStorageDto> getInfoOfFiles();

    MultiFileResponse getMultiFilesInfo(UUID[] uuids, UUID tenantId);

    MultiFileResponse getMultiFilesInfo(UUID[] uuids);

    PaginationResponse getFilesInfoByTenant(UUID tenantId, int page, int size);

    PaginationResponse getFilesInfoByUser(UUID userId, int page, int size);

    ResponseEntity<Object> downloadFile(UUID fileId, UUID tenantId);

    ResponseEntity<Object> downloadFileNoAuth(UUID fileId);

    ResponseEntity<Object> getFile(UUID fileId, UUID tenantId);

    FileStorageDto saveFile(MultipartFile file, String title);
}
