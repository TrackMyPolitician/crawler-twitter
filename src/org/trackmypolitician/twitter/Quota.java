package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable JSON object to represent Twitter API quotas
 *
 */
public final class Quota {

	/**
	 * Limit construction to JSON only
	 */
	private Quota() {
	}

	/**
	 * Maximum number of requests allowed
	 */
	@JsonProperty
	private long limit;

	/**
	 * Number of requests remaining
	 */
	@JsonProperty
	private long remaining;

	/**
	 * Epoch time when the requests will reset (in seconds)
	 */
	@JsonProperty
	private long reset;

	/**
	 * Gets the maximum number of requests allowed
	 * 
	 * @return Limit
	 */
	public long getLimit() {
		return limit;
	}

	/**
	 * Gets the number of requests remaining
	 * 
	 * @return Remaining requests
	 */
	public long getRemaining() {
		return remaining;
	}

	/**
	 * Returns the time remaining until quota reset.
	 * 
	 * @return Time in milliseconds. 0 if time already reached.
	 */
	public long getReset() {
		long diff = reset * 1000L - System.currentTimeMillis();
		return (diff < 0L) ? 0L : diff;
	}
}
