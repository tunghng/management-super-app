package com.im.news.dto.response;

import com.im.news.dto.model.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDetailDto {
    Collection<String> attachedFile;
    private UUID id;
    private String cover;
    private String code;
    private String headline;
    private String body;
    private String category;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AppUserDto createdBy;
    private AppUserDto updatedBy;
}
