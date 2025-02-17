package com.serevin.patyforboost.service;

import com.serevin.patyforboost.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User getByEmail(String email);
    User save(User user);

}
