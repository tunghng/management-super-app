package com.im.news.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.news.dto.model.AppUserDto;
import com.im.news.dto.model.NewsCategoryDto;
import com.im.news.dto.model.NewsDto;
import com.im.news.dto.response.page.PageLink;
import com.im.news.dto.response.page.SortOrder;
import com.im.news.exception.ForbiddenException;
import com.im.news.exception.NotFoundException;
import com.im.news.service.NewsCategoryService;
import com.im.news.service.NewsService;
import com.im.news.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class BaseController {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    NewsCategoryService newsCategoryService;

    public PageLink createPageLink(int page, int pageSize, String searchText, String sortProperty, String sortOrder) {
        if (!StringUtils.isEmpty(sortProperty)) {
            SortOrder.Direction direction = SortOrder.Direction.DESC;
            if (!StringUtils.isEmpty(sortOrder)) {
                direction = SortOrder.Direction.lookup(sortOrder.toUpperCase());
            }
            SortOrder sort = new SortOrder(sortProperty, direction);
            return new PageLink(page, pageSize, searchText, sort);
        } else {
            return new PageLink(page, pageSize, searchText);
        }
    }

    protected AppUserDto getCurrentUser(HttpServletRequest request) {
        try {
            Map<String, String> jwt = parseJwt(request.getHeader("Authorization"));
            AppUserDto currentUser = userService.findByUserId(UUID.fromString(jwt.get("userId")));
            if (currentUser == null)
                throw new ForbiddenException("You aren't authorized to perform this operation.");
            else return currentUser;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }


    Map<String, String> parseJwt(String token) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, String> map = mapper.readValue(payload, Map.class);
        return map;
    }

    NewsDto checkNewsId(UUID newsId, UUID tenantId) {
        NewsDto newsDto = newsService.findById(newsId, tenantId);
        checkNotNull(newsDto, String.format("News with id [%s] is not found", newsId));
        return newsDto;
    }

    NewsCategoryDto checkNewsCategory(String category, UUID tenantId) {
        NewsCategoryDto categoryDto = newsCategoryService.findByName(category, tenantId);
        checkNotNull(categoryDto, String.format("Category with name [%s] is not found", category));
        return categoryDto;
    }

    <T> T checkNotNull(T reference, String notFoundMessage) {
        if (reference == null) {
            throw new NotFoundException(notFoundMessage);
        }
        return reference;
    }
}
