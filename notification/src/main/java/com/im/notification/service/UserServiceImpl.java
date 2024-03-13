package com.im.notification.service;

import com.im.notification.dto.mapper.AppUserMapper;
import com.im.notification.dto.model.AppUserDto;
import com.im.notification.model.AppUser;
import com.im.notification.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    AppUserRepository userRepository;

    @Autowired
    AppUserMapper userMapper;

    @Override
    public AppUserDto findByUserId(UUID userId) {
        AppUser user = userRepository.findById(userId).orElse(null);
        return user == null ? null : userMapper.toDto(user);
    }
}
