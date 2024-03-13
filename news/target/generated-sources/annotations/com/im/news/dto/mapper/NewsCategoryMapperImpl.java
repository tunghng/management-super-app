package com.im.news.dto.mapper;

import com.im.news.dto.model.NewsCategoryDto;
import com.im.news.model.NewsCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T10:40:26+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class NewsCategoryMapperImpl implements NewsCategoryMapper {

    @Override
    public NewsCategoryDto toDto(NewsCategory newsCategory) {
        if ( newsCategory == null ) {
            return null;
        }

        NewsCategoryDto newsCategoryDto = new NewsCategoryDto();

        newsCategoryDto.setName( newsCategory.getName() );
        newsCategoryDto.setId( newsCategory.getId() );
        newsCategoryDto.setTenantId( newsCategory.getTenantId() );

        return newsCategoryDto;
    }

    @Override
    public NewsCategory toModel(NewsCategoryDto newsCategoryDto) {
        if ( newsCategoryDto == null ) {
            return null;
        }

        NewsCategory newsCategory = new NewsCategory();

        newsCategory.setId( newsCategoryDto.getId() );
        newsCategory.setName( newsCategoryDto.getName() );
        newsCategory.setTenantId( newsCategoryDto.getTenantId() );

        return newsCategory;
    }
}
