package com.bluepixel.android.sgpool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bluepixel.android.sgpool.R;
import com.firebase.client.Firebase;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseObject testObject = new ParseObject("TestObject");
                testObject.put("foo", "bar");
                testObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.v("Parse Data", testObject.toString());
                        if (e == null) {
                            Toast.makeText(MyActivity.this, "Result: " + testObject.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // By specifying no write privileges for the ACL, we can ensure the role cannot be altered.
                ParseACL roleACL = new ParseACL();
                roleACL.setPublicReadAccess(true);
                roleACL.setPublicWriteAccess(true);

                ParseRole role = new ParseRole("Administrator");
                role.getUsers().add(ParseUser.getCurrentUser());
                //role.getUsers().add(ParseUser.getCurrentUser());

                role.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                    }
                });



            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
//                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {

                    public void done(final List<ParseObject> scoreList, ParseException e) {
                        if (e == null) {
                            // Toast.makeText(MyActivity.this, "Retrieved " + scoreList.size() + " scores", Toast.LENGTH_SHORT).show();
                            Log.v("Retrieved", scoreList.size() + " scores");

                        } else {
                            // Toast.makeText(MyActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.v("Error", "Error: " + e.getMessage());

                        }

                        if (e!=null)
                            return;

                        ParseObject.unpinAllInBackground("test_object_all", scoreList, new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                ParseObject.pinAllInBackground("test_object_all", scoreList, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Log.v("Pinned", "Pin all");
                                    }
                                });
                            }
                        });
                    }
                });

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

                getIntAsync().onSuccessTask(new Continuation<String, Task<ParseObject>>() {
                    @Override
                    public Task<ParseObject> then(Task<String> task) throws Exception {
                        String result = task.getResult();

                        return null;
                    }
                });
            }
        });

        TextView textView = (TextView) findViewById(R.id.textView);
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("email:").append(user.getEmail());
            builder.append(", name:").append(user.getString("name"));
            builder.append(", id:").append(user.getObjectId());
            textView.setText(builder.toString());
        } else {
            textView.setText("Null user");
        }

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


    private Task<String> getIntAsync() {
        Task<String> successful = Task.forResult("The good result.");
        return successful;
    }


    private void doTryWithFirebase() {

        Firebase.setAndroidContext(this);
        Firebase rootRef = new Firebase("https://docs-examples.firebaseio.com/web/data");

        // Firebase rootRef = new Firebase("https://docs-examples.firebaseio.com/web/data/users/mchen/name");

        Firebase rootRef2 = new Firebase("https://docs-examples.firebaseio.com/web/data");
        rootRef2.child("users/mchen/name");

        User alanisawesome = new User("Alan Turing", 1912);
        User gracehop = new User("Grace Hopper", 1906);

        Firebase usersRef = rootRef.child("users");

        Map<String, User> users = new HashMap<String, User>();
        users.put("alanisawesome", alanisawesome);
        users.put("gracehop", gracehop);

        usersRef.setValue(users);


        //Referencing the child node using a .child() on it's parent node
        usersRef.child("alanisawesome").child("name").setValue("Alan Turing");
        usersRef.child("alanisawesome").child("birthYear").setValue(1912);

        //Using the / in the .child() call to specify a child and a grandchild node also works!
        usersRef.child("gracehop/name").setValue("Grace Hopper");
        usersRef.child("gracehop/birthYear").setValue(1906);

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

































