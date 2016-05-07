package io.projectandroid.twlocator.activities;

import android.app.SearchManager;
import android.content.Context;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.providers.SearchRecentValuesProvider;
import io.projectandroid.twlocator.utils.Location;

public class SearchMapActivity extends AppCompatActivity {

    public static final String TAG = "SearchMapActivity";

    private GoogleMap mGoogleMap;
    private Menu mSearchAction;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        setupMap();
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Intent queryIntent = getIntent();
        final String queryAction = queryIntent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction))
        {
            processSearchQuery(queryIntent.getStringExtra(SearchManager.QUERY));
        }
    }*/

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

    /*private void openSearchBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_layout);

        mSearchAction.setIcon(mIconCloseSearch);
        mSearchOpened=true;

        mSearchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.search_txt);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSearchText = mSearchEditText.getText().toString();
                searchTweets(mSearchText);
            }
        });
        mSearchEditText.requestFocus();

    }
    private void closeSearchBar(){
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        mSearchAction.setIcon(mIconOpenSearch);
        mSearchOpened=false;

    })*/

    private void searchTweets(String searchedText){

    }

    private void saveSuggestionQuery(String query){
        Log.v(TAG, query);

        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SearchRecentValuesProvider.AUTHORITY, SearchRecentValuesProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }



    private void clearHistory(){
        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(this,
                        SearchRecentValuesProvider.AUTHORITY,
                        SearchRecentValuesProvider.MODE);
        suggestions.clearHistory();

    }

    private void setupMap(){
        if (mGoogleMap == null) {
            mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void searchLatLngOfQuery(String query){
        Location location = new Location();
        location.getLocationFromAddressInThread(this, query, new Location.OnLocationSearchFinish() {
            @Override
            public void onLocationSearchFinished(LatLng location) {
                Log.v(TAG, "Finded longitude " + location.longitude);
                Log.v(TAG, "Finded longitude " + location.latitude);

            }
        });


    }

}
