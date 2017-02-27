package org.trackmypolitician.twitter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tweet {
	private final String Text;

	@JsonCreator
	public Tweet(@JsonProperty("text") String text) {
		this.Text = text;
	}

	@Override
	public String toString() {
		return Text;
	}
}
