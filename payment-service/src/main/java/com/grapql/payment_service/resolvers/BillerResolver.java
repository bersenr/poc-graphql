package com.grapql.payment_service.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.grapql.payment_service.entity.Biller;
import com.grapql.payment_service.serviceImpl.BillerServiceImpl;

@Controller
public class BillerResolver {

	@Autowired
	private BillerServiceImpl billerServiceImpl;

	@MutationMapping
	public Biller addBiller(@Argument int billerAccountNum, @Argument String billerName) {
		return billerServiceImpl.addBiller(billerAccountNum, billerName);
	}

	@MutationMapping
	public String deleteBiller(@Argument int billerAccountNum) {
		billerServiceImpl.deleteBiller(billerAccountNum);
		return "Biller deleted successfully";
	}

	@QueryMapping
	public Biller getBiller(@Argument int billerAccountNum) {
		return billerServiceImpl.getBiller(billerAccountNum).get();
	}
}
