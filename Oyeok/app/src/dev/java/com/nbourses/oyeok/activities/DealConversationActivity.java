package com.nbourses.oyeok.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.adapters.ChatListAdapter;
import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserType;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ChatMessage;
import com.nbourses.oyeok.realmModels.DealTime;
import com.nbourses.oyeok.realmModels.Message;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pubnub.api.Callback;
import com.pubnub.api.PnApnsMessage;
import com.pubnub.api.PnGcmMessage;
import com.pubnub.api.PnMessage;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static android.util.Log.i;

public class DealConversationActivity extends AppCompatActivity implements OnRatingBarChangeListener {

    @Bind(R.id.edtTypeMsg)
    TextView edtTypeMsg;

    @Bind(R.id.imgSendMsg)
    ImageView imgSendMsg;

    @Bind(R.id.chat_list_view)
    ListView chatListView;

    @Bind(R.id.conversationRootView)
    RelativeLayout conversationRootView;

    @Bind(R.id.suggestionRootView)
    LinearLayout suggestionRootView;

    @Bind(R.id.suggestionList)
    ListView suggestionList;

    // @Bind(R.id.spinnerProgress)
    //  ProgressBar spinnerProgress;

    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    // @Bind(R.id.texRating)
    // ProgressBar texrating;
    private TextView texrating;

    private DBHelper dbHelper;


    private static final String TAG = "DealConversationActivit";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Pubnub pubnub;
    private String channel_name = "";
    private ChatListAdapter listAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private String UUID;


    private String userRole = "client";
    private static final String[] suggestionsForClientArray = {"How can I use this app?", "How can I find property?", "Will I get broker within 15 or 20 minutes?"};
    private static final String[] suggestionsForBrokerArray = {"How can I use this app?", "How can I find client?", "Can I post property?"};
    private static final String[] suggestionsLLAvl = {"I need vegetarian tenant.", "I need family as tenant.", "I have parking available."};
    private static final String[] suggestionsORAvl = {"I have parking available with house.", "Society with good security/surveillance.", "Ready for negotiation"};

    private static final String[] suggestionsLLReq = {"I need Society with good security/surveillance.", "I need parking.", "I need new construction."};
    private static final String[] suggestionsORReq = {"I need Society with good security/surveillance.", "I need parking.", "I need new construction."};
    private static final String[] suggestionsBroker = {"Do you need Society with good security/surveillance?", "Do you need parking?", "Do you need new construction?", "Are you Ready for negotiation?"};

    private Boolean isDefaultDeal;
    private Boolean firstMessage = false;
    private Boolean present = true;
    private JSONObject jsonMsgtoWhereNow = new JSONObject();
    private String specCode;
    private ArrayList<JSONObject> cachedmsgs = new ArrayList<JSONObject>();
    String[] bNames = new String[3];
    int[] bPrice = new int[3];
    Bundle b;
    private String lastMessageTime;

    private RealmConfiguration config;
    private Realm myRealm;
    private Message message;
    private JSONArray jsonArrayHistory;
    private HashMap<String, Float> listings;
    private Boolean oyed = false;
    private Boolean okyed = false;

    // File fileToUpload = new File("/storage/sdcard0/Pictures/Screenshots/photos.png");
    private File fileToUpload = new File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1467990952511.jpg");
    private File fileToDownload = new File("/storage/sdcard0/Pictures/OYEOK");
    public AmazonS3 s3;
    public TransferUtility transferUtility;
    private String imageName = null;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();
        }
    };
    private final TextWatcher edtTypeMsgListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            i(TAG, "before edtTypeMsg changed");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            i(TAG, "on edtTypeMsg changed");
            if (edtTypeMsg.getText().toString().equals("")) {
                //when nothing is typed shows attachement symbol
                imgSendMsg.setImageResource(R.drawable.attachment);
                imgSendMsg.setTag("attachment");
            }
            else {
                //when somethingg is typed shows send arrow symbol
                imgSendMsg.setImageResource(R.drawable.ic_chat_send_active);
                imgSendMsg.setTag("message");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            i(TAG, "after edtTypeMsg changed");
        }
    };

    @Override
    //protected
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deal_conversation);


        ButterKnife.bind(this);
        texrating = (TextView) findViewById(R.id.texRating);
        ratingBar.setOnRatingBarChangeListener(this);

        if(!General.isNetworkAvailable(this)){
            General.internetConnectivityMsg(this);
        }

        dbHelper = new DBHelper(getBaseContext());

//        IntentFilter filter = new IntentFilter("shine");
//        LocalBroadcastManager.getInstance(this).registerReceiver(handlePushNewMessage, filter);
        // UUID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
        UUID = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);
        i("WHERENOW", "UUID " + UUID);

        pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);

        init();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));

        super.onResume();

        if (!channel_name.equals(""))
            Log.i(TAG, "chanel name was not null" + channel_name);
        if(!channel_name.equals("my_channel"))
        {

            setupPubnub(channel_name);}  // DEals OK ID
        else {
            setupPubnub(UUID);
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);

        super.onPause();
        //  if (pubnub != null)
        //   pubnub.unsubscribeAll();

    }



    private void init() {

        Log.i(TAG,"pathas "+Environment.getExternalStorageDirectory().toString() + "/oyeok");

        Log.i(TAG,"pathas 1 "+Environment.getDataDirectory() + "/oyeok");

        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            File directory = new File(Environment.getDataDirectory()
                    + "/oyeok/");
            /*File photoDirectory = new File(Environment.getDataDirectory()
                    + "/oyeok/");*/
            /*
             * this checks to see if there are any previous test photo files
             * if there are any photos, they are deleted for the sake of
             * memory
             */
            /*if (photoDirectory.exists()) {
                File[] dirFiles = photoDirectory.listFiles();
                if (dirFiles.length != 0) {
                    for (int ii = 0; ii <= dirFiles.length; ii++) {
                        dirFiles[ii].delete();
                    }
                }
            }*/
            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File directory = new File(Environment.getExternalStorageDirectory()
                    + "/oyeok/");
            /*File photoDirectory = new File(
                    Environment.getExternalStorageDirectory()
                            + "/oyeok/");*/
            /*if (photoDirectory.exists()) {
                File[] dirFiles = photoDirectory.listFiles();
                if (dirFiles.length > 0) {
                    for (int ii = 0; ii < dirFiles.length; ii++) {
                        dirFiles[ii].delete();
                    }
                    dirFiles = null;
                }
            }*/
            // if no directory exists, create new directory to store test
            // results
            if (!directory.exists()) {
                directory.mkdir();
            }
        }// end of SD card checking
        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        Log.i(TAG, "role of user def yoman " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));
        listings = new HashMap<String, Float>();
//        try {
//
//            Realm myRealm = General.realmconfig(this);
//            RealmResults<UserInfo> results1 =
//                    myRealm.where(UserInfo.class).equalTo(AppConstants.MOBILE_NUMBER, "+918483014575").findAll();
//
//            UserInfo myPuppy = myRealm.where(UserInfo.class).equalTo(AppConstants.MOBILE_NUMBER, "+918483014575").findFirst();
//
//            Log.i(TAG, "my name is " + myPuppy.getName());
//
//            for (UserInfo c : results1) {
//                Log.i(TAG, "insidero2 ");
//                Log.i(TAG, "insidero3 " + c.getName());
//                Log.i(TAG, "insidero4 " + c.getEmailId());
//                Log.i(TAG, "insidero4 " + c.getUserId());
//            }
//        }catch(Exception e){}

        edtTypeMsg.addTextChangedListener(edtTypeMsgListener);

        chatMessages = new ArrayList<>();

        Log.i(TAG, "chatMessages are" + chatMessages);



        Bundle bundle = getIntent().getExtras();
        try {
            if (bundle != null) {
                if (bundle.containsKey("OkAccepted") && bundle.containsKey(AppConstants.OK_ID)) {
                    channel_name = bundle.getString(AppConstants.OK_ID);

                    if (bundle.getString("OkAccepted").equalsIgnoreCase("yes")) {

                        okyed = true;
                        General.storeDealTime(bundle.getString(AppConstants.OK_ID), this);

                    }

                }

                else if (bundle.containsKey("Oyed") && bundle.containsKey(AppConstants.OK_ID)) {
                    channel_name = bundle.getString(AppConstants.OK_ID);

                    if (bundle.getString("Oyed").equalsIgnoreCase("yes")) {
                        oyed = true;
                        //chatMessages.clear();


                    }

                }

                else if(bundle.containsKey(AppConstants.OK_ID)){
                    channel_name = bundle.getString(AppConstants.OK_ID); //my_channel if came from root item
                }
                Log.i(TAG,"listing ghya listing 1 ");
                if(bundle.containsKey("listings")){
                    Log.i(TAG,"listing ghya listing 2 ");
                    Log.i(TAG,"listing ghya listing "+bundle.getSerializable("listing"));

                }
            }
        }
        catch(Exception e){
            Log.i(TAG,"caught in exception saving accept deal time "+e);
        }




        // Bundle bundle  = getIntent().getExtras();
        //channel_name = bundle.getString(AppConstants.OK_ID); //my_channel if came from root item

        Log.i("Deals Conv Act","channel name is the "+channel_name);
        specCode = bundle.getString(AppConstants.SPEC_CODE);
        isDefaultDeal = bundle.getBoolean("isDefaultDeal");

        Log.i("TRACE DEALS FLAG 3", "FLAG " + isDefaultDeal);

        //display messages from cache
//        String s = General.getSharedPreferences(this,channel_name+AppConstants.CACHE);
//        //List<JSONObject> itemList = Arrays.asList(s);
//        Log.i("CACHE","string is from cache type"+General.getSharedPreferences(this,channel_name+AppConstants.CACHE).getClass().getName());
//
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(s);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        //  bNames = b.getStringArray("bNames");
        //  bPrice = b.getIntArray("bPrice");

        //Cardview

//            String[] bNames = new String[]{"Building1","Building2","Building3"};
//            int[] bPrice = new int[]{35000,50000,65000};
//            Log.i("Listing card data","card data "+bNames+" "+bPrice+"channel_name "+channel_name);
//        try{
//            JSONObject jsonMsg = new JSONObject();
//            //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
//            jsonMsg.put("from", "LISTING");
//            //jsonMsg.put("to", "client");
//
//            jsonMsg.put("to", "client");
//
//
//            jsonMsg.put("message",String.valueOf(bNames[0]+"--"+bPrice[0]+"--"+bNames[1]+"--"+bPrice[1]+"--"+bNames[2]+"--"+bPrice[2]));
//
//            //Log.i("TRACE","messageText is "+messageText);
//            //Log.i(TAG,"messageText is"+messageText);
//            //publish message
//            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
//            Log.i(TAG, "Listing message published");
//        }
//        catch(Exception e){
//            Log.i(TAG, "caught in listing msg exception "+e);
//
//        }





        // Log.i("CHAT","cache cache from shared yo mana string "+jsonArray +"    type is "+jsonArray.getClass().getName());
//



//        try {
//            String s = General.getSharedPreferences(this,channel_name+AppConstants.CACHE);
//
//            Log.i("CHAT","cache cache from shared yo mana "+General.getSharedPreferences(this,channel_name+AppConstants.CACHE));
//           // Log.i("CACHE", "cache from shared yo mana " + General.getSharedPreferences(this, channel_name + AppConstants.CACHE));
//
//            Log.i("CHAT","cache cache from shared yo mana string "+s);
//           //JSONObject jsonObj = new JSONObject(s);
//            //JSONObject j = new JSONObject(General.getSharedPreferences(this,channel_name+AppConstants.CACHE));
//
//            //Log.i("CACHE", "cache from shared yo mana jsonObj " + jsonObj);
//           // displayMessage(jsonObj);
//            JSONArray jsonArray = new JSONArray(s);
//            Log.i("CHAT","cache cache from shared yo mana string "+jsonArray +"    type is "+jsonArray.getClass().getName());
//
//
//            int jsonArrayLength = jsonArray.length();
//            if (jsonArray.length() > 0) {
//                for (int i = 0; i < jsonArrayLength; i++) {
//                    JSONObject jsonMsg = jsonArray.getJSONObject(i);
//
//
//                    Log.i(TAG, "jsonMsg catched success " + jsonMsg);
//                    displayMessage(jsonMsg);
//
//                }
//            }
//
//
//
//        }
//        catch(Exception e){
//            Log.i("CACHE", "cache from shared yo mana caught in exception " + e);
//
//        }





        //test pubnub gcm

        pubnub.enablePushNotificationsOnChannel("t9i9e2x6se110871a_", General.getSharedPreferences(this,AppConstants.GCM_ID), new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                super.successCallback(channel, message);

                Log.i("PUBNUB PUSH","SUCCESSFUL======");
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                super.errorCallback(channel, error);

                Log.i("PUBNUB PUSH","Error======");
            }
        });
// subscribe a channel for Pubnub push notifications end

        pubnub.enablePushNotificationsOnChannel(
                "t9i9e2x6se110871a_",
                General.getSharedPreferences(this,AppConstants.GCM_ID));


        try {
            sendNotification("ci1qnk9074361a_");
        }
        catch (Exception e)
        {
            Log.i("ERROR ON NOTIFICATION"," "+e.getMessage());
        }










        General.setSharedPreferences(this,AppConstants.GCM_ID,dbHelper.getValue(DatabaseConstants.gcmId));


        General.setSharedPreferences(this,AppConstants.GCM_ID,SharedPrefs.getString(getApplicationContext(),SharedPrefs.MY_GCM_ID));
        if (!channel_name.equals("")) {
            Log.i(TAG, "chanel name was not null" + channel_name);

            // subscribe a channel for Pubnub push notifications start

            Log.i("Pubnub push","channel_name is "+channel_name);
            Log.i("Pubnub push","GCM id is "+General.getSharedPreferences(this,AppConstants.GCM_ID));

            pubnub.enablePushNotificationsOnChannel(
                    channel_name,
                    "dOtsO1_3XFs:APA91bHuog28jPdP3_A5mGbd12K20c2S7aHIL5OI6rlfyChGG7GH7QJWNWmTpS4ivtc_g-eysnuPLn5DHFCNoWawh_sSeONxByb1YzAHPR8TV6VifmjcPCx_ugPjJ_TgJeB4pr0Obr5C");

            // subscribe a channel for Pubnub push notifications end


        }




//        try {
//            pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);
//
//            if(channel_name.equals("my_channel"))
//            {
//                Log.i("WHERENOW", "2 ");
//
//                pubnub.subscribe(UUID, new Callback() {
//                            @Override
//                            public void connectCallback(String channel, Object message) {
//                                Log.i("WHERENOW", "61 ");
//                            }
//
//                            @Override
//                            public void disconnectCallback(String channel, Object message) {
//                                Log.i("WHERENOW", "62 ");
//                            }
//
//                            public void reconnectCallback(String channel, Object message) {
//                                Log.i("WHERENOW", "63 ");
//                            }
//
//                            @Override
//                            public void successCallback(String channel, final Object message) {
//                                try {
//                                    Log.i("WHERENOW", "6 ");
//                                    JSONObject jsonMsg = (JSONObject) message;
//                                    i(TAG, "pubnub setup success" + jsonMsg);
//
//                                    // displayMessage(jsonMsg);
//                                } catch (Exception e) {
//                                    Log.i("WHERENOW", "64 "+e);
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void errorCallback(String channel, PubnubError error) {
//                                Log.i("WHERENOW", "error subscribing " + error);
//                            }
//                        }
//                );
//            }
//
//            Log.i("WHERENOW", "UUID " + pubnub.getUUID());
//
//            Callback callback = new Callback() {
//                public void successCallback(String channel, Object response) {
//                    System.out.println(response.toString());
//                    Log.i("WHERENOW", "success " + response.toString());
//                    JSONObject obj = null;
//                    try {
//                        obj = new JSONObject(response.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        String a = obj.getString("channels");
//                        Log.i("WHERENOW", "successaa" +a);
//
//
//                        JSONObject obje = new JSONObject(response.toString());
//                        JSONArray arr = obje.getJSONArray("channels");
//                        for (int i = 0; i < arr.length(); i++)
//                            // System.out.println(arr.getString(i));
//                            Log.i("WHERENOW", "successaab" + arr.getString(i));
//
//
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//
//                public void errorCallback(String channel, PubnubError error) {
//                    System.out.println(error.toString());
//                    i("WHERENOW", "failure " + error.toString());
//
//                }
//            };
//            pubnub.whereNow(UUID, callback);
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }








        listAdapter = new ChatListAdapter(chatMessages,isDefaultDeal, this);
        chatListView.setAdapter(listAdapter);

        //  displayMessage();

//        Bundle bundle  = getIntent().getExtras();
//        channel_name = bundle.getString(AppConstants.OK_ID);

        Log.i(TAG, "channel_name yo" + channel_name);
        userRole = bundle.getString("userRole");
        Log.i(TAG, "userRole is" + userRole);
        Log.i(TAG, "speccode is yo" + specCode);

        ArrayAdapter<String> adapterSuggestions = null;
        //userRole.equals("other") then load simple_list_item_2

        if (channel_name.equalsIgnoreCase("my_channel") || channel_name.equalsIgnoreCase(UUID) ) {
            if (userRole.equals("client"))
                adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsForClientArray);
            else
                adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsForBrokerArray);
        }

        else if (userRole.equals("client")) {
            if (specCode.contains("LL")) {
                if (specCode.contains("REQ")) {
                    adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsLLReq);
                } else {
                    adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsLLAvl);
                }

            } else {
                if (specCode.contains("REQ")) {
                    adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsORReq);
                } else {
                    adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsORAvl);
                }


            }
        }
        else{
            adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsBroker);


        }
        //assign adapter to ListView
        suggestionList.setAdapter(adapterSuggestions);
        suggestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //hide suggestion box
                hideSuggestionBox();
                String itemValue = (String) suggestionList.getItemAtPosition(position);
                edtTypeMsg.setText(itemValue);
            }
        });

        if (channel_name.equals("")) {
            imgSendMsg.setClickable(false);
        }

        edtTypeMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //hide suggestion box
                    hideSuggestionBox();
                }
            }
        });
    }

    private void hideSuggestionBox() {
        LinearLayout.LayoutParams suggestionRootViewLayoutParams = (LinearLayout.LayoutParams) suggestionRootView.getLayoutParams();
        LinearLayout.LayoutParams conversationRootViewParams = (LinearLayout.LayoutParams) conversationRootView.getLayoutParams();
        suggestionRootViewLayoutParams.weight = 0;
        conversationRootViewParams.weight = 100;
        suggestionRootView.setLayoutParams(suggestionRootViewLayoutParams);
        conversationRootView.setLayoutParams(conversationRootViewParams);
        suggestionRootView.setVisibility(View.GONE);
    }

    private void showSuggestionBox() {
        LinearLayout.LayoutParams suggestionRootViewLayoutParams = (LinearLayout.LayoutParams) suggestionRootView.getLayoutParams();
        LinearLayout.LayoutParams conversationRootViewParams = (LinearLayout.LayoutParams) conversationRootView.getLayoutParams();
        suggestionRootViewLayoutParams.weight = 30;
        conversationRootViewParams.weight = 70;
        suggestionRootView.setLayoutParams(suggestionRootViewLayoutParams);
        conversationRootView.setLayoutParams(conversationRootViewParams);
        suggestionRootView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.imgSendMsg)
    public void onSendMsgClick(View v) {
        String currentImg = (String) v.getTag();
        if (currentImg != null) {
            if (currentImg.equals("attachment")) {
                //open file chooser options
                selectImage();
            } else {
                //send message
                sendMessage(edtTypeMsg.getText().toString());
                final ChatMessage message = new ChatMessage();
                message.setUserName("self");
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(edtTypeMsg.getText().toString());
                message.setUserType(ChatMessageUserType.OTHER);
                message.setMessageTime(System.currentTimeMillis()/10000);
                chatMessages.add(message);
                listAdapter.notifyDataSetChanged();

                edtTypeMsg.setText("");
                chatListView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        chatListView.setSelection(listAdapter.getCount());
                    }

                });
                chatMessages.remove(chatMessages.size()-1);

            }
        }
    }

    /**
     * pubnub setup
     */

    private void setupPubnub(String channel_name) {

        chatMessages.clear();
        listAdapter.notifyDataSetChanged();
        Log.i("WHERENOW", "3 ");

        try {
            Log.i("WHERENOW", "4 ");

            //pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);
            pubnub.setUUID(UUID);

            Log.i(TAG, "before loadHistoryFromPubnub");
            if(!channel_name.equals("my_channel")) {

                if(oyed || okyed){
                    oyed = false;
                    okyed = false;
                    displayDefaultMessage();

                }
                else {
                    displayMessage();
                }
                loadHistoryFromPubnub(channel_name);
            }

            pubnub.subscribe(channel_name, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                        }

                        public void reconnectCallback(String channel, Object message) {
                        }

                        @Override
                        public void successCallback(String channel, final Object message) {
                            try {
                                Log.i("WHERENOW", "6 ");
                                JSONObject jsonMsg = (JSONObject) message;
                                Log.i(TAG, "pubnub setup success" + jsonMsg);

                                displayMessage(jsonMsg);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                        }
                    }
            );

           /* String channel = channel_name + "-pndebug";
            Log.i("channel name for debug","====== "+channel);
            pubnub.subscribe(channel, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                        }

                        public void reconnectCallback(String channel, Object message) {
                        }

                        @Override
                        public void successCallback(String channel, final Object message) {
                            try {
                                Log.i("WHERENOW", "6 ");
                                JSONObject jsonMsg = (JSONObject) message;
                                Log.i(TAG, "pubnub setup success" + jsonMsg);

                                displayMessage(jsonMsg);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                        }
                    }
            );*/
        }
        catch (PubnubException e) {
            e.getMessage();
            Log.i("WHERENOW", "6 ");
        }
    }

    private void selectImage() {
        final CharSequence[] items = {/* "Take Photo",*/ "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DealConversationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                /*if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else*/ if (items[item].equals("Choose from Library")) {
                    verifyStoragePermissions(DealConversationActivity.this);
                    Log.d(TAG, "lolwa imagewa 4 ");
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    Log.d(TAG, "lolwa imagewa 1 ");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                    Log.d(TAG, "lolwa imagewa 2 ");
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "lolwa imagewa 3 ");

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Log.d(TAG, "lolwa imagewa uri "+uri);

            fileToUpload = new File(getRealPathFromURI(this,uri));
            Log.i(TAG, "lolwa imagewa uri fileToUpload "+fileToUpload);
            setFileToUpload(fileToUpload);



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.i(TAG, "lolwa imagewa "+String.valueOf(bitmap));




                /*ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    /**
     * display incoming messages through pubnub
     * @param jsonMsg
     */
    private void displayMessage(JSONObject jsonMsg) {


        Log.i("WHERENOW", "5 ");
        Log.i("CONVER", "jsonMsg" + jsonMsg);
        Log.i(TAG, "inside displayMessage" + jsonMsg);



        String body = null;
        String timetoken = null;
        ChatMessageUserType userType = null;
        String imageUrl = null;
        final ChatMessage message = new ChatMessage();
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";


        try {
            if (jsonMsg.has("message")) {
                JSONObject j = jsonMsg.getJSONObject("message");
                timetoken = jsonMsg.getString("timetoken");
                Log.i("CHAT","samosa1 "+jsonMsg.getString("message"));

                if (j.has("message") && j.has("from") && j.has("to")) {
                    Log.i("CHAT","here");
                    body = j.getString("message");
                    Log.i("CHAT","here is "+body);

                    if (j.getString("from").equalsIgnoreCase("DEFAULT")){
                        Log.i("CONVER", "DEFAULT set");
                        userType = ChatMessageUserType.DEFAULT;
                    }
                    else if (j.getString("from").equalsIgnoreCase("IMG")){
                        Log.i("CONVER", "grrrr IMG set");
                        userType = ChatMessageUserType.IMG;
                        if(j.has("imageUrl")){
                            Log.i("CONVER", "grrrr 1 IMG set");
                            imageUrl = j.getString("imageUrl");
                        }

                    }
                    else if (j.getString("from").equalsIgnoreCase("LISTING")){
                        Log.i("CONVER", "LISTING set");
                        userType = ChatMessageUserType.LISTING;
                    }
                    /*else if(!j.getString("to").equalsIgnoreCase(General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI)))
                    {
                        userType = ChatMessageUserType.OTHER;     // for support
                    }*/
                    else if (!j.getString("from").equalsIgnoreCase(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID)))
                    {
                        userType = ChatMessageUserType.SELF;
                    }

                    else {
                        userType = ChatMessageUserType.OTHER;
                    }


                    //  if(userType == ChatMessageUserType.OTHER && channel_name.equals("my_channel"))
                    //     channel_name = General.getSharedPreferences(this ,AppConstants.USER_ID);
                    message.setUserName(roleOfUser);
                    message.setMessageStatus(ChatMessageStatus.SENT);
                    message.setMessageText(body);
                    message.setUserType(userType);
                    message.setImageUrl(imageUrl);
                    // message.setMessageTime(new Date().getTime());
                    message.setMessageTime(Long.valueOf(timetoken)/10000);




                    chatMessages.add(message);


                    Log.i(TAG, "message after adding to chatMessages" + chatMessages);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "message runOnUiThread" + listAdapter);
                            if (listAdapter != null) {
                                Log.i(TAG, "message runOnUiThread  not null");
                                listAdapter.notifyDataSetChanged();
                            }
                            Log.i(TAG, "message runOnUiThread edtTypeMsg1");
                            edtTypeMsg.setText("");
                            Log.i(TAG, "message runOnUiThread edtTypeMsg2");
                        }
                    });
                }



            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "caught in display message1" + e);
        }



        try {
            if (jsonMsg.has("message") && jsonMsg.has("from") && jsonMsg.has("to")) {
                Log.i("CHAT","samosa");
                body = jsonMsg.getString("message");
                Log.i("CHAT","papdi "+body);

                if (jsonMsg.getString("from").equalsIgnoreCase("DEFAULT")){
                    Log.i("CONVER", "DEFAULT set");
                    userType = ChatMessageUserType.DEFAULT;
                }
                else if (!jsonMsg.getString("from").equalsIgnoreCase(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID)))
                {
                    userType = ChatMessageUserType.SELF;
                }
                else {
                    userType = ChatMessageUserType.OTHER;
                }


                //  if(userType == ChatMessageUserType.OTHER && channel_name.equals("my_channel"))
                //     channel_name = General.getSharedPreferences(this ,AppConstants.USER_ID);


                message.setUserName(roleOfUser);
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(body);
                message.setUserType(userType);
                message.setMessageTime(new Date().getTime());
                Log.i(TAG, "message before adding to chatMessages" + message);
                chatMessages.add(message);
                Log.i(TAG, "message after adding to chatMessages" + chatMessages);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "message runOnUiThread" + listAdapter);
                        if (listAdapter != null) {
                            Log.i(TAG, "message runOnUiThread  not null");
                            listAdapter.notifyDataSetChanged();
                        }
                        Log.i(TAG, "message runOnUiThread edtTypeMsg1");
                        edtTypeMsg.setText("");
                        Log.i(TAG, "message runOnUiThread edtTypeMsg2");
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "caught in display message" + e);
        }

        chatListView.post(new Runnable() {
            @Override
            public void run() {
                chatListView.setSelection(listAdapter.getCount());
            }
        });
    }


    private void displayMessage(){
        myRealm = General.realmconfig(this);
        String body = null;
        String timetoken = null;
        ChatMessageUserType userType = null;
        ChatMessage message;
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";


        /*message = new ChatMessage();
        message.setUserName(roleOfUser);
        message.setMessageStatus(ChatMessageStatus.SENT);
        message.setMessageText("testo");
        message.setUserType(ChatMessageUserType.OTHER);
        message.setMessageTime(Long.valueOf("14685047443226704")/10000);
        chatMessages.add(message);
       // listAdapter.notifyDataSetChanged();


        message = new ChatMessage();
        message.setUserName(roleOfUser);
        message.setMessageStatus(ChatMessageStatus.SENT);
        message.setMessageText("testero");
        message.setUserType(ChatMessageUserType.OTHER);
        message.setMessageTime(Long.valueOf("14685047443226704")/10000);
        chatMessages.add(message);
        listAdapter.notifyDataSetChanged();*/


        try {



            myRealm.beginTransaction();
            RealmResults<Message> results1 =
                    myRealm.where(Message.class).equalTo(AppConstants.OK_ID, channel_name).findAll();


            for (Message c : results1) {
                Log.i(TAG, "until insideroui2 ");
                Log.i(TAG, "until insideroui3 " + c.getOk_id());
                Log.i(TAG, "until insideroui4 " + c.getTimestamp());
                Log.i(TAG, "until insideroui4 " + c.getMessage());
                Log.i(TAG, "until insideroui4 " + c.getFrom());
                Log.i(TAG, "until insideroui4 " + c.getTo());

                if (c.getFrom().equalsIgnoreCase("DEFAULT")) {
                    Log.i("CONVER", "DEFAULT set");
                    userType = ChatMessageUserType.DEFAULT;
                } else if (c.getFrom().equalsIgnoreCase("LISTING")) {
                    Log.i("CONVER", "LISTING set");
                    userType = ChatMessageUserType.LISTING;
                } else if (!c.getFrom().equalsIgnoreCase(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID))) {
                    userType = ChatMessageUserType.SELF;
                } else {
                    userType = ChatMessageUserType.OTHER;
                }

                message = new ChatMessage();
                message.setUserName(roleOfUser);
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(c.getMessage());
                message.setUserType(userType);
                message.setMessageTime(Long.valueOf(c.getTimestamp())/10000);
                chatMessages.add(message);


                Log.i(TAG, "cache yo  message after adding to chatMessages" + chatMessages);


            }
            listAdapter.notifyDataSetChanged();
        }catch(Exception e){
            Log.i(TAG,"Caught in the exception reading cache from realm "+e);
        }
        finally {
            myRealm.commitTransaction();
            Log.i(TAG,"until fitra 4 "+myRealm.isInTransaction());
        }
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                chatListView.setSelection(listAdapter.getCount());
            }
        });

    }

    /**
     * send message through pubnub
     * @param messageText
     */





    private void sendMessage(final String messageText)
    {
        Log.i(TAG, "Inside send message");
        if(messageText.trim().length()==0)
            return;

        // Rt find to?
        String To = "support";
        String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
        if(role.equals("broker"))
            To = "client";
        else if(role.equals("client"))
            To = "broker";
        else
            Log.i(TAG, "Role is not properly saved in shared preferences" + role);



        try {
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("from", General.getSharedPreferences(this ,AppConstants.USER_ID));
            jsonMsg.put("name", dbHelper.getValue(DatabaseConstants.name));
            Log.i(TAG, "name is " + dbHelper.getValue(DatabaseConstants.name));



            //.put("from", General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));
            //jsonMsg.put("to", "client");
            jsonMsg.put("to", To);
            jsonMsg.put("message", messageText);

            Log.i("TRACE", "messageText is " + messageText);
            Log.i(TAG, "messageText is" + messageText);
            Log.i(TAG, "channel_name is" + channel_name);
            Log.i(TAG, "jsonMsg" + jsonMsg);
            jsonMsgtoWhereNow = jsonMsg;
            Log.i("TEST", "jsonMsgtoWhereNow in send msg " + jsonMsgtoWhereNow);
            //publish message


            Log.i("TEST","published to channel" +General.getSharedPreferences(this ,AppConstants.USER_ID));
            // pubnub.publish(General.getSharedPreferences(this, AppConstants.USER_ID), jsonMsg, true, new Callback() {});

            Log.i("TEST","published to channel_name" +channel_name);
            if (channel_name.equals("my_channel")){
                //channel_name = General.getSharedPreferences(this, AppConstants.USER_ID);
                // pubnubWhereNow(General.getSharedPreferences(this, AppConstants.USER_ID));
                channel_name = General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI);
                pubnubWhereNow(General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI));
                Log.i("Pubnub push","channel_name_userid case is "+channel_name);
                Log.i("Pubnub push","GCM id is "+General.getSharedPreferences(this,AppConstants.GCM_ID));
                // subscribe a channel for Pubnub push notifications start

                pubnub.enablePushNotificationsOnChannel(
                        channel_name,
                        General.getSharedPreferences(this,AppConstants.GCM_ID));

                // subscribe a channel for Pubnub push notifications end
            }

            pubnub.enablePushNotificationsOnChannel(
                    channel_name,
                    General.getSharedPreferences(this,AppConstants.GCM_ID));

            sendNotification(channel_name);
            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
            lastMessageTime = String.valueOf(System.currentTimeMillis());
// if channel name is ok id dont call pubnubwhere now (moved above)
            //        pubnubWhereNow(General.getSharedPreferences(this, AppConstants.USER_ID));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pubnubWhereNow(final String UUID){

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());

                Log.i("TEST", "pubnubWhereNow" + response.toString());


                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String a = obj.getString("channels");
                    Log.i("TEST", "successaa" + a);


                    JSONObject obje = new JSONObject(response.toString());
                    JSONArray arr = obje.getJSONArray("channels");
                    firstMessage = false;
                    Log.i("TEST","uuid inside wherenow" +UUID);
                    Log.i("TEST","firstMessage" +firstMessage);
                    Log.i("TEST", "arr is" + arr);


                    for (int i = 0; i < arr.length(); i++){
                        Log.i("WHERENOW", "successaab" + arr.getString(i));
                        if(arr.getString(i).equals(UUID)){
                            present = true;
                            Log.i("TEST","contains UUID" +present);
                        }
                    }

                    if(present){
                        firstMessage = false;
                        Log.i("TEST", "first message" + firstMessage);

                    }else
                    {
                        Log.i("TEST", "jsonMsgtoWhereNow in wherenow " + jsonMsgtoWhereNow);
                        firstMessage = true;
                        sendNotification("my_channel");
                        Log.i("Pubnub push","inside pubnub where now its first msg ");
                        pubnub.publish("my_channel", jsonMsgtoWhereNow, true, new Callback() {
                        });
                        lastMessageTime = String.valueOf(System.currentTimeMillis());

                        Log.i("TEST","first message" +firstMessage);
                    }


//                    if (Arrays.asList(arr).contains(UUID)) {
//
//                        firstMessage = false;
//                        Log.i("TEST","contains firstMessage set" +firstMessage);
//                    }else{
//
//                        firstMessage = true;
//                        Log.i("TEST","not contains firstMessage set" +firstMessage);
//                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        pubnub.whereNow(UUID, callback);



    }



    /**
     * load pubnub history
     */
    private void loadHistoryFromPubnub(String channel_name) {
        Log.i(TAG, "inside loadHistoryFromPubnub");
        Log.i(TAG, "inside loadHistoryFromPubnub channel name is" + channel_name);

        Callback callback = new Callback() {

            public void successCallback(String channel, Object response) {
                Log.i(TAG, "inside loadHistoryFromPubnub channel name is2" + channel);
                try {
                    JSONArray jsonArrayResponse = new JSONArray(response.toString());
                    JSONArray jsonArrayHistory = jsonArrayResponse.getJSONArray(0);
                    int jsonArrayHistoryLength = jsonArrayHistory.length();
                    // cacheMessages(jsonArrayHistory);
                    if (jsonArrayHistory.length() > 0) {
                        chatMessages.clear();// to remove cached messages
                        clearCache();
                        Log.i(TAG, "loadhistory not empty");
                        for (int i = 0; i < jsonArrayHistoryLength; i++) {
                            JSONObject jsonMsg = jsonArrayHistory.getJSONObject(i);


                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub jsonArrayResponse" + jsonArrayResponse);
                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub jsonArrayHistory" + jsonArrayHistory);
                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub" + jsonMsg);
                            displayMessage(jsonMsg);



                        }




                    }

                    else {

                        Log.i(TAG, "loadhistory empty");
                        if(!okyed && !oyed ) {
                            //displayDefaultMessage();  // if client have not okeyed or oyed , its old dealing room but its empty then we need to show him default message
                            //need to be handled
                        }


                        /*JSONObject jsonMsg = new JSONObject();
                        //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
                        jsonMsg.put("from", "DEFAULT");
                        //jsonMsg.put("to", "client");



                        jsonMsg.put("to", "client");
                        Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));


                        if(General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
//                            String ptype = General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE);
//                            Log.i(TAG, "role of user def 1 "+ptype);
                            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                            // Log.i(TAG, "role of user def 2 "+ptype.substring(0, 1).toUpperCase());
                            String json = gson.toJson(AppConstants.letsOye);
                            // Log.i(TAG, "role of user def 3 "+General.getSharedPreferences(DealConversationActivity.this,AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(DealConversationActivity.this,AppConstants.PTYPE).substring(1));
                            JSONObject jsonResponse = new JSONObject(json);
                            // Log.i(TAG, "role of user def 4 ");

                            Log.i(TAG,"Client have initiated enquiry for a " + jsonResponse.getString("property_type").substring(0, 1).toUpperCase() + jsonResponse.getString("property_type").substring(1) + " property (" + jsonResponse.getString("property_subtype") + ") within budget " + General.currencyFormat(jsonResponse.getString("price"))+".");
                            //Log.i(TAG, "AppConstants.letsOye u " + jsonResponse.getString("property_subtype"));
                            Log.i(TAG, "role of user def 5 ");
                            jsonMsg.put("message", "Client have initiated enquiry for a " + jsonResponse.getString("property_type").substring(0, 1).toUpperCase() + jsonResponse.getString("property_type").substring(1) + " property (" + jsonResponse.getString("property_subtype") + ") within budget " + General.currencyFormat(jsonResponse.getString("price"))+".");
                            Log.i(TAG, "role of user def 6 ");
                            //Log.i("TRACE","messageText is "+messageText);
                            //Log.i(TAG,"messageText is"+messageText);
                            //publish message



                        }
                        if(General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){

                            // prepare a default message now get ptype
                            String ptype = General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE);

                            jsonMsg.put("message", "Client have initiated enquiry for " + General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE).substring(1)
                                    + " property (" + General.getSharedPreferences(getApplicationContext(),AppConstants.PSTYPE) + ") within budget " + General.currencyFormat(General.getSharedPreferences(getApplicationContext(),AppConstants.PRICE)) + ".");


                        }*/
                        /*chatMessages.clear();
                        Message message = new Message();
                        message.setUserName(roleOfUser);
                        message.setMessageStatus(ChatMessageStatus.SENT);
                        message.setMessageText(body);
                        message.setUserType(userType);
                        // message.setMessageTime(new Date().getTime());
                        message.setMessageTime(Long.valueOf(timetoken)/10000);*/




                       /* chatMessages.add(message);*/
//                        displayMessage(jsonMsg);
                      /*  pubnub.publish(channel, jsonMsg, true, new Callback() {});*/
//                        chatMessages.clear();

                        //default deal time we are storing at default deal creation

                        Log.i(TAG, "Default message published");
                        lastMessageTime = String.valueOf(System.currentTimeMillis());





                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
            public void errorCallback(String channel, PubnubError error) {
            }
        };
        // pubnub.history(channel_name, 10, false, callback);
        pubnub.history(channel_name,true, 50,callback);
    }


    /**
     *  pubnub push notification
     */

    public void sendNotification(String channel_name) throws JSONException {

        Log.i("INSIDE PUSH NOTIFY","============");
        PnGcmMessage gcmMessage = new PnGcmMessage();

        JSONObject json = new JSONObject();

        try {
            json.put("a","android===");
        }
        catch (JSONException e)
        {

        }

        gcmMessage.setData(json);


        Log.i("INSIDE PUSH NOTIFY","============");
// Create APNS message

        PnApnsMessage apnsMessage = new PnApnsMessage();
        apnsMessage.setApsSound("melody");
        apnsMessage.setApsAlert("Hi from android !!!");
        apnsMessage.setApsBadge(4);
        apnsMessage.put("C ","3");

        PnMessage message = new PnMessage(pubnub, "", new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                Log.i("TRACE NOTIFICATION","Successfull "+message);
            }

            @Override
            public void errorCallback(String channel,PubnubError error)
            {
                Log.i("TRACE NOTIFICATION","Error "+error);
            }
        },gcmMessage);

        message.put("b","20");

        try {

            message.publish();
        }
        catch (PubnubException e){
            Log.i("INSIDE PUSH NOTIFY","ERROR============ "+e.getMessage());
        }

    }


    public void sendNotification111(String channel_name) throws JSONException {

        PnGcmMessage gcmMessage = new PnGcmMessage();

        JSONObject json = new JSONObject();

        try {
            json.put("a","1");
        }
        catch (JSONException e)
        {

        }

        gcmMessage.setData(json);


        // Create APNS message

        PnApnsMessage apnsMessage = new PnApnsMessage();
        apnsMessage.setApsSound("melody");
        apnsMessage.setApsAlert("Hi from android !!!");
        apnsMessage.setApsBadge(4);
        apnsMessage.put("C ","3");

        PnMessage message = new PnMessage(pubnub, channel_name, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                Log.i("TRACE NOTIFICATION","Successfull "+message);
            }

            @Override
            public void errorCallback(String channel,PubnubError error)
            {
                Log.i("TRACE NOTIFICATION","Error "+error);
            }
        },apnsMessage,gcmMessage);

        message.put("b","20");

        try {
            message.publish();
        }
        catch (PubnubException e){}
    }


    public void sendNotification1(String channel_name) {

        Log.i("Pubnub push","channel_name is "+channel_name);
        Log.i("Pubnub push","GCM id is "+General.getSharedPreferences(this,AppConstants.GCM_ID));

        pubnub.enablePushNotificationsOnChannel(
                channel_name,
                General.getSharedPreferences(this,AppConstants.GCM_ID));

        Log.i("Pubnub push","inside send notif with chanel name "+channel_name);
        PnGcmMessage gcmMessage = new PnGcmMessage();
        JSONObject jso = new JSONObject();
        try {
            jso.put("GCMSays", "hi");
        } catch (JSONException e) { }
        Log.i("Pubnub push","inside send notif with msg "+jso);
        gcmMessage.setData(jso);

        PnMessage message = new PnMessage(
                pubnub,
                channel_name,
                callback,
                gcmMessage);
        try {
            message.publish();
            Log.i("Pubnub push","inside send notif with msg published "+jso);
        } catch (PubnubException e) {
            Log.i("Pubnub push","inside send notif failed "+e);
            e.printStackTrace();
        }
    }

    public static Callback callback = new Callback() {
        @Override
        public void successCallback(String channel, Object message) {
            Log.i(TAG, "Success on Channel " + channel + " : " + message);
        }
        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.i(TAG, "Error On Channel " + channel + " : " + error);
        }
    };
    /**
     * all active userIds on channel
     */
    private void pubnubHereNow() {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println("pubnubHereNow "+response.toString());
                Log.i(TAG, "pubnubHereNow" + response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        pubnub.hereNow(channel_name, callback);
    }

    @OnClick(R.id.imgSuggestion)
    public void onImgSuggestion(View v) {

        if (suggestionRootView.getVisibility() == View.VISIBLE) {
            hideSuggestionBox();
        }
        else {
            showSuggestionBox();
        }

        //hide keyboard if opened and edit message focus
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            getWindow().getDecorView().clearFocus();
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        texrating.setText("Rating: " + rating);

    }


    private  void networkConnectivity(){
        Log.i("TRACE","networkConnectivity1");
        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
    }

    private void cacheMessages(JSONArray jsonArrayHistory){
        Log.i(TAG, "until cacheMessages called  ");
        //JSONArray jsonArrayHistory = loadFinalHistory();
        myRealm = General.realmconfig(this);


//        try {
//
//            //clear cache
//            Log.i(TAG,"until 3 ");
//            myRealm.beginTransaction();
//            Log.i(TAG,"until 4 ");
//            RealmResults<Message> result = myRealm.where(Message.class).equalTo(AppConstants.OK_ID,channel_name).findAll();
//            Log.i(TAG,"until result to del is 6 "+result);
//            result.clear();
//
//        }catch(Exception e){
//            Log.i(TAG,"Caught in the exception clearing cache "+e );
//        }
//        finally{
//            myRealm.commitTransaction();
//        }


        try {

            int jsonArrayHistoryLength = jsonArrayHistory.length();
            Log.i(TAG, "until here is the 1 jsonArrayHistory "+jsonArrayHistory);
            Log.i(TAG, "until here is the 1 jsonArrayHistoryLength "+jsonArrayHistoryLength);
//            myRealm = General.realmconfig(this);
            myRealm = General.realmconfig(this);
            myRealm.beginTransaction();
            for (int i = 0; i < jsonArrayHistoryLength; i++) {
                JSONObject jsonMsg = jsonArrayHistory.getJSONObject(i);



                Log.i(TAG, "until here is the 1 jsonMsg "+jsonMsg);

                if (jsonMsg.has("message")) {
                    JSONObject j = jsonMsg.getJSONObject("message");
                    String timetoken = jsonMsg.getString("timetoken");
                    Log.i(TAG, "until here is the 1  " + jsonMsg.getString("message"));
                    String from = j.getString("from");
                    String to = j.getString("to");
                    String body = j.getString("message");
                    Log.i(TAG,"until here is the 1 2 "+from+" "+to+" "+body);

                    message = new Message();
                    Log.i(TAG,"until here is the 1 2 ithe "+myRealm.isInTransaction());
//
                    message = myRealm.createObject(Message.class); //new Message();
                    Log.i(TAG,"until here is the 1 2 Tithe");
                    message.setOk_id(channel_name);
                    message.setMessage(body);
                    message.setTimestamp(timetoken);
                    message.setFrom(from);
                    message.setTo(to);

                    Log.i(TAG,"until here is the 246 "+body);
                    myRealm.copyToRealm(message);
                    Log.i(TAG,"until here is the 634 "+body);

//                    myRealm.executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            Log.i(TAG,"here is the 1 2 andar");
//                            Message message = realm.createObject(Message.class);
//                            message.setOk_id(channel_name);
//                            message.setMessage("body");
//                            message.setTimestamp("timetoken");
//                            message.setFrom("from");
//                            message.setTo("to");
//                            Log.i(TAG,"here is the 1 2 khali");
//                        }
//                    });


                    Log.i(TAG,"until here is the 1 3 I am back "+body);

                }
            }

            // myRealm.commitTransaction();


        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception in caching messages in Realm "+e);

        }
        finally{


            myRealm.commitTransaction();
            Log.i(TAG,"until fitra 3 "+myRealm.isInTransaction());
        }


//        JSONObject jo = new JSONObject();
//
//// populate the array
//        try {
//            jo.put("msg",msgs);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        General.setSharedPreferences(this,channel_name+AppConstants.CACHE,msgs.toString());
//        Log.i("CHAT","cache "+General.getSharedPreferences(this,channel_name+AppConstants.CACHE));

        try {

//            Realm myRealm = General.realmconfig(this);
            RealmResults<Message> results1 =
                    myRealm.where(Message.class).equalTo(AppConstants.OK_ID, channel_name).findAll();

//            Message myPuppy = myRealm.where(Message.class).equalTo(AppConstants.MOBILE_NUMBER, "+918483014575").findFirst();
//
//            Log.i(TAG, "my name is " + myPuppy.getName());

            for (Message c : results1) {
                Log.i(TAG, "until insiderou2 ");
                Log.i(TAG, "until insiderou3 " + c.getOk_id());
                Log.i(TAG, "until insiderou4 " + c.getTimestamp());
                Log.i(TAG, "until insiderou4 " + c.getMessage());
            }
        }catch(Exception e){
            Log.i(TAG,"Caught in the exception reading cache from realm "+e);
        }
        finally {
            myRealm.commitTransaction();
            Log.i(TAG,"until fitra 4 "+myRealm.isInTransaction());
        }

    }



    private void storeDealTime(){
        String dealTime;
        HashMap<String, String> dealTime1;
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        dealTime = General.getDealTime(this);


        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();


        if(gson.fromJson(dealTime, type) != null) {
            dealTime1 = gson.fromJson(dealTime, type);
            Log.i("dealtime","DealTime1 "+dealTime1);
        }
        else {
            dealTime1 = new HashMap<>();
            Log.i("dealtime","DealTime2 "+dealTime1);
        }


        Log.i("dealtime","DealTime "+channel_name);
        Log.i("dealtime","DealTime "+lastMessageTime);
        Log.i("dealtime","DealTime "+gson.fromJson(dealTime, type));
        if(lastMessageTime != null)
            dealTime1.put(channel_name,lastMessageTime);

        Gson g = new Gson();
        String hashMapString = g.toJson(dealTime1);
        General.saveDealTime(this, hashMapString);
        Log.i("dealtime","DealTime3 "+General.getDealTime(this));



        //store dealtimestamp in realm db
        Log.i(TAG,"lastMessageTime rapter "+lastMessageTime);
        try {
            if(lastMessageTime != null) {
                Realm myRealm = General.realmconfig(this);
                DealTime dealtime = new DealTime();

                dealtime.setOk_id(channel_name);
                dealtime.setTimestamp(lastMessageTime);


                myRealm.beginTransaction();
                myRealm.copyToRealmOrUpdate(dealtime);
                myRealm.commitTransaction();

            }
        }
        catch(Exception e){
            Log.i(TAG,"caught in exception deleting default droom timestamp");
        }

        try{
            Realm myRealm = General.realmconfig(this);
            RealmResults<DealTime> results1 =
                    myRealm.where(DealTime.class).findAll();
            Log.i(TAG, "insider timestamp " +results1);
            for (DealTime c : results1) {
                Log.i(TAG, "insider timestamp " + c.getOk_id());
                Log.i(TAG, "insider timestamp " + c.getTimestamp());
            }
        }
        catch(Exception e){}


    }

    private void clearCache(){
        myRealm = General.realmconfig(this);
        try {
            //clear cache
            Log.i(TAG,"until 31 ");

            myRealm.beginTransaction();
            Log.i(TAG,"until 41 ");
            RealmResults<Message> result = myRealm.where(Message.class).equalTo(AppConstants.OK_ID,channel_name).findAll();
            Log.i(TAG,"until end result to del is 5 "+result);
            result.clear();

        }catch(Exception e){
            Log.i(TAG,"Caught in the exception clearing cache "+e );
        }
        finally{
            myRealm.commitTransaction();
            Log.i(TAG,"until fitra 1 "+myRealm.isInTransaction());
        }
    }

    private void loadFinalHistory(){
        Callback callback = new Callback() {
            JSONArray jsonArrayHistory;
            public void successCallback(String channel, Object response) {
                Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo");
                try {
                    JSONArray jsonArrayResponse = new JSONArray(response.toString());
                    Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayResponse "+jsonArrayResponse);
                    jsonArrayHistory = jsonArrayResponse.getJSONArray(0);
                    Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayHistory "+jsonArrayHistory);
                    cacheMessages(jsonArrayHistory);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
            public void errorCallback(String channel, PubnubError error) {
            }
        };
        // pubnub.history(channel_name, 10, false, callback);
        pubnub.history(channel_name,true, 50,callback);
        Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayHistory 1 "+jsonArrayHistory);


    }

    private void displayDefaultMessage(){
        Log.i(TAG, "displayDefaultMessage called ");
        try {
            JSONObject jsonMsg = new JSONObject();




            //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
            jsonMsg.put("from", "DEFAULT");
            //jsonMsg.put("to", "client");


            jsonMsg.put("to", "client");
            Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));


            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
//                            String ptype = General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE);
//                            Log.i(TAG, "role of user def 1 "+ptype);
                final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                // Log.i(TAG, "role of user def 2 "+ptype.substring(0, 1).toUpperCase());
                String json = gson.toJson(AppConstants.letsOye);
                // Log.i(TAG, "role of user def 3 "+General.getSharedPreferences(DealConversationActivity.this,AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(DealConversationActivity.this,AppConstants.PTYPE).substring(1));
                JSONObject jsonResponse = new JSONObject(json);
                // Log.i(TAG, "role of user def 4 ");

                Log.i(TAG, "Client have initiated enquiry for a " + jsonResponse.getString("property_type").substring(0, 1).toUpperCase() + jsonResponse.getString("property_type").substring(1) + " property (" + jsonResponse.getString("property_subtype") + ") within budget " + General.currencyFormat(jsonResponse.getString("price")) + ".");
                //Log.i(TAG, "AppConstants.letsOye u " + jsonResponse.getString("property_subtype"));
                Log.i(TAG, "role of user def 5 ");
                jsonMsg.put("message", "Client have initiated enquiry for a " + jsonResponse.getString("property_type").substring(0, 1).toUpperCase() + jsonResponse.getString("property_type").substring(1) + " property (" + jsonResponse.getString("property_subtype") + ") within budget " + General.currencyFormat(jsonResponse.getString("price")) + ".");
                Log.i(TAG, "role of user def 6 ");
                //Log.i("TRACE","messageText is "+messageText);
                //Log.i(TAG,"messageText is"+messageText);
                //publish message


            }
            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {

                // prepare a default message now get ptype
                String ptype = General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE);

                jsonMsg.put("message", "Client have initiated enquiry for " + General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE).substring(1)
                        + " property (" + General.getSharedPreferences(getApplicationContext(), AppConstants.PSTYPE) + ") within budget " + General.currencyFormat(General.getSharedPreferences(getApplicationContext(), AppConstants.PRICE)) + ".");


            }

            displayMessage(jsonMsg);
            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
        }
        catch(Exception e){}
        finally {
            //chatMessages.clear();   // clears chatmessages to avoid redundant messages after loadhistory called(Clears out cached msgs before loading actual)
        }
    }

    private void displayImgMessage(String bucketName, String imgName){
        Log.i(TAG, "displayImgMessage called ");
        try {
            JSONObject jsonMsg = new JSONObject();


            jsonMsg.put("timestamp",String.valueOf(System.currentTimeMillis()));

            //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
            jsonMsg.put("from", "IMG");
            //jsonMsg.put("to", "client");
            jsonMsg.put("imageUrl", "https://s3.ap-south-1.amazonaws.com/"+bucketName+"/"+imgName);



            Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));


            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
//                            String ptype = General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE);
//                            Log.i(TAG, "role of user def 1 "+ptype);
                jsonMsg.put("to", "broker");


                Log.i(TAG, "role of user def 6 ");

            }
            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                jsonMsg.put("to", "client");
                // prepare a default message now get ptype


            }
            jsonMsg.put("message", "https://s3.ap-south-1.amazonaws.com/"+bucketName+"/"+imgName);
            Log.i("yoyoyo","urlo "+jsonMsg);



            displayMessage(jsonMsg);
            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
        }
        catch(Exception e){}


    }

    // Amazon S3 service///


    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:d166d43a-32b3-4690-9975-aed1c61f2b28", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     * @param fileToU
     */
    public void setFileToUpload(File fileToU){
        fileToUpload = fileToU;
        imageName = "droid"+String.valueOf(System.currentTimeMillis());


        TransferObserver transferObserver = transferUtility.upload(
                "oyeok-chat-images",     /* The bucket to upload to */
                imageName,    /* The key for the uploaded object */
                fileToUpload       /* The file where the data to upload exists */
        );

        transferObserverListener(transferObserver);
        //



    }

    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     * @param
     **/
    public void setFileToDownload(){

        TransferObserver transferObserver = transferUtility.download(
                "oyeok-chat-images",     /* The bucket to download from */
                "photos11234097.png",    /* The key for the object to download */
                fileToDownload        /* The file to download the object to */
        );

        transferObserverListener(transferObserver);

    }

    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we get status of uploading and downloading file,
     * to display percentage of the part of file to be uploaded or downloaded to S3
     * It displays an error, when there is a problem in  uploading or downloading file to or from S3.
     * @param transferObserver
     */

    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("imageu statechange", state+"");
                if(state.toString().equalsIgnoreCase("COMPLETED")){
                    /*General.setSharedPreferences(DealConversationActivity.this,AppConstants.UPLOADED_IMAGE_PATH,fileToUpload.toString());
                    Log.i("asakasa","asakasa123 "+General.getSharedPreferences(DealConversationActivity.this,AppConstants.UPLOADED_IMAGE_PATH));*/
                    sendMessage("https://s3.ap-south-1.amazonaws.com/oyeok-chat-images/"+imageName);
                    //displayImgMessage("oyeok-chat-images","photos11234097.png");

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.i("imageu percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("imageu error","error"+ex);
            }

        });
    }



    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onBackPressed() {

        General.setSharedPreferences(this, channel_name + AppConstants.CACHE, cachedmsgs.toString());
        Log.i("CHAT", "cache " + General.getSharedPreferences(this, channel_name + AppConstants.CACHE));

        storeDealTime();

        //
        loadFinalHistory();

//        try {
//            RealmResults<Message> results1 =
//                    myRealm.where(Message.class).findAll();
//            Log.i(TAG, "insiderr cached msgs is the "+results1);
//            for (Message c : results1) {
//                Log.i(TAG, "insiderr cached msgs ");
//                Log.i(TAG, "insiderr cached msgs " + c.getMessage());
//                Log.i(TAG, "insiderr cached msgs " + c.getTimestamp());
//            }
//
//        }
//        catch(Exception e){
//            Log.i(TAG, "Caught in exception read cache msgs realm "+e);
//        }
//
//        try {
//
//            Realm myRealm = General.realmconfig(this);
//            RealmResults<Message> results1 =
//                    myRealm.where(Message.class).equalTo("message", "Hi").findAll();
//
//
//
//            for (Message c : results1) {
//                Log.i(TAG, "insidero2 ");
//                Log.i(TAG, "insidero3 " + c.getOk_id());
//
//            }
//        }catch(Exception e){Log.i(TAG, "Caught in exception read cache msgs realm yo "+e);}

        //onbackpressed save time of last message , so get time n a l=global variable

//        SharedPreferences appSharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(this.getApplicationContext());
//        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(cachedmsgs);
//        prefsEditor.putString(channel_name+AppConstants.CACHE, json);
//        prefsEditor.commit();

//        Set<JSONObject> set = new HashSet<>();
//        set.addAll(cachedmsgs);
//        General.saveSet(this,channel_name+AppConstants.CACHE,set.toString());
//        General.setSharedPreferences(this,channel_name+AppConstants.CACHE,cachedmsgs.toString());
//       Log.i("CACHE","cache got from shared  "+General.getSharedPreferences(this,channel_name+AppConstants.CACHE));
//
       /* if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            Intent back = new Intent(this, ClientDealsListActivity.class); // to refresh adapter to display newly saved last message time
            startActivity(back);
        } else {

            Intent back = new Intent(this, BrokerDealsListActivity.class); // to refresh adapter to display newly saved last message time
            startActivity(back);

            // super.onBackPressed();
        }*/
        if(AppConstants.CLIENT_DEAL_FLAG == true){
            Intent back = new Intent(this, ClientDealsListActivity.class); // to refresh adapter to display newly saved last message time
            startActivity(back);
            AppConstants.CLIENT_DEAL_FLAG = false;
        }else if(AppConstants.BROKER_DEAL_FLAG == true){
            Intent back = new Intent(this, BrokerDealsListActivity.class); // to refresh adapter to display newly saved last message time
            startActivity(back);
            AppConstants.BROKER_DEAL_FLAG = false;
        }

    }

}