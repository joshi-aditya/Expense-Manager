package com.cloud.service;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cloud.model.User;
import com.cloud.repository.UserRepository;

public class UserServiceTest {

	//User details
	private int userId  		= 1;
	private String email  		= "test@neu.com";
	private String lastName  	= "lastName";
	private String name  		= "name";
	private String password 	= "1234";
	
	@InjectMocks
	UserService userService;

	@Mock
	UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private User userAccount;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockUserCreation();
		mockFindUser();
	}
	
	/**
	 * Added to mock the user creation
	 */
	private void mockUserCreation()
	{
		userAccount = new User();
		userAccount.setId(userId);
		userAccount.setName(name);
		userAccount.setLastName(lastName);
		userAccount.setEmail(email);
		userAccount.setPassword(password);
		userAccount.setActive(1);
		when(userRepository.save(userAccount)).thenReturn(userAccount);
	}
	
	/**
	 * Added to mock the retrieve user
	 */
	private void mockFindUser()
	{
		when(userRepository.findByEmail(email)).thenReturn(userAccount);
	}

	@Test
	public void registerUser() {

		User user = userService.saveUser(userAccount);
		Assert.assertEquals("User Created successfully", user.getEmail(), email);
	}
	
	@Test
	public void findUserByEmail() {
		
		User user = userService.findUserByEmail(email);
		Assert.assertEquals("Found user successfully", user.getEmail(), email);
	}
	
	@Test
	public void getTime() {
		
		String time = userService.getTime();
		Assert.assertNotNull("Retrieved time successfully", time);
	}

}