package com.grapql.payment_service.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grapql.payment_service.entity.Biller;
import com.grapql.payment_service.repo.BillerRepo;
import com.grapql.payment_service.service.BillerService;

import jakarta.transaction.Transactional;

@Service
public class BillerServiceImpl implements BillerService {

	@Autowired
	private BillerRepo billerRepo;

	/**
     * Adds a new Biller to the database.
     *
     * @param billerAccountNum The unique account number of the Biller.
     * @param billerName       The name of the Biller.
     * @return The newly created and saved Biller entity.
     */
    @Override
    public Biller addBiller(int billerAccountNum, String billerName) {
        Biller biller = new Biller();
        biller.setBillerAccountNumber(billerAccountNum);
        biller.setBillerName(billerName);
        return billerRepo.save(biller);
    }

    /**
     * Retrieves a Biller by its account number.
     *
     * @param billerAccountNum The unique account number of the Biller.
     * @return An Optional containing the Biller entity if found, or empty if not.
     */
    @Override
    public Optional<Biller> getBiller(int billerAccountNum) {
        return billerRepo.findByBillerAccountNumber(billerAccountNum);
    }

    /**
     * Deletes a Biller by its account number.
     * Uses the `@Transactional` annotation to ensure data consistency.
     *
     * @param billerAccountNum The unique account number of the Biller to be deleted.
     */
    @Transactional
    @Override
    public void deleteBiller(int billerAccountNum) {
        billerRepo.deleteByBillerAccountNumber(billerAccountNum);
    }
}
