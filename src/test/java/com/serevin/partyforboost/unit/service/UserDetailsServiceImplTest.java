package com.serevin.partyforboost.unit.service;

import com.serevin.partyforboost.entity.User;
import com.serevin.partyforboost.service.impl.UserDetailsServiceImpl;
import com.serevin.partyforboost.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername_UserExists() {
        User user = User.builder()
                .email("testemail@gmail.com")
                .username("testUsername")
                .password("testPassword1")
                .build();
        when(userService.getByEmail("testemail@gmail.com")).thenReturn(user);

        UserDetails testUsername = userDetailsService.loadUserByUsername("testemail@gmail.com");
        assertNotNull(testUsername);

        assertEquals("testUsername", testUsername.getUsername());
    }

    @Test
    @Disabled
    void testLoadUserByUsername_UserNotFound(){
        //?
    }

}
