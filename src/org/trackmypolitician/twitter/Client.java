package org.trackmypolitician.twitter;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public final class Client {

	/**
	 * Request with Twitter API OAuth token
	 */
	private final HttpEntity<Object> twitterRequest;

	/**
	 * REST client
	 */
	private final RestTemplate restClient;

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
		this.restClient = new RestTemplate();
		Map<?, ?> response = restClient.exchange(oauth, HttpMethod.POST, request, Map.class).getBody();

		// Verify response, and set token
		if (!response.get("token_type").equals("bearer"))
			throw new Exception("Token type is not bearer");

		// Setup header and entity for requests
		HttpHeaders twitterHeader = new HttpHeaders();
		twitterHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		twitterHeader.add("Authorization", "Bearer " + (String) response.get("access_token"));
		this.twitterRequest = new HttpEntity<>(twitterHeader);
	}

	/**
	 * Gets tweets by a certain user
	 * 
	 * @param user
	 *            Twitter username
	 * @return Array of tweets
	 */
	public Tweet[] GetTweets(String user) {
		String uri = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name={user}";
		return restClient.exchange(uri, HttpMethod.GET, twitterRequest, Tweet[].class, user).getBody();
	}

	/**
	 * Gets the remaining requests for user timelines
	 * 
	 * @return Number of remaining requests
	 */
	public int RequestsRemaining() {
		String uri = "https://api.twitter.com/1.1/application/rate_limit_status.json?resources=statuses";

		Map<?, ?> result = restClient.exchange(uri, HttpMethod.GET, twitterRequest, Map.class).getBody();
		Map<?, ?> resource = (Map<?, ?>) result.get("resources");
		Map<?, ?> statuses = (Map<?, ?>) resource.get("statuses");
		Map<?, ?> user_time = (Map<?, ?>) statuses.get("/statuses/user_timeline");

		return (int) user_time.get("remaining");
	}
}
