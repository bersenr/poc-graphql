package com.grapql.transaction_service.constant;

/**
 * Defines constants for different transaction types. This class provides
 * predefined values to avoid hardcoded strings in the codebase.
 */
public class TransactionType {

	// Transaction types for account operations
	public static final String DEPOSIT = "Deposit"; // Represents a deposit transaction
	public static final String WITHDRAW = "Withdraw"; // Represents a withdrawal transaction
	public static final String BALANCE_INQUIRY = "Balance Inquiry"; // Represents a balance check transaction

	// GraphQL operation types
	public static final String QUERY = "Query"; // Used for GraphQL queries
	public static final String MUTATION = "Mutation"; // Used for GraphQL mutations
}
