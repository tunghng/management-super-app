package com.im.sso.dto.mapper;

import com.im.sso.dto.model.AccountPlanDto;
import com.im.sso.model.AccountPlan;
import org.mapstruct.Mapper;

@Mapper
public interface AccountPlanMapper {
    AccountPlanDto toDto(AccountPlan accountPlan);
}
