package com.grapql.account_service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import com.grapql.account_service.entity.Account;
import com.grapql.account_service.repo.AccountRepo;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class OptimisticLockingTest {

	@Autowired
	private AccountRepo accountRepo;

	@Test
	@DisplayName("Optimistic Locking Test")
	void testOptimisticLocking() throws Exception {
		log.info("Starting Optimistic Locking Test");

		Account account = new Account();
		account.setAccountName("Savings");
		account.setAccountNumber("1234");
		account.setBalance(100);
		account = accountRepo.save(account);
		log.info("Initial Account Saved: {}", account);

		String accountNumber = account.getAccountNumber();

		ExecutorService executor = Executors.newFixedThreadPool(2);

		Future<Void> task1 = executor.submit(() -> {
			log.info("Task 1 - Updating Account");
			updateAccountConcurrently(accountNumber, 200, "Task 1");
			return null;
		});

		Future<Void> task2 = executor.submit(() -> {
			log.info("Task 2 - Updating Account");
			updateAccountConcurrently(accountNumber, 300, "Task 2");
			return null;
		});

		assertThatThrownBy(() -> {
			try {
				task1.get();
				task2.get();
			} catch (ExecutionException e) {
				throw e.getCause();
			}
		}).isInstanceOf(ObjectOptimisticLockingFailureException.class);

		executor.shutdown();
		log.info("Optimistic Locking Test Completed");
	}

	@Transactional
	void updateAccountConcurrently(String accountNumber, long balance, String taskName) throws Exception {
		try {
			log.info("{} - Fetching account {}", taskName, accountNumber);
			Account account = accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber)
					.orElseThrow(() -> new RuntimeException("Account not found"));

			account.setBalance(balance);
			accountRepo.save(account);
			log.info("{} - Account updated successfully with new balance: {}", taskName, balance);
		} catch (Exception e) {
			log.error("{} - Optimistic Locking Failure: {}", taskName, e.getMessage(), e);
			throw e;
		}
	}
}