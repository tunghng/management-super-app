package com.im.sso.service;

import com.im.sso.exception.BadRequestException;
import com.im.sso.model.AppComponent;
import com.im.sso.model.AppUser;
import com.im.sso.model.UserComponent;
import com.im.sso.model.UserSubPlan;
import com.im.sso.model.enums.AccountPlanType;
import com.im.sso.repository.AppComponentRepository;
import com.im.sso.repository.AppUserRepository;
import com.im.sso.repository.UserComponentRepository;
import com.im.sso.repository.UserSubPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final String USER_NOT_FOUND = "User with id [%s] is not found";

    private final AppComponentRepository appComponentRepository;

    private final UserComponentRepository userComponentRepository;

    private final AppUserRepository userRepository;

    private final UserSubPlanRepository userSubPlanRepository;

    public boolean validateUserPermission(UUID userId, String url, String method) {
        AppComponent component = appComponentRepository.findByUrlBase(url);
        UserComponent userComponent = userComponentRepository
                .findByAppComponentIdAndUserId(component.getId(), userId);
        if (userComponent != null) {
            List<String> permissionsList = userComponent.getPermissions();
            List<String> permissions = convertMethodToPermission(method);
            List<String> checklist = new ArrayList<>();
            assert permissions != null;
            permissions.forEach(permission -> {
                if (permissionsList.contains(permission)) checklist.add(permission);
            });
            return !checklist.isEmpty();
        }
        return false;
    }

    @Override
    public boolean checkUserPlan(UUID tenantId) {
        AppUser tenantUser = userRepository.findById(tenantId)
                .orElseThrow(() ->
                        new BadRequestException(
                                String.format(USER_NOT_FOUND, tenantId)
                        ));
        Optional<UserSubPlan> userSubPlan = userSubPlanRepository.findByUserId(tenantUser.getTenantId());
        if (userSubPlan.isEmpty()) {
            return false;
        }
        if (!userSubPlan.get().getAccountPlan()
                .getName().equals(AccountPlanType.BASIC)
        ) {
            return checkExpiredIn(userSubPlan.get().getExpiredIn());
        }
        return true;
    }

    private boolean checkExpiredIn(LocalDateTime expired) {
        if (expired.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    private List<String> convertMethodToPermission(String method) {
        switch (method) {
            case "GET":
                return List.of("READ");
            case "POST":
                return List.of("ADD", "EDIT");
            case "PUT":
                return List.of("EDIT");
            default:
                return null;
        }
    }

}
