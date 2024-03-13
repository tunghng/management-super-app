package com.im.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.LogDto;
import com.im.sso.dto.request.UserPlanRequest;
import com.im.sso.exception.BadRequestException;
import com.im.sso.model.AccountPlan;
import com.im.sso.model.AppUser;
import com.im.sso.model.UserSubPlan;
import com.im.sso.model.enums.*;
import com.im.sso.repository.AccountPlanRepository;
import com.im.sso.repository.AppUserRepository;
import com.im.sso.repository.UserSubPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class UserPlanServiceImpl implements UserPlanService {

    private final String USER_IS_NOT_TENANT = "User with id [%s] is not TENANT_ADMIN";
    private final String ACCOUNT_PLAN_NOT_FOUND = "Account plan with name [%s] is not found";
    private final String USER_PLAN_SUCCESS = "User with id [%s] set plan with name [%s] %s successful";

    private final AppUserRepository userRepository;

    private final AccountPlanRepository accountPlanRepository;

    private final UserSubPlanRepository userSubPlanRepository;

    private final LogService logService;

    @Override
    public String save(UserPlanRequest userPlanRequest, AppUserDto currentUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        AppUser user = userRepository.findByIdAndAuthority(
                userPlanRequest.getUserId(),
                AuthorityType.TENANT_ADMIN
        ).orElseThrow(() -> {
            logService.save(LogDto.builder()
                    .entityType(EntityType.USER_PLAN)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionData(objectMapper.valueToTree(userPlanRequest))
                    .actionFailureDetails(String.format(USER_IS_NOT_TENANT, userPlanRequest.getUserId()))
                    .build(), currentUser);
            return new BadRequestException(String.format(
                    USER_IS_NOT_TENANT, userPlanRequest.getUserId())
            );
        });
        AccountPlan accountPlan = accountPlanRepository.findByName(
                AccountPlanType.lookup(userPlanRequest.getPlanName())
        ).orElseThrow(() -> {
            logService.save(LogDto.builder()
                    .entityType(EntityType.USER_PLAN)
                    .actionStatus(ActionStatus.FAILURE)
                    .actionData(objectMapper.valueToTree(userPlanRequest))
                    .actionFailureDetails(String.format(ACCOUNT_PLAN_NOT_FOUND, userPlanRequest.getPlanName()))
                    .build(), currentUser);
            return new BadRequestException(
                    String.format(ACCOUNT_PLAN_NOT_FOUND, userPlanRequest.getPlanName())
            );
        });
        UserSubPlan userSubPlan = userSubPlanRepository.findByUserId(user.getTenantId())
                .orElse(new UserSubPlan());
        boolean isAdded = userSubPlan.getId() == null;
        if (!accountPlan.getName().equals(AccountPlanType.BASIC)) {
            if (userPlanRequest.getExpiredIn() == null) {
                logService.save(LogDto.builder()
                        .entityType(EntityType.USER_PLAN)
                        .entityId(userSubPlan.getId())
                        .actionType(isAdded ? ActionType.CREATED : ActionType.UPDATED)
                        .actionStatus(ActionStatus.FAILURE)
                        .actionData(objectMapper.valueToTree(userPlanRequest))
                        .actionFailureDetails("Missing `expiredIn`")
                        .build(), currentUser);
                throw new BadRequestException("Missing `expiredIn`");
            }
        }

        userSubPlan.setUser(user);
        userSubPlan.setAccountPlan(accountPlan);
        LocalDateTime expiredIn = userPlanRequest.getExpiredIn() != null
                ? Instant.ofEpochMilli(userPlanRequest.getExpiredIn()).atZone(ZoneId.systemDefault()).toLocalDateTime()
                : null;
        String bonusMessage = expiredIn != null ? "expired in " + expiredIn : "";
        userSubPlan.setExpiredIn(expiredIn);
        userSubPlanRepository.saveAndFlush(userSubPlan);
        logService.save(LogDto.builder()
                .entityType(EntityType.USER_PLAN)
                .entityId(userSubPlan.getId())
                .actionType(isAdded ? ActionType.CREATED : ActionType.UPDATED)
                .actionStatus(ActionStatus.SUCCESS)
                .actionData(objectMapper.valueToTree(userPlanRequest))
                .build(), currentUser);
        return String.format(USER_PLAN_SUCCESS, userPlanRequest.getUserId(), accountPlan.getName(), bonusMessage);
    }
}
