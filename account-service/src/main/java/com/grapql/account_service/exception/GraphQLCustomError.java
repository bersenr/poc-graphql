package com.grapql.account_service.exception;

import graphql.ErrorClassification;

/**
 * Custom GraphQL error types.
 */
public enum GraphQLCustomError implements ErrorClassification {

	/** Optimistic locking conflict (HTTP 409). */
	OPTIMISTIC_LOCKING_FAILURE,

	/** Internal server error (HTTP 500). */
	INTERNAL_SERVER_ERROR,

	/** Entity not found (HTTP 404). */
	ENTITY_NOT_FOUND
}
