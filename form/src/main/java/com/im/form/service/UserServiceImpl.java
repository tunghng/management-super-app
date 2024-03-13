package com.im.form.service;

import com.im.form.dto.mapper.AppUserMapper;
import com.im.form.dto.model.AppUserDto;
import com.im.form.model.AppUser;
import com.im.form.repository.AppUserRepository;
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
