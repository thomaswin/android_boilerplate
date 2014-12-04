package com.bluepixel.android.sgpool.ui.parse;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.parse.*;

/**
 * Created by thomas_win on 12/4/2014.
 */
public class ParseFacebookUserSample {


    private void do_sample_facebook_setup() {

        int requestCode = 0;
        int resultCode= 0;
        Intent data = null;

        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);


        // login
        Activity context = null;
        ParseFacebookUtils.logIn(context, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (parseUser.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });

        // Linking
        final ParseUser user = null;
        if (!ParseFacebookUtils.isLinked(user)) {
            ParseFacebookUtils.link(user, context, new SaveCallback() {
                @Override
                public void done(ParseException ex) {
                    if (ParseFacebookUtils.isLinked(user)) {
                        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                    }
                }
            });
        }



    }
}
