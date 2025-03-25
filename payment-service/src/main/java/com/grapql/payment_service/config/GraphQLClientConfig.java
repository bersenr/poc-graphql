package com.grapql.payment_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQLClientConfig {

	@Value("${services.account.url}")
	private String accountServiceUrl;

	@Value("${services.transaction.url}")
	private String transactionServiceUrl;

	/**
	 * Creates a shared WebClient instance for making HTTP requests.
	 *
	 * @return A configured WebClient instance.
	 */
	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

	/**
	 * Configures a GraphQL client for interacting with the Account Service.
	 *
	 * @param webClient The WebClient instance used for HTTP communication.
	 * @return A GraphQL client for the account service.
	 */
	@Bean
	public HttpGraphQlClient accountService(WebClient webClient) {
		return HttpGraphQlClient.builder(webClient).url(accountServiceUrl).build();
	}

	/**
	 * Configures a GraphQL client for interacting with the Transaction Service.
	 *
	 * @param webClient The WebClient instance used for HTTP communication.
	 * @return A GraphQL client for the transaction service.
	 */
	@Bean
	public HttpGraphQlClient transactionService(WebClient webClient) {
		return HttpGraphQlClient.builder(webClient).url(transactionServiceUrl).build();
	}
}
