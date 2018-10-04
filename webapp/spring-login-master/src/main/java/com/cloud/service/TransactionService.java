package com.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.model.Transaction;
import com.cloud.repository.TransactionRepository;

@Service("transactionService")
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	/**
	 * Create a new transaction for a User
	 * @param transaction
	 * @throws Exception
	 */
	public void save(Transaction transaction) throws Exception {

		transactionRepository.save(transaction);
	}
	
	/**
	 * Update a transaction for a User
	 * @param id
	 * @throws Exception
	 */
	public Transaction find(Integer id) throws Exception {
		
		return transactionRepository.findByTransactionId(id);
		 
	}
}
