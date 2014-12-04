package com.bluepixel.android.sgpool.ui.parse;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bluepixel.android.sgpool.R;
import com.bluepixel.android.sgpool.common.Log;
import com.bluepixel.android.sgpool.ui.NavigationDrawerFragment;
import com.bluepixel.android.sgpool.util.LogUtils;
import com.parse.*;

import java.util.List;

public class ParseUserFragment extends Fragment {

    public static final String TAG = LogUtils.makeLogTag(ParseUserFragment.class);

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ParseUserFragment newInstance(int sectionNumber) {
        ParseUserFragment fragment = new ParseUserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parse_dev_user, container, false);
        initUI(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ((NavigationDrawerFragment.NavigationDrawerCallbacks) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

    }


    private void initUI(View rootView) {
        rootView.findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        rootView.findViewById(R.id.loginA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupWithannonymous();
            }
        });
        rootView.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetUser();
            }
        });
        rootView.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        rootView.findViewById(R.id.queryUserINfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryUserINfo();
            }
        });
        rootView.findViewById(R.id.getUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentUser();
            }
        });


        ParseUser user = ParseUser.getCurrentUser();
        Button logoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
        if (user != null) {
            logoutBtn.setEnabled(true);
        } else {
            logoutBtn.setEnabled(false);
        }
    }

    private void signup() {

        EditText username   = (EditText) getActivity().findViewById(R.id.usernameEditText);
        EditText password   = (EditText)getActivity().findViewById(R.id.passwordEditText);
        EditText email      = (EditText)getActivity().findViewById(R.id.emailEditText);

        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());


        String emailString = email.getText().toString();
        if (!(emailString == null || TextUtils.isEmpty(emailString))) {
            user.setEmail(emailString);
        }

        // other fields can be set just like with ParseObject
        user.put("phone", "650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous login failed.");
                } else {
                    Log.d(TAG, "Anonymous user logged in.");
                    Button logoutBtn = (Button) getActivity().findViewById(R.id.logoutBtn);
                    logoutBtn.setEnabled(true);
                }
            }
        });
    }

    private void signupWithannonymous() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d(TAG, "Anonymous login failed.");
                } else {
                    Log.d(TAG, "Anonymous user logged in.");
                    Button logoutBtn = (Button) getActivity().findViewById(R.id.logoutBtn);
                    logoutBtn.setEnabled(true);
                }
            }
        });
    }

    private void resetUser() {
        ParseUser.logOut();
        Button logoutBtn = (Button) getActivity().findViewById(R.id.logoutBtn);
        logoutBtn.setEnabled(false);
    }

    private void loginUser() {

        EditText username   = (EditText) getActivity().findViewById(R.id.usernameEditText);
        EditText password   = (EditText)getActivity().findViewById(R.id.passwordEditText);

        ParseUser.logInInBackground(
                username.getText().toString(),
                password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {

                    }
                });
    }

    private void loginAnnoymousUser() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

            }
        });
    }

    private void getCurrentUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null) {
            Log.i(TAG, "Current User :" + currentUser.toString());
        } else {
            Log.e(TAG, "Current User NULL");
        }

        byte[] data = "Working at Parse is great!".getBytes();
        final ParseFile file = new ParseFile("resume.txt", data);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, e.getMessage());
                } else {
                    Log.i(TAG, "done");
                    final ParseObject jobApplication = new ParseObject("JobApplication");
                    jobApplication.put("applicantName", "Joe Smith");
                    jobApplication.put("applicantResumeFile", file);
                    jobApplication.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseFile applicationResume = (ParseFile) jobApplication.get("applicantResumeFile");
                            Log.i(TAG, applicationResume.getUrl());
                            applicationResume.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null) {
                                        Log.i(TAG, "Down done");
                                    } else
                                        Log.e(TAG, "Down Error " + e.getMessage());
                                }
                            }, new ProgressCallback() {
                                @Override
                                public void done(Integer integer) {
                                    Log.i(TAG, "Down :" + integer);
                                }
                            });

                        }
                    });


                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                Log.i(TAG, "Up:" + integer);
            }
        });

    }

    /**
     * User info query
     */
    private void queryUserINfo() {

        if (ParseUser.getCurrentUser() == null) {
            Log.e(TAG, "Current user null");
            Toast.makeText(getActivity(), "Null user", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseObject privateNote = new ParseObject("Note");
        privateNote.put("content", "This note is private!");
        privateNote.setACL(new ParseACL(ParseUser.getCurrentUser()));
        privateNote.saveInBackground();


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.

                    Log.i(TAG, "user total : " + objects.size());
                } else {
                    Log.e(TAG, "user null");
                }
            }
        });
    }

}
