package io.projectandroid.twlocator.activities;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;


import java.lang.ref.WeakReference;
import java.util.List;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.model.Tweet;
import io.projectandroid.twlocator.model.Tweets;
import io.projectandroid.twlocator.model.db.DBConstants;
import io.projectandroid.twlocator.model.db.dao.TweetDAO;
import io.projectandroid.twlocator.providers.SearchRecentValuesProvider;
import io.projectandroid.twlocator.utils.ImageDownloader;
import io.projectandroid.twlocator.utils.Location;
import io.projectandroid.twlocator.utils.MapHelper;
import io.projectandroid.twlocator.utils.NetworkHelper;
import io.projectandroid.twlocator.utils.twitter.ConnectTwitterTask;
import io.projectandroid.twlocator.utils.twitter.TwitterHelper;
import twitter4j.AsyncTwitter;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;



public class SearchMapActivity extends AppCompatActivity implements ConnectTwitterTask.OnConnectTwitterListener {

    public static final String TAG = "SearchMapActivity";

    private GoogleMap mGoogleMap;
    ConnectTwitterTask mTwitterTask;
    private SearchView mSearchView;
    private String mSearchedAddress;
    private Context mContext;
    private boolean isInternetConnection;
    private SearchRecentSuggestions mSuggestions;
    private MapHelper mMapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);


        mSuggestions = new SearchRecentSuggestions(this, SearchRecentValuesProvider.AUTHORITY, SearchRecentValuesProvider.MODE);
        if (mGoogleMap == null) {
            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        if (NetworkHelper.isNetworkConnectionOK(new WeakReference<>(getApplication()))) {
            Log.v(TAG, getString(R.string.twitter_connection_info));
            isInternetConnection=true;
            mTwitterTask = new ConnectTwitterTask(this);
            mTwitterTask.setListener(this);
            mTwitterTask.execute();

        } else {
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG).show();
            isInternetConnection=false;
        }

        mMapHelper = new MapHelper();
        mMapHelper.setupMap(mGoogleMap, mContext );
        mMapHelper.setUpCluster(mGoogleMap, mContext);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mMapHelper.clearMap(mGoogleMap);
                mSearchedAddress=query;
                saveSuggestionQuery(query);

                if (isInternetConnection == true){
                    searchLatLngOfQuery(query);
                } else  {
                    searchInHistory(query);

                }

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
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                mMapHelper.clearMap(mGoogleMap);
                mSearchView.setIconified(true);
                Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(
                        position);
                int indexColumnSuggestText = 2;
                String suggest = cursor.getString(indexColumnSuggestText);
                mSearchedAddress=suggest;


                if (isInternetConnection == true){
                    searchLatLngOfQuery(suggest);
                } else  {
                    searchInHistory(suggest);

                }



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
        mSuggestions.saveRecentQuery(query, null);
    }


    //Remove suggestions, clear DB and remove history data from db:
    private void clearHistory(){
        mSuggestions.clearHistory();
        mMapHelper.clearMap(mGoogleMap);
        mMapHelper.moveCamaraToStartPoint(mGoogleMap);
        TweetDAO tweetDAO = new TweetDAO();
        tweetDAO.deleteAll();

    }



    private void searchLatLngOfQuery(String query){
        Location location = new Location();
        location.getLocationFromAddressInThread(this, query, new Location.OnLocationSearchFinish() {
            @Override
            public void onLocationSearchFinished(final LatLng location) {
                if (location!=null){
                    Log.v(TAG, getString(R.string.longitude_place_info) + location.longitude);
                    Log.v(TAG, getString(R.string.latitude_place_info) + location.latitude);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMapHelper.moveCamara(mGoogleMap, location.latitude, location.longitude);

                        }
                    });

                    launchTwitter(location.latitude, location.longitude );
                }else{
                    Toast.makeText(getApplicationContext(), R.string.unknown_location_info, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void twitterConnectionFinished() {
        Toast.makeText(this, getString(R.string.twiter_auth_ok), Toast.LENGTH_SHORT).show();
    }

    private void launchTwitter(final double latitude, final double longitud) {
        AsyncTwitter twitter = new TwitterHelper(this).getAsyncTwitter();
        TwitterListener listener = new TwitterAdapter() {

            @Override
            public void searched(QueryResult queryResult) {
                List<Status> resultsTweets = queryResult.getTweets();

                int count = 0;
                for (final Status tweet : resultsTweets){
                    final Tweet localTweet;
                    if (tweet.getGeoLocation() !=null) {
                        localTweet = new Tweet(tweet.getId(),tweet.getUser().getScreenName(),
                                tweet.getUser().getProfileImageURL(), tweet.getText(), mSearchedAddress,
                                tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude());
                    }else {
                        //Put default location for data received without geolocation but from searching area:
                        localTweet = new Tweet(tweet.getId(),tweet.getUser().getScreenName(),
                                tweet.getUser().getProfileImageURL(), tweet.getText(), mSearchedAddress,
                                latitude, longitud);
                    }
                    count++;
                    TweetDAO tweetDAO = new TweetDAO();

                    //Checking if the result was not already saved:
                    String[]arg = {String.valueOf(localTweet.getTwitterId())};
                    Tweets results = tweetDAO.queryBySelection(DBConstants.KEY_TWITTER_ID, arg);
                    boolean isSaved=false;
                    if (results!=null){
                        for (int i= 0; i<results.size(); i++){
                            if (results.get(i).getSearchedAddrress().equalsIgnoreCase(mSearchedAddress)){
                                isSaved=true;
                                localTweet.setId(results.get(i).getId());
                            }
                        }
                    }

                    if (isSaved==false){
                        tweetDAO.insert(localTweet);
                    }


                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                        //MapHelper.addMarkerToTheMap(mGoogleMap, tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude(), tweet.getText());
                        mMapHelper.addItems(localTweet.getLatitude(), localTweet.getLongitud(),
                                localTweet.getText() , localTweet.getUserName(), localTweet.getUserProfileImage());
                      }
                    });

                   // }
                }
                Log.v(TAG, getString(R.string.number_result_info) + count);
                if (count==0){
                    Toast.makeText(mContext, getString(R.string.no_results_message), Toast.LENGTH_SHORT).show();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMapHelper.zoomIn(mGoogleMap);

                        }
                    });

                }


            }
        };
        twitter.addListener(listener);

        Query query = new Query();
        query.count(50);
        GeoLocation location = new GeoLocation(latitude, longitud);
        String unit = Query.Unit.km.toString();
        query.geoCode(location , 20, unit);
        twitter.search(query);



    }

    private void searchInHistory(String query){
        TweetDAO tweetDAO = new TweetDAO();
        String[]arg = {query};
        final Tweets results = tweetDAO.queryBySelection(DBConstants.KEY_SEARCHED_ADDRESS, arg);
        if (results.size()==0){
            Toast.makeText(mContext, getString(R.string.no_results_message), Toast.LENGTH_SHORT).show();
        }else{
            for (int i = 0; i<results.size(); i++){
                //MapHelper.addMarkerToTheMap(mGoogleMap, results.get(i).getLatitude(),results.get(i).getLongitud(), results.get(i).getText() );
                mMapHelper.addItems(results.get(i).getLatitude(),results.get(i).getLongitud(),
                        results.get(i).getText(), results.get(i).getUserName(), results.get(i).getUserProfileImage() );
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMapHelper.moveCamara(mGoogleMap, results.get(0).getLatitude(), results.get(0).getLongitud());

                }
            });

        }

    }

}
