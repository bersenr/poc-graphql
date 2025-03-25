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

	/**
     * GraphQL Mutation to process a bill payment.
     *
     * @param accountNum       The account number of the payer.
     * @param billerAccountNum The biller's account number to which the payment is made.
     * @param amount           The amount to be paid.
     * @return The newly created Payment entity.
     */
    @MutationMapping
    public Payment payBill(@Argument String accountNum, @Argument int billerAccountNum, @Argument long amount) {
        return paymentServiceImpl.payBill(accountNum, billerAccountNum, amount);
    }

    /**
     * GraphQL Query to retrieve a specific Payment by its ID.
     *
     * @param paymentId The unique identifier of the Payment.
     * @return The Payment entity if found.
     */
    @QueryMapping
    public Payment getPayment(@Argument Long paymentId) {
        return paymentServiceImpl.getPayment(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment with ID " + paymentId + " not found"));
    }

    /**
     * GraphQL Query to retrieve all Payments associated with a given account number.
     *
     * @param accountNumber The account number of the payer.
     * @return A list of Payment entities related to the specified account number.
     */
    @QueryMapping
    public List<Payment> getPaymentsByAccountNumber(@Argument String accountNumber) {
        return paymentServiceImpl.getPaymentsByAccountNumber(accountNumber);
    }

    /**
     * GraphQL Query to retrieve all Payments made from a specific account to a specific Biller.
     *
     * @param accountNumber    The account number of the payer.
     * @param billerAccountNum The biller's account number.
     * @return A list of Payment entities that match the account and biller.
     */
    @QueryMapping
    public List<Payment> getPaymentsByAccountNumberAndBiller(@Argument String accountNumber,
                                                             @Argument int billerAccountNum) {
        return paymentServiceImpl.getPaymentsByAccountNumberAndBiller(accountNumber, billerAccountNum);
    }
}
