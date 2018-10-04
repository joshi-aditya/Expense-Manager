package com.cloud.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cloud.model.Transaction;

@Repository("transactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	List<Transaction> findByUserId(int userid);

	Transaction findByTransactionId(int id);
}
