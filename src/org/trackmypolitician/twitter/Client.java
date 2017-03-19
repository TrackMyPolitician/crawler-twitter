package org.trackmypolitician.twitter;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to refactor REST API for Twitter
 *
 */
public final class Client {
	/**
	 * Location of the Twitter API server
	 */
	private static final String TWITTER = "https://api.twitter.com/1.1";

	/**
	 * Maximum allowed per API request. TODO: Make programmatic
	 */
	public final int MaxTweetsPerRequest = 200;

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
	 * Gets tweets by a certain {@link User}
	 * 
	 * @param user
	 *            Twitter user
	 * @param maxID
	 *            Highest tweet ID (inclusive)
	 * @return Array of tweets
	 */
	public Tweet[] GetTweets(User user, long maxID) {
		return GetTweets(user.getScreenName(), user.getLastKnownTweet(), maxID);
	}
 
	
	/**
	 * Gets tweets by certain user
	 * 
	 * @param user
	 *            Tweeter screen name
	 * @param sinceID
	 *            Lowest tweet ID (exclusive)
	 * @param maxID
	 *            Highest tweet ID (inclusive)
	 * @return
	 */
	public Tweet[] GetTweets(final String user, final long sinceID, final long maxID) {
		// API location
		StringBuilder uri = new StringBuilder(TWITTER).append("/statuses/user_timeline.json");

		// Screen name
		uri.append("?screen_name=").append(user);

		// Count
		uri.append("&count=").append(MaxTweetsPerRequest);

		// since_id
		if (sinceID > 0L)
			uri.append("&since_id=").append(sinceID);

		// max_id
		if (maxID < Long.MAX_VALUE)
			uri.append("&max_id=").append(maxID);

		return restClient.exchange(uri.toString(), HttpMethod.GET, twitterRequest, Tweet[].class).getBody();
	}

	/**
	 * Gets the remaining requests for user timelines
	 * 
	 * @return Remaining quota
	 */
	public Quota TimelineQuota() {
		final String uri = TWITTER + "/application/rate_limit_status.json?resources=statuses";

		JsonNode result = restClient.exchange(uri, HttpMethod.GET, twitterRequest, JsonNode.class).getBody();
		JsonNode timeline = result.get("resources").get("statuses").get("/statuses/user_timeline");

		return new ObjectMapper().convertValue(timeline, Quota.class);
	}
}
