package com.cloud.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cloud.model.Transaction;

@Repository("transactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

	Transaction findByTransactionId(UUID id);

	List<Transaction> findByUserId(int userid);

}
