package com.im.billing.service;

import com.im.billing.dto.mapper.AppUserMapper;
import com.im.billing.dto.model.AppUserDto;
import com.im.billing.model.AppUser;
import com.im.billing.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AppUserRepository userRepository;

    @Autowired
    AppUserMapper userMapper;

    @Override
    public AppUserDto findByUserId(UUID userId) {
        if (userId == null) return null;
        AppUser user = userRepository.findById(userId).orElse(null);
        return user != null ? userMapper.toDto(user) : null;
    }
}
