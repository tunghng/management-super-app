package com.im.filestorage.controller;

import com.im.filestorage.dto.model.AppUserDto;
import com.im.filestorage.dto.model.FileStorageDto;
import com.im.filestorage.dto.request.UpdateRequest;
import com.im.filestorage.dto.response.MultiFileResponse;
import com.im.filestorage.dto.response.PaginationResponse;
import com.im.filestorage.dto.response.Response;
import com.im.filestorage.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileStorageController extends BaseController {

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(summary = "Upload file")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileStorageDto uploadFile(
            @RequestPart(value = "file")
            MultipartFile file,
            @RequestPart(value = "title")
            @Schema(description = "Name of the file")
            String title,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        return fileStorageService.saveFile(file, title,
                currentUser.getUserId(), currentUser.getTenantId());
    }

    @Operation(summary = "Get several files information")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("info")
    public MultiFileResponse getMultipleFiles(@RequestParam("fileId")
                                              @Schema(description = "List of file IDs")
                                              UUID[] uuids,
                                              HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) return fileStorageService.getMultiFilesInfo(uuids, null);
        AppUserDto currentUser = getCurrentUser(request);
        return fileStorageService.getMultiFilesInfo(uuids, currentUser.getTenantId());
    }

    @Operation(summary = "Get file by Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("{fileId}")
    public ResponseEntity<Object> getFile(@PathVariable("fileId") UUID fileId, HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) return fileStorageService.getFile(fileId, null);
        AppUserDto currentUser = getCurrentUser(request);
        return fileStorageService.getFile(fileId, currentUser.getTenantId());
    }

//    @GetMapping("info")
//    public List<FileStorageDto> getAllFiles() {
//        return fileStorageService.getInfoOfFiles();
//    }

    @Operation(summary = "Get files by user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("info/user")
    public PaginationResponse getFilesInfoByUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        return fileStorageService.getFilesInfoByUser(currentUser.getUserId(), page, size);
    }

    @Operation(summary = "Get files by tenant")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("info/tenant")
    public PaginationResponse getFilesInfoByTenant(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int size,
                                                   HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        return fileStorageService.getFilesInfoByTenant(currentUser.getTenantId(), page, size);
    }

    @Operation(summary = "Download file")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("{fileId}/download")
    public ResponseEntity<Object> downloadFile(@PathVariable("fileId") UUID fileId, HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) return fileStorageService.downloadFile(fileId, null);
        AppUserDto currentUser = getCurrentUser(request);
        return fileStorageService.downloadFile(fileId, currentUser.getTenantId());
    }

    @Operation(summary = "Publish/Unpublished file")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("{fileId}/publish")
    public Response publishFile(@PathVariable("fileId") UUID fileId, HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(200, fileStorageService.publishFile(fileId,
                currentUser.getUserId(), currentUser.getTenantId()));
    }

    @Operation(summary = "Update file info")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("info")
    public Response updateFileInfo(@RequestBody UpdateRequest updateRequest, HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(200, fileStorageService.updateFileInfo(updateRequest,
                currentUser.getUserId(), currentUser.getTenantId()));
    }

    @Operation(summary = "Delete file by Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("{fileId}")
    public Response deleteFile(@PathVariable("fileId") UUID fileId, HttpServletRequest request) {
        AppUserDto currentUser = getCurrentUser(request);
        return new Response(200, fileStorageService.deleteFile(fileId, currentUser.getUserId()));
    }
}
