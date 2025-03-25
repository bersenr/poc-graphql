package com.grapql.account_service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grapql.account_service.entity.Account;
import com.grapql.account_service.repo.AccountRepo;
import com.grapql.account_service.service.AccountService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepo accountRepo;

	/**
	 * Creates and saves a new account.
	 *
	 * @param account The account entity to be created.
	 * @return The saved account.
	 */
	@Override
	public Account createAccount(Account account) {
		return accountRepo.save(account);
	}

	/**
	 * Retrieves an account by its ID.
	 *
	 * @param id The unique ID of the account.
	 * @return An Optional containing the account if found, otherwise empty.
	 */
	@Override
	public Optional<Account> getAccount(Long id) {
		return accountRepo.findById(id);
	}

	/**
	 * Retrieves all accounts.
	 *
	 * @return A list of all accounts.
	 */
	@Override
	public List<Account> getAccounts() {
		return accountRepo.findAll();
	}

	/**
	 * Retrieves an account by its account number (case-insensitive).
	 *
	 * @param accountNumber The unique account number.
	 * @return An Optional containing the account if found, otherwise empty.
	 */
	@Override
	public Optional<Account> getAccountByAccountNumber(String accountNumber) {
		return accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber);
	}

	/**
	 * Updates the balance of an account using optimistic locking.
	 * 
	 * @param accountNumber The account number to update.
	 * @param balance       The new balance to set.
	 * @return The updated account.
	 * @throws RuntimeException If the account does not exist or an error occurs.
	 */
	@Override
	@Transactional
	public Account updateAccountBalance(String accountNumber, long balance) {
		try {
			Account account = accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber).orElseThrow(
					() -> new RuntimeException("Account with number " + accountNumber + " does not exist."));

			Thread.sleep(3000); // Simulate concurrent update

			account.setBalance(balance);
			return accountRepo.save(account);
		} catch (Exception e) {
			log.error("Unexpected error while updating account {}: {}", accountNumber, e.getMessage());
			throw new RuntimeException("Could not update account balance.");
		}
	}
}
