package com.grapql.payment_service.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.grapql.payment_service.entity.Biller;
import com.grapql.payment_service.entity.Payment;
import com.grapql.payment_service.repo.PaymentRepo;
import com.grapql.payment_service.service.PaymentService;
import com.grapql.transaction_service.constant.TransactionType;
import com.grapql.transaction_service.model.Account;
import com.grapql.transaction_service.model.AccountResponse;
import com.grapql.transaction_service.util.TransactionServiceUtil;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private BillerServiceImpl billerServiceImpl;

	@Value("${services.account.url}")
	private String accountServiceUrl;

	@Override
	public Payment payBill(String accountNum, int billerAccountNum, long amount) throws RuntimeException {
		Account account = getAccountByAccountNumber(accountNum);
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

		long newBalance = account.getBalance() - amount;
		updateAccountBalance(accountNum, newBalance);

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
