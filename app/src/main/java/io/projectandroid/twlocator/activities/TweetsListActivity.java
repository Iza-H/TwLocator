package io.projectandroid.twlocator.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.adapters.TweetsListAdapter;
import io.projectandroid.twlocator.model.ItemMarker;
import io.projectandroid.twlocator.model.Tweet;

/**
 * Created by izabela on 08/05/16.
 */
public class TweetsListActivity extends AppCompatActivity {
    private ListView mListView;
    private TextView mMoreText;
    public static final String TWEETS_VALUES = "tweetsValues";
    private ArrayList<Tweet> mData;


    public TweetsListActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tweets);

        mListView = (ListView) findViewById(R.id.list_with_results);
        mMoreText = (TextView) findViewById(R.id.more_info_text);
        mMoreText.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
            mData = new ArrayList<Tweet>();
            mData.addAll((ArrayList<Tweet>) getIntent().getSerializableExtra(TWEETS_VALUES));
             mListView.setAdapter(new TweetsListAdapter(this, mData, true));

    }



}


