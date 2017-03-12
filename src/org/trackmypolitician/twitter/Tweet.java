package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable JSON object to represent tweets
 *
 */
public final class Tweet implements Comparable<Tweet> {

	/**
	 * Limit construction to JSON only
	 */
	private Tweet() {
	}

	/**
	 * Text of the tweet
	 */
	@JsonProperty
	private String text;

	/**
	 * Global ID of the tweet
	 */
	@JsonProperty
	private long id;

	/**
	 * Gets the text of the tweet
	 * 
	 * @return Tweet value
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the global ID of the tweet
	 * 
	 * @return Global ID
	 */
	public long getID() {
		return id;
	}

	/**
	 * Sorts tweets in descending order (newer on top)
	 */
	@Override
	public int compareTo(Tweet other) {
		return -Long.compare(id, other.id);
	}
}
