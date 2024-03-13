package com.im.sso.service;

import java.util.UUID;

public interface UserPermissionService {
    boolean validateUserPermission(UUID userId, String url, String method);

    boolean checkUserPlan(UUID tenantId);
}
