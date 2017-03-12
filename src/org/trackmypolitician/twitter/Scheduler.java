package org.trackmypolitician.twitter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handles scheduling historical and real-time timelines for multiple users
 *
 */
public class Scheduler {

	/**
	 * Twitter client to access tweets
	 */
	private final Client twitter;

	/**
	 * Constructs an instance of Scheduler
	 * 
	 * @param twitter
	 *            Twitter client
	 */
	public Scheduler(Client twitter) {
		if (twitter == null)
			throw new NullPointerException("Twitter Client is null");

		this.twitter = twitter;
	}

	/**
	 * Array of Twitter users to be tracked
	 */
	private User[] users = new User[0];

	/**
	 * Current index on {@link Scheduler#users}
	 */
	private int userIndex = 0;

	/**
	 * Temporarily stores tweets for the current user
	 */
	private final ArrayList<Tweet> userTweets = new ArrayList<>();

	/**
	 * Infinitely iterates and searches for tweets
	 */
	public void next() {
		// update users
		updateUsers();

		// Exit if user list is empty
		if (users.length == 0)
			return;

		// Next page of tweets for the current user
		Tweet[] tweets = nextPage();

		// If the page size is lower than requested, it must be the final page
		if (tweets.length < twitter.MaxTweetsPerRequest)
			nextUser();
	}

	/**
	 * Updates the user list at initialization and at the of the cycle
	 */
	private void updateUsers() {
		/*
		 * Since userIndex = 0, and users = Empty, this runs at least once. When
		 * the end of user list is reached, the user list is updated, and
		 * iterations over the user restarts. This ensures infinite cycles.
		 */
		if (userIndex >= users.length) {
			// users = TODO Get users from database
			userIndex = 0;
		}
	}

	/**
	 * Gets the next page of tweets on the user timeline. Handles shifts in the
	 * paging due to real-time nature of timelines. Also appends the new page of
	 * tweets to {@link Scheduler#userTweets}.
	 * 
	 * @return Next page of tweets
	 */
	private Tweet[] nextPage() {
		/*
		 * Since maxID = MAX_VALUE at first, the timeline starts at the top.
		 * However, maxID is updated for subsequent runs for the same user. This
		 * ensures that new real-time tweets don't shift the paging location.
		 * Since maxID is inclusive, -1 ensures the last tweet isn't duplicated.
		 * The userTweet is always sorted newest to oldest tweet.
		 */
		long maxID = userTweets.isEmpty() ? Long.MAX_VALUE : (userTweets.get(userTweets.size() - 1).getID() - 1);
		Tweet[] page = twitter.GetTweets(users[userIndex], maxID);

		userTweets.addAll(Arrays.asList(page));

		return page;
	}

	/**
	 * Sends the crawled tweets to be processed, and moves to the next user
	 */
	private void nextUser() {
		if (!userTweets.isEmpty()) {
			// TODO Send for processing
			// TODO: Update last_known_tweet to userTweets.get(0)

			// Empty for the next user
			userTweets.clear();
		}

		userIndex++;
	}
}
