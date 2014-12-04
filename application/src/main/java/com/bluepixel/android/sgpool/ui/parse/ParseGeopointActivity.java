package com.bluepixel.android.sgpool.ui.parse;

        import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.bluepixel.android.sgpool.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

public class ParseGeopointActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String TAG = "ParseGeopointActivity";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final LatLng INITIAL_CENTER = new LatLng(1.27786, 103.84147);
    public static final String FIREBASE_BACKEND_TOUCH_LOCATION = "https://bluepixels.firebaseio.com/web/data/touchlocation";

    private GoogleMap map;
    private LocationClient mLocationClient;
    private Firebase rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_geopoint);


        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        rootRef = new Firebase(FIREBASE_BACKEND_TOUCH_LOCATION);
        LatLng currentPosition = null;
        mLocationClient = new LocationClient(this, this, this);
//        Location lastLocation = mLocationClient.getLastLocation();
//        if (lastLocation != null) {
//            currentPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//        } else {
//            currentPosition = INITIAL_CENTER;
//        }

        currentPosition = INITIAL_CENTER;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                currentPosition, 16));

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                saveLocationToParseBackend(latLng);
                saveLocationToFirebaseBackend(latLng);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();



    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parse_geopoint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_retrieve_geo_point) {
            retrieveFromParseBackend();
            return true;
        } else if (id == R.id.action_clear) {
            map.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TouchLocation");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    for(ParseObject obj : parseObjects) {
                        obj.deleteInBackground();
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        Location location = mLocationClient.getLastLocation();
        if (location == null) return;

        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        GeoLocation currentGeoLocation = new GeoLocation(loc.latitude, loc.longitude);

        // Current location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16));
        map.addMarker(new MarkerOptions().position(loc));

        GeoFire geoFire = new GeoFire(rootRef);
        GeoQuery geoQuery = geoFire.queryAtLocation(currentGeoLocation, 1.6);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                // New add location
                LatLng newLoc = new LatLng(location.latitude, location.longitude);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc, 16));
                map.addMarker(new MarkerOptions().position(newLoc));
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(FirebaseError error) {

            }
        });


    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }



    private void saveLocationToFirebaseBackend(final LatLng latLng) {


        // rootRef = rootRef.push();
        GeoFire geoFire = new GeoFire(rootRef);

        geoFire.setLocation("location", new GeoLocation(latLng.latitude, latLng.longitude), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, FirebaseError error) {
            }
        });
    }

    private void saveLocationToParseBackend(final LatLng latLng) {
        ParseObject point = new ParseObject("TouchLocation");
        ParseGeoPoint geoPoint = new ParseGeoPoint(latLng.latitude, latLng.longitude);
        point.put("location", geoPoint);
        point.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(ParseGeopointActivity.this, "Success:" + latLng, Toast.LENGTH_SHORT).show();
                    map.addMarker(new MarkerOptions().position(latLng));
                } else {
                    Toast.makeText(ParseGeopointActivity.this, "error", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void retrieveFromParseBackend() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TouchLocation");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<LatLng> locations = new ArrayList<LatLng>();
                for(ParseObject obj : parseObjects) {
                    ParseGeoPoint point =(ParseGeoPoint) obj.get("location");
                    locations.add(new LatLng(point.getLatitude(), point.getLongitude()));
                }

                for (LatLng loc : locations) {
                    map.addMarker(new MarkerOptions().position(loc));
                }
            }
        });
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}

class Outlet {

    public String name;
    public String address;
    public double lat;
    public double lng;

    Outlet(String name, String address, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }
}
