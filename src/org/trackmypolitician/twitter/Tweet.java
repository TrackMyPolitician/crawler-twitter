package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Immutable JSON object to represent tweets
 *
 */
public class Tweet {
	public final String Text;

	@JsonCreator
	public Tweet(@JsonProperty("text") String text) {
		this.Text = text;
	}

	@Override
	public String toString() {
		return Text;
	}
}
