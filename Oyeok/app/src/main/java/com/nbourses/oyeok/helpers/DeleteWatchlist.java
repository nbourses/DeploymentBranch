package com.nbourses.oyeok.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AutoOk;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sushil on 09/02/17.
 */

public class DeleteWatchlist extends AsyncTask<String, String, String> {
    private Context mContext;
    public DeleteWatchlist(Context context){
        this.mContext = context;
    }


    private String resp;
    ProgressDialog progressDialog;

    @Override
    protected String doInBackground(String... params) {



        return params[0];
    }


    @Override
    protected void onPostExecute(String result) {

        Log.i("autoOk","calling autook "+result);
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {
        //  finalResult.setText(text[0]);

    }
}
