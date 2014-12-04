package com.bluepixel.android.sgpool.ui.parse.sample;

import com.parse.*;

import java.util.List;

/**
 * Created by thomas_win on 12/4/2014.
 */
public class ParseGeopointSample {



    private void do_parse_geopoint() {
        ParseGeoPoint point = new ParseGeoPoint(40.0, -30.0);
        ParseObject placeObject = new ParseObject("Promotion");
        placeObject.put("location", point);


        // geo queries
        ParseObject userObject = new ParseObject("Customer");
        ParseGeoPoint userLocation = (ParseGeoPoint) userObject.get("location");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PlaceObject");
        query.whereNear("location", userLocation);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

            }
        });

        // Geo box queries
        ParseGeoPoint southwestOfSF = new ParseGeoPoint(37.708813, -122.526398);
        ParseGeoPoint northeastOfSF = new ParseGeoPoint(37.822802, -122.373962);
        ParseQuery<ParseObject> boxQuery = ParseQuery.getQuery("PizzaPlaceObject");
        boxQuery.whereWithinGeoBox("location", southwestOfSF, northeastOfSF);
        boxQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

            }
        });
    }
}
