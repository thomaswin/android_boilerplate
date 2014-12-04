package com.bluepixel.android.sgpool.ui.parse.sample;

import com.parse.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by thomas_win on 12/3/2014.
 */
public class ParseObjectSample {

    /**
     * Pasre object, saving object, retrieve object
     */
    private void doSampleParseObject() {

        // Create new parse object
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);
        gameScore.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });


        // value of parse object
        int score = gameScore.getInt("score");
        String playerName = gameScore.getString("playerName");
        boolean cheatMode = gameScore.getBoolean("cheatMode");

        // Get metadata of the object
        String objectId = gameScore.getObjectId();
        Date updatedAt = gameScore.getUpdatedAt();
        Date createdAt = gameScore.getCreatedAt();


        // To refresh object that already in the cloud with latest data
        gameScore.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Success!
                } else {
                    // Failure!
                }
            }
        });


    }

    /**
     * Local Datastore
     */
    private void doSampleLocalDataStore() {

        // Create new object
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);
        gameScore.pinInBackground();


        // Retrieve from local data store and network if possible
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.fromLocalDatastore();
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });


        // Fetch data from only datastore
        ParseObject object = ParseObject.createWithoutData("GameScore", "xWMyZ4YEGZ");
        object.fetchFromLocalDatastoreInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                } else {
                    // something went wrong
                }
            }
        });

        // unpin object - remove data from the localstore
        gameScore.unpinInBackground();

    }

    private void saveObjectOffline () {
        // Saving object offline
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);
        gameScore.saveEventually();

    }

    private void doUpdateObjects() {

        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 1337);
        gameScore.put("playerName", "Sean Plott");
        gameScore.put("cheatMode", false);
        gameScore.saveEventually();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        // Retrieve the object by id
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    gameScore.put("score", 1338);
                    gameScore.put("cheatMode", true);
                    gameScore.saveInBackground();
                }
            }
        });


        // counters
        gameScore.increment("score");
        gameScore.saveInBackground();


        gameScore.addAllUnique("skills", Arrays.asList("flying", "kungfu"));
        gameScore.saveInBackground();

    }

    private void deleteObjects() {
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.deleteInBackground();


        // After this, the playerName field will be empty
        gameScore.remove("playerName");

        // Saves the field deletion to the Parse Cloud
        gameScore.saveInBackground();

    }


    private void doSample_relational_data() {
        // Create the post
        ParseObject myPost = new ParseObject("Post");
        myPost.put("title", "I'm Hungry");
        myPost.put("content", "Where should we go for lunch?");

        // Create the comment
        ParseObject myComment = new ParseObject("Comment");
        myComment.put("content", "Let's do Sushirrito.");

        // Add a relation between the Post and Comment
        myComment.put("parent", myPost);

        // This will save both myPost and myComment
        myComment.saveInBackground();

        // Add a relation between the Post with objectId "1zEcyElZ80" and the comment
        myComment.put("parent", ParseObject.createWithoutData("Post", "1zEcyElZ80"));


        /*
        // related parseobject not fected
        // object cannot be retrieved until they have been fetched
        fetchedComment.getParseObject("post")
                .fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject post, ParseException e) {
                        String title = post.getString("title");
                        // Do something with your new title variable
                    }
                });

        */


        // many to many relation
        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = user.getRelation("likes");
        relation.add(myPost);
        user.saveInBackground();


        relation.remove(myPost);


        // by default, list of objects in this relation not downloaded.
        // get the list of posts by calling fin in background on parse query retruned by getQuery
        relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the Posts the current user liked.
                }
            }
        });


        // Add query constraints
        ParseQuery<ParseObject> query = relation.getQuery();
        // Add other query constraints.
    }

    /**
     *
     */
    private void doSample_datatype() throws JSONException {
        int myNumber = 42;
        String myString = "the number is " + myNumber;
        Date myDate = new Date();

        JSONArray myArray = new JSONArray();
        myArray.put(myString);
        myArray.put(myNumber);

        JSONObject myObject = new JSONObject();
        myObject.put("number", myNumber);
        myObject.put("string", myString);

        byte[] myData = { 4, 8, 16, 32 };

        ParseObject bigObject = new ParseObject("BigObject");
        bigObject.put("myNumber", myNumber);
        bigObject.put("myString", myString);
        bigObject.put("myDate", myDate);
        bigObject.put("myData", myData);
        bigObject.put("myArray", myArray);
        bigObject.put("myObject", myObject);
        bigObject.put("myNull", JSONObject.NULL);
        bigObject.saveInBackground();
    }

}
