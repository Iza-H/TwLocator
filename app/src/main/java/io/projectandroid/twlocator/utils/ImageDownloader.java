package io.projectandroid.twlocator.utils;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.projectandroid.twlocator.model.Tweet;
import io.projectandroid.twlocator.model.db.dao.TweetDAO;


/**
 * Created by izabela on 08/05/16.
 */
//Not used
public class ImageDownloader {
    Tweet mTweet;

    public ImageDownloader(Tweet tweet){
        mTweet=tweet;
    }

    public void exceuteDownload(){
        new Downloader().execute(mTweet.getUserProfileImage());
    }



    private class Downloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            mTweet.setPicture(byteArray);
            TweetDAO tweetDAO = new TweetDAO();
            tweetDAO.update(mTweet.getId(), mTweet);
        }
    }


}
