package io.projectandroid.twlocator.model;

import java.sql.Blob;

/**
 * Created by izabela on 07/05/16.
 */
public class Tweet implements ModelPersistable {
    private long mId;
    private long mTwitterId;
    private String mUserName;
    private String mUserProfileImage;
    private String mText;
    private String mSearchedAddrress;
    private double mLatitude;
    private double mLongitud;
    private byte[] mPicture;

    public Tweet(long twitterId, String userName, String userProfileImage, String text, String searchedAddrress, double latitude, double longitud) {
        mTwitterId = twitterId;
        mUserName = userName;
        mUserProfileImage = userProfileImage;
        mText = text;
        mSearchedAddrress = searchedAddrress;
        mLatitude = latitude;
        mLongitud = longitud;
    }


    public Tweet(long twitterId, String userName, String userProfileImage, String text, String searchedAddrress, double latitude, double longitud, byte[] picture) {
        mTwitterId = twitterId;
        mUserName = userName;
        mUserProfileImage = userProfileImage;
        mText = text;
        mSearchedAddrress = searchedAddrress;
        mLatitude = latitude;
        mLongitud = longitud;
        mPicture = picture;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserProfileImage() {
        return mUserProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        mUserProfileImage = userProfileImage;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSearchedAddrress() {
        return mSearchedAddrress;
    }

    public void setSearchedAddrress(String searchedAddrress) {
        mSearchedAddrress = searchedAddrress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitud() {
        return mLongitud;
    }

    public void setLongitud(double longitud) {
        mLongitud = longitud;
    }

    public byte[] getPicture() {
        return mPicture;
    }

    public void setPicture(byte[] picture) {
        mPicture = picture;
    }


    public long getTwitterId() {
        return mTwitterId;
    }

    public void setTwitterId(long twitterId) {
        this.mTwitterId = twitterId;
    }

}
