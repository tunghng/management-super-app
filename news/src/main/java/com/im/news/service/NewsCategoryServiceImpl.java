package com.im.news.service;

import com.im.news.dto.mapper.NewsCategoryMapper;
import com.im.news.dto.model.NewsCategoryDto;
import com.im.news.dto.response.page.PageData;
import com.im.news.dto.response.page.PageLink;
import com.im.news.exception.BadRequestException;
import com.im.news.model.NewsCategory;
import com.im.news.repository.NewsCategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final static String CATEGORY_NAME_ALREADY_EXIST = "News category with name [%s] is already exist";

    @Autowired
    NewsCategoryRepository newsCategoryRepository;

    @Autowired
    NewsCategoryMapper newsCategoryMapper;


    @Override
    public PageData<NewsCategoryDto> findNewsCategories(PageLink pageLink, UUID tenantId, Boolean isSearchMatchCase) {
        Pageable pageable = PageRequest.of(
                pageLink.getPage(),
                pageLink.getPageSize(),
                pageLink.toSort(pageLink.getSortOrder()));
        String searchText = Objects.toString(pageLink.getSearchText(), "")
                .replace("%", "\\%");

        searchText =  !isSearchMatchCase ? removeAccent(searchText.toLowerCase()) : searchText;

        Page<NewsCategory> newsCategoryPage = newsCategoryRepository.findNewsCategories(
                searchText,
                isSearchMatchCase,
                tenantId,
                pageable
        );
        Page<NewsCategoryDto> newsCategoryDtoPage = newsCategoryPage.map(newsCategoryMapper::toDto);
        return new PageData<NewsCategoryDto>(newsCategoryDtoPage);
    }

    @Override
    public NewsCategoryDto save(NewsCategoryDto newsCategoryDto, UUID tenantId) {

        NewsCategory category = newsCategoryDto.getId() != null
                ? newsCategoryMapper.toModel(newsCategoryDto) : new NewsCategory();

        checkIfNameExist(newsCategoryDto.getName(), tenantId);

        BeanUtils.copyProperties(newsCategoryDto, category);
        category.setTenantId(tenantId);
        NewsCategory savedCategory = newsCategoryRepository.save(category);
        return newsCategoryMapper.toDto(savedCategory);
    }

    @Override
    public NewsCategoryDto findByName(String category, UUID tenantId) {
        return newsCategoryMapper.toDto(newsCategoryRepository.findByNameAndTenantId(category, tenantId));
    }

    @Override
    public NewsCategory toModel(String category) {
        return newsCategoryRepository.findByName(category);
    }

    @Override
    public String toString(NewsCategory category) {
        return category != null ? category.getName() : null;
    }

    private void checkIfNameExist(String name, UUID tenantId) {
        NewsCategory category = newsCategoryRepository.findByNameAndTenantId(name, tenantId);
        if (category != null) {
            throw new BadRequestException(
                    String.format(CATEGORY_NAME_ALREADY_EXIST, name)
            );
        }
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
