package com.im.sso.dto.mapper;

import com.im.sso.dto.model.AccountPlanDto;
import com.im.sso.model.AccountPlan;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T15:39:11+0700",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class AccountPlanMapperImpl implements AccountPlanMapper {

    @Override
    public AccountPlanDto toDto(AccountPlan accountPlan) {
        if ( accountPlan == null ) {
            return null;
        }

        AccountPlanDto accountPlanDto = new AccountPlanDto();

        if ( accountPlan.getName() != null ) {
            accountPlanDto.setName( accountPlan.getName().name() );
        }
        accountPlanDto.setDescription( accountPlan.getDescription() );

        return accountPlanDto;
    }
}
