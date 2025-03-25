package com.grapql.transaction_service.serviceImpl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.grapql.transaction_service.constant.TransactionType;
import com.grapql.transaction_service.entity.Transaction;
import com.grapql.transaction_service.model.Account;
import com.grapql.transaction_service.model.AccountResponse;
import com.grapql.transaction_service.repo.TransactionRepo;
import com.grapql.transaction_service.service.TransactionService;
import com.grapql.transaction_service.util.TransactionServiceUtil;

@Service
public class TransactionServiceImpl implements TransactionService {
	// URL for the Account Service, fetched from application properties
	@Value("${services.account.url}")
	private String accountServiceUrl;

	@Autowired
	private RestTemplate restTemplate; // RestTemplate for making HTTP requests to Account Service

	@Autowired
	private TransactionRepo transactionRepo;

	/**
	 * Performs a deposit transaction.
	 *
	 * @param accountNumber The account to deposit into.
	 * @param amount        The amount to deposit.
	 * @return The saved transaction record.
	 */
	@Override
	public Transaction deposit(String accountNumber, long amount) {
		Account account = getAccountByAccountNumber(accountNumber);
		if (account == null) {
			throw new RuntimeException("Account does not exist");
		}

		long updatedBalance = account.getBalance() + amount;

		Account depositedAccount = updateAccountBalance(accountNumber, updatedBalance);

		Transaction transaction = new Transaction();
		transaction.setAccountNumber(depositedAccount.getAccountNumber());
		transaction.setAmount(amount);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionType(TransactionType.DEPOSIT);
		transaction.setBalance(depositedAccount.getBalance());

		return transactionRepo.save(transaction);
	}

	/**
	 * Performs a withdrawal transaction.
	 *
	 * @param accountNumber The account to withdraw from.
	 * @param amount        The amount to withdraw.
	 * @return The saved transaction record.
	 */
	@Override
	public Transaction withdraw(String accountNumber, long amount) {
		Account account = getAccountByAccountNumber(accountNumber);
		if (account == null) {
			throw new RuntimeException("Account does not exist");
		}

		long updatedBalance = account.getBalance() - amount;

		// Update the account balance in the Account Service
		Account withdrawnAccount = updateAccountBalance(accountNumber, updatedBalance);

		// Create and save a new transaction record
		Transaction transaction = new Transaction();
		transaction.setAccountNumber(withdrawnAccount.getAccountNumber());
		transaction.setAmount(amount);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionType(TransactionType.WITHDRAW);
		transaction.setBalance(withdrawnAccount.getBalance());

		return transactionRepo.save(transaction);
	}

	/**
	 * Performs a balance inquiry.
	 *
	 * @param accountNumber The account to check the balance for.
	 * @return The saved transaction record for the balance inquiry.
	 */
	@Override
	public Transaction balanceInquiry(String accountNumber) {
		Account account = getAccountByAccountNumber(accountNumber);
		if (account == null) {
			throw new RuntimeException("Account does not exist");
		}

		Transaction transaction = new Transaction();
		transaction.setAccountNumber(accountNumber);
		transaction.setAmount(0);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionType(TransactionType.BALANCE_INQUIRY);
		transaction.setBalance(account.getBalance());

		return transactionRepo.save(transaction);
	}

	/**
	 * Retrieves the transaction history for a specific account.
	 *
	 * @param accountNumber The account number to fetch transactions for.
	 * @return A sorted list of all transactions related to the account.
	 */
	@Override
	public List<Transaction> getTransactionHistory(String accountNumber) {
		return transactionRepo.findAll().stream().sorted(Comparator.comparing(Transaction::getTransactionDate))
				.collect(Collectors.toList());
	}

	/**
	 * Retrieves all deposit transactions for a specific account.
	 *
	 * @param accountNumber The account number to fetch deposits for.
	 * @return A sorted list of deposit transactions.
	 */
	@Override
	public List<Transaction> getDeposits(String accountNumber) {
		return transactionRepo.findAll().stream()
				.filter(trans -> trans.getTransactionType().equals(TransactionType.DEPOSIT))
				.sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
	}

	/**
	 * Retrieves all withdrawal transactions for a specific account.
	 *
	 * @param accountNumber The account number to fetch withdrawals for.
	 * @return A sorted list of withdrawal transactions.
	 */
	@Override
	public List<Transaction> getWithdrawal(String accountNumber) {
		return transactionRepo.findAll().stream()
				.filter(trans -> trans.getTransactionType().equals(TransactionType.WITHDRAW))
				.sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
	}

	/**
	 * Retrieves all balance inquiry transactions for a specific account.
	 *
	 * @param accountNumber The account number to fetch balance inquiries for.
	 * @return A sorted list of balance inquiry transactions.
	 */
	@Override
	public List<Transaction> getBalanceInquiry(String accountNumber) {
		return transactionRepo.findAll().stream()
				.filter(trans -> trans.getTransactionType().equals(TransactionType.BALANCE_INQUIRY))
				.sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
	}

	/**
	 * Fetches account details from the Account Service using a GraphQL query.
	 *
	 * @param accountNumber The account number to fetch details for.
	 * @return The account details retrieved from the Account Service.
	 */
	private Account getAccountByAccountNumber(String accountNumber) {
		// GraphQL query to get account details by account number
		String query = "getAccountByAccountNumber(accountNumber: \\\"" + accountNumber
				+ "\\\"){ accountNumber balance }";

		// Create an HTTP request entity for the GraphQL query
		HttpEntity<String> request = TransactionServiceUtil.createHttpEntity(query, TransactionType.QUERY);

		// Send request to the Account Service and retrieve response
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(accountServiceUrl, request,
				AccountResponse.class);

		return response.getBody().getData().getGetAccountByAccountNumber();
	}

	/**
	 * Updates the account balance in the Account Service using a GraphQL mutation.
	 *
	 * @param accountNumber The account number to update.
	 * @param balance       The new balance to set.
	 * @return The updated account details retrieved from the Account Service.
	 */
	private Account updateAccountBalance(String accountNumber, long balance) {
		// GraphQL mutation to update account balance
		String query = "updateAccountBalance(accountNumber: \\\"" + accountNumber + "\\\", balance: " + balance
				+ "){ accountNumber balance }";

		// Create an HTTP request entity for the GraphQL mutation
		HttpEntity<String> request = TransactionServiceUtil.createHttpEntity(query, TransactionType.MUTATION);

		// Send request to the Account Service and retrieve response
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(accountServiceUrl, request,
				AccountResponse.class);

		return response.getBody().getData().getUpdateAccountBalance();
	}
}
