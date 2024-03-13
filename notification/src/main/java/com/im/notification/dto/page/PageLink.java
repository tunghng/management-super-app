package com.im.notification.dto.page;

import lombok.Data;

@Data
public class PageLink {
    private final int page;
    private final int pageSize;

    public PageLink(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
}
