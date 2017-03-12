package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable JSON object to represent Twitter users
 *
 */
public final class User {

	/**
	 * Limit construction to JSON only
	 */
	private User() {
	}

	/**
	 * Twitter handle of the user
	 */
	@JsonProperty
	private String screen_name;

	/**
	 * Latest processed tweet by the user
	 */
	@JsonProperty
	private long last_known_tweet;

	/**
	 * Gets the user's handle (screen name)
	 * 
	 * @return Screen name
	 */
	public String getScreenName() {
		return screen_name;
	}

	/**
	 * Gets the latest processed tweet by the user
	 * 
	 * @return Tweet ID
	 */
	public long getLastKnownTweet() {
		return last_known_tweet;
	}
}
