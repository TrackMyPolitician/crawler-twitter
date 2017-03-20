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
	public final Client Client;

	/**
	 * Database to retrieve and store results
	 */
	public final Database Database;

	/**
	 * Scheduler to manage to real-time user timelines
	 */
	private final Scheduler scheduler;

	/**
	 * Constructs a Twitter crawler with the given Twitter client
	 * 
	 * @param twitter
	 *            Twitter client
	 * @param database
	 *            Database object
	 */
	public Crawler(Client client, Database database) {
		if (client == null)
			throw new NullPointerException("Twitter Client is null");

		if (database == null)
			throw new NullPointerException("Database is null");

		Client = client;
		Database = database;

		scheduler = new Scheduler(this);
	}

	/**
	 * Crawl Twitter at allowed Twitter quotas
	 */
	public void crawl() {
		try {
			logger.info("Crawling...");

			// Get available quota
			Quota quota = Client.TimelineQuota();

			logger.info("Requests available: " + quota.getRemaining());

			// Exhaust the remaining limit
			for (long counter = 0; counter < quota.getRemaining(); counter++)
				scheduler.next();

			logger.info("Requests exhausted: sleeping until request quota resets");

			// Wait until quota resets
			Thread.sleep(quota.getReset());

		} catch (InterruptedException ex) {
			// Recoverable error. Log and continue.
			logger.warn(ex.getMessage(), ex);
		}
	}
}
