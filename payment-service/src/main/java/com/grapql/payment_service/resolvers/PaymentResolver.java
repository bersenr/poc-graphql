package com.grapql.payment_service.resolvers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.grapql.payment_service.entity.Payment;
import com.grapql.payment_service.serviceImpl.PaymentServiceImpl;

@Controller
public class PaymentResolver {

	@Autowired
	private PaymentServiceImpl paymentServiceImpl;

	@MutationMapping
	public Payment payBill(@Argument String accountNum, @Argument int billerAccountNum, @Argument long amount) {
		return paymentServiceImpl.payBill(accountNum, billerAccountNum, amount);
	}

	@QueryMapping
	public Payment getPayment(@Argument Long paymentId) {
		return paymentServiceImpl.getPayment(paymentId).get();
	}

	@QueryMapping
	public List<Payment> getPaymentsByAccountNumber(@Argument String accountNumber) {
		return paymentServiceImpl.getPaymentsByAccountNumber(accountNumber);
	}

	@QueryMapping
	public List<Payment> getPaymentsByAccountNumberAndBiller(@Argument String accountNumber,
			@Argument int billerAccountNum) {
		return paymentServiceImpl.getPaymentsByAccountNumberAndBiller(accountNumber, billerAccountNum);
	}
}
