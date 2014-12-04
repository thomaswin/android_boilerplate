package com.bluepixel.android.sgpool.ui.parse.sample;

import com.parse.*;

/**
 * Created by thomas_win on 12/3/2014.
 */
public class ParseFileSample {

    private void doSample() {
        byte[] data = "Working at Parse is great!".getBytes();
        ParseFile file = new ParseFile("resume.txt", data);
        file.saveInBackground();

        // Finally, after the save completes, you can associate a ParseFile onto a ParseObject just like any other piece of data:
        ParseObject jobApplication = new ParseObject("JobApplication");
        jobApplication.put("applicantName", "Joe Smith");
        jobApplication.put("applicantResumeFile", file);
        jobApplication.saveInBackground();


        //Retrieving it back involves calling one of the getData variants on the ParseObject. Here we retrieve the resume file off another JobApplication object:
        ParseFile applicantResume = (ParseFile)jobApplication.get("applicantResumeFile");
        applicantResume.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    // data has the bytes for the resume
                } else {
                    // something went wrong
                }
            }
        });


    }

    private void doSample_progress() {
        byte[] data = "Working at Parse is great!".getBytes();
        ParseFile file = new ParseFile("resume.txt", data);

        file.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // Handle success or failure here ...
            }
        }, new ProgressCallback() {
            public void done(Integer percentDone) {
                // Update your progress spinner here. percentDone will be between 0 and 100.
            }
        });
    }
}
