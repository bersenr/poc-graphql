package com.grapql.transaction_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grapql.transaction_service.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

}
