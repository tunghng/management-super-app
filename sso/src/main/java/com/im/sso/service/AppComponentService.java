package com.im.sso.service;

import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.dto.response.page.PageData;
import com.im.sso.dto.response.page.PageLink;

public interface AppComponentService {
    PageData<AppComponentDto> findUserComponents(PageLink pageLink);

    AppComponentDto findByName(String name);
}
