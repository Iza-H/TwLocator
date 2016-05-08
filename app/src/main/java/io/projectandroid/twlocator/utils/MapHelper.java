package io.projectandroid.twlocator.utils;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.activities.TweetsListActivity;
import io.projectandroid.twlocator.model.ItemMarker;
import io.projectandroid.twlocator.adapters.TweetsListAdapter;
import io.projectandroid.twlocator.model.Tweet;
import io.projectandroid.twlocator.model.Tweets;


/**
 * Created by izabela on 07/05/16.
 */
public class MapHelper {
    public static final String TAG = "MapHelper";

    //Central point:
    public static final double LATITUDE_MADRID = 40.4167754;
    public static final double LONGITUDE_MADRID = -3.7037902;


    private ItemMarker  mClickedClusterItem;
    private Cluster<ItemMarker> mClickedCluster;
    private ClusterManager<ItemMarker> mClusterManager;
    private Context mContext;
    private GoogleMap mMap;
    private ArrayList<Tweet> mTweetsCluster;




    public static void addMarkerToTheMap(GoogleMap googleMap, double latitude, double longitude, String text){
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(text);
        googleMap.addMarker(marker);
    }

    public void clearMap(GoogleMap map){
        //mClusterManager.getClusterMarkerCollection().clear();
        setUpCluster(map, mContext);
        map.clear();

    }


    public void setupMap(GoogleMap googleMap, Context context){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //Central map to Spain:
        moveCamaraToStartPoint(googleMap);
        mMap=googleMap;
        mContext = context;
    }

    public static void moveCamara(GoogleMap googleMap, double latitude, double longitude){
        moveCamara(googleMap, latitude, longitude, 12);
    }

    public static void moveCamara(GoogleMap googleMap, double latitude, double longitude, int zoom){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
    }

    public static void moveCamaraToStartPoint(GoogleMap googleMap){
        moveCamara(googleMap, LATITUDE_MADRID, LONGITUDE_MADRID, 3 );
    }

    public static void zoomIn(GoogleMap googleMap){
        googleMap.moveCamera(CameraUpdateFactory.zoomIn());
    }


    public void addItems(  double latitude, double longitude, String text, String author, String pictureURL) {
        double lat = latitude;
        double lng = longitude;
        ItemMarker offsetItem = new ItemMarker( lat, lng, text, author, pictureURL);
        mClusterManager.addItem(offsetItem);

    }

    public void setUpCluster(GoogleMap googleMap, Context context) {

        mClusterManager = new ClusterManager<ItemMarker>(context, googleMap);
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ItemMarker>() {
            @Override
            public boolean onClusterItemClick(ItemMarker item) {
                mClickedClusterItem = item;
                mClickedCluster=null;
                return false;
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ItemMarker>() {
            @Override
            public boolean onClusterClick(Cluster<ItemMarker> cluster) {
                mClickedCluster = cluster;
                mClickedClusterItem=null;
                return false;
            }
        });


        //googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        googleMap.setInfoWindowAdapter(new MyCustomAdapterForCluster());
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForCluster());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForCluster());

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (mClickedCluster!=null){
                    Intent myIntent = new Intent(mContext, TweetsListActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra(TweetsListActivity.TWEETS_VALUES, mTweetsCluster);
                    mContext.startActivity(myIntent);
                }


            }
        });

    }




    class MyCustomAdapterForCluster implements GoogleMap.InfoWindowAdapter {
        private static final String TAG = "CustomAdapterForCluster";

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.map_info_window, null);
            view.setBackgroundColor(0xFFFFFFFF);

            if (mClickedCluster != null) {
                ListView list = (ListView) view.findViewById(R.id.list_with_results);
                TweetsListAdapter adapter = null;
                ArrayList<ItemMarker> valuesList = new ArrayList<>();
                valuesList.addAll(mClickedCluster.getItems());
                mTweetsCluster = parseItemsMarketColection(valuesList);
                TextView txtText = (TextView) view.findViewById(R.id.more_info_text);
                txtText.setText(R.string.more_txt);
                adapter = new TweetsListAdapter(mContext, valuesList);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else if (mClickedClusterItem != null){
                view = inflater.inflate(R.layout.list_single_tweet, null);
                TextView txtText = (TextView) view.findViewById(R.id.list_text);
                TextView txtAutor= (TextView) view.findViewById(R.id.list_autor);
                ImageView imageView = (ImageView) view.findViewById(R.id.img_menu);
                txtText.setText(mClickedClusterItem.getText());
                txtAutor.setText(mClickedClusterItem.getAutor());

                if (mClickedClusterItem.getPictureUrl()!=null){
                    Picasso.with(mContext).load(mClickedClusterItem.getPictureUrl()).into(imageView);
                }




            }

            return view;

        }

    }

    private ArrayList<Tweet>  parseItemsMarketColection(ArrayList<ItemMarker> items){
        ArrayList<Tweet> result = new ArrayList<Tweet>();
        for (int i = 0; i<items.size(); i++){
                Tweet t = new Tweet(items.get(i).getAutor(),items.get(i).getPictureUrl(),
                        items.get(i).getText(), null, items.get(i).getPosition().latitude, items.get(i).getPosition().longitude);
                result.add(t);
            }
        return result;

    }




}




