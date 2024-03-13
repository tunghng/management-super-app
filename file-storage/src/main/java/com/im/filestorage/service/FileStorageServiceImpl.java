package com.im.filestorage.service;

import com.im.filestorage.dto.mapper.FileStorageMapper;
import com.im.filestorage.dto.model.FileStorageDto;
import com.im.filestorage.dto.request.UpdateRequest;
import com.im.filestorage.dto.response.MessageResponse;
import com.im.filestorage.dto.response.MultiFileResponse;
import com.im.filestorage.dto.response.PaginationResponse;
import com.im.filestorage.exception.BadRequestException;
import com.im.filestorage.exception.NotFoundException;
import com.im.filestorage.exception.UnAuthorizedException;
import com.im.filestorage.model.FileStorage;
import com.im.filestorage.repository.FileStorageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.storage.path}")
    private String rootPath;

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private FileStorageMapper fileStorageMapper;

    @Override
    @Transactional
    public FileStorageDto saveFile(MultipartFile file, String title, UUID currentUserId, UUID tenantId) {
        try {

            UUID fileId = UUID.randomUUID();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = FilenameUtils.getExtension(fileName);
            String filePath = "/" + fileId;

            Files.createDirectories(Paths.get(rootPath));
            Path targetLocation = Paths.get(rootPath + filePath + "." + extension);
            Files.write(targetLocation, file.getBytes());

            FileStorage fileStorage = FileStorage.builder()
                    .id(fileId)
                    .title(title)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .createdBy(currentUserId)
                    .updatedBy(currentUserId)
                    .userId(currentUserId)
                    .tenantId(tenantId)
                    .isDeleted(false)
                    .isPublished(false)
                    .filePath(filePath)
                    .extension(extension)
                    .build();
            FileStorage savedFile = fileStorageRepository.save(fileStorage);
            return fileStorageMapper.mapEntityToDto(savedFile);
        } catch (IOException e) {
            throw new BadRequestException(e.getLocalizedMessage());
        }
    }

    @Override
    public FileStorageDto saveFile(MultipartFile file, String title) {
        try {
            UUID fileId = UUID.randomUUID();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = FilenameUtils.getExtension(fileName);
            String filePath = "/" + fileId;

            Files.createDirectories(Paths.get(rootPath));
            Path targetLocation = Paths.get(rootPath + filePath + "." + extension);
            Files.write(targetLocation, file.getBytes());

            FileStorage fileStorage = FileStorage.builder()
                    .id(fileId)
                    .title(title)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isDeleted(false)
                    .isPublished(true)
                    .filePath(filePath)
                    .extension(extension)
                    .build();
            FileStorage savedFile = fileStorageRepository.save(fileStorage);
            return fileStorageMapper.mapEntityToDto(savedFile);
        } catch (IOException e) {
            throw new BadRequestException(e.getLocalizedMessage());
        }
    }

    @Override
    @Transactional
    public String deleteFile(UUID fileId, UUID userId) {
        try {
            FileStorage fileStorage = fileStorageRepository.findById(fileId)
                    .orElseThrow(() -> new NotFoundException("File not found"));
            if (!userId.equals(fileStorage.getUserId()))
                throw new UnAuthorizedException("You are not allow to delete this file");

            if (!checkFileIsStillInMemory(fileStorage)) {
                setDeleted(fileStorage);
                return "Success";
            }
            Path path = Paths.get(rootPath + fileStorage.getFilePath() + "." + fileStorage.getExtension());
            Files.delete(path);
            fileStorage.setIsDeleted(true);
            fileStorageRepository.save(fileStorage);
            return "Success";
        } catch (IOException e) {
            throw new BadRequestException("Failed to delete file");
        }
    }

    @Override
    public String publishFile(UUID fileId, UUID userId, UUID tenantId) {
        FileStorage fileStorage = fileStorageRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found"));
        if (fileStorage.getIsDeleted())
            throw new BadRequestException("File is deleted");

        if (!checkFileIsStillInMemory(fileStorage)) {
            setDeleted(fileStorage);
            throw new BadRequestException("File is deleted");
        }

        if (!tenantId.equals(fileStorage.getTenantId()))
            throw new UnAuthorizedException("You are not allow to access this file");

        fileStorage.setPublished(!fileStorage.isPublished());
        fileStorageRepository.save(fileStorage);
        return "Updated successfully";
    }

    @Override
    public String updateFileInfo(UpdateRequest updateRequest, UUID userId, UUID tenantId) {
        FileStorage fileStorage = fileStorageRepository.findById(updateRequest.getId()).orElseThrow(() -> new NotFoundException("File not found"));
        if (fileStorage.getIsDeleted())
            throw new BadRequestException("File is deleted");

        if (!checkFileIsStillInMemory(fileStorage)) {
            setDeleted(fileStorage);
            throw new BadRequestException("File is deleted");
        }

        if (!tenantId.equals(fileStorage.getTenantId()))
            throw new UnAuthorizedException("You are not allow to access this file");

        fileStorage.setTitle(updateRequest.getTitle());
        fileStorageRepository.save(fileStorage);
        return "Updated successfully";
    }

    @Override
    public FileStorageDto getFileInfo(UUID fileId, UUID tenantId) {
        FileStorage fileStorage = fileStorageRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found"));
        if (fileStorage.getIsDeleted())
            throw new BadRequestException("File is deleted");

        if (!checkFileIsStillInMemory(fileStorage)) {
            setDeleted(fileStorage);
            throw new BadRequestException("File is deleted");
        }

        if (!fileStorage.isPublished())
            checkAccessForNonPublishedFile(fileStorage, tenantId);
        return fileStorageMapper.mapEntityToDto(fileStorage);
    }

    @Override
    public List<FileStorageDto> getInfoOfFiles() {
        return fileStorageRepository.findAll().stream().map(file -> fileStorageMapper.mapEntityToDto(file)).collect(Collectors.toList());
    }

    @Override
    public MultiFileResponse getMultiFilesInfo(UUID[] uuids, UUID tenantId) {
        List<Object> data = Arrays.stream(uuids)
                .map(uuid -> findAndGetSingleFileInfo(uuid, tenantId))
                .collect(Collectors.toList());
        return getMultiFileResponse(data);
    }

    @Override
    public MultiFileResponse getMultiFilesInfo(UUID[] uuids) {
        return getMultiFilesInfo(uuids, null);
    }

    @Override
    public PaginationResponse getFilesInfoByTenant(UUID tenantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FileStorage> fileStoragePage = fileStorageRepository.findByTenantId(pageable, tenantId);
        List<Object> fileStorageData = fileStoragePage.getContent().stream()
                .map(fileStorage -> getSingleFileInfo(fileStorage, tenantId, false))
                .collect(Collectors.toList());
        return new PaginationResponse(fileStorageData,
                fileStoragePage.getTotalElements(),
                fileStoragePage.getTotalPages(),
                fileStoragePage.hasNext());
    }

    @Override
    public PaginationResponse getFilesInfoByUser(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FileStorage> fileStoragePage = fileStorageRepository.findByUserId(pageable, userId);
        List<Object> fileStorageData = fileStoragePage.getContent().stream()
                .map(fileStorage -> getSingleFileInfo(fileStorage, null, true))
                .collect(Collectors.toList());
        return new PaginationResponse(fileStorageData,
                fileStoragePage.getTotalElements(),
                fileStoragePage.getTotalPages(),
                fileStoragePage.hasNext());
    }

    @Override
    public ResponseEntity<Object> downloadFile(UUID fileId, UUID tenantId) {
        return getFileResponse(fileId, tenantId, true);
    }

    @Override
    public ResponseEntity<Object> downloadFileNoAuth(UUID fileId) {
        FileStorage fileStorage = fileStorageRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found"));
        if (fileStorage.getIsDeleted())
            throw new BadRequestException("File is deleted");

        if (!checkFileIsStillInMemory(fileStorage)) {
            setDeleted(fileStorage);
            throw new BadRequestException("File is deleted");
        }

        try {
            String filePath = fileStorage.getFilePath() + "." + fileStorage.getExtension();
            Path path = Paths.get(rootPath + filePath);
            Resource resource = new UrlResource(path.toUri());
            String contentType = Files.probeContentType(path);
            if (contentType == null) contentType = "application/octet-stream";

            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
            responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileStorage.getTitle() + "." + fileStorage.getExtension() + "\"");

            return responseBuilder.contentType(MediaType.valueOf(contentType)).body(resource);
        } catch (IOException e) {
            throw new BadRequestException("Failed to get file");
        }
    }

    @Override
    public ResponseEntity<Object> getFile(UUID fileId, UUID tenantId) {
        return getFileResponse(fileId, tenantId, false);
    }

    private void checkAccessForNonPublishedFile(FileStorage fileStorage, UUID tenantId) {
        if (tenantId == null || !tenantId.equals(fileStorage.getTenantId())) {
            throw new UnAuthorizedException("You are not allow to access this file");
        }
    }

    private boolean checkFileIsStillInMemory(FileStorage fileStorage) {
        String filePath = fileStorage.getFilePath() + "." + fileStorage.getExtension();
        File file = new File(rootPath + filePath);
        return file.exists();
    }

    private void setDeleted(FileStorage fileStorage) {
        fileStorage.setIsDeleted(true);
        fileStorageRepository.save(fileStorage);
    }

    private Object findAndGetSingleFileInfo(UUID fileId, UUID tenantId) {
        Optional<FileStorage> foundFile = fileStorageRepository.findById(fileId);
        if (foundFile.isEmpty())
            return new MessageResponse(fileId.toString(), "File is not exists");
        FileStorage fileStorage = foundFile.get();
        return getSingleFileInfo(fileStorage, tenantId, false);
    }

    public Object getSingleFileInfo(FileStorage fileStorage, UUID tenantId, boolean isUser) {
        if (fileStorage.getIsDeleted())
            return new MessageResponse(fileStorage.getId().toString(), "File is deleted");
        if (!checkFileIsStillInMemory(fileStorage)) {
            setDeleted(fileStorage);
            return new MessageResponse(fileStorage.getId().toString(), "File is not in memory");
        }
        if (!fileStorage.isPublished()) {
            if (tenantId != null) {
                if (!fileStorage.getTenantId().equals(tenantId)) {
                    return new MessageResponse(fileStorage.getId().toString(), "File is not publish");
                }
                return fileStorageMapper.mapEntityToDto(fileStorage);
            }
            if (isUser) {
                return fileStorageMapper.mapEntityToDto(fileStorage);
            }
            return new MessageResponse(fileStorage.getId().toString(), "File is not publish");
        }
        return fileStorageMapper.mapEntityToDto(fileStorage);
    }

    private MultiFileResponse getMultiFileResponse(List<Object> data) {
        List<Object> successData = data.stream().filter(o -> o instanceof FileStorageDto).collect(Collectors.toList());
        int totalElements = successData.size();
        int fail = data.size() - totalElements;
        return new MultiFileResponse(successData, totalElements, fail);
    }

    private ResponseEntity<Object> getFileResponse(UUID fileId, UUID tenantId, boolean asAttachment) {
        FileStorage fileStorage = fileStorageRepository.findById(fileId).orElseThrow(() -> new NotFoundException("File not found"));
        if (fileStorage.getIsDeleted())
            throw new BadRequestException("File is deleted");

        if (!checkFileIsStillInMemory(fileStorage)) {
            setDeleted(fileStorage);
            throw new BadRequestException("File is deleted");
        }

        try {
            if (!fileStorage.isPublished())
                checkAccessForNonPublishedFile(fileStorage, tenantId);
            String filePath = fileStorage.getFilePath() + "." + fileStorage.getExtension();
            Path path = Paths.get(rootPath + filePath);
            Resource resource = new UrlResource(path.toUri());
            String contentType = Files.probeContentType(path);
            if (contentType == null)
                contentType = "application/octet-stream";

            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
            if (asAttachment) {
                responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileStorage.getTitle() + "." + fileStorage.getExtension() + "\"");
            }

            return responseBuilder.contentType(MediaType.valueOf(contentType)).body(resource);
        } catch (IOException e) {
            throw new BadRequestException("Failed to get file");
        }
    }
}
