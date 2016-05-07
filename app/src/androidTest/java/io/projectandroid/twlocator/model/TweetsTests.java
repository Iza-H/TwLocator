package io.projectandroid.twlocator.model;

import android.test.AndroidTestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Created by izabela on 07/05/16.
 */
public class TweetsTests extends AndroidTestCase {

    Tweets mTweets;
    Tweet t2;

    private static final String NAME_USER = "nameUser";
    private  static final String PROFILE_IMAGE_URL = "profileImageURL";
    private  static final String TEXT = "text";
    private static final String SEARCHED_ADDRRESS = "Madrid";
    private static final double LATITUDE = 1;
    private static final double LONGITUD = 2;
    private static final long ID = 1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Tweet t1 = new Tweet( NAME_USER, PROFILE_IMAGE_URL, TEXT, SEARCHED_ADDRRESS, LATITUDE, LONGITUD);;
        t2 = new Tweet(NAME_USER, PROFILE_IMAGE_URL, TEXT, SEARCHED_ADDRRESS, LATITUDE, LONGITUD);;
        Tweet[] arrayOfTweets={t1, t2};
        List<Tweet> tweetsList= Arrays.asList(arrayOfTweets);
        mTweets= Tweets.createTweets(tweetsList);
    }

    public void testCreateTweets() throws Exception {
        assertNotNull(mTweets);
    }

    public void testAddNewTweetToTweets() throws Exception {
        int index = 3;
        Tweet t = new Tweet(NAME_USER, PROFILE_IMAGE_URL, TEXT, SEARCHED_ADDRRESS, LATITUDE, LONGITUD);
        t.setId(index);
        mTweets.add(t);
        assertEquals(index, mTweets.getTweets().size());
        assertEquals(mTweets.getTweets().get(index-1).getId(), 3);
    }


    public void testSizeOfTweets() throws Exception {
        assertEquals(mTweets.getTweets().size(), 2);

    }

    public void testGetTweetFromTweets() throws Exception {
        assertEquals(mTweets.getTweets().get(0).getText(), TEXT);

    }

    public void testRemoveTweetFromTweets() throws Exception {
        mTweets.remove(t2);
        assertEquals(mTweets.getTweets().size(), 1);

    }


    public void testGetTweets() throws Exception {
        assertNotNull(mTweets.getTweets());
        assertEquals(mTweets.getTweets().size(), 2);
    }
}
