package com.example.soil.biogi.chat.app;

import android.app.Application;

import com.example.soil.biogi.chat.helper.myPreferenceManager;

/**
 * Created by soil on 2016/8/27.
 */
public class myApplication extends Application {

    public static final String TAG = myApplication.class
            .getSimpleName();

    private static myApplication mInstance;

    private myPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized myApplication getInstance() {
        return mInstance;
    }


    public myPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new myPreferenceManager(this);
        }

        return pref;
    }
}