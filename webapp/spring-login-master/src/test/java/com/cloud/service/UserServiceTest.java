package com.cloud.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cloud.model.User;
import com.cloud.repository.UserRepository;

public class UserServiceTest {

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;


    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void save() {

        User userAccount = new User();
        userAccount.setEmail("meven.dcunha@gmail.com");
        userAccount.setPassword("1234");
        userService.saveUser(userAccount);

    }


}