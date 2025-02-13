package com.grapql.transaction_service.resolvers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.grapql.transaction_service.entity.Transaction;
import com.grapql.transaction_service.serviceImpl.TransactionServiceImpl;

@Controller
public class TransactionResolver {

	@Autowired
	private TransactionServiceImpl transactionServiceImpl;

	@MutationMapping
	public Transaction deposit(@Argument String accountNumber, @Argument long amount) {
		return transactionServiceImpl.deposit(accountNumber, amount);
	}

	@MutationMapping
	public Transaction withdraw(@Argument String accountNumber, @Argument long amount) {
		return transactionServiceImpl.withdraw(accountNumber, amount);
	}

	@QueryMapping
	public Transaction balanceInquiry(@Argument String accountNumber) {
		return transactionServiceImpl.balanceInquiry(accountNumber);
	}

	@QueryMapping
	public List<Transaction> getTransactionHistory(@Argument String accountNumber) {
		return transactionServiceImpl.getTransactionHistory(accountNumber);
	}

	@QueryMapping
	public List<Transaction> getDeposits(@Argument String accountNumber) {
		return transactionServiceImpl.getDeposits(accountNumber);
	}

	@QueryMapping
	public List<Transaction> getWithdrawal(@Argument String accountNumber) {
		return transactionServiceImpl.getWithdrawal(accountNumber);
	}

	@QueryMapping
	public List<Transaction> getBalanceInquiry(@Argument String accountNumber) {
		return transactionServiceImpl.getBalanceInquiry(accountNumber);
	}
}
