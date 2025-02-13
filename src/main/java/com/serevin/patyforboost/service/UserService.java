package com.serevin.patyforboost.service;

import com.serevin.patyforboost.entity.User;

public interface UserService {

    User getByEmail(String email);
    User save(User user);

}
