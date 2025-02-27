package com.grapql.account_service.exception;

import graphql.ErrorClassification;

public enum GraphQLCustomError implements ErrorClassification {
	OPTIMISTIC_LOCKING_FAILURE
}
