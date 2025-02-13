package com.grapql.account_service.resolvers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.grapql.account_service.entity.Account;
import com.grapql.account_service.entity.Profile;
import com.grapql.account_service.serviceImpl.AccountServiceImpl;
import com.grapql.account_service.serviceImpl.ProfileServiceImpl;

@Controller
public class AccountResolvers {

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@Autowired
	private ProfileServiceImpl profileServiceImpl;

	@MutationMapping
	public Account addAccount(@Argument String accountNumber, @Argument String accountName, @Argument String fullName,
			@Argument String number) {
		Profile profile = new Profile();
		profile.setFullName(fullName);
		profile.setNumber(number);

		Profile savedProfile = profileServiceImpl.addProfile(profile);

		Account account = new Account();
		account.setAccountName(accountName);
		account.setAccountNumber(accountNumber);
		account.setBalance(0);
		account.setAccountProfile(savedProfile);

		return accountServiceImpl.createAccount(account);
	}

	@MutationMapping
	public Account updateAccountBalance(@Argument String accountNumber, @Argument long balance) throws Exception {
		return accountServiceImpl.updateAccountBalance(accountNumber, balance);
	}

	@QueryMapping
	public Account getAccountById(@Argument Long id) {
		Optional<Account> account = accountServiceImpl.getAccount(id);
		return account.get();
	}

	@QueryMapping
	public List<Account> getAccounts() {
		return accountServiceImpl.getAccounts();
	}

	@QueryMapping
	public Account getAccountByAccountNumber(@Argument String accountNumber) {
		Optional<Account> account = accountServiceImpl.getAccountByAccountNumber(accountNumber);
		return account.get();
	}
}
