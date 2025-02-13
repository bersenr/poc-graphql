package com.grapql.payment_service.service;

import java.util.Optional;

import com.grapql.payment_service.entity.Biller;

public interface BillerService {
	Biller addBiller(int billerAccountNum, String billerName);

	Optional<Biller> getBiller(int billerAccountNum);

	void deleteBiller(int billerAccountNum);

}
