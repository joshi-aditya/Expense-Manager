package com.cloud.service;

import java.util.List;
import java.util.UUID;
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
	 * 
	 * @param transaction
	 * @throws Exception
	 */
	public void save(Transaction transaction) throws Exception {

		transactionRepository.save(transaction);
	}

	/**
	 * Update a transaction for a User
	 * 
	 * @param id
	 * @throws Exception
	 */
	public Transaction find(String id) throws Exception {

		return transactionRepository.findByTransactionId(UUID.fromString(id));

	}

	/**
	 * Deletes the transaction by transaction Id
	 * @param id
	 */
	public void deleteById(String id) {
		transactionRepository.deleteById(UUID.fromString(id));

	}

	/**
	 * Fetches all the transaction for the logged in user
	 * 
	 * @param userId
	 * @return
	 */
	public List<Transaction> findByUserId(int userId) {
		List<Transaction> transactions = transactionRepository.findByUserId(userId);
		return transactions;
	}
}

