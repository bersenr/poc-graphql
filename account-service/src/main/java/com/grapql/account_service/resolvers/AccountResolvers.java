package com.grapql.account_service.resolvers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.grapql.account_service.entity.Account;
import com.grapql.account_service.entity.Profile;
import com.grapql.account_service.exception.AccountNotFoundException;
import com.grapql.account_service.serviceImpl.AccountServiceImpl;
import com.grapql.account_service.serviceImpl.ProfileServiceImpl;

/**
 * GraphQL resolver for handling account-related mutations and queries.
 */
@Controller
public class AccountResolvers {

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@Autowired
	private ProfileServiceImpl profileServiceImpl;

	/**
	 * Mutation to create a new account with an associated profile.
	 * 
	 * @param accountNumber Unique account number.
	 * @param accountName   Name of the account.
	 * @param fullName      Full name of the profile owner.
	 * @param number        Contact number of the profile.
	 * @return The newly created Account.
	 */
	@MutationMapping
	public Account addAccount(@Argument String accountNumber, @Argument String accountName, @Argument String fullName,
			@Argument String phoneNumber) {
		Profile profile = new Profile();
		profile.setFullName(fullName);
		profile.setPhoneNumber(phoneNumber);

		Profile savedProfile = profileServiceImpl.addProfile(profile);

		Account account = new Account();
		account.setAccountName(accountName);
		account.setAccountNumber(accountNumber);
		account.setBalance(0);
		account.setAccountProfile(savedProfile);

		return accountServiceImpl.createAccount(account);
	}

	/**
	 * Mutation to update an account's balance. Uses optimistic locking to prevent
	 * concurrent update issues.
	 *
	 * @param accountNumber The account number to update.
	 * @param balance       The new balance value.
	 * @return The updated Account entity.
	 * @throws Exception If an error occurs during update (e.g., optimistic locking
	 *                   failure).
	 */
	@MutationMapping
	public Account updateAccountBalance(@Argument String accountNumber, @Argument long balance) throws Exception {
		return accountServiceImpl.updateAccountBalance(accountNumber, balance);
	}

	/**
	 * Query to retrieve an account by its unique ID.
	 * 
	 * @param id The account ID.
	 * @return The Account entity.
	 * @throws AccountNotFoundException If no account is found with the given ID.
	 */
	@QueryMapping
	public Account getAccountById(@Argument Long id) {
		return accountServiceImpl.getAccount(id)
				.orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
	}

	/**
	 * Query to retrieve all accounts.
	 * 
	 * @return A list of all Account entities.
	 */
	@QueryMapping
	public List<Account> getAccounts() {
		return accountServiceImpl.getAccounts();
	}

	/**
	 * Query to retrieve an account by its account number.
	 * 
	 * @param accountNumber The account number to search for.
	 * @return The Account entity.
	 * @throws AccountNotFoundException If no account is found with the given
	 *                                  account number.
	 */
	@QueryMapping
	public Account getAccountByAccountNumber(@Argument String accountNumber) {
		return accountServiceImpl.getAccountByAccountNumber(accountNumber).orElseThrow(
				() -> new AccountNotFoundException("Account with account number " + accountNumber + " not found"));
	}
}