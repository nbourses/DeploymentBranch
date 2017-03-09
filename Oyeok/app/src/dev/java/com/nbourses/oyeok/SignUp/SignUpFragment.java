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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.MyApplication;
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
import com.nbourses.oyeok.fragments.BrokerPreokFragment;
import com.nbourses.oyeok.fragments.MatchListingFragment;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.HalfDeals;
import com.nbourses.oyeok.realmModels.UserInfo;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.nbourses.oyeok.Database.DBHelper;
//import com.nbourses.oyeok.Firebase.UserProfileFirebase;

public class SignUpFragment extends Fragment implements OnAcceptOkSuccess, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
    private LoginButton loginButton;
    private LinearLayout fconnect;
    private LinearLayout gconnect;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
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
    private TextView txtfconnect;
    private TextView txtgconnect;
   // private boolean mClearDefaultAccount;
    ////////////////////////////////////////////////////
    // Variables defined for digits authentication
////////////////////////////////////////////////////
    private AuthCallback authCallback;
    private String mobile_number="";
    private static final String TWITTER_KEY = "CE00enRZ4tIG82OJp6vKib8YS";
    private static final String TWITTER_SECRET = "5AMXDHAXG0luBuuHzSrDLD0AvwP8GzF06klXFgcwnzAVurXUoS";
    HashMap<String, Float> listings = new HashMap<String, Float>();
    UserInfo userInfo = new UserInfo();





    public static final int RC_SIGN_IN = 0;



    private static final int PROFILE_PIC_SIZE = 800;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button gbtnSignOut;

    private Context mContext;
    private Activity mActivity;
    private  Profile profile;
    private TextView signinOR;

    private Boolean gcon =  false;

private LinearLayout signinpanel;




    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();


            Log.i(TAG,"facebook 2 "+loginResult);
          GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            facebookConnected(object);

                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();


        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mContext = getActivity().getApplicationContext();

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.i(TAG,"facebook 18 "+newToken);
                if (newToken == null) {
                    //write your code here what to do when user logout
                    fbdisconnected();
                }
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.i(TAG,"facebook 1 "+newProfile);
            }
        };

        profile = Profile.getCurrentProfile();

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

       /* AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                Log.i(TAG,"facebook currentAccessToken "+currentAccessToken);
                if (currentAccessToken == null){
                    name.setText("");
                    email.setText("");
                }
            }
        };
        accessTokenTracker.startTracking();*/
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        //loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

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
            MyApplication application = (MyApplication) getActivity().getApplication();
            Tracker mTracker = application.getDefaultTracker();

            mTracker.setScreenName("SignUpPage");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }




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




        //Initializing signinbutton


        //Initializing google api client




        fragment_container1 = (FrameLayout) view1.findViewById(R.id.fragment_container1);
        supportChat = (LinearLayout) view1.findViewById(R.id.supportChat);
        listViewDeals = (ListView) view1.findViewById(R.id.listViewDeals);
        if(lastFragment.equalsIgnoreCase("brokerDrawer")|| lastFragment.equalsIgnoreCase("okyed") || lastFragment.equalsIgnoreCase("brokerDeal") || lastFragment.equalsIgnoreCase("brokerIntro")||lastFragment.equalsIgnoreCase("brokermap"))
                okBroker = true;
            Log.i("Signup called =", "view assigned");
            View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        btnSignIn = (SignInButton) view.findViewById(R.id.sign_in_button);
        gbtnSignOut = (Button) view.findViewById(R.id.g_sign_out_button);

        gbtnSignOut.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google, 0, 0, 0);


        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity() , this )
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }


       /* mGoogleApiClient = new GoogleApiClient.Builder(view.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();*/







        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // mClearDefaultAccount = true;
                signInG();
             // signInWithGplus();

            }
        });
        gbtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutG();
            }
        });



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

       //// digitsButton = (DigitsAuthButton) view.findViewById(R.id.auth_button);
      ////  digitsButton.setText("sign me up");
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        already_registered_tab=(Button) view.findViewById(R.id.already_registered_tab);
        new_user_tab=(Button) view.findViewById(R.id.new_user_tab);
        signinpanel = (LinearLayout) view.findViewById(R.id.signinpanel);
        name= (EditText) view.findViewById(R.id.etname);
        txtfconnect = (TextView) view.findViewById(R.id.txtfconnect);
        txtgconnect = (TextView) view.findViewById(R.id.txtgconnect);
        email= (EditText) view.findViewById(R.id.etemail);
        submit=(Button)view.findViewById(R.id.submitprofile);
        fconnect=(LinearLayout) view.findViewById(R.id.fconnect);
        gconnect=(LinearLayout) view.findViewById(R.id.gconnect);
        tvheading= (TextView) view.findViewById(R.id.tvheading);
        tvcontent= (TextView) view.findViewById(R.id.tvcontent);
        editProfile_pic = (ImageView) view.findViewById(R.id.editProfile_pic);
        loginButton = (LoginButton)view.findViewById(R.id.login_button);
        signinOR = (TextView) view.findViewById(R.id.signinOR);
        /*  if(okBroker==false && AppConstants.CURRENT_USER_ROLE.equalsIgnoreCase("client"))*/
        Log.i(TAG,"last fragment narcos "+lastFragment);
        //gconnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google, 0, 0, 0);
        //fconnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook, 0, 0, 0);


        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            Log.i(TAG, "facebook success "+loginResult);
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "facebook cancel ");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i(TAG, "facebook error"+e.getMessage());
            }
        });

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
                signinpanel.setVisibility(View.GONE);
                signinOR.setVisibility(View.GONE);
                try {
                    MyApplication application = (MyApplication) getActivity().getApplication();
                    Tracker mTracker = application.getDefaultTracker();

                    mTracker.setScreenName("ExistingUser");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                signinpanel.setVisibility(View.VISIBLE);

                try {
                    MyApplication application = (MyApplication) getActivity().getApplication();
                    Tracker mTracker = application.getDefaultTracker();

                    mTracker.setScreenName("NewUser");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                signinOR.setVisibility(View.VISIBLE);
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
                try {
                    MyApplication application = (MyApplication) getActivity().getApplication();
                    Tracker mTracker = application.getDefaultTracker();

                    mTracker.setScreenName("MobileVerification");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        fconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    MyApplication application = (MyApplication) getActivity().getApplication();
                    Tracker mTracker = application.getDefaultTracker();

                    mTracker.setScreenName("FBLogin");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (gcon) {
                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text("Already connected with Google.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                } else {
                    loginButton.performClick();

                }
            }
        });

        gconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // btnSignIn.performClick();
                try {
                    MyApplication application = (MyApplication) getActivity().getApplication();
                    Tracker mTracker = application.getDefaultTracker();

                    mTracker.setScreenName("GoogleLogin");
                    mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isLoggedInfb()) {
                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text("Already connected with Facebook.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                }

                /*if(LoginManager.getInstance() != null){
                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text("Already connected with Facebook.")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                }else*/ else if(txtgconnect.getText().toString().equalsIgnoreCase("connect"))
                    signInG();
                else
                    signOutG();

            }
        });

        return view;

    }







    /**
     * Sign-in into google
     * */

    private void signInG(){

        try {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            Log.i(TAG,"connecto 123 "+e.getMessage());
            e.printStackTrace();
        }

    }
    /*private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }*/

    /**
     * Method to resolve any signin errors
     * */
   /* private void resolveSignInError() {

        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(mActivity,
                        RC_SIGN_IN);

            } catch (*//*IntentSender.SendIntentException e*//*Exception e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }*/








    public void submitButton() {

        submit.setEnabled(false);
        submit.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
        submit.setText("Registering...");

      AppConstants.REGISTERING_FLAG=true;
           signup_success();

    }


    void signup_success() {


        try {
            MyApplication application = (MyApplication) getActivity().getApplication();
            Tracker mTracker = application.getDefaultTracker();

            mTracker.setScreenName("LoginAPI");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.i("magic","signup request json "+json);


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
                        Log.i("TAG", "Inside signup success "+signUp.getError());
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
                                            try {
                                                MyApplication application = (MyApplication) getActivity().getApplication();
                                                Tracker mTracker = application.getDefaultTracker();

                                                mTracker.setScreenName("LoginSuccess");
                                                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
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
                        /*else if(signUp.getError().equals(9)){
                            TastyToast.makeText(getContext(),"Your mobile number is already registerd with us with a different name or email, to update name/email go to profile.",TastyToast.LENGTH_LONG,TastyToast.INFO);

                        }*/
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
                        General.setSharedPreferences(context, AppConstants.IS_SIGNUP,"true");
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







                        try {

                            final String pushmsg = "Have signed up as "+role_of_user.toUpperCase();

                            General.setSharedPreferences(getContext(), AppConstants.REFERING_ACTIVITY_LOG_ID, AppConstants.MASTER_ACTIVITY_LOG_ID);
                            if (General.getSharedPreferences(getContext(), AppConstants.REFERING_ACTIVITY_LOG_ID) != null) {
                                Map message = new HashMap();

                                String name;
                                if (General.getSharedPreferences(context, AppConstants.USER_ID) == null || General.getSharedPreferences(context, AppConstants.USER_ID) == "")
                                    name = "Client";
                                else
                                    name = General.getSharedPreferences(context, AppConstants.NAME);

                                final String finalName = name;


                                message.put("pn_gcm", new HashMap() {{
                                    put("data", new HashMap() {{
                                        put("message", pushmsg);
                                        put("_from", General.getSharedPreferences(getContext(), AppConstants.USER_ID));
                                        put("to", AppConstants.MASTER_ACTIVITY_LOG_ID);
                                        put("name", finalName);
                                        put("status", "LOG_AUTOOK");
                                    }});
                                }});
                                message.put("pn_apns", new HashMap() {{
                                    put("aps", new HashMap() {{
                                        put("alert", pushmsg);
                                        put("from", General.getSharedPreferences(getContext(), AppConstants.USER_ID));
                                        put("to", AppConstants.MASTER_ACTIVITY_LOG_ID);
                                        put("name", finalName);
                                        put("status", "LOG_AUTOOK");
                                    }});
                                }});

                                String channel = "global_log_" + General.getSharedPreferences(getContext(), AppConstants.REFERING_ACTIVITY_LOG_ID);
                                Log.i(TAG, "channel channel channel " + channel);
                                General.pushMessage(getContext(), channel, message);


                            }


                        } catch (Exception e) {
                        }





                        try {
                            MyApplication application = (MyApplication) getActivity().getApplication();
                            Tracker mTracker = application.getDefaultTracker();

                            mTracker.setScreenName("LoginSuccess");
                            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
                        } catch (Exception e) {
                            e.printStackTrace();
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


                            //open matching fragment






                            MatchListingFragment matchListingFragment = new MatchListingFragment();
                            Bundle bun = new Bundle();

                            if(b.containsKey("oye_id"))
                                bun.putString("oye_id",  b.getString("oye_id"));

                            if(b.containsKey("broker_name"))
                                bun.putString("broker_name",  b.getString("broker_name"));

                            if(b.containsKey("config"))
                                bun.putString("config",  b.getString("config"));

                            if(b.containsKey("locality"))
                                bun.putString("locality",  b.getString("locality"));

                            if(b.containsKey("price"))
                                bun.putString("price",  b.getString("price"));

                            if(b.containsKey("growth_rate"))
                                bun.putString("growth_rate",  b.getString("growth_rate"));

                            if(b.containsKey("date"))
                                bun.putString("date",  b.getString("date"));


                            /*bun.putString("broker_name", "broker_name");
                            bun.putString("config", "config");
                            bun.putString("locality", "locality");
                            bun.putString("price", "5000");
                            bun.putString("growth_rate", "-6");
                            bun.putString("date", "date");*/

                            //b.putString("JsonArray", jsonObjectArray.toString());
                            AppConstants.MATCHINGOKFLAG = true;
                            loadFragmentAnimated(matchListingFragment, bun, R.id.container_sign, "");

                            /*jsonArray=b.getString("JsonArray");
                            try {
                                p=new JSONArray(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            int j=b.getInt("Position");
                            Log.i(TAG,"c prasanna 2 " +j);
                            AcceptOkCall a = new AcceptOkCall();
                            a.setmCallBack(SignUpFragment.this);
                            a.acceptOk(listings,p,j,getActivity());*/
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("google signin", "onConnected");
      /*  mSignInClicked = false;
        getProfileInformation();*/

          /*  if (mClearDefaultAccount) {
                mClearDefaultAccount = false;
                mGoogleApiClient.clearDefaultAccountAndReconnect();
                return;
            }*/

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("google signin", "onConnected suspende");
        /*mGoogleApiClient.connect();*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       /* Log.i("google signin", "onConnected failed "+connectionResult.getErrorMessage()+ " "+connectionResult.getErrorCode()+"  "+connectionResult.isSuccess());

            if (!connectionResult.hasResolution()) {
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),
                        mActivity, 0).show();
                Log.e(TAG, "" + connectionResult.getErrorCode());
                return;
            }

            if (!mIntentInProgress) {

                mConnectionResult = connectionResult;

                if (mSignInClicked) {

                    Log.e(TAG, "" + connectionResult.getErrorCode());
                    resolveSignInError();
                }
            }
*/
     //   mClearDefaultAccount = false;
        }


    private void getProfileInformation() {
        /*Log.i("google signin", "getProfileInformation");
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();

               // String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String personGooglePlusProfile = currentPerson.getUrl();
               *//* String email = Plus.AccountApi.getAccountName(mGoogleApiClient);*//*

                Log.i(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl + " user id:"
                        + currentPerson.getId());






            } else {
                Toast.makeText(mContext, "Person information is null",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("google signin", "onActivityResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        if (requestCode == RC_SIGN_IN) {
            Log.i("google signin", "onActivityResult 1");

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
          /*  if (resultCode != Activity.RESULT_OK) {
                Log.i("google signin", "onActivityResult 2");
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                Log.i("google signin", "onActivityResult 3");
                mGoogleApiClient.connect();

            }*/
        }


    }

    private void handleSignInResult(GoogleSignInResult result) {
        try {
            Log.i(TAG, "handleSignInResult 1:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                Log.i(TAG, "handleSignInResult 2:" + acct.getDisplayName()+"   "+acct.getEmail());

                btnSignIn.setVisibility(View.GONE);
                gbtnSignOut.setVisibility(View.VISIBLE);



                /*try {
                   LoginManager.getInstance().logOut();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                setData(acct.getDisplayName(), acct.getEmail());
                txtgconnect.setText("Remove");

                gcon = true;
            } else {
                // Signed out, show unauthenticated UI.
               // updateUI(false);


                gcon = false;
                Log.i(TAG, "handleSignInResult 3:" + result.isSuccess());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        LoginManager.getInstance().logOut();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onStart() {
        super.onStart();
     //   mGoogleApiClient.connect();
    }



    @Override
    public void onStop() {
        super.onStop();
        /*if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }*/
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
       /* Profile profile = Profile.getCurrentProfile();*/
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
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

    private void setData(String name1, String email1){
        Log.i(TAG,"handleSignInResult 4 "+name1+"  "+email1);
        name.setText(name1);
        email.setText(email1);
        Sname = name.getText().toString();
        Semail = email.getText().toString();
        Digits.authenticate(authCallback, R.style.CustomDigitsTheme);

    }

    private void signOutG() {
        try {
            if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Log.i(TAG,"google logout "+status);
                            name.setText("");
                            email.setText("");
                            btnSignIn.setVisibility(View.VISIBLE);
                            gbtnSignOut.setVisibility(View.GONE);
                            txtgconnect.setText("Connect");
                            gcon = false;

                        }
                    });
        }else{
                name.setText("");
                email.setText("");
                txtgconnect.setText("Connect");
                gcon = false;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void facebookConnected(JSONObject object){
        try {

            // Application code

            String email = object.getString("email");
            String name = object.getString("name");
            Log.i(TAG,"facebook 2 1 "+email);

            Log.i(TAG,"facebook 2 2 "+name);
            /*if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                btnSignIn.setVisibility(View.VISIBLE);
                gbtnSignOut.setVisibility(View.GONE);
                txtgconnect.setText("Connect");
            }*/
            txtfconnect.setText("Remove");
           // txtgconnect.setText("Connect");

            setData(name, email);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG,"facebook connected "+e.getMessage());
        }
    }

    private void fbdisconnected(){
        txtfconnect.setText("Connect");

            name.setText("");
            email.setText("");

    }

    public boolean isLoggedInfb() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

}
