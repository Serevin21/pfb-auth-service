package com.serevin.patyforboost.service.impl;

import com.serevin.patyforboost.entity.User;
import com.serevin.patyforboost.exception.EntityNotFoundException;
import com.serevin.patyforboost.repository.UsersRepository;
import com.serevin.patyforboost.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;


    @Transactional(readOnly = true)
    @Override
    public User getByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }


    @Transactional
    @Override
    public User save(User user) {
        return usersRepository.save(user);
    }
}
