package com.grapql.transaction_service.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.grapql.transaction_service.constant.TransactionType;

/**
 * Utility class for constructing HTTP requests for GraphQL queries and mutations.
 * This class helps in creating properly formatted GraphQL request payloads 
 * to interact with the Account and Transaction services.
 */
public class TransactionServiceUtil {

    /**
     * Creates an HTTP request entity with a properly formatted GraphQL query or mutation.
     *
     * @param query The GraphQL query or mutation string (e.g., fetching account details or updating balance).
     * @param type  The type of request: either {@code TransactionType.QUERY} or {@code TransactionType.MUTATION}.
     * @return An {@link HttpEntity} containing the formatted GraphQL request with appropriate headers.
     */
    public static HttpEntity<String> createHttpEntity(String query, String type) {
        String queryString = "";

        // Determine whether the request is a Query or a Mutation and format it accordingly
        if (type.equals(TransactionType.QUERY)) {
            queryString = String.format("{ \"query\": \"query { %s }\" }", query);
        } else if (type.equals(TransactionType.MUTATION)) {
            queryString = String.format("{ \"query\": \"mutation { %s }\" }", query);
        }

        // Create HTTP headers and set content type as JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Return the formatted GraphQL request entity with headers
        return new HttpEntity<>(queryString, headers);
    }
}
