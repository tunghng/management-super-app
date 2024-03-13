package com.im.news.service;

import com.im.news.dto.mapper.NewsMapper;
import com.im.news.dto.model.AppUserDto;
import com.im.news.dto.model.NewsDto;
import com.im.news.dto.model.NewsSaveDto;
import com.im.news.dto.response.NewsDetailDto;
import com.im.news.dto.response.NewsShortInfoDto;
import com.im.news.dto.response.page.PageData;
import com.im.news.dto.response.page.PageLink;
import com.im.news.exception.BadRequestException;
import com.im.news.exception.NotFoundException;
import com.im.news.model.News;
import com.im.news.model.NewsCategory;
import com.im.news.model.NewsCode;
import com.im.news.repository.NewsCategoryRepository;
import com.im.news.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
public class NewsServiceImpl implements NewsService {
    private static final String NEWS_NOT_FOUND = "New with id [%s] is not found";

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    NewsCategoryRepository newsCategoryRepository;

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    NewsCodeRepository newsCodeRepository;

    @Override
    public PageData<NewsShortInfoDto> findNews(
            PageLink pageLink,
            UUID categoryId,
            String categoryName,
            Boolean isDeleted,
            Long updatedAtStartTs,
            Long updatedAtEndTs,
            UUID tenantId,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));
        isTimeStampValid(updatedAtStartTs, updatedAtEndTs);
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());

        Page<News> newsPage = newsRepository.findNews(
                searchText,
                isSearchMatchCase,
                isDeleted,
                categoryId,
                categoryName,
                convertTimestampToDateTime(updatedAtStartTs),
                convertTimestampToDateTime(updatedAtEndTs),
                tenantId,
                pageable
        );
        Page<NewsShortInfoDto> newsDtoPage = newsPage.map(newsMapper::toShortDto);
        return new PageData<>(newsDtoPage);
    }

    @Override
    public NewsDto findById(UUID newsId, UUID tenantId) {
        News news = newsRepository.findByIdAndTenantId(newsId, tenantId);
        return news != null ? newsMapper.toDto(news) : null;
    }

    @Override
    public NewsDetailDto findDetailById(UUID newsId, UUID tenantId) {
        News news = newsRepository.findByIdAndTenantId(newsId, tenantId);
        return news != null ? newsMapper.toDetailDto(news) : null;
    }

    @Override
    @Transactional
    public NewsDto save(NewsSaveDto newsDto, AppUserDto currentUser) {

        News news = newsDto.getId() != null ?
                newsRepository.findById(newsDto.getId())
                        .orElseThrow(() -> new NotFoundException(String.format(NEWS_NOT_FOUND, newsDto.getId())))
                : new News();

        BeanUtils.copyProperties(newsDto, news,
                "isDeleted", "code", "createdAt", "updatedAt", "createdBy", "updatedBy");
        if (newsDto.getId() == null) {
            news.setCreatedBy(currentUser.getId());
            String code = newsCodeRepository.save(new NewsCode()).getId();
            news.setCode(code);
        }

        NewsCategory category = news.getCategory();
        if (newsDto.getCategory() != null) { // will deleted
            category = newsCategoryRepository.findByNameAndTenantId(newsDto.getCategory(), currentUser.getTenantId());
        }
        if (newsDto.getCategoryId() != null) {
            category = newsCategoryRepository.findByIdAndTenantId(newsDto.getCategoryId(), currentUser.getTenantId());
        }

        news.setCategory(category);

        news.setIsDeleted(false);
        news.setUpdatedBy(currentUser.getId());
        news.setTenantId(currentUser.getTenantId());

        News savedNews = newsRepository.saveAndFlush(news);
        return newsMapper.toDto(savedNews);
    }

    @Override
    @Transactional
    public String deleteNewsById(UUID newsId) {
        News news = newsRepository
                .findById(newsId)
                .orElseThrow(() ->
                        new NotFoundException(String.format(NEWS_NOT_FOUND, newsId))
                );
        if (news.getIsDeleted()) {
            throw new BadRequestException(String.format("News with id [%s] has already deleted", newsId));
        }
        news.setIsDeleted(true);
        newsRepository.saveAndFlush(news);
        return String.format("News with id [%s] has deleted successful", newsId);
    }

    @Override
    @Transactional
    public String restoreNewsById(UUID newsId) {
        News news = newsRepository
                .findById(newsId)
                .orElseThrow(() ->
                        new NotFoundException(String.format(NEWS_NOT_FOUND, newsId))
                );
        if (!news.getIsDeleted()) {
            throw new BadRequestException(String.format("News with id [%s] has already displayed", newsId));
        }
        news.setIsDeleted(false);
        newsRepository.saveAndFlush(news);
        return String.format("News with id [%s] has restored successful", newsId);
    }

    private void isTimeStampValid(Long startTs, Long endTs) {
        if (startTs != null && endTs != null) {
            if (!(startTs >= 0 && endTs >= 0 && startTs <= endTs)) {
                throw new BadRequestException("Start time and end time must be valid");
            }
        }
    }

    private LocalDateTime convertTimestampToDateTime(Long timestamp) {
        return timestamp != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                : null;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
