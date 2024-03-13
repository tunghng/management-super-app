package com.im.news.service;

import com.im.news.dto.model.AppUserDto;
import com.im.news.dto.model.NewsDto;
import com.im.news.dto.model.NewsSaveDto;
import com.im.news.dto.response.NewsDetailDto;
import com.im.news.dto.response.NewsShortInfoDto;
import com.im.news.dto.response.page.PageData;
import com.im.news.dto.response.page.PageLink;

import java.util.UUID;

public interface NewsService {

    PageData<NewsShortInfoDto> findNews(
            PageLink pageLink,
            UUID categoryId,
            String categoryName,
            Boolean isDeleted,
            Long updatedAtStartTs,
            Long updatedAtEndTs,
            UUID tenantId,
            Boolean isSearchMatchCase
    );

    NewsDto findById(UUID newsId, UUID tenantId);

    NewsDetailDto findDetailById(UUID newsId, UUID tenantId);

    NewsDto save(NewsSaveDto newsDto, AppUserDto currentUser);

    String deleteNewsById(UUID newsId);

    String restoreNewsById(UUID newsId);
}
