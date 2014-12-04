package com.bluepixel.android.sgpool.ui.parse;

import android.content.Intent;
import android.util.Log;
import com.parse.ConfigCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas_win on 12/3/2014.
 */
public class ParseAnalyticsConfigSample {


    private void doSample(Intent intent) {
        ParseAnalytics.trackAppOpened(intent);


        Map<String, String> dimensions = new HashMap<String, String>();
        // Define ranges to bucket data points into meaningful segments
        dimensions.put("priceRange", "1000-1500");
        // Did the user filter the query?
        dimensions.put("source", "craigslist");
        // Do searches happen more often on weekdays or weekends?
        dimensions.put("dayType", "weekday");
        // Send the dimensions to Parse along with the 'search' event
        ParseAnalytics.trackEvent("search", dimensions);

        /*
        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put('code', Integer.toString(error.getCode()));
        ParseAnalytics.trackEvent('error', dimensions);
        */

    }

    private void dosample_config() {
        ParseConfig.getInBackground(new ConfigCallback() {

            @Override
            public void done(ParseConfig parseConfig, com.parse.ParseException e) {
                int number = parseConfig.getInt("winningNumber");
                Log.d("TAG", String.format("Yay! The number is %d!", number));
            }
        });

        Log.d("TAG", "Getting the latest config...");
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig parseConfig, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("TAG", "Yay! Config was fetched from the server.");
                } else {
                    Log.e("TAG", "Failed to fetch. Using Cached Config.");
                    parseConfig = ParseConfig.getCurrentConfig();
                }

                // Get the message from config or fallback to default value
                String welcomeMessage = parseConfig.getString("welcomeMessage", "Welcome!");
                Log.d("TAG", String.format("Welcome Messsage From Config = %s", welcomeMessage));
            }
        });

    }

}
class Helper {
    private static final long configRefreshInterval = 12 * 60 * 60 * 1000;
    private static long lastFetchedTime;

    // Fetches the config at most once every 12 hours per app runtime
    public static void refreshConfig() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFetchedTime > configRefreshInterval) {
            lastFetchedTime = currentTime;
            ParseConfig.getInBackground();
        }
    }
}