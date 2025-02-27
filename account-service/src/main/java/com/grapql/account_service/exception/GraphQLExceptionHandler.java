package com.grapql.account_service.exception;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

	/**
	 * https://www.baeldung.com/spring-graphql-error-handling
	 */
	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof ObjectOptimisticLockingFailureException) {
			log.error("Optimistic locking error for field {}: {}", env.getField().getName(), ex.getMessage(), ex);
			return GraphqlErrorBuilder.newError().errorType(GraphQLCustomError.OPTIMISTIC_LOCKING_FAILURE)
					.message("Row was updated or deleted by another transaction")
					.path(env.getExecutionStepInfo().getPath()).location(env.getField().getSourceLocation()).build();
		} else {
			return null;
		}
	}
}