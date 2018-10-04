package com.cloud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cloud.model.Transaction;

@Repository("transactionRepository")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
