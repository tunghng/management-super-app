package com.im.filestorage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultiFileResponse {
    private List<Object> data;
    private int totalElements;
    private int totalFails;
}
