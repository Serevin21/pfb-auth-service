package com.serevin.partyforboost.service.impl;

import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exception.EntityNotFoundException;
import com.serevin.partyforboost.repository.UsersRepository;
import com.serevin.partyforboost.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public User save(User user) {
        return usersRepository.save(user);
    }
}
