package org.trackmypolitician.twitter;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public final class Client {
	/**
	 * Logger for the class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	/**
	 * Header with Twitter API OAuth token
	 */

	private final HttpHeaders twitterHeader;

	/**
	 * Constructs a client and generates OAuth token for secure requests
	 * 
	 * @param appKey
	 *            Twitter Consumer Key
	 * @param appSecret
	 *            Twitter Consumer Secret
	 * @throws Exception
	 */
	public Client(String appKey, String appSecret) throws Exception {
		// To RFC1738 (skipped), combine, then encode to Base64
		String consumer_rfc1738 = appKey + ":" + appSecret;
		String auth_base64 = Base64.getEncoder().encodeToString(consumer_rfc1738.getBytes());

		// Setup headers
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		header.add("Authorization", "Basic " + auth_base64);

		// POST authorization request
		String oauth = "https://api.twitter.com/oauth2/token";
		HttpEntity<String> request = new HttpEntity<String>("grant_type=client_credentials", header);
		RestTemplate rest = new RestTemplate();
		Map<?, ?> response = rest.exchange(oauth, HttpMethod.POST, request, Map.class).getBody();

		// Verify response, and set token
		if (!response.get("token_type").equals("bearer"))
			throw new Exception("Token type is not bearer");

		// Setup header for requests
		this.twitterHeader = new HttpHeaders();
		this.twitterHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		this.twitterHeader.add("Authorization", "Bearer " + (String) response.get("access_token"));

		logger.info("Logging complete");
	}
}
