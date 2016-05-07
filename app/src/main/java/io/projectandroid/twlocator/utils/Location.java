package io.projectandroid.twlocator.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by izabela on 07/05/16.
 */
public class Location {

    public interface OnLocationSearchFinish{
        public void onLocationSearchFinished(LatLng location);
    }



    public void getLocationFromAddressInThread(final Context context, final String strAddress, final OnLocationSearchFinish finalBlock){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LatLng locationPoint = getLocationFromAddress(context, strAddress);
                finalBlock.onLocationSearchFinished(locationPoint);
            }
        }).start();
    }

    private LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng locationPoint = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            locationPoint  = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return locationPoint;
    }
}
