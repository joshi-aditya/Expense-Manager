package com.cloud.model;

import java.util.List;

public class TransactionWrapper extends Status{

	private List<Transaction> transactions;

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}	
}
