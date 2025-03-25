package com.grapql.account_service.exception;

import java.util.Map;

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
	 * Handles exceptions thrown during GraphQL query execution. This method maps
	 * specific exceptions to custom GraphQL errors for better error handling.
	 *
	 * @param ex  The exception that was thrown.
	 * @param env The GraphQL environment providing details about the query
	 *            execution.
	 * @return A formatted GraphQL error with appropriate status and metadata.
	 * @see <a href="https://www.baeldung.com/spring-graphql-error-handling"> Spring
	 *      GraphQL Error Handling - Baeldung</a>
	 */
	@Override
	protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
		if (ex instanceof ObjectOptimisticLockingFailureException) {
			log.error("Optimistic locking error for field {}: {}", env.getField().getName(), ex.getMessage(), ex);
			return GraphqlErrorBuilder.newError().errorType(GraphQLCustomError.OPTIMISTIC_LOCKING_FAILURE)
					.message("Row was updated or deleted by another transaction").extensions(Map.of("status", 409))
					.path(env.getExecutionStepInfo().getPath()).location(env.getField().getSourceLocation()).build();
		} else if (ex instanceof AccountNotFoundException || ex instanceof ProfileNotFoundException) {
			log.error("Entity not found for field {}: {}", env.getField().getName(), ex.getMessage(), ex);
			return GraphqlErrorBuilder.newError().errorType(GraphQLCustomError.ENTITY_NOT_FOUND)
					.message("Requested entity not found").extensions(Map.of("status", 404))
					.path(env.getExecutionStepInfo().getPath()).location(env.getField().getSourceLocation()).build();
		} else {
			log.error("Unexpected error: {}", ex.getMessage(), ex);
			return GraphqlErrorBuilder.newError().errorType(GraphQLCustomError.INTERNAL_SERVER_ERROR)
					.message("An unexpected error occurred").extensions(Map.of("status", 500))
					.path(env.getExecutionStepInfo().getPath()).location(env.getField().getSourceLocation()).build();
		}
	}
}