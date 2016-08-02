package com.nbourses.oyeok.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.ShareOwnersNoM;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ShareOwnersNo extends Fragment {

    /*@Bind(R.id.mob_no)
    TextView mob_no;*/



    public ShareOwnersNo() {

    }

    private Button submit;
    private EditText mob_no;
    private EditText owner_name;
    private TextView coupon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_share_owners_no, container, false);

        submit = (Button) v.findViewById(R.id.submit);
        mob_no = (EditText) v.findViewById(R.id.mob_no);
        coupon = (TextView) v.findViewById(R.id.coupon);
        owner_name = (EditText) v.findViewById(R.id.owner_name);

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

              if( !((mob_no.getText().toString().equals("")&&(owner_name.getText().toString().equals(""))))){
                  Log.i("TAG","coupon test "+isValidMobile(mob_no.getText().toString()));
                  if(mob_no.getText().toString().matches("^[789]\\d{9}$")){
                      shareOwnersno(mob_no.getText().toString(),owner_name.getText().toString());
                  }
                  /*if(isValidMobile(mob_no.getText().toString())){

                      //shareOwnersno(mob_no.getText().toString());
                  }*/
                  else{
                      try {
                          SnackbarManager.show(
                                  Snackbar.with(getContext())
                                          .position(Snackbar.SnackbarPosition.TOP)
                                          .text("Please enter valid 10 digit mobile number.")
                                          .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                                          .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                      }
                      catch(Exception e){
                          Log.i("TAG","Caught in exception 1 "+e);
                      }

                  }
               }
               else{
                  try {
                      SnackbarManager.show(
                              Snackbar.with(getContext())
                                      .position(Snackbar.SnackbarPosition.TOP)
                                      .text("Please enter mobile number.")
                                      .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                                      .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                  }
                  catch(Exception e){
                      Log.i("TAG","Caught in exception 2 "+e);
                  }
              }



           }
       });


        return v;
    }

    private boolean isValidMobile(String phone)
    { Log.i("TAG","coupon test 1 "+phone);
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

private void shareOwnersno(String mobileNo,String name){
    Log.i("BROKER BUILDINGS CALLED","coupon "+mobileNo);

    ShareOwnersNoM shareOwnersNoM = new ShareOwnersNoM();

    shareOwnersNoM.setMobile(mobileNo);
    shareOwnersNoM.setName(name);
    shareOwnersNoM.setUser_id(General.getSharedPreferences(getContext(),AppConstants.USER_ID));


    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(AppConstants.SERVER_BASE_URL)
            .build();
    restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

    OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


    try {
        oyeokApiService.generateCoupon(shareOwnersNoM, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                Log.i("BROKER BUILDINGS CALLED","coupon success ");



                JsonObject k = jsonElement.getAsJsonObject();
                try {

                    Log.i("BROKER BUILDINGS CALLED","success response "+response);


                    JSONObject ne = new JSONObject(k.toString());
                    Log.i("BROKER BUILDINGS CALLED","success ne "+ne);
                    Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData"));


                    Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData").getString("coupon"));
                 coupon.setText(ne.getJSONObject("responseData").getString("coupon"));









                }
                catch (JSONException e) {
                    Log.e("TAG", e.getMessage());
                    Log.i("BROKER BUILDINGS CALLED","Failed "+e.getMessage());
                }




            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("BROKER BUILDINGS CALLED","coupon fail "+error);
            }
        });


    }
    catch (Exception e){
        Log.e("TAG", e.getMessage());
    }

}



}
