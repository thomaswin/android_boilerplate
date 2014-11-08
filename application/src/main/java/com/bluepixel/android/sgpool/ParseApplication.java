package com.bluepixel.android.sgpool;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "gaQ350TScDWrA1914BBXPqku7xh09948DNEeIfy5", "oVR6clIwYykqw5wBMncEYtmmPmV9VU5t7N0B4V1E");
        ParseFacebookUtils.initialize(getResources().getString(R.string.facebook_app_id));


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
