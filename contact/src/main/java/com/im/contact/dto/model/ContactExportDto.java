package com.im.contact.dto.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContactExportDto {
    final private Long startTs;
    final private Long endTs;
    final private Integer totalAttributes;
    final private Integer totalElements;
    final private List<String> attributes;
    final private List<Object[]> data;
}