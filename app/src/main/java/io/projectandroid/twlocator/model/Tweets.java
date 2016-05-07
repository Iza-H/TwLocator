package io.projectandroid.twlocator.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by izabela on 07/05/16.
 */
public class Tweets {
    List<Tweet> mTweets;

    public static Tweets createTweets(List<Tweet> tweets){
        Tweets myTweets = new Tweets();
        for(Tweet t: tweets){
            myTweets.add(t);
        }
        return  myTweets;
    }

    public static Tweets createTweets(){
        Tweets myTweets = new Tweets();
        return  myTweets;
    }


    public void add(Tweet t) {
        getTweets().add(t);
    }

    public int size(){
        return getTweets().size();
    }

    public Tweet get(int index){
        return getTweets().get(index);
    }

    public void remove(Tweet tweet){
        getTweets().remove(tweet);
    }

    private Tweets() {
    }

    public List<Tweet> getTweets(){
        if (this.mTweets == null){
            this.mTweets = new LinkedList<>();
        }
        return this.mTweets;
    }
}
