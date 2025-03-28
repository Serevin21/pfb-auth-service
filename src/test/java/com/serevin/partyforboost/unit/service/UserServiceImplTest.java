package com.serevin.partyforboost.unit.service;

import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.exception.EntityNotFoundException;
import com.serevin.partyforboost.repository.UsersRepository;
import com.serevin.partyforboost.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("testemail@gmail.com")
                .username("testUsername")
                .password("testPassword1")
                .build();
    }

    @Test
    void testGetByEmail_UserExists() {
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userService.getByEmail(user.getEmail());
        assertNotNull(foundUser);

        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getUsername(), foundUser.getUsername());

        verify(usersRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void testGetByEmail_UserNotFound() {
        when(usersRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getByEmail("notfound@example.com"));
    }

    @Test
    void testFindByEmail_UserExists() {
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByEmail(user.getEmail());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(usersRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByEmail("notfound@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testFindByUsername_UserExists() {
        when(usersRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername(user.getUsername());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(usersRepository.findByUsername("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("notfound@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSave(){
        when(usersRepository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());

        verify(usersRepository, times(1)).save(user);
    }
}
