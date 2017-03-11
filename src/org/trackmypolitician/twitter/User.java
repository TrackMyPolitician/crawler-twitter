package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable JSON object to represent Twitter users
 *
 */
public class User {
	public final String screen_name;

	@JsonCreator
	public User(@JsonProperty("screen_name") String screen_name) {
		this.screen_name = screen_name;
	}

	@Override
	public String toString() {
		return screen_name;
	}
}
