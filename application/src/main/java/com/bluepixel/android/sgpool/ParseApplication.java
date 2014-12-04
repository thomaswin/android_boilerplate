package com.bluepixel.android.sgpool;

import android.app.Application;
import com.bluepixel.android.sgpool.ui.MainPageActivity;
import com.firebase.client.Firebase;
import com.parse.Parse;
import com.parse.PushService;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here


        Parse.initialize(this, "gxfnyMAVphd9HujbZS1Yeozn7LzkDSUTt0y0jI2B", "4pBWWLwC0sOHIYyjVK7d86PQ5o7Vdul4rhtQ3Qet");
        PushService.setDefaultPushCallback(this, MainPageActivity.class);
        Firebase.setAndroidContext(this);


    }
}
