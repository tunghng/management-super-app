package com.im.filestorage.dto.mapper;

import com.im.filestorage.dto.model.FileStorageDto;
import com.im.filestorage.model.FileStorage;

public interface FileStorageMapper {
    FileStorageDto mapEntityToDto(FileStorage fileStorage);
}
