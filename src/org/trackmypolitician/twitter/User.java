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
	 * Gets the user's handle (screen name)
	 * 
	 * @return Screen name
	 */
	public String getScreenName() {
		return screen_name;
	}
}
