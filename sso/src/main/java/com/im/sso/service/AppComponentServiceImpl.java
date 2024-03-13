package com.im.sso.service;

import com.im.sso.dto.mapper.AppComponentMapper;
import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.dto.response.page.PageData;
import com.im.sso.dto.response.page.PageLink;
import com.im.sso.model.AppComponent;
import com.im.sso.repository.AppComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppComponentServiceImpl implements AppComponentService {

    private final AppComponentRepository appComponentRepository;

    private final AppComponentMapper appComponentMapper;


    @Override
    public PageData<AppComponentDto> findUserComponents(PageLink pageLink) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        Page<AppComponent> componentPage = appComponentRepository.findAll(pageable);
        Page<AppComponentDto> componentDtoPage = componentPage.map(appComponentMapper::toDto);
        return new PageData<>(componentDtoPage);
    }

    @Override
    public AppComponentDto findByName(String name) {
        AppComponent component = appComponentRepository.findByName(name);
        return component != null ? appComponentMapper.toDto(component) : null;
    }


}
