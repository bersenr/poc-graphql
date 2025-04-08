package com.grapql.account_service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grapql.account_service.entity.Account;
import com.grapql.account_service.repo.AccountRepo;
import com.grapql.account_service.service.AccountService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepo accountRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Account createAccount(Account account) {
		return accountRepo.save(account);
	}

	@Override
	public Optional<Account> getAccount(Long id) {
		return accountRepo.findById(id);
	}

	@Override
	public List<Account> getAccounts() {
		return accountRepo.findAll();
	}

	@Override
	public Optional<Account> getAccountByAccountNumber(String accountNumber) {
		Optional<Account> account = accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber);
		return account;
	}

	@Override
	@Transactional
	public Account updateAccountBalance(String accountNumber, long balance) {
		try {
			Account account = accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber)
					.orElseThrow(() -> new RuntimeException("Account does not exist."));

			log.info("Account version selected {}: {}", accountNumber, account.getVersion());

			Thread.sleep(3000); // Trigger delay to simulate concurrent update

			account.setBalance(balance);
			return accountRepo.save(account);
		} catch (Exception e) {
			log.error("Unexpected error while updating account {}: {}", accountNumber, e.getMessage());
			throw new RuntimeException("Could not update account balance.");
		}
	}
}
