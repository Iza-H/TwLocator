package io.projectandroid.twlocator.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import io.projectandroid.twlocator.R;

public class SearchMapActivity extends AppCompatActivity {

    private boolean mSearchOpened = false;
    private EditText mSearchEditText;
    private Drawable mIconOpenSearch;
    private Drawable mIconCloseSearch;
    private String mSearchText;
    private MenuItem mSearchAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        //Getting the icons:
        mIconOpenSearch = ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_search, null);
        mIconCloseSearch = ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_close_clear_cancel, null);

        if (mSearchOpened) {
            openSearchBar();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_search){
            if (mSearchOpened) {
                closeSearchBar();
            } else {
                openSearchBar();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.menu_search);
        return super.onPrepareOptionsMenu(menu);
    }

    private void openSearchBar(){
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

    }

    private void searchTweets(String searchedText){

    }
}
