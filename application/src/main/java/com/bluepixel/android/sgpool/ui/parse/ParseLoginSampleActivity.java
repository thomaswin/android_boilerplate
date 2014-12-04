/*
 *  Copyright (c) 2014, Facebook, Inc. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Facebook.
 *
 *  As with any software that integrates with the Facebook platform, your use of
 *  this software is subject to the Facebook Developer Principles and Policies
 *  [http://developers.facebook.com/policy/]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.bluepixel.android.sgpool.ui.parse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bluepixel.android.sgpool.R;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Shows the user profile. This simple activity can function regardless of whether the user
 * is currently logged in.
 */
public class ParseLoginSampleActivity extends Activity {
    private static final int LOGIN_REQUEST = 0;

    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;

    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        titleTextView = (TextView) findViewById(R.id.profile_title);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        nameTextView = (TextView) findViewById(R.id.profile_name);
        loginOrLogoutButton = (Button) findViewById(R.id.login_or_logout_button);
        titleTextView.setText(R.string.profile_title_logged_in);

        loginOrLogoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // User clicked to log out.
                    ParseUser.logOut();
                    currentUser = null;
                    showProfileLoggedOut();
                } else {
                    // User clicked to log in.
                    ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                        ParseLoginSampleActivity.this);


                    ParseLoginBuilder builder = new ParseLoginBuilder(
                        ParseLoginSampleActivity.this);
                    Intent parseLoginIntent = builder.setParseLoginEnabled(true)
                        .setParseLoginButtonText("Go")
                        .setParseSignupButtonText("Register")
                        .setParseLoginHelpText("Forgot password?")
                        .setParseLoginInvalidCredentialsToastText("You email and/or password is not correct")
                        .setParseLoginEmailAsUsername(true)
                        .setParseSignupSubmitButtonText("Submit registration")
                        .setFacebookLoginEnabled(true)
                        .setFacebookLoginButtonText("Facebook")
                        .setFacebookLoginPermissions(Arrays.asList(
                            "user_status",
                            "read_stream",
                            "user_friends",
                            "read_friendlists"))
                        .setTwitterLoginEnabled(true)
                        .setTwitterLoginButtontext("Twitter")
                        .build();
                    startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            showProfileLoggedIn();
        } else {
            showProfileLoggedOut();
        }
    }

    /**
     * Shows the profile of the given user.
     */
    private void showProfileLoggedIn() {
        titleTextView.setText(R.string.profile_title_logged_in);
        emailTextView.setText(currentUser.getEmail());
        String fullName = currentUser.getString("name");
        if (fullName != null) {
            nameTextView.setText(fullName);
        }
        loginOrLogoutButton.setText(R.string.profile_logout_button_label);
    }

    /**
     * Show a message asking the user to log in, toggle login/logout button text.
     */
    private void showProfileLoggedOut() {
        titleTextView.setText(R.string.profile_title_logged_out);
        emailTextView.setText("");
        nameTextView.setText("");
        loginOrLogoutButton.setText(R.string.profile_login_button_label);
    }


    private void testFacebook() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            makeMeRequest();
        }

    }

    private void makeMeRequest() {
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
            new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {

                    Log.v("User Response --- ", response.toString() );

                    if (user != null) {
                        // Create a JSON object to hold the profile info
                        JSONObject userProfile = new JSONObject();
                        try {
                            // Populate the JSON object
                            userProfile.put("facebookId", user.getId());
                            userProfile.put("name", user.getName());
                            if (user.getProperty("gender") != null) {
                                userProfile.put("gender", (String) user.getProperty("gender"));
                            }
                            if (user.getProperty("email") != null) {
                                userProfile.put("email", (String) user.getProperty("email"));
                            }



                            // Show the user info
                        } catch (JSONException e) {
                            Log.d("Facebook", "Error parsing returned user data. " + e);
                        }

                    } else if (response.getError() != null) {
                        if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) ||
                            (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {

                        } else {

                        }
                    }
                }
            }
        );
        Bundle params = new Bundle();
        //params.putString("fields", "id,name,friends");
        // params.putString("fields", "id, name, picture");
        params.putString("fields", "id, name, friendlists");
        request.setParameters(params);
        request.executeAsync();

        Request friendRequest = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(), new Request.GraphUserListCallback() {
            @Override
            public void onCompleted(List<GraphUser> users, Response response) {

                Log.i("Friend Response", response.toString());

            }
        });


//         friendRequest.setParameters(params);
        friendRequest.executeAsync();

    }


    private void getFriendList() {
        // TODO get friend list
        Log.d("Facebook", "Friend search >>");

        Session fbSession = ParseFacebookUtils.getSession();
        if (fbSession == null) {
            Toast.makeText(ParseLoginSampleActivity.this, "Session null", Toast.LENGTH_SHORT).show();
            return;
        }

        Request.newMyFriendsRequest(ParseFacebookUtils.getSession(), new Request.GraphUserListCallback() {
            @Override
            public void onCompleted(List<GraphUser> users, Response response) {
                Log.v("Friends", "Total friend : " + users.size());

                if (response != null) {
                    Log.v("Response --- ", response.toString() );
                } else {
                    Log.v("Response --- ", "Response empty");
                }
            }
        });
    }


    private void getFriendProfileInfo() {
        // TODO get friend info - username, id, profile pic, relationship status
    }
}



