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

	/**
	 * Mutation to deposit a specified amount into an account.
	 * 
	 * @param accountNumber The account number to deposit into.
	 * @param amount        The deposit amount.
	 * @return The completed transaction details.
	 */
	@MutationMapping
	public Transaction deposit(@Argument String accountNumber, @Argument long amount) {
		return transactionServiceImpl.deposit(accountNumber, amount);
	}

	/**
	 * Mutation to withdraw a specified amount from an account.
	 * 
	 * @param accountNumber The account number to withdraw from.
	 * @param amount        The withdrawal amount.
	 * @return The completed transaction details.
	 */
	@MutationMapping
	public Transaction withdraw(@Argument String accountNumber, @Argument long amount) {
		return transactionServiceImpl.withdraw(accountNumber, amount);
	}

	/**
	 * Query to check the current balance of an account.
	 * 
	 * @param accountNumber The account number for balance inquiry.
	 * @return The transaction details containing the balance.
	 */
	@QueryMapping
	public Transaction balanceInquiry(@Argument String accountNumber) {
		return transactionServiceImpl.balanceInquiry(accountNumber);
	}

	/**
	 * Query to retrieve the transaction history of an account.
	 * 
	 * @param accountNumber The account number to retrieve history for.
	 * @return A list of past transactions.
	 */
	@QueryMapping
	public List<Transaction> getTransactionHistory(@Argument String accountNumber) {
		return transactionServiceImpl.getTransactionHistory(accountNumber);
	}

	/**
	 * Query to retrieve all deposit transactions for an account.
	 * 
	 * @param accountNumber The account number to retrieve deposits for.
	 * @return A list of deposit transactions.
	 */
	@QueryMapping
	public List<Transaction> getDeposits(@Argument String accountNumber) {
		return transactionServiceImpl.getDeposits(accountNumber);
	}

	/**
	 * Query to retrieve all withdrawal transactions for an account.
	 * 
	 * @param accountNumber The account number to retrieve withdrawals for.
	 * @return A list of withdrawal transactions.
	 */
	@QueryMapping
	public List<Transaction> getWithdrawal(@Argument String accountNumber) {
		return transactionServiceImpl.getWithdrawal(accountNumber);
	}

	/**
	 * Query to retrieve all balance inquiry transactions for an account.
	 * 
	 * @param accountNumber The account number to retrieve balance inquiries for.
	 * @return A list of balance inquiry transactions.
	 */
	@QueryMapping
	public List<Transaction> getBalanceInquiry(@Argument String accountNumber) {
		return transactionServiceImpl.getBalanceInquiry(accountNumber);
	}
}
