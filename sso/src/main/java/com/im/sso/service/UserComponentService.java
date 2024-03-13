package com.im.sso.service;

import com.im.sso.dto.model.AppComponentDto;
import com.im.sso.dto.model.AppUserDto;
import com.im.sso.dto.model.UserComponentDto;
import com.im.sso.model.UserComponent;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserComponentService {
    Collection<AppComponentDto> findAllTenantComponent();

    Collection<AppComponentDto> findAllCustomerComponent(UUID userId);

    UserComponentDto save(AppUserDto userDto, AppComponentDto componentDto, List<String> permission, AppUserDto currentUser);

    String delete(AppUserDto userDto, AppComponentDto componentDto, AppUserDto currentUser);

    Collection<AppComponentDto> convertUserComponentToComponent(Collection<UserComponent> userComponents);
}
