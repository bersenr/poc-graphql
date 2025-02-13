package com.grapql.payment_service.service;

import java.util.List;
import java.util.Optional;

import com.grapql.payment_service.entity.Payment;

public interface PaymentService {
	Payment payBill(String accountNum, int billerAccountNum, long amount);

	Optional<Payment> getPayment(Long paymentId);

	List<Payment> getPaymentsByAccountNumber(String accountNum);

	List<Payment> getPaymentsByAccountNumberAndBiller(String accountNum, int billerAccountNum);
}
