package com.gpch.login.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gpch.login.model.User;
import com.gpch.login.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    @Mock
    private UserService userServiceUnderTest;
    private User user;

    @Before
    public void setUp() {
        
//        userServiceUnderTest = new UserService(mockUserRepository,
//                                               mockBCryptPasswordEncoder);
//        user = new User("test@test.com", "test", "Meven", "DCunha", 1);
//        		
//        Mockito.when(mockUserRepository.save(user))
//                .thenReturn(user);
//        Mockito.when(mockUserRepository.findByEmail(user.getEmail()))
//                .thenReturn(user);
    }

    @Test
    public void testSaveUser() {
        // Setup
        final String email = "test@test.com";

        //user = new User("test@test.com", "test", "Meven", "DCunha", 1);
        // Run the test
        User result = userServiceUnderTest.saveUser(user);

        // Verify the results
        assertEquals(email, result.getEmail());
    }

	@Test
    public void testFindUserByEmail() {
        // Setup
        final String email = "test@test.com";

        // Run the test
        final User result = userServiceUnderTest.findUserByEmail(email);

        // Verify the results
        assertEquals(email, result.getEmail());
    }

    
}
