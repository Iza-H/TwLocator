package io.projectandroid.twlocator.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import io.projectandroid.twlocator.R;


/**
 * Created by izabela on 07/05/16.
 */
public class MapHelper {
    public static final String TAG = "MapHelper";
    private ItemMarker  mClickedClusterItem;
    private Cluster<ItemMarker> mClickedCluster;
    private ClusterManager<ItemMarker> mClusterManager;
    private Context mContext;
    private View mView;



    public static void addMarkerToTheMap(GoogleMap googleMap, double latitude, double longitude, String text){
        Log.v(TAG, "Lat " + latitude);
        Log.v(TAG, "Long " + longitude);
        MarkerOptions marker = new MarkerOptions().position
                (new LatLng(latitude, longitude)).title(text);

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
        //Central mapa to Spain:
        moveCamaraToStartPoint(googleMap);
        mContext = context;

        // Setting a custom info window adapter for the google map
        /*googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }


            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude:" + latLng.latitude);

                // Setting the longitude
                tvLng.setText("Longitude:"+ latLng.longitude);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });*/

    }

    public static void moveCamara(GoogleMap googleMap, double latitude, double longitude){
        moveCamara(googleMap, latitude, longitude, 12);
    }

    public static void moveCamara(GoogleMap googleMap, double latitude, double longitude, int zoom){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
    }

    public static void moveCamaraToStartPoint(GoogleMap googleMap){
        moveCamara(googleMap, 40.4167754,-3.7037902, 3 );
    }

    public static void zoomIn(GoogleMap googleMap){
        googleMap.moveCamera(CameraUpdateFactory.zoomIn());
    }


    public void addItems( double latitude, double longitude, String text, String author) {
        double lat = latitude;
        double lng = longitude;
        ItemMarker offsetItem = new ItemMarker(lat, lng, text, author);
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
           mView.setBackgroundColor(0xFFFFFFFF);
            ListView list = (ListView) mView.findViewById(R.id.list_with_results);
            TweetsListAdapter adapter = null;
            ArrayList<ItemMarker> valuesList = new ArrayList<>();
            if (mClickedCluster != null) {
                valuesList.addAll(mClickedCluster.getItems());
            }else if (mClickedClusterItem != null){
                valuesList.add(mClickedClusterItem);

            }
            adapter = new TweetsListAdapter(mContext, valuesList);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            return mView;

        }

    }




}

class ItemMarker implements ClusterItem {
    private final LatLng mPosition;
    private final String mText;
    private final String mAutor;

    public ItemMarker(double lat, double lng, String text, String autor) {
        mText = text;
        mAutor = autor;
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getText(){
        return mText;
    }
    public String getAutor(){
        return mAutor;
    }
}



/*class MyCustomAdapterForCluster implements GoogleMap.InfoWindowAdapter {
    private static final String TAG = "CustomAdapterForCluster";
    private final View myContentsView;

    MyCustomAdapterForCluster() {
        myContentsView = getLayoutInflater().inflate(
                R.layout.info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //if (mClickedCluster != null) {
            //for (MarkerItem item : clickedCluster.getItems()) {
                // Extract data from each item in the cluster as needed
            //}
        //}
        // build your custom view
        // ...

        Log.v(TAG, "Clicked");
        return null;
    }

}*/

class TweetsListAdapter extends ArrayAdapter<String> {
    private final String TAG = "TweetsListAdapter";
    private final Context context;
    private ArrayList<ItemMarker> valuesList;

    public TweetsListAdapter(Context context, ArrayList<ItemMarker> valuesList) {
        super(context, R.layout.list_single_tweet);
        this.context = context;
        this.valuesList = valuesList;

    }

    @Override
    public int getCount() {
        return valuesList.size();
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView= inflater.inflate(R.layout.list_single_tweet, null, true);
        TextView txtText = (TextView) rowView.findViewById(R.id.list_text);
        TextView txtAutor= (TextView) rowView.findViewById(R.id.list_autor);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_menu);
        /*Picasso.with(context)
                .load("http://pbs.twimg.com/profile_images/514107565702012929/08pdnbsJ_normal.jpeg")
                .into(imageView);*/
        txtText.setText(valuesList.get(position).getText());
        txtAutor.setText(valuesList.get(position).getAutor());
        //int resID = context.getResources().getIdentifier(menu.getListMeals().get(position).getImg() , "drawable", context.getPackageName());
        //imageView.setImageResource(resID);
        return rowView;
    }




}