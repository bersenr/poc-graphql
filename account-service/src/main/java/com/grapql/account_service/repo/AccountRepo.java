package com.grapql.account_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grapql.account_service.entity.Account;

import jakarta.transaction.Transactional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
	Optional<Account> findByAccountNumberEqualsIgnoreCase(String accountNumber);

	@Modifying
	@Transactional
	@Query("UPDATE Account ac SET balance = :balance WHERE accountNumber = :accountNumber")
	int updateAccountBalance(@Param("accountNumber") String accountNumber, @Param("balance") long balance);
}
