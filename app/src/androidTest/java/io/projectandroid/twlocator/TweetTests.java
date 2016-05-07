package io.projectandroid.twlocator;

import android.test.AndroidTestCase;

import java.util.Arrays;
import java.util.List;

import io.projectandroid.twlocator.model.Tweet;

/**
 * Created by izabela on 07/05/16.
 */
public class TweetTests extends AndroidTestCase {


    public static final String NAME_USER = "nameUser";
    public static final String PROFILE_IMAGE_URL = "profileImageURL";
    public static final String TEXT = "text";
    public static final String SEARCHED_ADDRRESS = "Madrid";
    public static final double LATITUDE = 1;
    public static final double LONGITUD = 2;
    public static final long ID = 1;

    private Tweet sut;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sut = new Tweet(ID, NAME_USER, PROFILE_IMAGE_URL, TEXT, SEARCHED_ADDRRESS, LATITUDE, LONGITUD);
    }

    public void testCanCreateATweet(){
        final Tweet sut = new Tweet(ID, NAME_USER, PROFILE_IMAGE_URL, TEXT, SEARCHED_ADDRRESS, LATITUDE, LONGITUD);
        assertNotNull(sut);
    }

    public void testCanGetIdOfTheTweet(){
        assertEquals(ID, sut.getId());
    }

    public void testCanGetNaameOfUserOfTheTweet(){
        assertEquals(NAME_USER, sut.getUserName());
    }

    public void testCanGetProfileImageOfTheUser(){
        assertEquals(PROFILE_IMAGE_URL, sut.getUserProfileImage());
    }

    public void testCanGetTextOfTheTweet(){
        assertEquals(TEXT, sut.getText());
    }

    public void testCanGetSearchedAdressForTweet(){
        assertEquals(SEARCHED_ADDRRESS, sut.getSearchedAddrress());
    }

    public void testCanGetLocationOfTheTweet(){
        assertEquals(LATITUDE, sut.getLatitude());
        assertEquals(LONGITUD, sut.getLongitud());
    }


}
