package com.im.announcement.dto.response;

import lombok.Data;

@Data
public class CountResponse {
    private final Long count;

    public CountResponse(Long count) {
        this.count = count;
    }
}
