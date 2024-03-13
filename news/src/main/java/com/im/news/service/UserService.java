package com.im.news.service;


import com.im.news.dto.model.AppUserDto;

import java.util.UUID;

public interface UserService {
    AppUserDto findByUserId(UUID userId);
}
