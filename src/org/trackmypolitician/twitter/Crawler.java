package org.trackmypolitician.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Crawler {

	/**
	 * Logger for the class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

	/**
	 * Twitter client to access tweets
	 */
	private final Client twitter;

	/**
	 * Constructs a Twitter crawler with the given Twitter client
	 * 
	 * @param twitter
	 *            Twitter client
	 */
	public Crawler(Client twitter) {
		if (twitter == null)
			throw new NullPointerException("Twitter Client is null");

		this.twitter = twitter;
	}

	/**
	 * Crawl Twitter
	 */
	public void crawl() {
		// Users for testing
		String[] users = { "realDonaldTrump", "HillaryClinton" };

		for (String user : users) {
			Tweet[] tweets = twitter.GetTweets(user);

			for (Tweet tweet : tweets)
				logger.info(tweet.toString());

		}
	}
}
