package io.projectandroid.twlocator.activities;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.model.Tweet;
import io.projectandroid.twlocator.providers.SearchRecentValuesProvider;
import io.projectandroid.twlocator.utils.Location;
import io.projectandroid.twlocator.utils.NetworkHelper;
import io.projectandroid.twlocator.utils.twitter.ConnectTwitterTask;
import io.projectandroid.twlocator.utils.twitter.TwitterHelper;
import twitter4j.AsyncTwitter;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;



public class SearchMapActivity extends AppCompatActivity implements ConnectTwitterTask.OnConnectTwitterListener {

    public static final String TAG = "SearchMapActivity";

    private GoogleMap mGoogleMap;
    ConnectTwitterTask mTwitterTask;
    //private Menu mSearchAction;
    private SearchView mSearchView;
    private String mSearchedAddress;
    private SearchMapActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        if (NetworkHelper.isNetworkConnectionOK(new WeakReference<>(getApplication()))) {
            Log.v(TAG, "Twitter connection");
            mTwitterTask = new ConnectTwitterTask(this);
            mTwitterTask.setListener(this);

            mTwitterTask.execute();
        } else {
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG).show();

        }

        setupMap();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mGoogleMap.clear();
                mSearchedAddress=query;
                saveSuggestionQuery(query);
                searchLatLngOfQuery(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                mGoogleMap.clear();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_clear_history){
            clearHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }




    private void saveSuggestionQuery(String query){
        Log.v(TAG, query);

        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SearchRecentValuesProvider.AUTHORITY, SearchRecentValuesProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }



    private void clearHistory(){
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SearchRecentValuesProvider.AUTHORITY,
                        SearchRecentValuesProvider.MODE);
        suggestions.clearHistory();

    }

    private void setupMap(){
        if (mGoogleMap == null) {
            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);


    }

    private void searchLatLngOfQuery(String query){
        Location location = new Location();
        location.getLocationFromAddressInThread(this, query, new Location.OnLocationSearchFinish() {
            @Override
            public void onLocationSearchFinished(final LatLng location) {
                Log.v(TAG, getString(R.string.longitude_place_info) + location.longitude);
                Log.v(TAG, getString(R.string.latitude_place_info) + location.latitude);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.latitude, location.longitude), 12));
                    }
                });


                launchTwitter(location.latitude, location.longitude );

            }
        });


    }

    @Override
    public void twitterConnectionFinished() {
        Toast.makeText(this, getString(R.string.twiiter_auth_ok), Toast.LENGTH_SHORT).show();
    }

    private void launchTwitter(double latitude, double longitud) {
        AsyncTwitter twitter = new TwitterHelper(this).getAsyncTwitter();
        TwitterListener listener = new TwitterAdapter() {

            @Override
            public void searched(QueryResult queryResult) {
                List<Status> resultsTweets = queryResult.getTweets();

                int count = 0;
                for (final Status tweet : resultsTweets){
                    if (tweet.getGeoLocation() !=null){
                        count++;
                        Tweet localTweet = new Tweet(tweet.getId(),tweet.getUser().getScreenName(),
                                tweet.getUser().getProfileImageURL(), tweet.getText(), mSearchedAddress,
                                tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude());
                        //localTweet.setImage(takeImage(tweet.getUser().getProfileImageURL()));




                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.v(TAG, "Lat" + tweet.getGeoLocation().getLatitude());
                                Log.v(TAG, "Long" + tweet.getGeoLocation().getLongitude());
                                MarkerOptions marker = new MarkerOptions().position
                                        (new LatLng(tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude())).title(tweet.getText());

                                mGoogleMap.addMarker(marker);

                            }
                        });



                    }
                }
                Log.v(TAG, getString(R.string.number_result_info) + count);
                if (count==0){
                    Toast.makeText(mActivity.getApplicationContext(), getString(R.string.no_results_message), Toast.LENGTH_SHORT).show();
                }


            }
        };
        twitter.addListener(listener);

        Query query = new Query();
        query.count(100);
        GeoLocation location = new GeoLocation(latitude, longitud);
        String unit = Query.Unit.km.toString();
        query.geoCode(location , 50, unit);
        twitter.search(query);

        //FilterQuery filter = new FilterQuery();


    }

    private Bitmap takeImage(String stringUrl){
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bmImg = BitmapFactory.decodeStream(is);
            return bmImg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
