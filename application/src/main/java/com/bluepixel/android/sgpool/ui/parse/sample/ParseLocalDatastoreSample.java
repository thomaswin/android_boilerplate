package com.bluepixel.android.sgpool.ui.parse.sample;

import android.util.Log;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas_win on 12/3/2014.
 */
public class ParseLocalDatastoreSample {


    private void do_pinning() {
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);

        gameScore.pinInBackground();


        List<ParseObject> listOfObjects = new ArrayList<ParseObject>();
        ParseObject.pinAllInBackground(listOfObjects);


        // Retrieving from the Local Datastore
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.fromLocalDatastore();
        query.getInBackground("xWMyZ4YE", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });

    }


    private void do_query_localstore() {
        // Querying the Local Datastore
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.whereEqualTo("playerName", "Joe Bob");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList,
                             ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size());
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        // gameScore.unpinInBackground();
        // ParseObject.unpinAllInBackground(listOfObjects);
    }

    private void do_caching_query_result() {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.orderByDescending("score");

        // Query for new results from the network.
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(final List<ParseObject> scores, ParseException e) {
                // Remove the previously cached results.
                ParseObject.unpinAllInBackground("highScores", new DeleteCallback() {
                    public void done(ParseException e) {
                        // Cache the new results.
                        ParseObject.pinAllInBackground("highScores", scores);

                    }
                });
            }
        });
    }

    private void do_sync_local_changes() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.fromPin("myChanges");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
               for (ParseObject score : parseObjects) {
                   score.saveInBackground();
                   score.unpinInBackground("myChanges");
                }
            }
        });
    }
}
