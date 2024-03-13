package com.im.news.dto.mapper;

import com.im.news.dto.model.NewsDto;
import com.im.news.dto.response.NewsDetailDto;
import com.im.news.dto.response.NewsShortInfoDto;
import com.im.news.model.News;
import com.im.news.service.NewsCategoryService;
import com.im.news.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-08T10:40:26+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class NewsMapperImpl implements NewsMapper {

    @Autowired
    private NewsCategoryService newsCategoryService;
    @Autowired
    private UserService userService;

    @Override
    public NewsDto toDto(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDto newsDto = new NewsDto();

        Collection<String> collection = news.getAttachedFile();
        if ( collection != null ) {
            newsDto.setAttachedFile( new ArrayList<String>( collection ) );
        }
        newsDto.setId( news.getId() );
        newsDto.setCode( news.getCode() );
        newsDto.setCover( news.getCover() );
        newsDto.setHeadline( news.getHeadline() );
        newsDto.setBody( news.getBody() );
        newsDto.setCategory( newsCategoryService.toString( news.getCategory() ) );
        newsDto.setIsDeleted( news.getIsDeleted() );
        newsDto.setCreatedAt( news.getCreatedAt() );
        newsDto.setUpdatedAt( news.getUpdatedAt() );
        newsDto.setCreatedBy( news.getCreatedBy() );
        newsDto.setUpdatedBy( news.getUpdatedBy() );

        return newsDto;
    }

    @Override
    public NewsDetailDto toDetailDto(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDetailDto newsDetailDto = new NewsDetailDto();

        Collection<String> collection = news.getAttachedFile();
        if ( collection != null ) {
            newsDetailDto.setAttachedFile( new ArrayList<String>( collection ) );
        }
        newsDetailDto.setId( news.getId() );
        newsDetailDto.setCover( news.getCover() );
        newsDetailDto.setCode( news.getCode() );
        newsDetailDto.setHeadline( news.getHeadline() );
        newsDetailDto.setBody( news.getBody() );
        newsDetailDto.setCategory( newsCategoryService.toString( news.getCategory() ) );
        newsDetailDto.setIsDeleted( news.getIsDeleted() );
        newsDetailDto.setCreatedAt( news.getCreatedAt() );
        newsDetailDto.setUpdatedAt( news.getUpdatedAt() );
        newsDetailDto.setCreatedBy( userService.findByUserId( news.getCreatedBy() ) );
        newsDetailDto.setUpdatedBy( userService.findByUserId( news.getUpdatedBy() ) );

        return newsDetailDto;
    }

    @Override
    public NewsShortInfoDto toShortDto(News news) {
        if ( news == null ) {
            return null;
        }

        NewsShortInfoDto newsShortInfoDto = new NewsShortInfoDto();

        newsShortInfoDto.setId( news.getId() );
        newsShortInfoDto.setCover( news.getCover() );
        newsShortInfoDto.setCode( news.getCode() );
        newsShortInfoDto.setHeadline( news.getHeadline() );
        newsShortInfoDto.setCategory( newsCategoryService.toString( news.getCategory() ) );
        newsShortInfoDto.setCreatedAt( news.getCreatedAt() );
        newsShortInfoDto.setUpdatedAt( news.getUpdatedAt() );
        newsShortInfoDto.setCreatedBy( userService.findByUserId( news.getCreatedBy() ) );
        newsShortInfoDto.setUpdatedBy( userService.findByUserId( news.getUpdatedBy() ) );

        return newsShortInfoDto;
    }
}
