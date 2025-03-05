package com.serevin.partyforboost.service;

import com.serevin.partyforboost.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User getByEmail(String email);
    User save(User user);

}
