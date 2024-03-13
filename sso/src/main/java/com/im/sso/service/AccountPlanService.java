package com.im.sso.service;

import com.im.sso.dto.model.AccountPlanDto;
import com.im.sso.dto.response.page.PageData;
import com.im.sso.dto.response.page.PageLink;

public interface AccountPlanService {
    PageData<AccountPlanDto> findAccountPlans(PageLink pageLink);
}
