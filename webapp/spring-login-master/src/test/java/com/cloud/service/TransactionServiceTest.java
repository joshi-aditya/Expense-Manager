package com.cloud.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloud.repository.TransactionRepository;

public class TransactionServiceTest {

	@Mock
	TransactionService transactionService;

	@Mock
	TransactionRepository transactionRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void registerUser() {

		
	}
	
	@Test
	public void getTime() {
		
		
	}

}