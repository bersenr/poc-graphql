package com.grapql.transaction_service.model;

import lombok.Data;

@Data
public class Account {
	private Long id;

	private String accountNumber;

	private String accountName;

	private long balance;
}
