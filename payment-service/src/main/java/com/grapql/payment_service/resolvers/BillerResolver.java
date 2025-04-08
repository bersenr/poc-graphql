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

	/**
	 * GraphQL Mutation to add a new Biller.
	 *
	 * @param billerAccountNum The unique account number of the Biller.
	 * @param billerName       The name of the Biller.
	 * @return The newly created Biller entity.
	 */
	@MutationMapping
	public Biller addBiller(@Argument int billerAccountNum, @Argument String billerName) {
		return billerServiceImpl.addBiller(billerAccountNum, billerName);
	}

	/**
	 * GraphQL Mutation to delete a Biller by its account number.
	 *
	 * @param billerAccountNum The unique account number of the Biller to be deleted.
	 * @return A success message after deletion.
	 */
	@MutationMapping
	public String deleteBiller(@Argument int billerAccountNum) {
		billerServiceImpl.deleteBiller(billerAccountNum);
		return "Biller deleted successfully";
	}

	/**
	 * GraphQL Query to fetch a Biller by its account number.
	 *
	 * @param billerAccountNum The unique account number of the Biller.
	 * @return The Biller entity if found.
	 */
	@QueryMapping
	public Biller getBiller(@Argument int billerAccountNum) {
		return billerServiceImpl.getBiller(billerAccountNum).orElseThrow(
				() -> new RuntimeException("Biller with account number " + billerAccountNum + " not found"));
	}
}
