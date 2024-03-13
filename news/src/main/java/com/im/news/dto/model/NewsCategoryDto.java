package com.im.news.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCategoryDto {
    private String name;
    private UUID id;
    private UUID tenantId;
}
