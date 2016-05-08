package io.projectandroid.twlocator.model.db.dao;

import android.test.AndroidTestCase;

import io.projectandroid.twlocator.model.Tweet;
import io.projectandroid.twlocator.model.Tweets;
import io.projectandroid.twlocator.model.db.DBConstants;
import io.projectandroid.twlocator.model.db.DBHelper;

/**
 * Created by izabela on 07/05/16.
 */
public class TweetDAOTests extends AndroidTestCase {

    public static final String NAME_USER = "nameUser";
    public static final String PROFILE_IMAGE_URL = "profileImageURL";
    public static final String TEXT = "text";
    public static final String SEARCHED_ADDRRESS = "Madrid";
    public static final double LATITUDE = 1;
    public static final double LONGITUD = 2;
    public static final long ID = 1;
    public static final long TWITTER_ID = 1;
    private Tweet sut;
    private long mID;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DBHelper.configure("TestDB.sqlite", getContext());
        sut = new Tweet(TWITTER_ID, NAME_USER, PROFILE_IMAGE_URL, TEXT, SEARCHED_ADDRRESS, LATITUDE, LONGITUD);
        TweetDAO tweetDAO = new TweetDAO();
        mID = tweetDAO.insert(sut);
    }


    public void testCanInsertTweetToDb(){
        assertTrue(mID>0);
    }

    public void testCanSearchedTweetByID() {
        TweetDAO tweetDAO = new TweetDAO();
        Tweet result = tweetDAO.query(mID);
        assertNotNull(result);
        assertEquals(result.getId(), mID);
    }



    public void testCanDeleteTweetFromDb(){
        TweetDAO tweetDAO = new TweetDAO();
        tweetDAO.delete(mID);
        Tweet t = tweetDAO.query(mID);
        assertNull(t);
    }

    public void testCanSearchedAllTweets() {
        TweetDAO tweetDAO = new TweetDAO();
        Tweets result = tweetDAO.query();
        assertNotNull(result);
        assertTrue(result.size()>0);
    }

    public void testCanDeleteAllTweetsFromDb(){
        TweetDAO tweetDAO = new TweetDAO();
        tweetDAO.deleteAll();
        Tweets result = tweetDAO.query();
        assertEquals(result.size(), 0);
    }

    public void testCanSearchedTweetsFromSearchedCity() {
        TweetDAO tweetDAO = new TweetDAO();
        String[]arg = {SEARCHED_ADDRRESS};
        Tweets result = tweetDAO.queryBySelection(DBConstants.KEY_SEARCHED_ADDRESS, arg);
        assertNotNull(result);
        assertTrue(result.size()>0);
    }


}
