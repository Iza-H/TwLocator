package io.projectandroid.twlocator.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by izabela on 08/05/16.
 */
public class ItemMarker implements ClusterItem, Serializable {
        private final LatLng mPosition;
        private final String mText;
        private final String mAutor;
        private final String mPictureUrl;


        public ItemMarker( double lat, double lng, String text, String autor, String pictureUrl) {
            mText = text;
            mAutor = autor;
            mPictureUrl = pictureUrl;
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        public String getText() {
            return mText;
        }

        public String getAutor() {
            return mAutor;
        }

        public String getPictureUrl() {
            return mPictureUrl;
        }


}
