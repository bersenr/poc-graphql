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

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	public HttpGraphQlClient accountService(WebClient webClient) {
		return HttpGraphQlClient.builder(webClient).url(accountServiceUrl).build();
	}

	@Bean
	public HttpGraphQlClient transactionService(WebClient webClient) {
		return HttpGraphQlClient.builder(webClient).url(transactionServiceUrl).build();
	}
}
