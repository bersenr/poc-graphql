package com.grapql.transaction_service.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for creating a RestTemplate bean. RestTemplate is used
 * for making REST API calls.
 */
@Configuration
public class RestTemplateConfig {

	/**
	 * Creates and configures a {@link RestTemplate} bean.
	 * 
	 * @param builder A {@link RestTemplateBuilder} for customizing the
	 *                RestTemplate.
	 * @return A configured RestTemplate instance.
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}