package io.projectandroid.twlocator;

import android.app.Application;

import io.projectandroid.twlocator.model.db.DBConstants;
import io.projectandroid.twlocator.model.db.DBHelper;

/**
 * Created by izabela on 07/05/16.
 */
public class TwLocatorApp extends Application {


    @Override
    public void onCreate() {

        DBHelper.configure(DBConstants.DBNAME, getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
