package com.im.news.controller;


import com.im.news.dto.model.AppUserDto;
import com.im.news.dto.model.NewsCategoryDto;
import com.im.news.dto.model.NewsDto;
import com.im.news.dto.model.NewsSaveDto;
import com.im.news.dto.response.NewsDetailDto;
import com.im.news.dto.response.NewsShortInfoDto;
import com.im.news.dto.response.Response;
import com.im.news.dto.response.page.PageData;
import com.im.news.dto.response.page.PageLink;
import com.im.news.service.NewsCategoryService;
import com.im.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/news")
@Slf4j
public class NewsController extends BaseController {

    @Autowired
    NewsService newsService;

    @Autowired
    NewsCategoryService newsCategoryService;

    @GetMapping
    @Operation(summary = "Get News (getNews)")
    public ResponseEntity<PageData<NewsShortInfoDto>> getNews(
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Search columns: headline, categoryName")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "Filter by categoryId")
            @RequestParam(required = false) UUID categoryId,
            @Parameter(description = "Filter by categoryName")
            @RequestParam(required = false) String category,
            @Parameter(description = "Filter by deleted state of each post")
            @RequestParam(required = false) Boolean isDeleted,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(required = false) String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Filter column: updatedAt. `updatedAtEndTs` is required.")
            @RequestParam(required = false) Long updatedAtStartTs,
            @Parameter(description = "Filter column: updatedAt. `updatedAtStartTs` is required.")
            @RequestParam(required = false) Long updatedAtEndTs,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(page, pageSize, searchText, sortProperty, sortOrder);
        AppUserDto currentUser = getCurrentUser(request);
        if (category != null)
            checkNewsCategory(category, currentUser.getTenantId());
        return ResponseEntity.ok(newsService.findNews(
                pageLink,
                categoryId,
                category,
                isDeleted,
                updatedAtStartTs,
                updatedAtEndTs,
                currentUser.getTenantId(),
                isSearchMatchCase
        ));
    }

    @GetMapping("{newsId}")
    @Operation(summary = "Get News By Id (getNewsById")
    public ResponseEntity<NewsDetailDto> getNewsById(
            @PathVariable UUID newsId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        checkNewsId(newsId, currentUser.getTenantId());
        return ResponseEntity.ok(
                newsService.findDetailById(newsId, currentUser.getTenantId())
        );
    }

    @PostMapping
    @Operation(summary = "Save News (saveNews)")
    public NewsDto saveNews(
            @Valid @RequestBody NewsSaveDto newsDto,
            HttpServletRequest request
    ) {
        return newsService.save(newsDto, getCurrentUser(request));
    }

    @DeleteMapping("{newsId}")
    @Operation(summary = "Delete News by Id (restoreNewsById")
    public Response deleteNews(
            @PathVariable UUID newsId,
            HttpServletRequest request
    ) {
        checkNewsId(newsId, getCurrentUser(request).getTenantId());
        return new Response(200, newsService.deleteNewsById(newsId));
    }

    @PutMapping("{newsId}/restore")
    @Operation(summary = "Restore News by Id (restoreNewsById")
    public Response restoreNews(
            @PathVariable UUID newsId,
            HttpServletRequest request
    ) {
        checkNewsId(newsId, getCurrentUser(request).getTenantId());
        return new Response(200, newsService.restoreNewsById(newsId));
    }

    @GetMapping("category")
    @Operation(summary = "Get News Category (getNewsCategory)")
    public PageData<?> getNewsCategories(
            @Parameter(description = "Sequence number of page starting from 0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Maximum amount of entities in a one page")
            @RequestParam(defaultValue = "10") int pageSize,
            @Parameter(description = "Search with category name")
            @RequestParam(required = false) String searchText,
            @Parameter(description = "Property of entity to sort by")
            @RequestParam(required = false) String sortProperty,
            @Parameter(description = "Sort order. ASC (ASCENDING) or DESC (DESCENDING)")
            @RequestParam(required = false) String sortOrder,
            @Parameter(description = "Search Match Case Or Not")
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase,
            HttpServletRequest request
    ) {
        PageLink pageLink = createPageLink(
                page, pageSize, searchText, sortProperty, sortOrder
        );
        return newsCategoryService.findNewsCategories(pageLink, getCurrentUser(request).getTenantId(), isSearchMatchCase);
    }

    @PostMapping("category")
    @Operation(summary = "Save News Category (saveNewsCategory)")
    public NewsCategoryDto saveNewsCategory(
            @Valid @RequestBody NewsCategoryDto categoryDto,
            HttpServletRequest request
    ) {
        return newsCategoryService.save(categoryDto, getCurrentUser(request).getTenantId());
    }

}
