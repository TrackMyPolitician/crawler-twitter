package org.trackmypolitician.twitter;

import java.util.Map;

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
			// Setup a crawler
			Crawler crawler = setup(args);

			// Crawl indefinitely
			while (true)
				crawler.crawl();

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Sets up all necessary resources to begin crawling. Additionally, it
	 * isolates sensitive data and allows it to be garbage-collected. Any data
	 * in {@link Controller#main(String[])} stays on the stack indefinitely.
	 * 
	 * @param args
	 *            Command line arguments
	 * @return An instance of {@link Crawler}
	 * @throws Exception
	 *             If command-line or environment variables are invalid
	 */
	private static Crawler setup(String[] args) throws Exception {
		// Setup Spring
		SpringApplication.run(Controller.class);

		// Look for authentication in the ENV
		if (args.length == 0) {
			String[] eVars = { "CRAWLER_TWITTER_KEY", "CRAWLER_TWITTER_SECRET", "DATABASE_URL" };
			Map<String, String> environment = System.getenv();

			for (String variable : eVars)
				if (!environment.containsKey(variable))
					throw new Exception("Missing environment variable: " + variable);

			// Get variable values
			String twitterKey = environment.get(eVars[0]);
			String twitterSecret = environment.get(eVars[1]);
			String databaseUrl = environment.get(eVars[2]);

			// Create crawler
			Client client = new Client(twitterKey, twitterSecret);
			Database database = new Database(databaseUrl);
			return new Crawler(client, database);
		}

		// Not enough command-line arguments
		if (args.length < 3)
			throw new Exception("Expecting 3 arguments: [Key] [Secret] [Database URL]");

		// Authentication from command line arguments
		Client client = new Client(args[0], args[1]);
		Database database = new Database(args[2]);

		// Too many arguments
		if (args.length > 3)
			logger.warn("Ignoring extra arguments");

		// Obscure sensitive data
		for (int i = 0; i < args.length; i++)
			args[i] = null;

		// Setup crawler
		return new Crawler(client, database);
	}
}