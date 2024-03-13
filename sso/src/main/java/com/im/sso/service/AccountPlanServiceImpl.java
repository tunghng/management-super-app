package com.im.sso.service;

import com.im.sso.dto.mapper.AccountPlanMapper;
import com.im.sso.dto.model.AccountPlanDto;
import com.im.sso.dto.response.page.PageData;
import com.im.sso.dto.response.page.PageLink;
import com.im.sso.model.AccountPlan;
import com.im.sso.repository.AccountPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountPlanServiceImpl implements AccountPlanService {

    private final AccountPlanRepository accountPlanRepository;

    private final AccountPlanMapper accountPlanMapper;

    @Override
    public PageData<AccountPlanDto> findAccountPlans(PageLink pageLink) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize());
        Page<AccountPlan> accountPlanPage = accountPlanRepository.findAll(pageable);
        Page<AccountPlanDto> accountPlanDtoPage = accountPlanPage.map(accountPlanMapper::toDto);
        return new PageData<>(accountPlanDtoPage);
    }
}
