package io.projectandroid.twlocator.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.drawable.Drawable;
import android.provider.SearchRecentSuggestions;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.util.Log;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.providers.SearchRecentValuesProvider;

public class SearchMapActivity extends AppCompatActivity {

    public static final String TAG = "SearchMapActivity";
    private boolean mSearchOpened = false;
    private EditText mSearchEditText;
    private Drawable mIconOpenSearch;
    private Drawable mIconCloseSearch;
    private String mSearchText;
    private MenuItem mSearchAction;

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);


        /*final Intent queryIntent = getIntent();
        final String queryAction = queryIntent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction))
        {
            processSearchQuery(queryIntent.getStringExtra(SearchManager.QUERY));
        }*/
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
                processSearchQuery(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        mSearchAction = menu.findItem(R.id.menu_search);
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

    private void processSearchQuery(String query){
        Log.v(TAG, query);
        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(this,
                        SearchRecentValuesProvider.AUTHORITY,
                        SearchRecentValuesProvider.MODE);
        suggestions.saveRecentQuery(query, null);
    }

    private void clearHistory(){
        SearchRecentSuggestions suggestions =
                new SearchRecentSuggestions(this,
                        SearchRecentValuesProvider.AUTHORITY,
                        SearchRecentValuesProvider.MODE);
        suggestions.clearHistory();

    }
}
