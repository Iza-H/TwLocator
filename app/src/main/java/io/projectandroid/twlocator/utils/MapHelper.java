package io.projectandroid.twlocator.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

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



    public static void addMarkerToTheMap(GoogleMap googleMap, double latitude, double longitude, String text){
        Log.v(TAG, "Lat " + latitude);
        Log.v(TAG, "Long " + longitude);
        MarkerOptions marker = new MarkerOptions().position
                (new LatLng(latitude, longitude)).title(text);

        googleMap.addMarker(marker);

    }

    public void setupMap(GoogleMap googleMap, Context context){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 20));
    }

    public void addItems( double latitude, double longitude, String text) {
        double lat = latitude;
        double lng = longitude;
        ItemMarker offsetItem = new ItemMarker(lat, lng);
        mClusterManager.addItem(offsetItem);
    }

    public void setUpCluster(GoogleMap googleMap, Context context) {
        ClusterManager<ItemMarker> clusterManager;

        // Position the map.
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));


        clusterManager = new ClusterManager<ItemMarker>(context, googleMap);
        googleMap.setOnCameraChangeListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ItemMarker>() {
            @Override
            public boolean onClusterItemClick(ItemMarker item) {
                mClickedClusterItem = item;
                return false;
            }
        });

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ItemMarker>() {
            @Override
            public boolean onClusterClick(Cluster<ItemMarker> cluster) {
                mClickedCluster = cluster;
                return false;
            }
        });


        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        clusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForCluster());
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForCluster());

        mClusterManager=clusterManager;

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
            if (mClickedCluster != null) {
                Log.v(TAG, "Clicked cluser");


                for (int i = 0; i<mClickedCluster.getSize(); i++) {

                }

            }else if (mClickedCluster != null){
                Log.v(TAG, "Clicked cluser item");

                }


            return view;
        }

    }




}

class ItemMarker implements ClusterItem {
    private final LatLng mPosition;

    public ItemMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

/*class ItemMarker implements ClusterItem {
    private final LatLng mPosition;

    public ItemMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}*/

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