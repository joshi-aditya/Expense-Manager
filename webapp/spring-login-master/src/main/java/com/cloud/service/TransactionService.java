package com.cloud.service;

import com.cloud.model.Transaction;
import com.cloud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {

        return transactionRepository.save(transaction);

    }
}
