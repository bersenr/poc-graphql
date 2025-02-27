package com.grapql.account_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grapql.account_service.entity.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
	Optional<Account> findByAccountNumberEqualsIgnoreCase(String accountNumber);
}
