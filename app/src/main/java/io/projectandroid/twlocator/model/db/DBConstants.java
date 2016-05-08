package io.projectandroid.twlocator.model.db;

/**
 * Created by izabela on 07/05/16.
 */
public class DBConstants {
	public static final String TABLE_TWEETS = "TWEETS";


	// Table field constants
	public static final String KEY_ID = "_id";
	public static final String KEY_TWITTER_ID = "twitter_id";
	public static final String KEY_TWEET_TEXT = "text";
	public static final String KEY_TWEET_USER_NAME = "user_name";
	public static final String KEY_PROFILE_IMAGE_URL = "profil_image_url";
	public static final String KEY_SEARCHED_ADDRESS = "searched_address";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitud";
	public static final String KEY_PICTURE = "picture";


	public static final String[] allColumns = {
			KEY_ID,
			KEY_TWITTER_ID,
			KEY_TWEET_TEXT,
			KEY_TWEET_USER_NAME,
			KEY_PROFILE_IMAGE_URL,
			KEY_SEARCHED_ADDRESS,
			KEY_LATITUDE,
			KEY_LONGITUDE,
			KEY_PICTURE
	};
	public static final String SQL_CREATE_TWEETS_TABLE =
			"create table " + TABLE_TWEETS
					+ "( " + KEY_ID
					+ " integer primary key autoincrement, "
					+ KEY_TWITTER_ID + " integer not null,"
					+ KEY_TWEET_TEXT + " text not null,"
					+ KEY_TWEET_USER_NAME + " text not null,"
					+ KEY_PROFILE_IMAGE_URL + " text,"
					+ KEY_SEARCHED_ADDRESS + " text not null,"
					+ KEY_LATITUDE + " real not null,"
					+ KEY_LONGITUDE + " real not null,"
					+ KEY_PICTURE + " blob"
					//+ KEY_PROFILE_IMAGE_URL + " text not null"
					+ ");";
	public static final String DBNAME = "twlocation.sql";
}
