package com.grapql.payment_service.serviceImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;

import com.grapql.payment_service.entity.Biller;
import com.grapql.payment_service.entity.Payment;
import com.grapql.payment_service.repo.PaymentRepo;
import com.grapql.payment_service.service.PaymentService;
import com.grapql.transaction_service.model.Account;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private BillerServiceImpl billerServiceImpl;

	@Value("${services.account.url}")
	private String accountServiceUrl;

	@Autowired
	private HttpGraphQlClient accountService; // Must match the bean method name in GraphQLClientConfig

	@Autowired
	private HttpGraphQlClient transactionService; // Must match the bean method name in GraphQLClientConfig

	/**
	 * Processes a bill payment.
	 *
	 * @param accountNum       The payer's account number.
	 * @param billerAccountNum The biller's account number.
	 * @param amount           The payment amount.
	 * @return The saved Payment entity after processing.
	 * @throws RuntimeException If the account or biller does not exist, or if funds are insufficient.
	 */
	@Override
	@Transactional
	public Payment payBill(String accountNum, int billerAccountNum, long amount) throws RuntimeException {
		Account account = getAccount(accountNum);
		if (account == null) {
			throw new RuntimeException("Account with number " + accountNum + " does not exist");
		}

		Optional<Biller> biller = billerServiceImpl.getBiller(billerAccountNum);
		if (biller.isEmpty()) {
			throw new RuntimeException("Biller with account number " + billerAccountNum + " does not exist");
		}

		Payment payment = new Payment();
		payment.setBiller(biller.get());
		payment.setAccountNumber(accountNum);
		payment.setPaymentDate(LocalDate.now());
		payment.setAmount(amount);

		Account accnt = getBalance(accountNum);
		if (accnt == null) {
			throw new RuntimeException("Account with number " + accountNum + " does not exist");
		}

		if (amount > accnt.getBalance()) {
			throw new RuntimeException("Not enough balance to complete the transaction");
		}

		long newBalance = accnt.getBalance() - amount;
		updateBalance(accountNum, newBalance);

		return paymentRepo.save(payment);
	}

	/**
	 * Retrieves a specific payment by its ID.
	 *
	 * @param paymentId The unique identifier of the Payment.
	 * @return An Optional containing the Payment entity if found.
	 */
	@Override
	public Optional<Payment> getPayment(Long paymentId) {
		return paymentRepo.findById(paymentId);
	}

	/**
	 * Retrieves all payments made by a specific account.
	 *
	 * @param accountNum The payer's account number.
	 * @return A list of payments associated with the given account.
	 */
	@Override
	public List<Payment> getPaymentsByAccountNumber(String accountNum) {
		return paymentRepo.findByAccountNumber(accountNum);
	}

	/**
	 * Retrieves all payments made by a specific account to a specific biller.
	 *
	 * @param accountNum       The payer's account number.
	 * @param billerAccountNum The biller's account number.
	 * @return A list of payments that match the account and biller.
	 */
	@Override
	public List<Payment> getPaymentsByAccountNumberAndBiller(String accountNum, int billerAccountNum) {
		return paymentRepo.findByAccountNumberAndBillerId(accountNum, billerAccountNum);
	}

	/**
	 * Fetches account details by account number via the account service.
	 *
	 * @param accountNumber The account number.
	 * @return The Account entity retrieved.
	 */
	private Account getAccount(String accountNumber) {
		return accountService.documentName("getAccountByAccountNumber").variable("accountNumber", accountNumber)
				.retrieveSync("getAccountByAccountNumber").toEntity(Account.class);
	}

	/**
	 * Updates the account balance after a payment transaction.
	 *
	 * @param accountNumber The account number.
	 * @param balance       The new balance after deduction.
	 * @return The updated Account entity.
	 */
	private Account updateBalance(String accountNumber, long balance) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("accountNumber", accountNumber);
		variables.put("balance", balance);
		return accountService.documentName("updateBalance").variables(variables).retrieveSync("updateAccountBalance")
				.toEntity(Account.class);
	}

	/**
	 * Retrieves the current balance of an account via the transaction service.
	 *
	 * @param accountNumber The account number.
	 * @return The Account entity containing balance information.
	 */
	private Account getBalance(String accountNumber) {
		return transactionService.documentName("balanceInquiry").variable("accountNumber", accountNumber)
				.retrieveSync("balanceInquiry").toEntity(Account.class);
	}
}
