package io.projectandroid.twlocator.utils;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.projectandroid.twlocator.R;
import io.projectandroid.twlocator.model.Tweet;

/**
 * Created by izabela on 07/05/16.
 */
public class MapHelper {
    public static final String TAG = "MapHelper";


    public static void addMarkerToTheMap(GoogleMap googleMap, double latitude, double longitude, String text){
        Log.v(TAG, "Lat " + latitude);
        Log.v(TAG, "Long " + longitude);
        MarkerOptions marker = new MarkerOptions().position
                (new LatLng(latitude, longitude)).title(text);

        googleMap.addMarker(marker);

    }

    public static void setupMap(GoogleMap googleMap){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public static void moveCamara(GoogleMap googleMap, double latitude, double longitude){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 12));
    }

}
