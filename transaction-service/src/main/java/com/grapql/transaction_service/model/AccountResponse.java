package com.grapql.transaction_service.model;

import lombok.Data;

/**
 * Represents the response structure for GraphQL queries related to accounts.
 * This class is used to map the response received from GraphQL
 * queries/mutations.
 */
@Data
public class AccountResponse {

	private DataWrapper data; // Wrapper for the actual account-related response data

	/**
	 * Inner class representing the data section in the GraphQL response. This
	 * contains fields that map to specific GraphQL queries/mutations.
	 */
	@Data
	public static class DataWrapper {
		private Account getAccountByAccountNumber; // Maps to the "getAccountByAccountNumber" GraphQL query
		private Account updateAccountBalance; // Maps to the "updateAccountBalance" GraphQL mutation
	}
}
