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

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private BillerServiceImpl billerServiceImpl;

	@Value("${services.account.url}")
	private String accountServiceUrl;

	@Autowired
	private HttpGraphQlClient accountService;

	@Autowired
	private HttpGraphQlClient transactionService;

	@Override
	public Payment payBill(String accountNum, int billerAccountNum, long amount) throws RuntimeException {
		Account account = getAccount(accountNum);
		if (account == null) {
			throw new RuntimeException("Account does not exists");
		}

		Optional<Biller> biller = billerServiceImpl.getBiller(billerAccountNum);
		if (biller == null) {
			throw new RuntimeException("Biller does not exists");
		}

		Payment payment = new Payment();
		payment.setBiller(biller.get());
		payment.setAccountNumber(accountNum);
		payment.setPaymentDate(LocalDate.now());
		payment.setAmount(amount);

		Account accnt = getBalance(accountNum);

		if (accnt == null) {
			throw new RuntimeException("Account does not exists");
		}

		if (amount > accnt.getBalance()) {
			throw new RuntimeException("Not enough balance");
		}

		long newBalance = accnt.getBalance() - amount;
		updateBalance(accountNum, newBalance);

		return paymentRepo.save(payment);
	}

	@Override
	public Optional<Payment> getPayment(Long paymentId) {
		return paymentRepo.findById(paymentId);
	}

	@Override
	public List<Payment> getPaymentsByAccountNumber(String accountNum) {
		return paymentRepo.findByAccountNumber(accountNum);
	}

	@Override
	public List<Payment> getPaymentsByAccountNumberAndBiller(String accountNum, int billerAccountNum) {
		return paymentRepo.findByAccountNumberAndBillerId(accountNum, billerAccountNum);
	}

	private Account getAccount(String accountNumber) {
		return accountService.documentName("getAccountByAccountNumber").variable("accountNumber", accountNumber)
				.retrieveSync("getAccountByAccountNumber").toEntity(Account.class);
	}

	private Account updateBalance(String accountNumber, long balance) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("accountNumber", accountNumber);
		variables.put("balance", balance);
		return accountService.documentName("updateBalance").variables(variables).retrieveSync("updateBalance")
				.toEntity(Account.class);
	}

	private Account getBalance(String accountNumber) {
		return transactionService.documentName("balanceInquiry").variable("accountNumber", accountNumber)
				.retrieveSync("balanceInquiry").toEntity(Account.class);
	}
}
