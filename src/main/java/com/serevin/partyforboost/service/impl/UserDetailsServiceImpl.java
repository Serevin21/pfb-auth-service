package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.security.UserDetailsImpl;
import com.serevin.partyforboost.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userService.getByEmail(username);
        return new UserDetailsImpl(user);
    }
}
