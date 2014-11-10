package com.bluepixel.android.sgpool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.bluepixel.android.sgpool.R;
import com.bluepixel.android.sgpool.util.Maps;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.parse.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doParseTry();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFirebaseTry();
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doParseTry() {


        {
            // Create Role
            ParseACL roleACL = new ParseACL();
            roleACL.setPublicReadAccess(true);
            roleACL.setPublicWriteAccess(true);

            ParseRole role = new ParseRole("Administrator");
            role.getUsers().add(ParseUser.getCurrentUser());

            role.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
        }
        {
            // Create new object
            final ParseObject parseObject1 = new ParseObject("SamplePromotion");
            parseObject1.put("name", "Christmas Promotion");
            parseObject1.put("start", new Date().getTime());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            parseObject1.put("end", calendar.getTime());
            // possible to include acl
            parseObject1.saveInBackground();
        }

        {
            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Sgpool");
            query1.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        Toast.makeText(MyActivity.this, "Sgpool " + parseObjects.size() + " result", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }



    }

    private void doFirebaseTry() {
        Firebase.setAndroidContext(this);
        Firebase dataRef = new Firebase("https://resplendent-heat-3418.firebaseio.com/web/data");

        Firebase userRef = dataRef.child("users");

        User user1 = new User("user1", 201);
        User user2 = new User("user2", 202);

        Map<String, User> users = Maps.newHashMap();
        users.put(user1.getFullName(), user1);
        users.put(user2.getFullName(), user2);
        userRef.setValue(users);

        userRef.child(user1.getFullName()).child("address").setValue("Bangkok", new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {

            }
        });

    }
}


class User {
    private int birthYear;
    private String fullName;

    public User() {}

    public User(String fullName, int birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public long getBirthYear() {
        return birthYear;
    }

    public String getFullName() {
        return fullName;
    }
}

































