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
	public Account updateAccountBalance(String accountNumber, long balance) throws Exception {
		Optional<Account> account = accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber);
		if (!account.isPresent()) {
			throw new Exception("Account does not exist.");
		}

		int update = accountRepo.updateAccountBalance(accountNumber, balance);

		if (update == 1) {
			entityManager.clear();
			return accountRepo.findByAccountNumberEqualsIgnoreCase(accountNumber)
					.orElseThrow(() -> new Exception("Failed to retrieve updated account"));
		}

		return null;
	}

}
