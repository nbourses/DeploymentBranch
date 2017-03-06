package com.nbourses.oyeok.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.activities.ClientDealsListActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.OkAccept;
import com.nbourses.oyeok.models.PayUHash.PayUHash;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartnerBrokerFragment extends Fragment {

    @Bind(R.id.pb_title)
    TextView pb_title;

    @Bind(R.id.pb_continue)
    TextView pb_continue;

    @Bind(R.id.pb_des)
    TextView pb_des;

    @Bind(R.id.pb_free)
    Button pb_free;

    @Bind(R.id.pb_partner)
    Button pb_partner;

    private String hash;
    private String ammount;
    private PayUmoneySdkInitilizer.PaymentParam.Builder builder;

    public PartnerBrokerFragment() {
        // Required empty public constructor
    }

    public static final String TAG = "PayUMoneySDK";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_partner_broker, container,
                false);
        ButterKnife.bind(this, rootView);


        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();


    }
    private void init(){

        //Payu.setInstance(getContext());

      //gethash();


    }
    @OnClick({R.id.pb_free, R.id.pb_partner, R.id.pb_continue})
    public void onOptionClick(View v) {


        if (v.getId() == pb_free.getId()) {
pb_title.setText("Lifetime Free Trial");
            pb_des.setText("*Plus Leads of other Brokers\n*Connect with App Users in your Locality\n*Match and Market your Listings with Brokers\n*Add new Buildings in Locality, get Site Visits");
           pb_free.setTextColor(ContextCompat.getColor(getContext(), R.color.greenish_blue));
            pb_partner.setTextColor(ContextCompat.getColor(getContext(), R.color.whitesmoke));
pb_continue.setText("Continue Lifetime Free Trial");
            //pb_free.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_button_bg_with_border));
            //pb_partner.setBackground(null);

        }
        else if (v.getId() == pb_partner.getId()) {
            //pb_partner.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.gradient_button_bg_with_border));
            //pb_free.setBackground(null);
            pb_title.setText("Exclusive Properties");
            pb_free.setTextColor(ContextCompat.getColor(getContext(), R.color.whitesmoke));
            pb_partner.setTextColor(ContextCompat.getColor(getContext(), R.color.greenish_blue));
pb_des.setText("*Assured 30 Site Visits per Month\n*Dedicated Oyeok Assistant Manager\n*Every Deal min: Rs 35,000\n*Limited Edition to 10 Brokers per Area");
            pb_continue.setText("Pay Rs. 50,000 for 3 Months");
        }
        else if (v.getId() == pb_continue.getId()) {
            if(pb_continue.getText().toString().equalsIgnoreCase("Pay Rs. 50,000 for 3 Months")){

                //startPayment();
               // makePayment();
                pay();
            }
        }


    }

    /*public void startPayment() {

        PaymentParams mPaymentParams = new PaymentParams();
        mPaymentParams.setKey("gtKFFx");
        mPaymentParams.setAmount("15.0");
        mPaymentParams.setProductInfo("Tshirt");
        mPaymentParams.setFirstName("Guru");
        mPaymentParams.setEmail("guru@gmail.com");
        mPaymentParams.setTxnId("0123479543689");
        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
    *//*    mPaymentParams.setUdf1(“udf1l”);
        mPaymentParams.setUdf2(“udf2”);
        mPaymentParams.setUdf3(“udf3”);
        mPaymentParams.setUdf4(“udf4”);
        mPaymentParams.setUdf5(“udf5”);*//*

        mPaymentParams.setHash("your payment hash");
        try {
            PostData postData = new PaymentPostParams(mPaymentParams, PayuConstants.CC).getPaymentPostParams();

        PayuConfig payuConfig = new PayuConfig();
        payuConfig.setEnvironment(PayuConstants.MOBILE_DEV_ENV);



        payuConfig.setData(postData.getResult());


        Intent intent = new Intent(getApplicationContext(), PayUBaseActivity.class);
        intent.putExtra(PayuConstants.PAYU_CONFIG,payuConfig);
            intent.putExtra(PayuConstants.PAYMENT_PARAMS,mPaymentParams);
            Log.i("TAG","payu "+mPaymentParams);
        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
      //Intent i = new Intent(PayUBaseActivity.class);

    }*/
    /*public void startPaymentRP() {

        Checkout checkout = new Checkout();


        checkout.setImage(R.drawable.digitslogo);


        final Activity activity = getActivity();


        try {
            JSONObject options = new JSONObject();


            options.put("name", "Ritesh Warke");
            JSONObject theme = new JSONObject();
            theme.put("color","#2dc4b6");
            options.put("theme",theme);
            checkout.setFullScreenDisable(true);


            options.put("description", "Paying for OYEOK Partner Broker Service");

            options.put("currency", "INR");



            options.put("amount", "100");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.i("Razorpay", "Error in starting Razorpay Checkout", e);
        }
    }*/

    private double getAmount() {


        Double amount = 10.0;


           // Toast.makeText(getContext(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;

    }

    public void pay(){
        String phone = "8882434664";
        String productName = "partner_broker";
        String firstName = General.getSharedPreferences(getContext(),AppConstants.NAME);
        String txnId = General.getSharedPreferences(getContext(),AppConstants.USER_ID) + System.currentTimeMillis();
        String email=General.getSharedPreferences(getContext(),AppConstants.EMAIL);
        /*String sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
        String fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";*/
        String sUrl = "https://oyeok.in";
        String fUrl = "https://oyeok.in";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = false;
        String key = "6n0lZhgF";
        String merchantId = "5703399" ;

        builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

//*//*.setProductName(productName)*//*
        builder.setAmount(getAmount())
                .setKey(key)
                .setTnxId(txnId)
                .setPhone(phone)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                //.setIsDebug(isDebug)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setProductName(productName)
                .setMerchantId(merchantId);


            /*String phone = "8882434664";
            String productName = "product_name";
            String firstName = "piyush";
            String txnId = "0nf7" + System.currentTimeMillis();
            String email="piyush.jain@payu.in";
            String sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
            String fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";
            String udf1 = "";
            String udf2 = "";
            String udf3 = "";
            String udf4 = "";
            String udf5 = "";
            boolean isDebug = true;
            String key = "dRQuiA";
            String merchantId = "4928174" ;

            PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();


            builder.setAmount(getAmount())
                    .setTnxId(txnId)
                    .setPhone(phone)
                    .setProductName(productName)
                    .setFirstName(firstName)
                    .setEmail(email)
                    .setsUrl(sUrl)
                    .setfUrl(fUrl)
                    .setUdf1(udf1)
                    .setUdf2(udf2)
                    .setUdf3(udf3)
                    .setUdf4(udf4)
                    .setUdf5(udf5)
                    .setIsDebug(isDebug)
                    .setKey(key)
                    .setMerchantId(merchantId);*/


        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();


        //paymentParam.setMerchantHash(hash);

        Log.i(TAG,"paymentParam "+paymentParam.getParams().toString());
      calculateServerSideHashAndInitiatePayment(paymentParam);
       // PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);


//             server side call required to calculate hash with the help of <salt>
//             <salt> is already shared along with merchant <key>
     /*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(dRQuiA|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||salt)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
*/

        // Recommended


        //calculateServerSideHashAndInitiatePayment(paymentParam);

//        testing purpose

       /* String salt = "";
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(MyActivity.this, paymentParam);*/
    }


    /*public void makePayment() {

        String phone = "";
        String productName = "partner_broker";
        String firstName = "piyush";
        String txnId = "oyeok" + System.currentTimeMillis();
        String email="test@payu.in";
        String sUrl = "https://test.payumoney.com/mobileapp/payumoney/success.php";
        String fUrl = "https://test.payumoney.com/mobileapp/payumoney/failure.php";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = true;
        String key = "6n0lZhgF";
        String merchantId = "5703399" ;

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();


        builder.setAmount(getAmount())
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(true)
                .setKey(key)
                .setMerchantId(merchantId);

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
        String salt = "";

        //String serverCalculatedHash = hashCal("dRQuiA|0nf7|10.0|productName|Ritesh|ritesh@oyeok.in||||||Qp1mhvgcSY");
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        Log.i(TAG,"serverCalculatedHash  "+serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);

//             server side call required to calculate hash with the help of <salt>
//             <salt> is already shared along with merchant <key>
     *//*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
*//*

        // Recommended
        /////calculateServerSideHashAndInitiatePayment(paymentParam);

//        testing purpose

       *//* String salt = "";
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(MyActivity.this, paymentParam);*//*


    }*/

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        //String url = "https://test.payumoney.com/payment/op/calculateHashForTest";
        String url = "https://test.hailyo.com/1/a/payu/hash";

       // Toast.makeText(getContext(), "Please wai Generating hash ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.i("app_activity", "Server calculated Hash jsonObject :  " + jsonObject.getJSONObject("responseData").getString("hash"));

                    String hash = jsonObject.getJSONObject("responseData").getString("hash");

                    paymentParam.setMerchantHash(hash);
                    PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);
                    /*if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);
                        } else {
                            Toast.makeText(getContext(),
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("app_activity", "Server calculated Hash jsonObject 1 :  " + e.getMessage());
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("app_activity", "Server calculated Hash jsonObject 1 VolleyError 1 :  " + error.networkResponse);
                Log.i("app_activity", "Server calculated Hash jsonObject 1 VolleyError 2 :  " + error.getNetworkTimeMs());
                Log.i("app_activity", "Server calculated Hash jsonObject 1 VolleyError 3 :  " + error);

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getContext(),
                            getActivity().getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

   /* private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(getContext(), "Plz wait..Generating hash", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);

                            PayUmoneySdkInitilizer.startPaymentActivityForResult(getActivity(), paymentParam);
                        } else {
                            Toast.makeText(getContext(),
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getContext(),
                            getActivity().getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("TAG","partner broker 2 "+requestCode+  " "+resultCode);
        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/


            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage("Payment Success Id : " + paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void gethash(){

        try {
            Log.i("percent","PayUHash called :");

            // {"user_id":"14dlr9x50bbuq1ptgkbb0lfh26f7y6no","long":"72","lat":"19","user_role":"broker","gcm_id":"gyani","oye_id":"xu4susi2y0852992","listing":"1"}
            PayUHash payUHash = new PayUHash();
            payUHash.setTxnid(General.getSharedPreferences(getContext(),AppConstants.USER_ID));
           payUHash.setEmail(General.getSharedPreferences(getContext(),AppConstants.EMAIL));
            payUHash.setFirstName(General.getSharedPreferences(getContext(),AppConstants.NAME));
            payUHash.setFURL("https://test.payumoney.com/mobileapp/payumoney/failure.php");
            payUHash.setSURL("https://test.payumoney.com/mobileapp/payumoney/success.php");
            payUHash.setKey("6n0lZhgF");
            payUHash.setPhone("8483");
            payUHash.setProductInfo("partner_broker");
            payUHash.setUdf1("");
            payUHash.setUdf2("");
            payUHash.setUdf3("");
            payUHash.setUdf4("");
            payUHash.setUdf5("");
           /* payUHash.setUdf6("");
            payUHash.setUdf7("");
            payUHash.setUdf8("");
            payUHash.setUdf9("");
            payUHash.setUdf10("");*/



            // g.setProperty_type(ptype.getText().toString());

            Gson gson = new Gson();
            String json = gson.toJson(payUHash);
            Log.i("magic","PayUHash  json "+json);

            Log.i("magic","PayUHash 1");
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_1).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            Log.i("magic","PayUHash 2");

            oyeokApiService.payUHash(payUHash, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, retrofit.client.Response response) {
                    try {
                        Log.i("magic", "PayUHash");
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                        JSONObject jsonResponse = new JSONObject(strResponse);

                        Log.i("magic", "PayUHash " + jsonResponse);


                        if (jsonResponse.getString("success").equalsIgnoreCase("false")) {



                                TastyToast.makeText(getContext(), jsonResponse.getJSONObject("responseData").getString("message"), TastyToast.LENGTH_LONG, TastyToast.WARNING);





                        } else {
                            Log.i("magic", "PayUHash success " + jsonResponse.getJSONObject("responseData"));
                            JSONObject j = jsonResponse.getJSONObject("responseData");
                            hash = j.getString("hash");
                            ammount = j.getString("price");
                            Log.i("magic", "PayUHash rollonar 1 " + jsonResponse.getJSONObject("responseData"));

                        }

                    }catch(Exception e){
                        Log.e("TAG", "Caught in the exception getLocality 1" + e.getMessage());

                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","PayUHash failed 2 "+error);
                    try {
                        SnackbarManager.show(
                                Snackbar.with(getContext())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Server Error: " + error.getMessage())
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }
                    catch(Exception e){}

                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the exception getLocality"+ e.getMessage());
        }

    }

}


