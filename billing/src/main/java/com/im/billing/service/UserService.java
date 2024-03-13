package com.im.billing.service;

import com.im.billing.dto.model.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);
}
