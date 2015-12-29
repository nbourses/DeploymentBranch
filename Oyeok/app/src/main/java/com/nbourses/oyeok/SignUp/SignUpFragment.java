package com.nbourses.oyeok.SignUp;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nbourses.oyeok.Firebase.UserProfileFirebase;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.MobileVerify;
import com.nbourses.oyeok.RPOT.ApiSupport.models.SignUp;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.User.UserProfileViewModel;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up,
                container, false);
        dbHelper=new DBHelper(getContext());

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

            if(dbHelper.getValue("offmode").equals("no")) {
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
                Toast.makeText(getContext(), "sign up in offline mode done",
                        Toast.LENGTH_LONG).show();
                dbHelper.save(DatabaseConstants.offmode, "no");
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
            if(otpReceived[0].equals(Svcode)) {
                signup_success();
                Log.i("error", "Validation success");
            }
            else{
                /*Toast.makeText(
                        getContext(),
                        "Please Enter Otp as mentioned in the SMS"+Svcode,
                        Toast.LENGTH_LONG).show();*/
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
        user.setMobileNo("8087275035");
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


        UserApiService user1 = restAdapter.create(UserApiService.class);
        user1.userSignUp(user, new Callback<SignUp>() {
            @Override
            public void success(SignUp signUp, retrofit.client.Response response) {
                Log.i("TAG", "Inside signup success");
                my_user_id=signUp.responseData.getUserId();
                Log.i("Firebase",userProfileViewModel.getUserProfile().toString());
                userProfileFirebase=new UserProfileFirebase(firebaseUrl,my_user_id);
                userProfileFirebase.setUserProfileValues(userProfileViewModel.getUserProfile());
                Toast.makeText(getContext(), "signup success", Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(RetrofitError error) {

                Log.i("TAG", "Inside signup Failure"+error.getMessage());
            }
        });


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
