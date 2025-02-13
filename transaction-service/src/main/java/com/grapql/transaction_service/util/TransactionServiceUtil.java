package com.grapql.transaction_service.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.grapql.transaction_service.constant.TransactionType;

public class TransactionServiceUtil {

	public static HttpEntity<String> createHttpEntity(String query, String type) {
		String queryString = "";

		if (type.equals(TransactionType.QUERY)) {
			queryString = String.format("{ \"query\": \"query { %s }\" }", query);
		} else if (type.equals(TransactionType.MUTATION)) {
			queryString = String.format("{ \"query\": \"mutation { %s }\" }", query);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new HttpEntity<String>(queryString, headers);
	}
}
