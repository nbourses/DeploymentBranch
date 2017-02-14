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
 * Created by Ritesh Warke on 03/01/17.
 */

public class AutoOkCall extends AsyncTask<String, String, String> {
    private Context mContext;
    public AutoOkCall(Context context){
    this.mContext = context;
    }


        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

                Log.i("autoOk","calling autook "+params[0]);

            AutoOk autoOk = new AutoOk();
            autoOk.setGcm_id(General.getSharedPreferences(mContext,AppConstants.GCM_ID));
            autoOk.setLat(params[0]);
            autoOk.setLng(params[1]);
            autoOk.setUser_id(General.getSharedPreferences(mContext,AppConstants.TIME_STAMP_IN_MILLI));
            autoOk.setPlatform("android");
            autoOk.setLocality(params[2]);
            autoOk.setEmail("ritesh@nexchanges1.com");
            autoOk.setReq_avl(AppConstants.Card_REQ_AVL);
            autoOk.setTt(AppConstants.Card_TT);
            Gson gson = new Gson();
            String json = gson.toJson(autoOk);
            Log.i("magic","autook request  json "+json);
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL)
                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


            try {
                oyeokApiService.autoOk(autoOk, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        JsonObject k = jsonElement.getAsJsonObject();
                        try {
                            Log.i("AUTOOK CALLED","autook success response 23 "+response);
                            JSONObject ne = new JSONObject(k.toString());
//                        JSONObject neo = ne.getJSONObject("responseData");
//                        Log.i("AUTOOK CALLED","autook response "+neo);
                            Log.i("AUTOOK CALLED","autook response "+ne);

//                        showHidepanel(false);


                            if(ne.getString("success").equalsIgnoreCase("true")){
                                JSONArray ne1 = ne.getJSONObject("responseData").getJSONArray("ok_ids");
                                Log.i("AUTOOK CALLED","autook response 24 "+ne1);

                                General.setBadgeCount(mContext, AppConstants.HDROOMS_COUNT_UV,General.getBadgeCount(mContext,AppConstants.HDROOMS_COUNT_UV) + ne1.length());
                                Log.i("autook","is the is "+General.getBadgeCount(mContext,AppConstants.HDROOMS_COUNT_UV));
                                if(AppConstants.Card_TT.equalsIgnoreCase("LL"))
                                    General.setSharedPreferences(mContext,AppConstants.Card_TT,"LL");
                                else
                                    General.setSharedPreferences(mContext,AppConstants.Card_TT,"OR");



                                //Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getString("message"));
                                //  Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getJSONArray("ok_ids"));
                                // Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getJSONArray("ok_ids").toJSONArray());

                               // TastyToast.makeText(getContext(), "We have connected you with "+ne1.length()+" brokers in your area.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                              //  TastyToast.makeText(getContext(), "Sign up to connect with "+(10 - ne1.length())+" more brokers waiting for you.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                        /*Log.i("BROKER BUILDINGS CALLED","success ne "+ne);
                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData"));
                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData").getString("coupon"));
                        coupon.setText(ne.getJSONObject("responseData").getString("coupon"));*/
                                // getFragmentManager().popBackStack();



                               // General.setSharedPreferences(mContext,AppConstants.STOP_CARD,"yes");


                            }


                        }
                        catch (JSONException e) {
                            Log.e("TAG","Something went wrong "+e.getMessage());
                           // TastyToast.makeText(mContext, "Something went wrong.", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
//                        TastyToast.makeText(getContext(), "Please signup to get connected with 10 brokers waiting for you", TastyToast.LENGTH_LONG, TastyToast.INFO);

                        }




                    }

                    @Override
                    public void failure(RetrofitError error) {


                        Log.i("AUTOOK CALLED","coupon fail "+error);
                        try {
                            SnackbarManager.show(
                                    Snackbar.with(mContext)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text("Server Error: " + error.getMessage())
                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                        }
                        catch(Exception e){}

                    }
                });


            }
            catch (Exception e){
                Log.e("AUTO OK CALLED", e.getMessage());
            }



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

