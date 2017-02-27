package org.trackmypolitician.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controls the Twitter Crawler
 */
public final class Controller {

	/**
	 * Logger for the class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	/**
	 * Parses command line arguments, and runs the crawler
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {
			// Check input arguments
			if (args.length < 3)
				throw new Exception("Expecting 3 arguments: [Key] [Secret] [Itervals in ms]");
			else if (args.length > 3)
				logger.info("Ignoring extra arguments");

			// Setup
			long intervalTime = Long.parseUnsignedLong(args[2]);
			Client twitter = new Client(args[0], args[1]);
			Crawler crawler = new Crawler(twitter);

			// Crawl at specified intervals
			while (true) {
				crawler.crawl();
				Thread.sleep(intervalTime);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}