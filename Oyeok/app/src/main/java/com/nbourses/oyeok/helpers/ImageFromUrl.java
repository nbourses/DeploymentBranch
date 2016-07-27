package com.nbourses.oyeok.helpers;

import android.os.AsyncTask;

/**
 * Created by ritesh on 26/07/16.
 */
public class ImageFromUrl extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String[] params) {
        // do above Server call here


        return "some message";
    }

    @Override
    protected void onPostExecute(String message) {
        //process message
    }

}



