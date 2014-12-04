package com.bluepixel.android.sgpool.ui.parse.sample;

import android.util.Log;
import com.parse.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by thomas_win on 12/3/2014.
 */
public class ParseQuerySample {


    private void doSample_basic_query() {

        // Basic Query

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.whereEqualTo("playerName", "Dan Stemkoski");
        /*
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        */


        query.whereNotEqualTo("playerName", "Michael Yabuti");
        query.whereGreaterThan("playerAge", 18);

        // default 100
        query.setLimit(10); // limit to at most 10 results
        query.setSkip(10); // skip the first 10 results

        // Exactly one result
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("GameScore");
        query1.whereEqualTo("playerEmail", "dstemkoski@example.com");
        query1.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    Log.d("score", "Retrieved the object.");
                }
            }
        });


        // Sorts the results in ascending order by the score field
        query.orderByAscending("score");

        // Sorts the results in descending order by the score field
        query.orderByDescending("score");

        // Sorts the results in ascending order by the score field if the previous sort keys are equal.
        query.addAscendingOrder("score");

        // Sorts the results in descending order by the score field if the previous sort keys are equal.
        query.addDescendingOrder("score");

        // Restricts to wins < 50
        query.whereLessThan("wins", 50);

        // Restricts to wins <= 50
        query.whereLessThanOrEqualTo("wins", 50);

        // Restricts to wins > 50
        query.whereGreaterThan("wins", 50);

        // Restricts to wins >= 50
        query.whereGreaterThanOrEqualTo("wins", 50);



        String[] names = {"Jonathan Walsh", "Dario Wunsch", "Shawn Simon"};
        query.whereContainedIn("playerName", Arrays.asList(names));

        // query.whereNotContainedIn("playerName", Arrays.asList(names));

        // Finds objects that have the score set
        query.whereExists("score");

        // Finds objects that don't have the score set
        query.whereDoesNotExist("score");



        ParseQuery<ParseObject> teamQuery = ParseQuery.getQuery("Team");
        teamQuery.whereGreaterThan("winPct", 0.5);
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereMatchesKeyInQuery("hometown", "city", teamQuery);
        userQuery.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                // results has the list of users with a hometown team with a winning record

            }
        });

        ParseQuery<ParseUser> losingUserQuery = ParseUser.getQuery();
        losingUserQuery.whereDoesNotMatchKeyInQuery("hometown", "city", teamQuery);
        losingUserQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> parseUsers, ParseException e) {
                // results has the list of users with a hometown team with a losing record
            }
        });

        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.selectKeys(Arrays.asList("playerName", "score"));;
        List<ParseObject> results = query.find();

        ParseObject object = results.get(0);
        object.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                // all fields of the object will now be available here.
            }
        });
        */
    }
}















