package org.trackmypolitician.twitter;

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
	 * Upper limit of tweet ID in the request
	 */
	private long userMaxID = Long.MAX_VALUE;

	/**
	 * Infinitely iterates and searches for tweets
	 */
	public void next() {

		/*
		 * Since userIndex = 0, and users = Empty, this runs at least once. When
		 * the end of user list is reached, the user list is updated, and
		 * iterations over the user restart. Hence, this ensures infinite loop.
		 */
		if (userIndex >= users.length) {
			// users = TODO Get users from database
			userIndex = 0;
		}

		// Exit if user list is empty
		if (users.length == 0)
			return;

		/*
		 * Get tweets for the current. Since userMaxID = MAX_VALUE at beginning
		 * for each user, the timeline always starts at the top. However,
		 * userMaxID is updated for subsequent runs for the same user. This
		 * ensures that new real-time tweets don't shift the paging location.
		 */
		final Tweet[] tweets = twitter.GetTweets(users[userIndex], userMaxID);

		if (tweets.length > 0) {
			// TODO Send for processing
			// TODO: Update last_known_tweet to tweets[0].getID() for the user
		}

		// Check whether to continue with the same user
		if (tweets.length < twitter.MaxTweetsPerRequest) {
			// Must be final page for the current user; progress to next user
			userIndex++;
			userMaxID = Long.MAX_VALUE;
		} else {
			// Otherwise, continue for the same user. Since userMaxID is
			// inclusive, -1 ensures the final tweet isn't duplicated.
			userMaxID = tweets[tweets.length - 1].getID() - 1;
		}
	}
}
