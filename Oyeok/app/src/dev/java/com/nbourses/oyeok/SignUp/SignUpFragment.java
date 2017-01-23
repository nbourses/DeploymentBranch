package com.nbourses.oyeok.SignUp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
//import com.nbourses.oyeok.Firebase.UserProfileFirebase;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.SignUp;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.AcceptOkCall;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OnAcceptOkSuccess;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.User.UserProfileViewModel;
import com.nbourses.oyeok.activities.BrokerDealsListActivity;
import com.nbourses.oyeok.activities.BrokerMainActivity;
import com.nbourses.oyeok.activities.BrokerMap;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.HalfDeals;
import com.nbourses.oyeok.realmModels.UserInfo;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpFragment extends Fragment implements OnAcceptOkSuccess {
    private static final String TAG = "SignUpFragment";
    private DigitsAuthButton digitsButton;
    @Bind(R.id.submitprofile)
    Button submitBut;
    private FrameLayout fragment_container1;
    private LinearLayout supportChat;
    private ListView listViewDeals;
    private Boolean redirectBroker = false;
    private Boolean redirectClient = false;
    private static final int RESULT_LOAD_IMAGE = 1;
    Dialog alertD;
    Context context;
    //DBHelper dbHelper;
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    String picturePath, mobile;
    private static final int SELECT_PHOTO = 1;
    GoogleCloudMessaging gcm;
    private FragmentDrawer drawerFragment;
    String regid, GCMID;
    String my_user_id;
    String jsonArray="";
    JSONArray p;
    String PROJECT_NUMBER = "463092685367";
    TextView fbdata;
    Button already_registered_tab,new_user_tab;
    Boolean okBroker=false;
    Boolean success = false, is_role_selected=false, validation_success, email_success;
    String subphone=null;
    LocationManager mLocationManager;
    //SendLocationUpdate sendLocationUpdate;
    //LocationResult locationResult;
    UserProfileViewModel userProfileViewModel;
    String Str_Lng="",Str_Lat="",shortPhone,pnum,Sname="",Semail="",Snumber="",Svcode="",user_role="client";
    private String userState;
    private int activityCounter = 0;
    private int pass = 50;
    //final String[] otpReceived = {""};
    //String otpReceived;
    final String[] otpReceived = {""};
    //private String firebaseUrl;
    EditText name,email,number,vcode;
//    UserProfileFirebase userProfileFirebase;
    LinearLayout llsignup;
    LinearLayout llotp;
    String[] propertySpecification;
    Boolean redirectToOyeIntentSpecs=false;
    Boolean newUser=true;
    Bundle b;
    TextView tvcontent,tvheading;
    String lastFragment="";
    ImageView editProfile_pic;
    private String role_of_user;
    private Activity activity;
    private Button submit;
    ImageView profile_pic;
    private String oldRole,UserOldRole,reqRole;
    private Realm myRealm;
    ////////////////////////////////////////////////////
    // Variables defined for digits authentication
////////////////////////////////////////////////////
    private AuthCallback authCallback;
    private String mobile_number="";
    private static final String TWITTER_KEY = "CE00enRZ4tIG82OJp6vKib8YS";
    private static final String TWITTER_SECRET = "5AMXDHAXG0luBuuHzSrDLD0AvwP8GzF06klXFgcwnzAVurXUoS";
    HashMap<String, Float> listings = new HashMap<String, Float>();
    UserInfo userInfo = new UserInfo();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppConstants.SIGNUP_FLAG = true;
        myRealm = General.realmconfig(getContext());
        b=getArguments();
        redirectToOyeIntentSpecs=false;
        okBroker=false;
        Log.i("signup fragment","building listings are "+listings);
        String[] bNames = new String[3];
        int[] bPrice = new int[3];

         try {
             // try catch to handle direct signup from deals button when building data not available
             bNames = b.getStringArray("bNames");
             bPrice = b.getIntArray("bPrice");
             Log.i("Listings are", "marol bNames " + bNames + "bPrice " + bPrice);
             listings.put(bNames[0], (float) bPrice[0]);
             listings.put(bNames[1], (float) bPrice[1]);
             listings.put(bNames[2], (float) bPrice[2]);
             Log.i("Listings are ","marol listings" +listings);
         }
         catch(Exception e){
             Log.i(TAG,"caught in exception listings not reached signup");

         }
//        if(b.getString("lastFragment")!=null)
            if(b.getString("lastFragment")!=null)
                lastFragment=b.getString("lastFragment");
        Log.i(TAG, "lastFragment "+lastFragment);
        View view1 =  inflater.inflate(R.layout.activity_deals_list, container, false);
        fragment_container1 = (FrameLayout) view1.findViewById(R.id.fragment_container1);
        supportChat = (LinearLayout) view1.findViewById(R.id.supportChat);
        listViewDeals = (ListView) view1.findViewById(R.id.listViewDeals);
        if(lastFragment.equalsIgnoreCase("brokerDrawer")|| lastFragment.equalsIgnoreCase("okyed") || lastFragment.equalsIgnoreCase("brokerDeal") || lastFragment.equalsIgnoreCase("brokerIntro")||lastFragment.equalsIgnoreCase("brokermap"))
                okBroker = true;
            Log.i("Signup called =", "view assigned");
            View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
            //dbHelper=new DBHelper(getActivity());
            Log.i("Signup called =", "view assigned");
            Digits.getSessionManager().clearActiveSession();
            authCallback = new AuthCallback() {
                @Override
                public void success(DigitsSession session, String phoneNumber) {
                    // Do something with the session
                    Log.i(TAG, "isValidUser " + session.isValidUser());
                    Log.i(TAG, "phoneNumber " + phoneNumber);
                    Log.i(TAG, "getPhoneNumber " + session.getPhoneNumber());
                    mobile_number = session.getPhoneNumber();
                    Log.i(TAG,"mobile number is the 2 "+mobile_number);

                    if(mobile_number.isEmpty()){
                        SnackbarManager.show(
                                Snackbar.with(context)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Currently We are experiencing issues with sign ups, Please try again later.")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                    }
                    else{
                        submitButton();
                    }
                }

                @Override
                public void failure(DigitsException exception) {
                    // Do something on failure
                    exception.printStackTrace();
                }
            };

        digitsButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
        digitsButton.setText("sign me up");
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        already_registered_tab=(Button) view.findViewById(R.id.already_registered_tab);
        new_user_tab=(Button) view.findViewById(R.id.new_user_tab);
        name= (EditText) view.findViewById(R.id.etname);
        email= (EditText) view.findViewById(R.id.etemail);
        submit=(Button)view.findViewById(R.id.submitprofile);
        tvheading= (TextView) view.findViewById(R.id.tvheading);
        tvcontent= (TextView) view.findViewById(R.id.tvcontent);
        editProfile_pic = (ImageView) view.findViewById(R.id.editProfile_pic);
        /*  if(okBroker==false && AppConstants.CURRENT_USER_ROLE.equalsIgnoreCase("client"))*/
        Log.i(TAG,"last fragment narcos "+lastFragment);
        
        if(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")){
            tvheading.setText(R.string.client_sign_up_heading);
            submit.setText("REGISTER AS A CUSTOMER");
            if(lastFragment.equalsIgnoreCase("clientDeal"))
                tvcontent.setText(R.string.client_sign_up_content_from_deals);
            else if (lastFragment.equalsIgnoreCase("clientIntro"))
                tvcontent.setText(R.string.client_sign_up_content_from_intro);
            else
                tvcontent.setText(R.string.client_sign_up_content);
        }
        else
        {
            tvheading.setText(R.string.broker_sign_up_heading);
            submit.setText("REGISTER AS A BROKER");
            if(lastFragment.equalsIgnoreCase("brokerDeal"))
                tvcontent.setText(R.string.broker_sign_up_content_from_deals);
            else if (lastFragment.equalsIgnoreCase("brokerIntro"))
                tvcontent.setText(R.string.broker_sign_up_content_from_intro);
            else
                tvcontent.setText(R.string.broker_sign_up_content);
        }

        editProfile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                profile_pic.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        already_registered_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser=false;
                name.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                already_registered_tab.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_greenish_blue));
                new_user_tab.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_box));
                Log.i(TAG,"last fragment narcos 2 "+okBroker);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                if(okBroker==false && General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                    tvheading.setText(R.string.client_log_in_heading);
                    submit.setText("LOGIN as a Customer");
                    if(lastFragment.equalsIgnoreCase("clientDeal"))
                        tvcontent.setText(R.string.client_sign_up_content_from_deals);
                    else if (lastFragment.equalsIgnoreCase("clientIntro"))
                        tvcontent.setText(R.string.client_sign_up_content_from_intro);
                    else
                        tvcontent.setText(R.string.client_sign_up_content);
                }
                else
                {
                    submit.setText("LOGIN as a Broker");
                    tvheading.setText(R.string.broker_log_in_heading);
                    if(lastFragment.equalsIgnoreCase("brokerDeal"))
                        tvcontent.setText(R.string.broker_sign_up_content_from_deals);
                    else if (lastFragment.equalsIgnoreCase("brokerIntro"))
                        tvcontent.setText(R.string.broker_sign_up_content_from_intro);
                    else
                        tvcontent.setText(R.string.broker_sign_up_content);
                }
            }
        });

        new_user_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser=true;
                name.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                already_registered_tab.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_box));
                new_user_tab.setBackground(getContext().getResources().getDrawable(R.drawable.gradient_greenish_blue));
                if(okBroker==false && General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                    submit.setText("REGISTER AS a CUSTOMER");
                    tvheading.setText(R.string.client_sign_up_heading);
                    if(lastFragment.equalsIgnoreCase("clientDeal"))
                        tvcontent.setText(R.string.client_sign_up_content_from_deals);
                    else if (lastFragment.equalsIgnoreCase("clientIntro"))
                        tvcontent.setText(R.string.client_sign_up_content_from_intro);
                    else
                        tvcontent.setText(R.string.client_sign_up_content);
                }
                else
                {
                    Log.i("broker121","broker site ");
                    submit.setText("REGISTER AS a BROKER");
                    tvheading.setText(R.string.broker_sign_up_heading);
                    if(lastFragment.equalsIgnoreCase("brokerDeal"))
                        tvcontent.setText(R.string.broker_sign_up_content_from_deals);
                    else if (lastFragment.equalsIgnoreCase("brokerIntro"))
                        tvcontent.setText(R.string.broker_sign_up_content_from_intro);
                    else
                        tvcontent.setText(R.string.broker_sign_up_content);
                }


            }
        });

        Sname = name.getText().toString();
        Semail = email.getText().toString();
        Log.i("TRACE", "inside submit");
        Log.i("TRACE", "after validationCheck");
        context = getContext();
        if (!Sname.matches("")) {

            if (!Semail.matches("")){

                submit.setVisibility(View.INVISIBLE);
                digitsButton.setVisibility(View.VISIBLE);
            }

        }
        llsignup = (LinearLayout)view.findViewById(R.id.llsignup);
        llotp = (LinearLayout)view.findViewById(R.id.llotp);

        role_of_user=General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER);

        Log.i("signupstatus","General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER)   "+General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER)+ "   "+role_of_user);
        SnackbarManager.show(
                Snackbar.with(getActivity())
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("Signing up as " + role_of_user)
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

        userProfileViewModel=new UserProfileViewModel();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                Log.i("TRACE", "inside submitprofile button");
                Log.i(TAG,"mobile number is the 1 "+mobile_number);
                Log.i("TRACE inside sb","mobile_number");
                Log.i("TRACE inside sb",""+mobile_number);
//                mobile_number="";
                Sname = name.getText().toString();
                Semail = email.getText().toString();
                Log.i("TRACE", "inside submit");

                Log.i("TRACE", "after validationCheck");
                // //validation_success = roleSelected()
                if(newUser==true) {
                    validationCheck();
                    context = getContext();
                    if (Sname.matches("")) {
                        SnackbarManager.show(
                                Snackbar.with(activity)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text("Please enter name.")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), activity);
                        return;
                    } else
                        email_success = isEmailValid(Semail);

                    if (email_success){
                        Digits.getSessionManager().clearActiveSession();
                        Digits.authenticate(authCallback, R.style.CustomDigitsTheme);
                    }
                }else {
//                    Log.i("mobile no before dc", mobile_number);
                    Digits.getSessionManager().clearActiveSession();
                    Digits.authenticate(authCallback, R.style.CustomDigitsTheme);

                    if (!mobile_number.isEmpty() && email_success) {
                        Log.i("mobile no before dc", mobile_number);
                        Log.i("mobile no after dc", mobile_number);
                        Log.i("TRACE", "in nomail ntempty");
                        userProfileViewModel.setName(Sname);
                        userProfileViewModel.setEmailId(Semail);
                        userProfileViewModel.setMobileNumber(mobile_number);
                        Str_Lat = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT);
                        Str_Lng = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG);
                        Log.i("TRACE", "Lat is" + Str_Lat);
                        Log.i("TRACE", "Long is" + Str_Lng);
                        String API = DatabaseConstants.serverUrl;
                        User user = new User();
                        user.setName(Sname);
                        user.setEmail(Semail);
                        user.setMobileNo(mobile_number);
                        user.setMobileCode("+91");
                        if (okBroker)
                            user.setUserRole("broker");
                        else
                            user.setUserRole("client");
                        regid = userProfileViewModel.getGcmId();
                        user.setPushToken(regid);
                        user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
                        user.setLongitude(Str_Lng);
                        user.setLocality(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
                        user.setLatitude(Str_Lat);
                        user.setPlatform("android");
                        user.setDeviceId("Hardware");
                        regid = SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID);
                        userProfileViewModel.setGcmId(regid);
                        userProfileViewModel.setLng(Str_Lng);
                        userProfileViewModel.setLat(Str_Lat);
                        userProfileViewModel.setDeviceId("Hardware");
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(API).build();
                        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
                        UserApiService user1 = restAdapter.create(UserApiService.class);
                    }
                }

            }
        });
        return view;

    }


    public void submitButton() {

        submit.setEnabled(false);
        submit.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
        submit.setText("Registering...");

      AppConstants.REGISTERING_FLAG=true;
           signup_success();

    }


    void signup_success() {
        if (General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI).equals("")) {
            General.setSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI, String.valueOf(System.currentTimeMillis()));
            Log.i("TIMESTAMP", "millis " + System.currentTimeMillis());
        }
        /*TelephonyManager tm = (TelephonyManager) context.getSystemService();*/
        Log.i("TRACE","in SinSuc");
        Log.i("inside","signup");
        //String API="http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000"
        String API = AppConstants.SERVER_BASE_URL_11;
        my_user_id = "icroi614g4su7pxts6p4w2nt7891jm4u";
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).setLogLevel(RestAdapter.LogLevel.FULL).build();
        OyeokApiService service;
        Log.i(TAG,"mobile number is the "+mobile_number);
        User user = new User();
        user.setMobileNo(mobile_number);
        user.setMobileCode("+91");
        user.setEmail(Semail);
        user.setDemoId(General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));
        user.setName(Sname);
        if(okBroker)
            user.setUserRole("broker");
        else
            user.setUserRole("client");
        user.setPushToken(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        user.setDeviceId("hardware");
        user.setPlatform("android");
        if(!SharedPrefs.getString(getActivity(),SharedPrefs.MY_LOCALITY).equalsIgnoreCase("")) {
            user.setLocality(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
        }
        else {
            user.setLocality("Mumbai");
        }

        Log.i(TAG,"fakat 5 "+user.getUserRole() + okBroker);
        Log.i("TAG", "role before signup call "+user.getUserRole() +okBroker);
        Log.i("TAG", "role before signup call "+user.getMobileCode());
        Log.i("TAG", "role before signup call "+user.getMobileNo());
        Log.i("TAG", "role before signup call "+user.getEmail());
        Log.i("TAG", "role before signup call "+user.getName());
        Log.i("TAG", "role before signup call "+user.getPushToken());
        Log.i("TAG", "role before signup call "+user.getGcmId());
        Log.i("TAG", "role before signup call "+user.getLatitude());
        Log.i("TAG", "role before signup call "+user.getLongitude());
        Log.i("TAG", "role before signup call "+user.getDeviceId());
        Log.i("TAG", "role before signup call "+user.getPlatform());
        Log.i("TAG", "role before signup call "+user.getLocality());

        userProfileViewModel.setName(Sname);
        userProfileViewModel.setEmailId(Semail);
        userProfileViewModel.setDeviceId(my_user_id);
        General.setSharedPreferences(getContext(),AppConstants.NAME,Sname); //necessary to get name for default deal



        if(General.isNetworkAvailable(getContext())) {
            General.slowInternet(getContext());
//                  if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
            try {
                UserApiService user1 = restAdapter.create(UserApiService.class);
                user1.userSignUp(user, new Callback<SignUp>() {
                    @Override
                    public void success(SignUp signUp, Response response) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.i("TAG", "Inside signup success");
                        Log.i(TAG,"fakata responsedata "+signUp.responseData.getMessage());
                        try {
                            my_user_id = signUp.responseData.getUserId();
                            Log.i(TAG,"fakata user id "+my_user_id);
                            Log.i(TAG, "fakata email " + signUp.responseData.getEmail());
                            if (!signUp.responseData.getName().equalsIgnoreCase("null")) {
                                Log.i(TAG, "fakata name " + signUp.responseData.getName());
                                General.setSharedPreferences(getContext(), AppConstants.NAME, signUp.responseData.getName());
                                //dbHelper.save(DatabaseConstants.name, signUp.responseData.getName());
                            }
                            if (!signUp.responseData.getEmail().equalsIgnoreCase("null")) {
                                Log.i(TAG, "fakata name " + signUp.responseData.getEmail());
                                General.setSharedPreferences(getContext(), AppConstants.EMAIL, signUp.responseData.getEmail());
                               // dbHelper.save(DatabaseConstants.email, signUp.responseData.getEmail());
                            }
                           // Log.i("TAG","fakata name 12 "+dbHelper.getValue(DatabaseConstants.name));
                          //  Log.i("TAG","fakata email 12 "+dbHelper.getValue(DatabaseConstants.email));
                            Log.i("TAG","fakata email 13 "+General.getSharedPreferences(getContext(), AppConstants.NAME));
                            Log.i("TAG","fakata email 13 "+General.getSharedPreferences(getContext(), AppConstants.EMAIL));
                        }catch(Exception e){
                            Log.i(TAG,"caught in exception sign up success fakata 14 "+e);
                        }
                        //  String ab = signUp.getError();
                        if(signUp.getError().equals(2)){
                            SnackbarManager.show(
                                    Snackbar.with(getActivity())
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text(signUp.responseData.getMessage())
                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), activity);
                            if(signUp.responseData.getMessage().contains("client")){
                                oldRole = "client";
                                UserOldRole="Customer";
                                reqRole="Broker";
                                okBroker = false;
                                role_of_user = "client";
                                General.setSharedPreferences(getContext(),AppConstants.ROLE_OF_USER,"client");
                                //dbHelper.save(DatabaseConstants.userRole, "Client");
                                Log.i(TAG,"fakat 1 "+General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
                                userInfo.setUserRole("client");
                            }  else{
                                oldRole = "broker";
                                UserOldRole="broker";
                                reqRole="Customer";
                                okBroker = true;
                                role_of_user = "broker";
                                General.setSharedPreferences(getContext(),AppConstants.ROLE_OF_USER,"broker");
                                //dbHelper.save(DatabaseConstants.userRole, "Broker");
                                Log.i(TAG,"fakat 2 "+General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
                                userInfo.setUserRole("broker");
                            }

                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                           // builder.set
                            builder.setMessage("You are already registered as a " +UserOldRole+ " with this Mobile Number.\nPlease signup as a "+reqRole+" from Another Mobile Number on another Device.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            Log.i(TAG,"yoyoyo11 "+oldRole +" "+redirectClient);
                                            if(oldRole.equalsIgnoreCase("broker")){
                                                Log.i(TAG,"fakat 3 "+oldRole);
                                                Log.i(TAG,"yo man pro ");
                                                redirectBroker = true;
                                                okBroker = true;
                                            }
                                            if(oldRole.equalsIgnoreCase("client")){
                                                Log.i(TAG,"fakat 4 "+oldRole);
                                                Log.i(TAG,"yo man pro no man ");
                                                redirectClient = true;
                                                okBroker = false;
                                                Log.i(TAG,"yoyoyo12 "+oldRole +" "+redirectClient);
                                            }
                                            signup_success();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            dialog.cancel();
                                            Intent intent = new Intent(getContext(), ClientMainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK)       ;
                                            startActivity(intent);
                                            AppConstants.REGISTERING_FLAG=false;
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
                            Log.i("TRACE", "message is chal "+signUp.responseData.getMessage());
                            submit.setEnabled(true);
                            submit.setBackgroundColor(ContextCompat.getColor(context, R.color.greenish_blue));
                            /* submit.setText("DONE");*/
                            submit.setText("Login/Register");
                            return;
                        }else if(signUp.getError().equals(1)||signUp.getError().equals(3)||signUp.getError().equals(4)||signUp.getError().equals(8)) {
                            Log.i("bhagana","signUp.responseData.getMessage()  : "+signUp.responseData.getMessage());
                            SnackbarManager.show(
                                    Snackbar.with(getContext())
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text(signUp.responseData.getMessage())
                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), activity);
                            submit.setEnabled(true);
                            submit.setBackgroundColor(ContextCompat.getColor(context, R.color.greenish_blue));
                            submit.setText("Login/Register");
//                                      AppConstants.REGISTERING_FLAG=false;
                            return;
                        }
                        try {
                            Realm myRealm = General.realmconfig(getContext());
                            if (myRealm.isInTransaction()) {
                                myRealm.cancelTransaction();
                            }
                            myRealm.beginTransaction();
                            RealmResults<HalfDeals> results1 =
                                    myRealm.where(HalfDeals.class).findAll();
                            results1.clear();
                            myRealm.commitTransaction();
                        }
                        catch(Exception e){
                            Log.i(TAG,"Caught in exception deleting cached deals after signup");
                        }
                        Log.i("TRACE", "Userid" +my_user_id);
                        //store user_id in shared preferences
                        General.setSharedPreferences(getActivity(), AppConstants.USER_ID, my_user_id);
                        General.setSharedPreferences(context, AppConstants.IS_LOGGED_IN_USER, "yes");
                        General.setSharedPreferences(context, AppConstants.ROLE_OF_USER, role_of_user.toLowerCase());
                        try {
                            userInfo.setUserId(my_user_id);
                            userInfo.setIsLoggedInUser("yes");
                            userInfo.setUserRole(role_of_user.toLowerCase());
                            userInfo.setName(Sname);
                            userInfo.setEmailId(Semail);
                            userInfo.setMobileNumber(mobile_number);
                            userInfo.setLat(General.getSharedPreferences(getContext(), AppConstants.MY_LAT));
                            userInfo.setLng(General.getSharedPreferences(getContext(), AppConstants.MY_LNG));
                            userInfo.setLocality(General.getSharedPreferences(getContext(), AppConstants.LOCALITY));
                            myRealm.beginTransaction();
                            UserInfo users = myRealm.copyToRealmOrUpdate(userInfo);
                            myRealm.commitTransaction();
                            RealmResults<UserInfo> results1 =
                                    myRealm.where(UserInfo.class).findAll();
                            for (UserInfo c : results1) {
                                Log.i(TAG, "insiderr2 ");
                                Log.i(TAG, "insiderr3 " + c.getName());
                                Log.i(TAG, "insiderr4 " + c.getEmailId());
                            }
                        }
                        catch(Exception e){
                            Log.i(TAG,"Caught in exdception UseerInfo Realm "+e );
                        }
                        Log.i("TRACE", "bef saveDb");
                       // dbHelper.save(DatabaseConstants.userId, my_user_id);
                        SharedPrefs.save(getActivity(), "UserId", my_user_id);
                        General.setSharedPreferences(getContext(),AppConstants.USER_ID,my_user_id);
                       // dbHelper.save(DatabaseConstants.name, Sname);
                       // dbHelper.save(DatabaseConstants.email,Semail);
                       // dbHelper.save(DatabaseConstants.mobileNumber,mobile_number);
                        General.setSharedPreferences(getContext(), AppConstants.MOBILE_NUMBER,mobile_number);
                       /* if (dbHelper.getValue(DatabaseConstants.userRole).equals("Broker")) {
                            dbHelper.save(DatabaseConstants.user, "Broker");
                        } else
                            dbHelper.save(DatabaseConstants.user, "Client");*/
                        try {  // clear unregistered deals from realm
                            Realm myRealm = General.realmconfig(getContext());
                            myRealm.beginTransaction();
                            RealmResults<HalfDeals> result = myRealm.where(HalfDeals.class).findAll();
                            result.clear();
                        }catch(Exception e){
                            Log.i(TAG,"Caught in the exception clearing deals after signup "+e );
                        }
                        finally{
                            myRealm.commitTransaction();
                        }
                        Log.i(TAG,"lastfragment "+lastFragment);
                        if(redirectBroker){
                            Intent intent = new Intent(getContext(),BrokerMainActivity.class);
                            intent.putExtra("userRole", "broker");
                            //intent.putExtra("default_deal_flag",true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            AppConstants.SIGNUP_FLAG =false;
                        }
                        else if(redirectClient){
                            Intent intent = new Intent(getContext(),ClientMainActivity.class);
                            intent.putExtra("userRole", "client");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            AppConstants.SIGNUP_FLAG =false;
                            //intent.putExtra("default_deal_flag",true);
                            startActivity(intent);
                        }
                        else if(lastFragment.equalsIgnoreCase("clientDrawer") || lastFragment.equalsIgnoreCase("clientDeal")||lastFragment.equalsIgnoreCase("clientIntro")){
                            Log.i(TAG,"lastfragment 1 "+lastFragment);
                            Intent intent = new Intent(getContext(),ClientMainActivity.class);
                            intent.putExtra("userRole", "client");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            AppConstants.SIGNUP_FLAG =false;
                        }
                        else if(lastFragment.equalsIgnoreCase("brokerDrawer")|| lastFragment.equalsIgnoreCase("brokerDeal")||lastFragment.equalsIgnoreCase("brokerIntro")){
                            Log.i(TAG,"lastfragment 2 "+lastFragment);
                            Intent intent = new Intent(getContext(),BrokerMainActivity.class);
                            intent.putExtra("userRole", "broker");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            AppConstants.SIGNUP_FLAG =false;
                        }
                        else if(lastFragment.equalsIgnoreCase("oyed")){

                            General.publishOye(getActivity());
                        }
                        else if(lastFragment.equalsIgnoreCase("okyed")){
                            Log.i(TAG,"prasanna 1");
                            jsonArray=b.getString("JsonArray");
                            try {
                                p=new JSONArray(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int j=b.getInt("Position");
                            Log.i(TAG,"c prasanna 2 " +j);
                            AcceptOkCall a = new AcceptOkCall();
                            a.setmCallBack(SignUpFragment.this);
                            a.acceptOk(listings,p,j,getActivity());
                        }else if(lastFragment.equalsIgnoreCase("brokermap")){

                            ((BrokerMap)getActivity()).CloseSignUP();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        submit.setEnabled(true);
                        submit.setBackgroundColor(ContextCompat.getColor(context, R.color.greenish_blue));
                        submit.setText("Login/Register");
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.i("TRACE","in signup failure");
                        Log.i("TRACE", "Inside signup Failure" + error);
                        Log.i("TAG", "Inside signup Failure" + error.getMessage());
                        if (redirectToOyeIntentSpecs) {
                            /* Fragment fragment = null;

                                      Bundle bundle = new Bundle();
                                      bundle.putString("cameFrom", "SignUp");
                                      bundle.putStringArray("propertySpecification", propertySpecification);
                                      fragment = new OyeIntentSpecs();
                                      fragment.setArguments(bundle);
                                      String title = "Oye Specifications";
                                      FragmentManager fragmentManager = getFragmentManager();
                                      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                      fragmentTransaction.replace(R.id.container_map, fragment);

                                      fragmentTransaction.commit();*/
                        }
                    }
                });
            }catch (Exception e){
                Log.i("Exception","caught in sign up");
            }
        }else{

            General.internetConnectivityMsg(getContext());
        }

    }

    public interface ShowChatList
    {
        public void showChatList();

    }

    public void letsOye()
    {
        Log.d(TAG, "start letsOye");
        Log.i("TRACE", "in LetsOye");

        if (role_of_user.equalsIgnoreCase("client")) {
            Log.i("TRACE", "in Client");
            General.publishOye(getActivity());

        }
        else {
            Log.i("TRACE", "in Broker");
            //open deals listing of broker
            Intent openBrokerDealsListing = new Intent(context, BrokerDealsListActivity.class);
            openBrokerDealsListing.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(openBrokerDealsListing);
        }

    }
    private boolean isEmailValid(CharSequence email) {



      Log.i("TRACE","in isEmailvalid");
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else
        {
            SnackbarManager.show(
                    Snackbar.with(activity)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("Please enter a valid email address")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), activity);
            return false;
        }
    }

    private boolean numberValidation(){


        if(mobile_number.length()==10)
            return true;
        else{
            SnackbarManager.show(
                    Snackbar.with(activity)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("Please enter a valid mobile number")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), activity);
            return false;
        }
    }


    private void validationCheck() {
        Log.i("TRACE","inside validCheck");
        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("Please enter name");
            Log.i("TRACE", "Plz enter name");
            return;
        }
        Semail = email.getText().toString();

        if (email.getText().toString().trim().equalsIgnoreCase("")) {
            email.setError("Please enter email-id");
            Log.i("TRACE", "Plz enter email");
            return;
        }
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // name.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // name.setError(null);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // email.setError(null);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                //email.setError(null);
                //isEmailValid(Semail);
            }
        });
    }

    private boolean isNetworkAvailable() {
       Log.i("TRACE","inside netAvail");
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void replaceFragment(Bundle args) {

         /* Log.i("TRACE","in ReplaceFrag");

         /*     Fragment fragment = new Droom_Chat_New();
         fragment.setArguments(args);
         FragmentManager fragmentManager = getFragmentManager();
         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
         fragmentTransaction.replace(R.id.container_map, fragment);
         fragmentTransaction.commitAllowingStateLoss();*/
     }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode != 0) {
            Uri mImageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
                if (getOrientation(getContext(), mImageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getContext(), mImageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix,true);
                    profile_pic.setImageBitmap(rotateImage);
                } else
                    profile_pic.setImageBitmap(Image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}
