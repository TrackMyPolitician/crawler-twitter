package org.trackmypolitician.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crawls Twitter for tweets at allowed API intervals
 *
 */
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
	 * Crawl Twitter at allowed Twitter quotas
	 */
	public void crawl() {
		try {
			Quota quota = twitter.TimelineQuota();

			// Exhaust the remaining limit
			for (long counter = 0; counter < quota.getLimit(); counter++) {
				// TODO

			}

			// Wait until quota resets
			Thread.sleep(quota.getReset());

		} catch (InterruptedException ex) {
			// Recoverable error. Log and continue.
			logger.error(ex.getMessage(), ex);
		}
	}
}
