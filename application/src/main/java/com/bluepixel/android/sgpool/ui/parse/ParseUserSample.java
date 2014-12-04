package com.bluepixel.android.sgpool.ui.parse;

import android.util.Log;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas_win on 12/4/2014.
 */
public class ParseUserSample {


    private void doSample() {

        /*
        username: The username for the user (required).
        password: The password for the user (required on signup).
        email: The email address for the user (optional).
        */

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

        // other fields can be set just like with ParseObject
        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });



    }


    private void login () {
        ParseUser.logInInBackground("Jerry", "showmethemoney", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    private void do_user_security() {

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // Optionally enable public read access while disabling public write access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);



        /*
        ParseUser has several properties that set it apart from ParseObject:
        username: The username for the user (required).
        password: The password for the user (required on signup).
        email: The email address for the user (optional).

        */

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

        // other fields can be set just like with ParseObject
        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });


        // Login background
        ParseUser.logInInBackground("Jerry", "showmethemoney", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });



        // Current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
        } else {
            // show the signup or login screen
        }


        // Logout
        ParseUser.logOut();


        // annonymous user
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("MyApp", "Anonymous login failed.");
                } else {
                    Log.d("MyApp", "Anonymous user logged in.");
                }
            }
        });

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            //enableSignUpButton();
        } else {
            //enableLogOutButton();
        }



        // No need network connection to create
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().increment("RunCount");
        ParseUser.getCurrentUser().saveInBackground();


        ParseUser.becomeInBackground("session-token-here", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // The current user is now set to user.
                } else {
                    // The token could not be validated.
                }
            }
        });

    }



    private void do_security_for_user_object() throws ParseException {

        ParseUser user = ParseUser.logIn("my_username", "my_password");
        user.setUsername("my_new_username"); // attempt to change username
        user.saveInBackground(); // This succeeds, since the user was authenticated on the device

        // Get the user from a non-authenticated manner
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                object.setUsername("another_username");

                // This will throw an exception, since the ParseUser is not authenticated
                object.saveInBackground();
            }
        });


        // other object
        ParseObject privateNote = new ParseObject("Note");
        privateNote.put("content", "This note is private!");
        privateNote.setACL(new ParseACL(ParseUser.getCurrentUser()));
        privateNote.saveInBackground();


        // Reset password
        ParseUser.requestPasswordResetInBackground("myemail@example.com",
                new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // An email was successfully sent with reset instructions.
                        } else {
                            // Something went wrong. Look at the ParseException to see what's up.
                        }
                    }
                });


    }

    private void do_query_user() {
        // Query User
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("gender", "female");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                } else {
                    // Something went wrong.
                }
            }
        });

    }


    private void do_association() {
        ParseUser user = ParseUser.getCurrentUser();

        // Make a new post
        ParseObject post = new ParseObject("Post");
        post.put("title", "My New Post");
        post.put("body", "This is some great content.");
        post.put("user", user);
        post.saveInBackground();

        // Find all posts by the current user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

            }
        });
    }


    private void do_role() {

        // By specifying no write privileges for the ACL, we can ensure the role cannot be altered.
        ParseACL roleACL = new ParseACL();
        roleACL.setPublicReadAccess(true);
        ParseRole role = new ParseRole("Administrator", roleACL);
        role.saveInBackground();


        List<ParseUser> usersToAddToRole = new ArrayList<ParseUser>();
        List<ParseRole> rolesToAddToRole = new ArrayList<ParseRole>();

        for (ParseUser user : usersToAddToRole) {
            role.getUsers().add(user);
        }
        for (ParseRole childRole : rolesToAddToRole) {
            role.getRoles().add(childRole);
        }
        role.saveInBackground();




        ParseRole moderators =  null;/* Query for some ParseRole */;
        ParseObject wallPost = new ParseObject("WallPost");
        ParseACL postACL = new ParseACL();
        // postACL.setRoleWriteAccess(moderators);
        postACL.setRoleWriteAccess(moderators, true);
        wallPost.setACL(postACL);
        wallPost.saveInBackground();

        ParseACL defaultACL = new ParseACL();
        // Everybody can read objects created by this user
        defaultACL.setPublicReadAccess(true);
        // Moderators can also modify these objects
        defaultACL.setRoleWriteAccess("Moderators", true);
        // And the user can read and modify its own objects
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
