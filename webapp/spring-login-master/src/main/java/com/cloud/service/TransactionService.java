package com.cloud.service;

import com.cloud.model.Transaction;
import com.cloud.repository.TransactionRepository;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("transactionService")
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public String saveTransaction(Transaction transaction) {

    	try
    	{
    		transaction.setDate(new Date());
    		transactionRepository.save(transaction);
    	}
    	catch (Exception e) {
			System.out.println("Error is "+e);
		}
        return "Success";

    }
}
