package io.projectandroid.twlocator.model.db;

/**
 * Created by izabela on 07/05/16.
 */
public class DBConstants {
	public static final String TABLE_TWEETS = "TWEETS";

	// Table field constants
	public static final String KEY_TWEET_ID = "_id";
	public static final String KEY_TWEET_TITLE = "title";
	public static final String[] allColumns = {
			KEY_TWEET_ID,
			KEY_TWEET_TITLE,
			//KEY_RADAR_DESCRIPTION
	};
	public static final String SQL_CREATE_TWEETS_TABLE =
			"create table " + TABLE_TWEETS
					+ "( " + KEY_TWEET_ID
					+ " integer primary key autoincrement, "
					+ KEY_TWEET_TITLE + " text not null"
					//+ KEY_RADAR_DESCRIPTION + " text not null"
					+ ");";
	public static final String DBNAME = "twlocation.sql";
}
