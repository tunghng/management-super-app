package com.im.news.service;

import com.im.news.dto.model.NewsCategoryDto;
import com.im.news.dto.response.page.PageData;
import com.im.news.dto.response.page.PageLink;
import com.im.news.model.NewsCategory;

import java.util.UUID;

public interface NewsCategoryService {

    PageData<NewsCategoryDto> findNewsCategories(PageLink pageLink, UUID tenantId, Boolean isSearchMatchCase);

    NewsCategoryDto save(NewsCategoryDto newsCategoryDto, UUID tenantId);

    NewsCategoryDto findByName(String category, UUID tenantId);

    NewsCategory toModel(String category);

    String toString(NewsCategory category);
}
