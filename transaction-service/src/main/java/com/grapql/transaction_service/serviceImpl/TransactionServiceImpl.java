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

	@Value("${services.account.url}")
	private String accountServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TransactionRepo transactionRepo;

	@Override
	public Transaction deposit(String accountNumber, long amount) {
		Account account = getAccountByAccountNumber(accountNumber);
		if (account == null) {
			throw new RuntimeException("Account does not exists");
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

	@Override
	public Transaction withdraw(String accountNumber, long amount) {
		Account account = getAccountByAccountNumber(accountNumber);
		if (account == null) {
			throw new RuntimeException("Account does not exists");
		}

		long updatedBalance = account.getBalance() - amount;

		Account withdrawnAccount = updateAccountBalance(accountNumber, updatedBalance);

		Transaction transaction = new Transaction();
		transaction.setAccountNumber(withdrawnAccount.getAccountNumber());
		transaction.setAmount(amount);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionType(TransactionType.WITHDRAW);
		transaction.setBalance(withdrawnAccount.getBalance());

		return transactionRepo.save(transaction);
	}

	@Override
	public Transaction balanceInquiry(String accountNumber) {
		Account account = getAccountByAccountNumber(accountNumber);
		if (account == null) {
			throw new RuntimeException("Account does not exists");
		}

		Transaction transaction = new Transaction();
		transaction.setAccountNumber(accountNumber);
		transaction.setAmount(0);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setTransactionType(TransactionType.BALANCE_INQUIRY);
		transaction.setBalance(account.getBalance());

		return transactionRepo.save(transaction);
	}

	@Override
	public List<Transaction> getTransactionHistory(String accountNumber) {
		return transactionRepo.findAll().stream().sorted(Comparator.comparing(Transaction::getTransactionDate))
				.collect(Collectors.toList());
	}

	@Override
	public List<Transaction> getDeposits(String accountNumber) {
		return transactionRepo.findAll().stream()
				.filter(trans -> trans.getTransactionType().equals(TransactionType.DEPOSIT))
				.sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
	}

	@Override
	public List<Transaction> getWithdrawal(String accountNumber) {
		return transactionRepo.findAll().stream()
				.filter(trans -> trans.getTransactionType().equals(TransactionType.WITHDRAW))
				.sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
	}

	@Override
	public List<Transaction> getBalanceInquiry(String accountNumber) {
		return transactionRepo.findAll().stream()
				.filter(trans -> trans.getTransactionType().equals(TransactionType.BALANCE_INQUIRY))
				.sorted(Comparator.comparing(Transaction::getTransactionDate)).collect(Collectors.toList());
	}

	private Account getAccountByAccountNumber(String accountNumber) {
		String query = "getAccountByAccountNumber(accountNumber: \\\"" + accountNumber
				+ "\\\"){ accountNumber balance }";

		HttpEntity<String> request = TransactionServiceUtil.createHttpEntity(query, TransactionType.QUERY);
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(accountServiceUrl, request,
				AccountResponse.class);

		return response.getBody().getData().getGetAccountByAccountNumber();
	}

	private Account updateAccountBalance(String accountNumber, long balance) {
		String query = "updateAccountBalance(accountNumber: \\\"" + accountNumber + "\\\", balance: " + balance
				+ "){ accountNumber balance }";

		HttpEntity<String> request = TransactionServiceUtil.createHttpEntity(query, TransactionType.MUTATION);
		ResponseEntity<AccountResponse> response = restTemplate.postForEntity(accountServiceUrl, request,
				AccountResponse.class);

		return response.getBody().getData().getUpdateAccountBalance();
	}
}
