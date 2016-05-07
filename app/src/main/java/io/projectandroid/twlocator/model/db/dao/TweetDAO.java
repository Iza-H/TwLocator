package io.projectandroid.twlocator.model.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import io.projectandroid.twlocator.model.Tweet;
import io.projectandroid.twlocator.model.Tweets;
import io.projectandroid.twlocator.model.db.DBConstants;
import io.projectandroid.twlocator.model.db.DBHelper;

/**
 * Created by izabela on 07/05/16.
 */
public class TweetDAO extends GenericDAO<Tweet, Tweets>{

        //private static final long INVALID_ID_DELETE_ALL_RECORDS = 0;

        private DBHelper db;
        private WeakReference<Context> context;


        public TweetDAO() {
            super();
        }

        public void setId(){

        }


        @Override
        public String getTableName() {
            return DBConstants.TABLE_TWEETS;
        }

        @Override
        public ContentValues getContentValues(Tweet tweet){
            ContentValues content = new ContentValues();
            content.put(DBConstants.KEY_TWEET_TEXT, tweet.getText());
            content.put(DBConstants.KEY_TWEET_USER_NAME, tweet.getUserName());
            content.put(DBConstants.KEY_PROFILE_IMAGE_URL, tweet.getUserProfileImage());
            content.put(DBConstants.KEY_LATITUDE, tweet.getLatitude());
            content.put(DBConstants.KEY_LONGITUDE, tweet.getLongitud());
            content.put(DBConstants.KEY_SEARCHED_ADDRESS, tweet.getSearchedAddrress());
            content.put(DBConstants.KEY_PICTURE, tweet.getPicture());

            return content;
        }



        public @NonNull
        Tweet elementFromCursor(final @NonNull Cursor c) {
            assert c != null;

            final long id = c.getLong(c.getColumnIndex(DBConstants.KEY_TWEET_ID));
            final String userName =  c.getString(c.getColumnIndex(DBConstants.KEY_TWEET_USER_NAME));
            final String text = c.getString(c.getColumnIndex(DBConstants.KEY_TWEET_USER_NAME));
            final String searchedAddrress = c.getString(c.getColumnIndex(DBConstants.KEY_SEARCHED_ADDRESS));
            final String userProfileImage = c.getString(c.getColumnIndex(DBConstants.KEY_PROFILE_IMAGE_URL));
            final Double latitude = c.getDouble(c.getColumnIndex(DBConstants.KEY_LATITUDE));
            final Double longitude = c.getDouble(c.getColumnIndex(DBConstants.KEY_LONGITUDE));
            final byte[] picture = c.getBlob(c.getColumnIndex(DBConstants.KEY_PICTURE));


            final Tweet tweet = new Tweet(id, userName, userProfileImage, text, searchedAddrress, latitude, longitude, picture);


            return tweet;
        }



        @Override
        public String[] getAllColumns() {
            return DBConstants.allColumns;
        }

        @Override
        public String getId() {
            return DBConstants.KEY_TWEET_ID;
        }

        @Override
        protected Tweets mapElementsInAgregate(List list) {
            return Tweets.createTweets(list);
        }


}
