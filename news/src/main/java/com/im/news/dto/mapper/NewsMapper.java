package com.im.news.dto.mapper;

import com.im.news.dto.model.NewsDto;
import com.im.news.dto.model.NewsSaveDto;
import com.im.news.dto.response.NewsDetailDto;
import com.im.news.dto.response.NewsShortInfoDto;
import com.im.news.model.News;
import com.im.news.service.NewsCategoryService;
import com.im.news.service.UserService;
import org.mapstruct.Mapper;

@Mapper(uses = {NewsCategoryService.class, UserService.class})
public interface NewsMapper {

    NewsDto toDto(News news);

    NewsDetailDto toDetailDto(News news);

    NewsShortInfoDto toShortDto(News news);

    NewsSaveDto toSaveDto(NewsDto newsDto);
}
