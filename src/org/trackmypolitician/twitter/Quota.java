package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable JSON object to represent Twitter API quotas
 *
 */
public class Quota {
	/**
	 * Maximum number of requests allowed
	 */
	public final long Limit;

	/**
	 * Number of requests remaining
	 */
	public final long Remaining;

	/**
	 * Epoch time when the requests will reset
	 */
	public final long Reset;

	/**
	 * Constructs the object
	 * 
	 * @param limit
	 *            Maximum number of requests allowed
	 * @param remaining
	 *            Number of requests remaining
	 * @param reset
	 *            Epoch time when the requests will reset
	 */
	public Quota(@JsonProperty("limit") long limit, @JsonProperty("remaining") long remaining,
			@JsonProperty("reset") long reset) {
		this.Limit = limit;
		this.Remaining = remaining;
		this.Reset = reset;
	}

	/**
	 * Returns the time remaining until quota reset.
	 * 
	 * @return Time in milliseconds. 0 if time already reached.
	 */
	public long TimeRemaining() {
		long diff = Reset * 1000 - System.currentTimeMillis();
		return (diff < 0L) ? 0L : diff;
	}
}
