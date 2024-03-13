package com.im.sso.security.service;

import com.im.sso.model.AppUser;
import com.im.sso.model.UserCredential;
import com.im.sso.repository.AppUserRepository;
import com.im.sso.repository.UserCredentialsRepository;
import com.im.sso.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Override
    @Transactional
    public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email [" + email + "] not found");
        }
        UserCredential userCredential = userCredentialsRepository.findByUserId(user.getId()).get();
        return new SecurityUser(user, userCredential);
    }
}
