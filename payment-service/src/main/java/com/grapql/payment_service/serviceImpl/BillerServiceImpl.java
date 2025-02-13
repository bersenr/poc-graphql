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

	@Override
	public Biller addBiller(int billerAccountNum, String billerName) {
		Biller biller = new Biller();
		biller.setBillerAccountNumber(billerAccountNum);
		biller.setBillerName(billerName);
		return billerRepo.save(biller);
	}

	@Override
	public Optional<Biller> getBiller(int billerAccountNum) {
		return billerRepo.findByBillerAccountNumber(billerAccountNum);
	}

	@Transactional
	@Override
	public void deleteBiller(int billerAccountNum) {
		billerRepo.deleteByBillerAccountNumber(billerAccountNum);
	}

}
