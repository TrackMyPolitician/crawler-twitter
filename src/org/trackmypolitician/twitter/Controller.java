package org.trackmypolitician.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controls the Twitter Crawler
 */
@RestController
@EnableAutoConfiguration
public final class Controller {

	/**
	 * Logger for the class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	/**
	 * Parses environment variables and/or command line arguments, and runs the
	 * crawler
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		try {
			SpringApplication.run(Controller.class);

			Client twitter;

			if (args.length == 0) {
				// Look for authentication in the ENV
				String key = "CRAWLER_TWITTER_KEY", secret = "CRAWLER_TWITTER_SECRET";

				if (System.getenv(key) == null)
					throw new Exception("Missing environment variable: " + key);

				if (System.getenv(secret) == null)
					throw new Exception("Missing environment variable: " + secret);

				twitter = new Client(System.getenv(key), System.getenv(secret));

			} else if (args.length < 2)
				throw new Exception("Expecting 2 arguments: [Key] [Secret]");

			else {
				// Authentication from command line arguments
				twitter = new Client(args[0], args[1]);

				// Obscure sensitive data
				args[0] = null;
				args[1] = null;

				if (args.length > 2)
					logger.warn("Ignoring extra arguments");
			}

			// Setup crawler
			Crawler crawler = new Crawler(twitter);

			// Crawl indefinitely
			while (true)
				crawler.crawl();

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}