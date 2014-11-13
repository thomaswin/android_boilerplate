package com.bluepixel.android.sgpool.ui.parse;



import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bluepixel.android.sgpool.R;
import com.bluepixel.android.sgpool.common.Log;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class ParseInstallationFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ParseInstallationFragment newInstance(int sectionNumber) {
        ParseInstallationFragment fragment = new ParseInstallationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parse_dev_installation, container, false);
        initUI(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ParseDevActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }



    private void initUI(View rootView) {
        rootView.findViewById(R.id.createInstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInstall();
            }
        });
        rootView.findViewById(R.id.getInstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInstall();
            }
        });


        rootView.findViewById(R.id.btnSubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });

        rootView.findViewById(R.id.btnUnsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeCancel();
            }
        });
        rootView.findViewById(R.id.btnPush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushParse();
            }
        });


    }
    private void createInstall () {

        ArrayList<String> channels = new ArrayList<String>();
        channels.add("channel1");
        channels.add("channel2");

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("model", Build.MODEL);
        installation.put("channels", channels);

        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT);
                    toast.show();

                    Log.i("TAG", "Success");
                } else {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                    toast.show();

                    Log.e("TAG", "Error");

                }
            }
        });
    }

    private void getInstall() {
        String installationInfo = ParseInstallation.getCurrentInstallation().getInstallationId();
        Log.i("TAG", installationInfo);
    }

    private void subscribe() {
        ParsePush.subscribeInBackground("channel1");


    }

    private void subscribeCancel() {
        ParsePush.unsubscribeInBackground("channel1");
    }

    private void pushParse() {
        ParsePush push = new ParsePush();
        push.setChannel("channel1");
        push.setMessage("The Giants just scored! It's now 2-2 against the Mets.");
        push.sendInBackground();

        /*
        // multiple channels
        LinkedList<String> channels = new LinkedList<String>();
        channels.add("Giants");
        channels.add("Mets");


        ParsePush push = new ParsePush();
        push.setChannels(channels); // Notice we use setChannels not setChannel
        push.setMessage("The Giants won against the Mets 2-3.");
        push.sendInBackground();
        */
    }
}
