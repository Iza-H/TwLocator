package io.projectandroid.twlocator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.model.ItemMarker;
import io.projectandroid.twlocator.model.Tweet;

/**
 * Created by izabela on 08/05/16.
 */
public class TweetsListAdapter extends ArrayAdapter<String> {
    private final String TAG = "TweetsListAdapter";
    private final Context context;
    private boolean fromActivity = false;
    private ArrayList<ItemMarker> valuesList; //for infoWindow
    private ArrayList<Tweet> valuesListTweets; //for TweetsListActivity


    public TweetsListAdapter(Context context, ArrayList<ItemMarker> valuesList) {
        super(context, R.layout.list_single_tweet);
        this.context = context;
        this.valuesList = valuesList;
    }
    public TweetsListAdapter(Context context, ArrayList<Tweet> valuesList, boolean fromActivity) {
        super(context, R.layout.list_single_tweet);
        this.context = context;
        this.valuesListTweets = valuesList;
        this.fromActivity = fromActivity;

    }

    @Override
    public int getCount() {
        if (valuesList!=null) {
            return valuesList.size();
        }else {
                return valuesListTweets.size();
            }
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView;
        if (fromActivity==false){
            rowView= inflater.inflate(R.layout.list_single_tweet_infowindow, null, true);
        }else{
            rowView= inflater.inflate(R.layout.list_single_tweet, null, true);
        }

        TextView txtText = (TextView) rowView.findViewById(R.id.list_text);
        TextView txtAutor= (TextView) rowView.findViewById(R.id.list_autor);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_menu);
        if (valuesList!=null){
            Picasso.with(context)
                    .load(valuesList.get(position).getPictureUrl())
                    .into(imageView);
            txtText.setText(valuesList.get(position).getText());
            txtAutor.setText(valuesList.get(position).getAutor());

        }else if (valuesListTweets!=null){
            Picasso.with(context)
                    .load(valuesListTweets.get(position).getUserProfileImage())
                    .into(imageView);
            txtText.setText(valuesListTweets.get(position).getText());
            txtAutor.setText(valuesListTweets.get(position).getUserName());
        }

        return rowView;
    }




}
