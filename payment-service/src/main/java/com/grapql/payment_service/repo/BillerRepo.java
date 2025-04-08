package com.grapql.payment_service.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grapql.payment_service.entity.Biller;

@Repository
public interface BillerRepo extends JpaRepository<Biller, Long> {

	Optional<Biller> findByBillerAccountNumber(int billerAccountNum);

	@Modifying
    @Query("DELETE FROM Biller b WHERE b.billerAccountNumber = :billerAccountNum")
    // This is a custom JPQL query to delete a Biller entity based on its account number.
    // `@Modifying` is required for DELETE and UPDATE queries.
    // `:billerAccountNum` is a named parameter mapped to the method parameter.
    void deleteByBillerAccountNumber(@Param("billerAccountNum") int billerAccountNum);
}
