package com.bluepixel.android.sgpool.ui.parse;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bluepixel.android.sgpool.R;
import com.bluepixel.android.sgpool.common.Log;
import com.bluepixel.android.sgpool.util.LogUtils;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = LogUtils.makeLogTag(FirebaseUserFragment.class);


    public static final int BTN_Signup      = R.id.btnSignup;
    public static final int BTN_Login       = R.id.btnLogin;
    public static final int BTN_LogOut      = R.id.btnLoginout;
    public static final int BTN_LoginAuth      = R.id.btnLoginAnnoymous;

    public static final int BTN_Reset         = R.id.btnReset;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Firebase ref;

    public static FirebaseUserFragment newInstance(int sectionNumber) {
        FirebaseUserFragment fragment = new FirebaseUserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        fragment.ref = new Firebase("https://resplendent-heat-3418.firebaseio.com/");

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_firebase_dev_user, container, false);
        initUI(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ParseDevActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

        setupListener();
    }


    private void initUI(View rootView) {
        rootView.findViewById(BTN_Signup).setOnClickListener(this);
        rootView.findViewById(BTN_Login).setOnClickListener(this);
        rootView.findViewById(BTN_LoginAuth).setOnClickListener(this);
        rootView.findViewById(BTN_Reset).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == BTN_Signup) {
            doSignup();
        } else if (v.getId() == BTN_Login) {

            doSignin();
        } else if (v.getId() == BTN_LoginAuth) {
            doLoginAnnonymous();
        }  else if (v.getId() == BTN_Reset) {

        }
    }

    private void setupListener() {
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    Log.i(TAG, "onAuthStateChanged : LOG IN");
                } else {
                    // user is not logged in
                    Log.i(TAG, "onAuthStateChanged : LOG OUT");
                }
            }
        });
    }

    private void doSignup() {
                ref.createUser("thomas.wintun@gmail.com", "correcthorsebatterystaple", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // user was created
                Log.i(TAG, "ONSUCCESS");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Log.i(TAG, "ON ERRIR l " + firebaseError.toString());
            }
        });
    }
    private void doSignin() {


        ref.authWithPassword("thomas.wintun@gmail.com", "correcthorsebatterystaple",
                new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        Log.e(TAG, "Authenticated : " + authData.toString());

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("provider", authData.getProvider());

                        if(authData.getProviderData().containsKey("id")) {
                            map.put("provider_id", authData.getProviderData().get("id").toString());
                        }
                        if(authData.getProviderData().containsKey("displayName")) {
                            map.put("displayName", authData.getProviderData().get("displayName").toString());
                        }
                        if (authData.getProviderData().containsKey("email")) {
                            map.put("email", authData.getProviderData().get("email").toString());
                        }
                        ref.child("users").child(authData.getUid()).setValue(map);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        // Something went wrong :(
                        Log.e(TAG, "Error : " + error.getMessage());
                    }
                });

    }

    private void doLoginAnnonymous() {
        ref.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                String authDataInfo = authData.toString();
                Log.i(TAG, "auth data: " + authDataInfo);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.i(TAG, "error: " + firebaseError.toString());
            }
        });
    }
    private void resetUser() {
        ref.unauth();
    }
}
