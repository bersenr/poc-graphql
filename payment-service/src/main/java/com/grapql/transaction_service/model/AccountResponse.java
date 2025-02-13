package com.grapql.transaction_service.model;

import lombok.Data;

@Data
public class AccountResponse {

	private DataWrapper data;

	@Data
	public static class DataWrapper {
		private Account getAccountByAccountNumber;
		private Account updateAccountBalance;
	}
}
