package com.im.filestorage.dto.mapper.impl;

import com.im.filestorage.dto.mapper.FileStorageMapper;
import com.im.filestorage.dto.model.FileStorageDto;
import com.im.filestorage.model.FileStorage;
import org.springframework.stereotype.Component;

@Component
public class FileStorageMapperImpl implements FileStorageMapper {
    @Override
    public FileStorageDto mapEntityToDto(FileStorage fileStorage) {
        return FileStorageDto.builder()
                .id(fileStorage.getId())
                .title(fileStorage.getTitle())
                .createdBy(fileStorage.getCreatedBy())
                .updatedBy(fileStorage.getUpdatedBy())
                .uploadAt(fileStorage.getUpdatedAt())
                .createAt(fileStorage.getCreatedAt())
                .isPublished(fileStorage.isPublished())
                .extension(fileStorage.getExtension())
                .build();
    }
}
