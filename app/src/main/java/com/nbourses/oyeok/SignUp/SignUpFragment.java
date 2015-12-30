package com.nbourses.oyeok.SignUp;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.UserProfileFirebase;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.LetsOye;
import com.nbourses.oyeok.RPOT.ApiSupport.models.MobileVerify;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.models.SignUp;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.OkBroker.UI.Ok_Broker_MainScreen;
import com.nbourses.oyeok.RPOT.OyeOkBroker.OyeIntentSpecs;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.RexMarkerPanelScreen;
import com.nbourses.oyeok.User.UserProfileViewModel;
import com.nbourses.oyeok.activity.MessagesFragment;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @Bind(R.id.submitprofile)
    Button submitBut;
    /* @Bind(R.id.edit) TextView edit;
     @Bind(R.id.etname) EditText name;
     @Bind(R.id.etemail) EditText email;
     @Bind(R.id.etnumber) EditText number;
     @Bind(R.id.etvcode)
     EditText vcode;*/
    /*@Bind(R.id.myphoto)
    ImageView myphoto;*/
    /*@Bind(R.id.chooseRoleHeader)
    TextView header;*/

    Dialog alertD;
    Context context;

    DBHelper dbHelper;

    String picturePath, mobile;
    private static final int SELECT_PHOTO = 1;
    GoogleCloudMessaging gcm;

    String regid, GCMID;
    String my_user_id;
    String PROJECT_NUMBER = "463092685367";
    TextView fbdata;
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
    private String firebaseUrl;
    EditText name,email,number,vcode;
    UserProfileFirebase userProfileFirebase;
    LinearLayout llsignup;
    LinearLayout llotp;
    String[] propertySpecification;
    Boolean redirectToOyeIntentSpecs=false;
    Bundle b;
    String lastFragment="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b=getArguments();
        if(b.getString("lastFragment")!=null)
        if(b.getString("lastFragment")!=null)
        lastFragment=b.getString("lastFragment");

        if(lastFragment.equals("OyeIntentSpecs")){
            Log.i("bundle_in",(b.getStringArray("propertySpecification"))[0]);
            redirectToOyeIntentSpecs=true;
            propertySpecification=b.getStringArray("propertySpecification");
        }
        View view = inflater.inflate(R.layout.fragment_sign_up,
                container, false);
        dbHelper=new DBHelper(getActivity());

        name= (EditText) view.findViewById(R.id.etname);
        email= (EditText) view.findViewById(R.id.etemail);
        number= (EditText) view.findViewById(R.id.etnumber);
        vcode= (EditText) view.findViewById(R.id.etvcode);
        llsignup = (LinearLayout)view.findViewById(R.id.llsignup);
        llotp = (LinearLayout)view.findViewById(R.id.llotp);
        llotp.setVisibility(View.GONE);

        firebaseUrl="https://resplendent-fire-6770.firebaseio.com/";
        userProfileViewModel=new UserProfileViewModel();
        Button sendOtp=(Button)view.findViewById(R.id.sendotp);
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                sendOtp();
            }
        });

        Button submit=(Button)view.findViewById(R.id.submitprofile);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
                submitButton();
            }
        });


        return view;

    }


    public void sendOtp(){



        Sname = name.getText().toString();
        Semail = email.getText().toString();
        Snumber = number.getText().toString();

        //dbHelper.save(DatabaseConstants.name,Sname);
        //dbHelper.save(DatabaseConstants.email,Semail);
        //dbHelper.save(DatabaseConstants.mobileNumber,Snumber);


        Log.i("captured number0 =",Snumber);
        validationCheck();
        validation_success = numberValidation();
        email_success = isEmailValid(Semail);

//        FirebaseClass.save(this,FirebaseClass.MY_SHORTMOBILE_KEY,""+Snumber);
        if(validation_success && email_success) {
            //UserCredentials.saveString(this, PreferenceKeys.MY_SHORTMOBILE_KEY, Snumber);
            userProfileViewModel.setName(Sname);
            userProfileViewModel.setEmailId(Semail);
            userProfileViewModel.setMobileNumber(Snumber);
            /*Str_Lat = UserCredentials.getString(this, PreferenceKeys.MY_CUR_LAT);    //FirebaseClass.getString(this,FirebaseClass.MY_CUR_LAT);
            Str_Lng = UserCredentials.getString(this, PreferenceKeys.MY_CUR_LNG);*/ //FirebaseClass.getString(this,FirebaseClass.MY_CUR_LNG);
            Str_Lat = "72.23";
            Str_Lng = "92.36";
            String API = "http://52.25.136.179:9000";

            User user = new User();
            user.setName(Sname);
            user.setEmail(Semail);
            user.setMobileNo(Snumber);
            user.setMobileCode("+91");
            user.setUserRole("Client");
            regid = userProfileViewModel.getGcmId();
            user.setGcmId(regid);
            user.setLongitude(Double.parseDouble(Str_Lng));
            user.setLatitude(Double.parseDouble(Str_Lat));
            user.setDeviceId("Hardware");


            /*user.setUserRole(dbHelper.getValue("userRole");
            regid = UserProfileViewModel.getGcmId();
            user.setGcmId(regid);
            user.setLongitude(Double.parseDouble(dbHelper.getValue("currentLng")));
            user.setLatitude(Double.parseDouble(dbHelper.getValue("currentLat")));
            user.setDeviceId(dbHelper.getValue("deviceId"));*/

            //User user = new User();
            //////////////////////////////////////////////////
            /*user.setName(Sname);
            user.setEmail(Semail);
            user.setMobileNo(Snumber);
            user.setMobileCode("+91");
            user.setUserRole(user_role);
            regid = UserCredentials.getString(context, UserCredentials.KEY_GCM_ID);*/
            regid="abhi";
            userProfileViewModel.setGcmId(regid);
            userProfileViewModel.setLng(Str_Lng);
            userProfileViewModel.setLat(Str_Lat);
            userProfileViewModel.setDeviceId("Hardware");
            //user.setDeviceId(FirebaseClass.getString(context,FirebaseClass.DEVICE_ID));

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            UserApiService user1 = restAdapter.create(UserApiService.class);
            llsignup.setVisibility(View.GONE);
            llotp.setVisibility(View.VISIBLE);

            if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")) {
                    user1.verifyMobile(user, new Callback<MobileVerify>() {
                        @Override

                        public void success(MobileVerify mobileVerify, retrofit.client.Response response) {
                            Log.i("TAG", "Inside Authentication success");
                            Toast.makeText(getContext(), "Authentication success",
                                    Toast.LENGTH_LONG).show();
                            //tv.setText(user.responseData.getUserId() + "hua");


                            Log.i("otp test", "My otp in  is:" + mobileVerify.getSuccess() + mobileVerify.responseData.getOtp());
                            otpReceived[0] = mobileVerify.responseData.getOtp();
                            //Svcode=mobileVerify.responseData.getOtp();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //tv.setText(error.getMessage());
                            Toast.makeText(getContext(), error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Log.i("TAG", "Inside authentication Failure");
                            Log.i("TAG", "error" + error.getMessage());
                        }
                    });
            }
            else{
                Toast.makeText(getContext(), "mobile verification in offline mode done",
                        Toast.LENGTH_LONG).show();
            }
            }
        }


    public void submitButton() {

        validationCheck();
        //validation_success = roleSelected();
        email_success = isEmailValid(Semail);
        context = getContext();
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(),0);



        if (validation_success && email_success) {

            Sname = name.getText().toString();
            Semail = email.getText().toString();

            Svcode = vcode.getText().toString();

            /*Log.i("error", "Sending post request");

            if (!Str_Lat.isEmpty() && !Str_Lng.isEmpty())
                sendPostRequest(subphone, "+91", Semail, Sname, user_role, regid, Str_Lng, Str_Lat, picturePath);
            else

                Toast.makeText(
                        getApplicationContext(),
                        "Please enable location services",
                        Toast.LENGTH_LONG).show();*/
            if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")) {
                if (otpReceived[0].equals(Svcode)) {
                    signup_success();
                    Log.i("", "Validation success");
                } else {
                /*Toast.makeText(
                        getContext(),
                        "Please Enter Otp as mentioned in the SMS"+Svcode,
                        Toast.LENGTH_LONG).show();*/
                }
            }
            else{
                Toast.makeText(
                        getContext(),
                        "otp validation in offline mode done",
                        Toast.LENGTH_LONG).show();
            }
        }}


    void signup_success() {


        /*TelephonyManager tm = (TelephonyManager) context.getSystemService();*/


        Log.i("inside","signup");
        String API="http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000";
        my_user_id = "icroi614g4su7pxts6p4w2nt7891jm4u";
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).setLogLevel(RestAdapter.LogLevel.FULL).build();
        OyeokApiService service;

        User user = new User();
        user.setMobileNo("Snumber");
        user.setMobileCode("+91");
        user.setEmail(Semail);
        user.setName(Sname);
        user.setUserRole("client");
        user.setGcmId(regid);
        user.setLongitude(Double.parseDouble(Str_Lng));
        user.setLatitude(Double.parseDouble(Str_Lat));
        user.setDeviceId("deviceId");

		/*user.setUserRole(dbHelper.getValue("userRole");
        regid = UserProfileViewModel.getGcmId();
        user.setGcmId(regid);
        user.setLongitude(Double.parseDouble(dbHelper.getValue("currentLng")));
        user.setLatitude(Double.parseDouble(dbHelper.getValue("currentLat")));
        user.setDeviceId(dbHelper.getValue("deviceId"));*/

        userProfileViewModel.setName(Sname);
        userProfileViewModel.setEmailId(Semail);
        userProfileViewModel.setDeviceId(my_user_id);

        if(dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")) {
            UserApiService user1 = restAdapter.create(UserApiService.class);
            user1.userSignUp(user, new Callback<SignUp>() {
                @Override
                public void success(SignUp signUp, retrofit.client.Response response) {
                    Log.i("TAG", "Inside signup success");
                    my_user_id=signUp.responseData.getUserId();
                    dbHelper.save(DatabaseConstants.userId, my_user_id);
                    Log.i("Firebase", userProfileViewModel.getUserProfile().toString());
                    userProfileFirebase=new UserProfileFirebase(firebaseUrl,my_user_id);
                    userProfileFirebase.setUserProfileValues(userProfileViewModel.getUserProfile());
                    if(dbHelper.getValue(DatabaseConstants.userRole).equals("Broker"))
                    {
                        dbHelper.save(DatabaseConstants.user,"Broker");
                    }
                    else
                        dbHelper.save(DatabaseConstants.user,"Client");
                    if(redirectToOyeIntentSpecs)
                    letsOye();

                    Fragment fragment = null;
                    if(lastFragment.equals("MainBroker"))
                        fragment = new Ok_Broker_MainScreen();
                    else{
                        fragment = new RexMarkerPanelScreen();
                    }

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();
                   // Toast.makeText(getContext(), "signup success", Toast.LENGTH_LONG).show();
                    /*if (redirectToOyeIntentSpecs)
                    {
                        Fragment fragment = null;
                        Bundle bundle=new Bundle();
                        bundle.putString("cameFrom","SignUp");
                        bundle.putStringArray("propertySpecification",propertySpecification);
                        fragment = new OyeIntentSpecs();
                        fragment.setArguments(bundle);
                        String title= "Oye Specifications";
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();
                    }*/

                }

                @Override
                public void failure(RetrofitError error) {

                    Log.i("TAG", "Inside signup Failure"+error.getMessage());
                    if (redirectToOyeIntentSpecs)
                    {
                        Fragment fragment = null;
                        Bundle bundle=new Bundle();
                        bundle.putString("cameFrom","SignUp");
                        bundle.putStringArray("propertySpecification",propertySpecification);
                        fragment = new OyeIntentSpecs();
                        fragment.setArguments(bundle);
                        String title= "Oye Specifications";
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();
                    }
                }
            });
        }
        else{
            Toast.makeText(getContext(), "signup success in offline mode", Toast.LENGTH_LONG).show();
        }



        //FirebaseClass.setAllValuesOfUserProfile("UserProfileViewModel", Snumber, my_user_id,"+91", Sname, Semail, user_role, regid, Str_Lng, Str_Lat);
//        FirebaseClass.setAllValuesOfUsers("Users", subphone, user_role, regid, Str_Lng, Str_Lat, userState);



        //FirebaseClass.setOyebookRecord(FirebaseClass.getString(context, FirebaseClass.MY_MOBILE_KEY), FirebaseClass.getString(context, FirebaseClass.rent), FirebaseClass.getString(context, FirebaseClass.sale), FirebaseClass.getString(context, FirebaseClass.show_property), FirebaseClass.getString(context, FirebaseClass.see_property), FirebaseClass.getString(context, FirebaseClass.budget), FirebaseClass.getString(context, FirebaseClass.bhk));
        //FirebaseClass.save(this, FirebaseClass.NAME_KEY, Sname);

        //FirebaseClass.save(this,FirebaseClass.EMAIL_KEY,Semail);

        //FirebaseClass.save(this,FirebaseClass.MY_ROLE_KEY,user_role);
        //UserProfileViewModel.saveString(ChooseRoleActivity.this, PreferenceKeys.MY_ROLE_KEY, user_role);
        //FirebaseClass.save(this,FirebaseClass.MY_MOBILE_KEY,subphone);

        // UserCredentials.saveString(ChooseRoleActivity.this, PreferenceKeys.PIC_PATH, picturePath);
        // Log.i(TAG,"My user id is:"+FirebaseClass.getString(context,FirebaseClass.MY_USER_ID));

        if (user_role.equalsIgnoreCase("client")) {
            Log.i("TAG", "Starting Client activity");


//            Intent NextActivity = new Intent(context, MainActivity.class);
//            startActivity(NextActivity);
//            finish();


            /*Intent returnIntent = new Intent();
            if(pass==0) {
                setResult(Activity.RESULT_OK, returnIntent);
            }else if(pass == 1)
            {
                setResult(Activity.RESULT_CANCELED, returnIntent);
            }*/


        }





    }

    public void letsOye()
    {
        Oyeok oyeOk = new Oyeok();
        oyeOk.setSpecCode(propertySpecification[2] + "-" + propertySpecification[1] + "-" + propertySpecification[4]);
        oyeOk.setReqAvl(propertySpecification[3]);
        oyeOk.setUserId(dbHelper.getValue(DatabaseConstants.userId));
        oyeOk.setUserRole(dbHelper.getValue(DatabaseConstants.userRole));
        oyeOk.setLong(SharedPrefs.getString(getActivity().getBaseContext(),SharedPrefs.MY_LNG));
        oyeOk.setLat(SharedPrefs.getString(getActivity().getBaseContext(), SharedPrefs.MY_LAT));
        oyeOk.setRegion(SharedPrefs.getString(getActivity().getBaseContext(), SharedPrefs.MY_REGION));
        oyeOk.setPincode(dbHelper.getValue(DatabaseConstants.pinCode));

        Log.i("UserId", "saved in DB");


        /*oyeOk.setSpecCode("LL-3BHK-9Cr");
        oyeOk.setReqAvl("req");
        oyeOk.setUserId("egtgxhr02ai31a2uzu82ps2bysljv43n");
        oyeOk.setUserRole("client");
        oyeOk.setLong("19");
        oyeOk.setLat("17");
        oyeOk.setRegion("powai");
        oyeOk.setPincode("400058");*/
        String off_mode = "NO";
        String API = "http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000";
        RestAdapter restAdapter1 = new RestAdapter.Builder().setEndpoint(API).build();
        restAdapter1.setLogLevel(RestAdapter.LogLevel.FULL);
        OyeokApiService oyeok = restAdapter1.create(OyeokApiService.class);
        oyeok.letsOye(oyeOk, new Callback<LetsOye>() {
            @Override
            public void success(LetsOye letsOye, retrofit.client.Response response) {
                //if(!off_mode.equals("yes")) {
                //String s = post(nameValuePairs);
                String s = letsOye.getResponseData();
                if (!s.equals("")) {
                    try {

                        if (s.equalsIgnoreCase("Your Oye is published")) {
                                /*FirebaseClass.setOyebookRecord(UserCredentials.getString(EnterConfigActivity.this, PreferenceKeys.MY_SHORTMOBILE_KEY), reNt, show, lng.toString(), lat.toString(), user_id, bhkval + "BHK", msg4, UserCredentials.getString(EnterConfigActivity.this, PreferenceKeys.CURRENT_LOC_KEY));
                                Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);
                                UserCredentials.saveString(context, PreferenceKeys.SUCCESSFUL_HAIL, "true");*/
                           // Toast.makeText(getContext(), "Oye published.Sit back and relax while we find a broker for you", Toast.LENGTH_LONG).show();
                            //finish();

                        } else if (s.equalsIgnoreCase("User already has an active oye. Pls end first")) {
                                /*Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);*/
                           // Toast.makeText(getContext(), "You already have an active oye. Pls end it first", Toast.LENGTH_LONG).show();
                            //finish();
                        } else

                        {
                                /*Intent NextActivity = new Intent(context, MainActivity.class);
                                startActivity(NextActivity);*/
                           // Toast.makeText(getContext(), "There is some error.", Toast.LENGTH_LONG).show();
                            //finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                ((MainActivity) getActivity()).changeFragment(new MessagesFragment(), null);
                /*}else
                {
                    *//*Intent NextActivity = new Intent(context, MainActivity.class);
                    startActivity(NextActivity);*//*
                    Toast.makeText(getContext(), "In offline mode.Done", Toast.LENGTH_LONG).show();
                    //finish();
                }*/
            }

            @Override
            public void failure(RetrofitError error) {
               /* Toast.makeText(getContext(), "lets oye call failed in enter config",
                        Toast.LENGTH_LONG).show();*/
                // FirebaseClass.setOyebookRecord(UserCredentials.getString(EnterConfigActivity.this,PreferenceKeys.MY_SHORTMOBILE_KEY),reNt,show,lng,lat,user_id,bhkval+"BHK",msg4,UserCredentials.getString(EnterConfigActivity.this,PreferenceKeys.CURRENT_LOC_KEY));
                //Intent NextActivity = new Intent(context, MainActivity.class);
                //startActivity(NextActivity);finish();
                Log.i("TAG", "lets oye call failed in enter config");
                Log.i("TAG", "inside error" + error.getMessage());
            }
        });
    }




    private boolean isEmailValid(CharSequence email) {
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else
        {
            //Toast.makeText(context,"Please enter a valid email address",Toast.LENGTH_LONG).show();
            Toast.makeText(
                    getContext(),
                    "Please enter a valid email address",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean numberValidation(){
        if(Snumber.length()==10)
            return true;
        else{
            Toast.makeText(getContext(),"please entera valid mobile number",Toast.LENGTH_LONG).show();

            return false;
        }
    }


    private void validationCheck() {

        if (name.getText().toString().trim().equalsIgnoreCase("")) {
            name.setError("Please enter name");
            return;
        }


        Semail = email.getText().toString();

        if (email.getText().toString().trim().equalsIgnoreCase("")) {
            email.setError("Please enter email-id");
            return;
        }

        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name.setError(null);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                name.setError(null);

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





}
