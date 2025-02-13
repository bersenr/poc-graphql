package com.grapql.transaction_service.service;

import java.util.List;

import com.grapql.transaction_service.entity.Transaction;

public interface TransactionService {
	public Transaction deposit(String accountNumber, long amount);

	public Transaction withdraw(String accountNumber, long amount);

	public Transaction balanceInquiry(String accountNumber);

	public List<Transaction> getTransactionHistory(String accountNumber);

	public List<Transaction> getDeposits(String accountNumber);

	public List<Transaction> getWithdrawal(String accountNumber);

	public List<Transaction> getBalanceInquiry(String accountNumber);
}
