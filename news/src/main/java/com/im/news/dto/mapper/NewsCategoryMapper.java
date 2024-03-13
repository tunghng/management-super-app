package com.im.news.dto.mapper;

import com.im.news.dto.model.NewsCategoryDto;
import com.im.news.model.NewsCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface NewsCategoryMapper {

    NewsCategoryDto toDto(NewsCategory newsCategory);

    NewsCategory toModel(NewsCategoryDto newsCategoryDto);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }
}
