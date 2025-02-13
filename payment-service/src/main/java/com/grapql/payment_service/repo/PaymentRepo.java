package com.grapql.payment_service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grapql.payment_service.entity.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

	List<Payment> findByAccountNumber(String accountNum);

	List<Payment> findByAccountNumberAndBillerId(String accountNum, int billerAccountNum);
}
