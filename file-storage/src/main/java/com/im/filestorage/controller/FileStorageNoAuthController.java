package com.im.filestorage.controller;

import com.im.filestorage.dto.model.FileStorageDto;
import com.im.filestorage.dto.response.MultiFileResponse;
import com.im.filestorage.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/noauth/file")
public class FileStorageNoAuthController {
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("info")
    public MultiFileResponse getMultiFileNoAuth(@RequestParam("fileId") UUID[] uuids) {
        return fileStorageService.getMultiFilesInfo(uuids);
    }

    @Operation(summary = "Upload file without token")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileStorageDto uploadFileNoAuth(
            @RequestPart(value = "file")
            MultipartFile file,
            @RequestPart(value = "title")
            @Schema(description = "Name of the file")
            String title
    ) {
        return fileStorageService.saveFile(file, title);
    }

    @Operation(summary = "Download file without token")
    @GetMapping("{fileId}/download")
    public ResponseEntity<Object> downloadFileNoAuth(@PathVariable("fileId") UUID fileId) {
        return fileStorageService.downloadFileNoAuth(fileId);
    }
}
