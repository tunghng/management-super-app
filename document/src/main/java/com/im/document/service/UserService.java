package com.im.document.service;


import com.im.document.dto.model.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);
}
