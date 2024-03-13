package com.im.sso.service;

import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.WhiteLabelDto;

import java.util.UUID;

public interface WhiteLabelService {

    WhiteLabelDto findByTenantId(UUID tenantId);

    WhiteLabelDto save(WhiteLabelDto whiteLabelDto, AppUserDto currentUser);
}
