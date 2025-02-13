package com.grapql.account_service.service;

import java.util.List;
import java.util.Optional;

import com.grapql.account_service.entity.Account;

public interface AccountService {
	Account createAccount(Account account);

	Optional<Account> getAccount(Long id);

	Optional<Account> getAccountByAccountNumber(String accountNumber);

	List<Account> getAccounts();

	Account updateAccountBalance(String accountNumber, long balance) throws Exception;

}
