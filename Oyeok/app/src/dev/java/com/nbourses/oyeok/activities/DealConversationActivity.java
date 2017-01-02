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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateStatus;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.adapters.ChatListAdapter;
import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserSubtype;
import com.nbourses.oyeok.enums.ChatMessageUserType;
import com.nbourses.oyeok.enums.DealStatusType;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ChatMessage;
import com.nbourses.oyeok.realmModels.DealStatus;
import com.nbourses.oyeok.realmModels.DealTime;
import com.nbourses.oyeok.realmModels.DefaultDeals;
import com.nbourses.oyeok.realmModels.Message;
import com.nbourses.oyeok.realmModels.NotifCount;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.util.Log.i;


public class DealConversationActivity extends AppCompatActivity implements OnRatingBarChangeListener, AbsListView.OnScrollListener {


    @Bind(R.id.edtTypeMsg)
    TextView edtTypeMsg;

    @Bind(R.id.imgSendMsg)
    ImageView imgSendMsg;

    @Bind(R.id.chat_list_view)
    ListView chatListView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

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
    private Boolean paginate = false;
    private Long timetoken;

    private SubscribeCallback s;

    private String UUUID;

    private TextView texrating;

   // private DBHelper dbHelper;

    private String onlineColor;
    private String offlineColor;
    private String typingColor;

    private static final String TAG = "DealConversationActivit";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private PubNub pubnub;
    private String channel_name = "";
    private ChatListAdapter listAdapter;
    private ArrayList<ChatMessage> chatMessages;
    private ArrayList<ChatMessage> chatMessagesCopy = new ArrayList<>();
    private String UUID;
    private Boolean isUnverified = false;
    private Boolean isDefault = false;
    private ArrayList<String> defaultOkIds = new ArrayList<String>();

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
    private JSONArray jsonArrayHistory = new JSONArray();
    private HashMap<String, Integer> listings;
    private Boolean oyed = false;
    private Boolean okyed = false;

    private Boolean UnverifiedDeal = false;

    // File fileToUpload = new File("/storage/sdcard0/Pictures/Screenshots/photos.png");
    private File fileToUpload = new File("/storage/emulated/0/DCIM/Facebook/FB_IMG_1467990952511.jpg");
    private File fileToDownload = new File("/storage/sdcard0/Pictures/OYEOK");
    public AmazonS3 s3;
    public TransferUtility transferUtility;
    private String imageName = null;
    private String messageTyped;
    //for default unverified msg
    private String locality;
    private String specs;
    private String name = "Dealing Room";



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    /*private BroadcastReceiver unverifiedDeal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = getIntent().getExtras();
            UnverifiedDeal = bundle.getBoolean("unverfieddeals");
        }
    };
*/
    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();
        }
    };

    private BroadcastReceiver chatMessageReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"message received is ");
            Log.i(TAG,"message received is "+intent.getExtras().get("bundle"));
            Log.i(TAG,"message received is  1"+intent.getExtras());

            Bundle b = (Bundle)intent.getExtras();

          /* Bundle b = (Bundle)intent.getExtras().get("bundle");
            Log.i(TAG,"message received is b"+b);*/

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
            setState("isTyping","true");
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

        //dbHelper = new DBHelper(getBaseContext());

//        IntentFilter filter = new IntentFilter("shine");
//        LocalBroadcastManager.getInstance(this).registerReceiver(handlePushNewMessage, filter);
        // UUID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);

        //String userId = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);

       // if(userId == null)
            UUID = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);


        if(!General.getSharedPreferences(DealConversationActivity.this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
               UUUID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
        }else{
            UUUID = UUID;
        }
       // else
        // UUID = userId;

        i("WHERENOW", "UUID lotto " + UUID);

        //pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY,true);
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(AppConstants.PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(AppConstants.PUBNUB_PUBLISH_KEY);

        if(!General.getSharedPreferences(DealConversationActivity.this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){

            pnConfiguration.setUuid(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));
        }else{

            pnConfiguration.setUuid(UUID);
        }




        pubnub = new PubNub(pnConfiguration);


        init();

    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(chatMessageReceived, new IntentFilter(AppConstants.CHAT_MESSAGE_RECIEVED));

        super.onResume();

        if(channel_name.equals("my_channel"))
        {
            //subscribeToPush(UUID);
            setupPubnub(UUID);
        }
        else {

            setupPubnub(channel_name);
        }

        Log.i(TAG,channel_name+"   "+ General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID)+" marine2    "+General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI));

        if(channel_name.equalsIgnoreCase(AppConstants.SUPPORT_CHANNEL_NAME))
            General.setSharedPreferences(this,AppConstants.CHAT_OPEN_OK_ID,General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI));
        else
            General.setSharedPreferences(this,AppConstants.CHAT_OPEN_OK_ID,channel_name);

        Log.i(TAG,channel_name+"   "+ General.getSharedPreferences(this, AppConstants.CHAT_OPEN_OK_ID)+" marine3    "+General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI));


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);
//        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(chatMessageReceived);
        super.onPause();
        General.setSharedPreferences(this,AppConstants.CHAT_OPEN_OK_ID,null);

          pubnub.removeListener(s);
          pubnub.unsubscribe();
        //  if (pubnub != null)
        //   pubnub.unsubscribeAll();
    }

    private void init() {
        int labelColor = getResources().getColor(R.color.greenish_blue);
        onlineColor = String.format("%X", labelColor).substring(2);

        int labelColor1 = getResources().getColor(R.color.yellow);
        offlineColor = String.format("%X", labelColor1).substring(2);

        int labelColor2 = getResources().getColor(R.color.blue_sky);
        typingColor = String.format("%X", labelColor2).substring(2);

        //if there is no SD card, create new directory objects to make directory on device
        if (Environment.getExternalStorageState() == null) {
            //create new file directory object
            File directory = new File(Environment.getDataDirectory()
                    + "/oyeok/");

            // if no directory exists, create new directory
            if (!directory.exists()) {
                directory.mkdir();
            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File directory = new File(Environment.getExternalStorageDirectory()
                    + "/oyeok/");

            // if no directory exists, create new directory to store test
            // results
            if (!directory.exists()) {
                directory.mkdir();
            }
        }

        credentialsProvider();

        setTransferUtility();



        imgSendMsg.setImageResource(R.drawable.attachment);
        imgSendMsg.setTag("attachment");

        edtTypeMsg.addTextChangedListener(edtTypeMsgListener);
        chatMessages = new ArrayList<>();

        Log.i(TAG, "chatMessages are" + chatMessages);

        Bundle bundle = getIntent().getExtras();

        Log.i(TAG,"bundle is "+bundle);

        try {
            if (bundle != null) {
                Log.i(TAG,"bundle is 1 "+bundle);
                if (bundle.containsKey("OkAccepted") && bundle.containsKey(AppConstants.OK_ID)) {
                    channel_name = bundle.getString(AppConstants.OK_ID);

                    if (bundle.getString("OkAccepted").equalsIgnoreCase("yes")) {

                        Log.i(TAG,"dealconv okyed");

                        okyed = true;
                        General.storeDealTime(bundle.getString(AppConstants.OK_ID), this);
                        listings = new HashMap<String, Integer>();

                        listings = (HashMap<String, Integer>) bundle.getSerializable("listings");

                        Log.i(TAG,"listings for cardview "+listings);

                    }

                }

                else if (bundle.containsKey("Oyed") && bundle.containsKey(AppConstants.OK_ID)) {
                    channel_name = bundle.getString(AppConstants.OK_ID);

                    if (bundle.getString("Oyed").equalsIgnoreCase("yes")) {
                        Log.i(TAG,"dealconv oyed");
                        oyed = true;

                        //chatMessages.clear();

                    }

                }

                else if(bundle.containsKey(AppConstants.OK_ID)){
                    Log.i(TAG,"listing ghya 1 "+channel_name+"   "+General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI));
                    if(bundle.getString(AppConstants.OK_ID).equalsIgnoreCase(General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI)))
                        channel_name = AppConstants.SUPPORT_CHANNEL_NAME;
                    else
                    channel_name = bundle.getString(AppConstants.OK_ID); //my_channel if came from root item

                }


                Log.i(TAG,"listing ghya 2 "+channel_name);


                if(channel_name.equalsIgnoreCase("my_channel"))
                {
                    String uuid = General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI);
                    Log.i("MY CHANNEL ","=== ====== ====== ===== ====== "+uuid);
                   // loadHistoryFromPubnub(uuid);
                    setSupportActionBar(mToolbar);
                    getSupportActionBar().setTitle("OyeOk Assistant");




                   /* if(firstMessage)
                    {

                        JSONObject jsonMsg = new JSONObject();
                        //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
                        jsonMsg.put("_from", uuid);
                        jsonMsg.put("message", "Looking for CRM");
                        jsonMsg.put("to", "my_channel");
                        jsonMsg.put("status","");

                        sendNotification(jsonMsg);

                    }*/
//                    pubnubWhereNow(uuid);
                }
                else{
                    setSupportActionBar(mToolbar);
                    if(bundle.containsKey(AppConstants.NAME)) {
                        name = bundle.getString(AppConstants.NAME);
                        if(name.equalsIgnoreCase("Searching brokers"))
                            getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\">...</font>", offlineColor)));
                        else
                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (offline)</font>", offlineColor)));


                    }
                        else {
                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (offline)</font>", offlineColor)));
                        //getSupportActionBar().setTitle("Dealing Room");
                    }



                }
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                Log.i(TAG,"bundle is 2 "+bundle);
                if(bundle.containsKey(AppConstants.OYE_ID)){
                    Log.i(TAG,"perser 1 ");
                    Log.i(TAG,"bundle is 3 "+bundle);
                    if (bundle.getString(AppConstants.OYE_ID).toLowerCase().contains("unverified_user".toLowerCase())) {
                        Log.i(TAG,"perser 2 ");
                        isUnverified = true;
                        locality = bundle.getString(AppConstants.LOCALITY);
                        specs = bundle.getString(AppConstants.SPEC_CODE);
                        Log.i(TAG,"perser 3 "+isUnverified);

                    }
                    else {
                        Realm myRealm = General.realmconfig(this);
                        myRealm.beginTransaction();
                        DefaultDeals result = myRealm.where(DefaultDeals.class).equalTo(AppConstants.OK_ID, bundle.getString(AppConstants.OYE_ID)).findFirst();

                        if(result != null)
                            isDefault = true;

                    }
                }
            }
        }
        catch(Exception e){
            Log.i(TAG,"caught in exception saving accept deal time "+e);
        }

        Log.i("Deals Conv Act","channel name is the "+General.getSharedPreferences(this,AppConstants.GCM_ID));
        specCode = bundle.getString(AppConstants.SPEC_CODE);

        isDefaultDeal = bundle.getBoolean("isDefaultDeal");

        Log.i("TRACE DEALS FLAG 3", "FLAG " + isDefaultDeal);

        //test pubnub gcm

    /*    pubnub.enablePushNotificationsOnChannel(channel_name, General.getSharedPreferences(this,AppConstants.GCM_ID), new Callback() {
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
        });*/


        listAdapter = new ChatListAdapter(chatMessages,isDefaultDeal, this);
        chatListView.setAdapter(listAdapter);

        chatListView.setOnScrollListener(this);
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
        Log.i(TAG,"imageshare yo 3"+v.getTag());
        Log.i(TAG,"imageshare yo 3"+currentImg);
        if(currentImg != null)
        if (currentImg != "") {
            if (currentImg.equals("attachment")) {
                Log.i(TAG,"imageshare yo 1");
                //open file chooser options
               if(verifyStoragePermissions(DealConversationActivity.this))
                selectImage();
            }
            else if(currentImg.equals("message")) {
                Log.i(TAG,"type and send 1"+currentImg);

                Log.i(TAG,"imageshare 2 edittypemsg "+edtTypeMsg.getText().toString());
                messageTyped = edtTypeMsg.getText().toString();
                edtTypeMsg.setText("");
                //send message

                Log.i("type and send 2"," "+channel_name);
                final ChatMessage message = new ChatMessage();
                message.setUserName("self");
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(messageTyped);
                message.setUserType(ChatMessageUserType.SELF);
                message.setMessageTime(System.currentTimeMillis());
                Log.i(TAG,"type and send 309"+message.getMessageTime());
                Log.i(TAG,"type and send 3"+chatMessages);
                chatMessages.add(message);
                Log.i(TAG,"type and send 4"+chatMessages);
                listAdapter.notifyDataSetChanged();
                setState("online","true");


               /* try {
                    Log.i(TAG,"type and send 5");
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    Log.i(TAG,"type and send 6");
                    e.printStackTrace();
                }
*/

                chatListView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        chatListView.setSelection(listAdapter.getCount());
                        //
                    }

                });


if(!channel_name.equalsIgnoreCase("my_channel") && !channel_name.equalsIgnoreCase(General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI)) ){
    Log.i(TAG,"uil");
    checkLocalBlockStatus();
}
  else{
    Log.i(TAG,"suil");
    sendMessage(messageTyped);

}


               // Log.i(TAG,"type and send 7"+chatMessages);
              //  chatMessages.remove(chatMessages.size()-1);
        }
        //imgSendMsg.setClickable(false);
       // edtTypeMsg.setText("");
    }
    }

    /**
     * pubnub setup
     */

    private void subscribeToPush(String channel_name){
        Log.i(TAG,"result result result "+channel_name);
        pubnub.addPushNotificationsOnChannels()
                .pushType(PNPushType.GCM)
                .channels(Arrays.asList(channel_name))
                .deviceId(General.getSharedPreferences(this,AppConstants.GCM_ID))
        .async(new PNCallback<PNPushAddChannelResult>() {
            @Override
            public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                Log.i(TAG,"result result result "+result.toString()+" " +status.getUuid()+" "+status.isError());
                // handle response.
            }
        });
    }

    private void setState(String state, String value){
        Map<String, Object> myState = new HashMap<>();
        myState.put(state, value);

        pubnub.setPresenceState()
                .uuid(pubnub.getConfiguration().getUuid())
                .channels(Arrays.asList(channel_name))
                .state(myState).async(new PNCallback<PNSetStateResult>() {
            @Override
            public void onResponse(PNSetStateResult result, PNStatus status) {
                try {
                    // handle set state response
                    Log.i(TAG, "zxc" + result.getState());
                    //getState();

                }catch(Exception e){}

            }
        });
    }


    private void herenow() {
        pubnub.hereNow()
                // tailor the next two lines to example
                .channels(Arrays.asList(channel_name))
                .includeUUIDs(true)
                .includeState(true)
                .async(new PNCallback<PNHereNowResult>() {
                    @Override
                    public void onResponse(PNHereNowResult result, PNStatus status) {
                        try {
                            if (status.isError()) {
                                // handle error
                                return;
                            }


                            for (PNHereNowChannelData channelData : result.getChannels().values()) {
                                System.out.println("---");
                                System.out.println("zxc4 channel:" + channelData.getChannelName());
                                System.out.println("zxc4 occupancy: " + channelData.getOccupancy());
                                System.out.println("zxc4 occupants:");
                                for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                                    System.out.println("zxc4 uuid: " + occupant.getUuid() + " state: " + occupant.getState());
                                    JSONObject j = new JSONObject(occupant.getState().toString());
                                    if (!occupant.getUuid().equalsIgnoreCase(pubnub.getConfiguration().getUuid())) {
                                        if (j.getString("online").equalsIgnoreCase("true")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (online)</font>", onlineColor)));

                                                }
                                            });
                                            return;
                                        } else if (j.getString("online").equalsIgnoreCase("false")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (offline)</font>", offlineColor)));


                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                        catch(Exception e){}

                    }

                });
    }




    private void getState(){
        pubnub.getPresenceState()
                .channels(Arrays.asList(channel_name))
                .async(new PNCallback<PNGetStateResult>() {
                    @Override
                    public void onResponse(PNGetStateResult result, PNStatus status) {
                        try{
                            Log.i(TAG,"zxc1"+result.getStateByUUID());
                            JSONObject j = new JSONObject(result.getStateByUUID().get(channel_name).toString());

                            if(j.getString("online").equalsIgnoreCase("true")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (online)</font>", onlineColor)));

                                    }
                                });
                            }
                            else if(j.getString("online").equalsIgnoreCase("false")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (offline)</font>", offlineColor)));


                                    }
                                });
                            }

                        }catch(Exception e){

                        }
                    }

                    });
    }

    private void setupPubnub(final String channel_name) {
        Log.i("WHERENOW", "4 lotto "+channel_name);
        s = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                Log.i(TAG, "PUBNUB subscribe status "+pubnub.getConfiguration().getUuid()+" "+channel_name);
                // subscribeToPush(channel_name);


                if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                    if(!channel_name.equalsIgnoreCase(General.getSharedPreferences(DealConversationActivity.this,AppConstants.TIME_STAMP_IN_MILLI))) {

                        //getState();
                        herenow();
                        setState("online", "true");

                    }

                }
                else if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    Log.i(TAG, "PUBNUB subscribe status PNUnexpectedDisconnectCategory "+pubnub.getConfiguration().getUuid()+" "+channel_name);
                    // internet got lost, do some magic and call reconnect when ready
                    pubnub.reconnect();

                } else if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                    // do some magic and call reconnect when ready
                    Log.i(TAG, "PUBNUB subscribe status PNTimeoutCategory"+pubnub.getConfiguration().getUuid()+" "+channel_name);
                    pubnub.reconnect();

                } else {

                }


            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {

                try {
                    System.out.println("PUBNUB message  1 "+message);
                    Log.i(TAG, "PUBNUB message status "+message.getTimetoken());
                    if (message.getMessage().has("pn_gcm")) {



                        String j = message.getMessage().get("pn_gcm").get("data").toString();
                        // JSONObject j = new JSONObject(historyItemResult.getEntry().get("pn_gcm").toString());
                        Log.i(TAG, "history is tha 7 " + j);
                        JSONObject jsonObj = new JSONObject(j);
                        jsonObj.put("timetoken",message.getTimetoken().toString());
                        String sender;
                        if(General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("yes"))
                        {
                            sender = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
                        }
                        else {
                            sender = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);
                        }
                        if(!jsonObj.getString("_from").equalsIgnoreCase(sender)){
                            displayMessage(jsonObj);
                        }

                    }
                }catch(Exception e){
                    Log.i(TAG,"Caught in exception displaying recieved msg "+e);
                }

                /*try {
                    Log.i("WHERENOW", "6 ");

                    JSONObject jsonMsg = (JSONObject) message;

                    Log.i(TAG, "pubnub setup success" + jsonMsg);
                    try{
                        if (!jsonMsg.getJSONObject("pn_gcm").getJSONObject("data").getString("_from").equalsIgnoreCase(General.getSharedPreferences(DealConversationActivity.this, AppConstants.USER_ID)) && !jsonMsg.getJSONObject("pn_gcm").getJSONObject("data").getString("_from").equalsIgnoreCase(General.getSharedPreferences(DealConversationActivity.this, AppConstants.TIME_STAMP_IN_MILLI))) {

                            displayMessage(jsonMsg);
                        }
                    }catch(Exception e){
                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                }*/

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                try {
                    Log.i(TAG,"presence timeout "+presence.getEvent());
                    Log.i(TAG, "state is set PUBNUB presence status bro" + channel_name + " " + presence.getUuid() + "   " + presence.getState() + "   " + presence.getChannel());
                    if (!presence.getUuid().equalsIgnoreCase(pubnub.getConfiguration().getUuid()) && presence.getChannel().equalsIgnoreCase(channel_name)) {
                        Log.i(TAG, "state is set PUBNUB presence status bro 5 " + presence.getUuid() + "   " + presence.getState() + "   " + presence.getChannel());
                        if (presence.getState().has("online")) {
                            if (presence.getState().get("online").textValue().equalsIgnoreCase("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (online)</font>", onlineColor)));
                                        setState("online", "true");
                                        //  getSupportActionBar().setTitle(name+" (online)");

                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (offline)</font>", offlineColor)));
                                        setState("online", "true");
                                        //getSupportActionBar().setTitle(name+" (offline)");

                                    }
                                });
                            }
                        }

                        if (presence.getState().has("isTyping")) {
                            if (presence.getState().get("isTyping").textValue().equalsIgnoreCase("true")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (typing...)</font>", onlineColor)));
                                        setState("online", "true");

                                        // getSupportActionBar().setTitle(name + " (typing...)");

                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        getSupportActionBar().setTitle(Html.fromHtml(String.format(name + "<font color=\"#%s\"> (online)</font>", onlineColor)));
                                        setState("online", "true");

                                        // getSupportActionBar().setTitle(name+" (online)");

                                    }
                                });
                            }
                        }


                    }
                }
                catch(Exception e){}
            }
        };

        pubnub.addListener(s);


        Log.i(TAG,"PUBNUB publish channel 1 "+channel_name);
        if(channel_name.equalsIgnoreCase("my_channel"))
            pubnub.subscribe().channels(Arrays.asList(General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI))).withPresence().execute();
        else
            pubnub.subscribe().channels(Arrays.asList(channel_name)).withPresence().execute();






//        chatMessages.clear();
//        listAdapter.notifyDataSetChanged();
        Log.i("WHERENOW", "3 ");

        try {
            Log.i("WHERENOW", "4 ");

            //pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);



            Log.i(TAG, "before loadHistoryFromPubnub");

            if(!channel_name.equals("my_channel")) { //if not support chat

                if(okyed){

                    Log.i("SETUPPUBNUB","OKYED ============= channel_name "+channel_name);

                    okyed = false;
                   // displayCardView();
                    loadHistoryFromPubnub(channel_name);
                    displayDefaultMessage();
                    //chatMessages.clear();

                }

                else if(oyed) {Log.i("SETUPPUBNUB","OYED =============");

                    oyed = false;
                    displayDefaultMessage();

                    //chatMessages.clear();
                }

                else {
                    Log.i("SETUPPUBNUB"," ================ "+channel_name);

                   displayMessage();
                    loadHistoryFromPubnub(channel_name);
                }


            }
            else {
                Log.i("SETUPPUBNUB","MY_CHANNEL ================");
             displayMessage();
                loadHistoryFromPubnub(channel_name);
            }
//            if(!channel_name.equals("my_channel"))

            /*{
                loadHistoryFromPubnub(channel_name);
            }*/

   Log.i(TAG,"gcm id is thae "+General.getSharedPreferences(this,AppConstants.GCM_ID)+" "+channel_name);
            pubnub.addPushNotificationsOnChannels()
                    .pushType(PNPushType.GCM)
                    .channels(Arrays.asList(channel_name))
                    .deviceId(General.getSharedPreferences(this,AppConstants.GCM_ID))
                    .async(new PNCallback<PNPushAddChannelResult>() {
                        @Override
                        public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                            Log.i(TAG,"result result result "+result+" " +status.getUuid()+" "+status.isError());
                            // handle response.


                            getChannelsPush();
                        }
                    });

        }
        catch (Exception e) {
            e.getMessage();
            Log.i("WHERENOW", "6 ");
        }
    }

    private void getChannelsPush(){

        pubnub.auditPushChannelProvisions()
                .deviceId(General.getSharedPreferences(this,AppConstants.GCM_ID))
                .pushType(PNPushType.GCM)
                .async(new PNCallback<PNPushListProvisionsResult>() {
                    @Override
                    public void onResponse(PNPushListProvisionsResult result, PNStatus status) {
                        try {
                            Log.i("getChannelsPush", "getChannelsPush " + result.getChannels());
                        }catch(Exception e){

                        }
                    }
                });

    }
    private void selectImage() {
        final CharSequence[] items = { /*"Take Photo",*/ "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DealConversationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Choose from Library")) {


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

                    Log.i("Cancel","======================= ");
                    dialog.dismiss();
                }
                else
                {
                    Log.i("Cancel"," 2222 222====================== ");
                    dialog.dismiss();
                }

            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "lolwa imagewa 3 "+data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            Log.d(TAG, "lolwa imagewa uri "+uri);

            fileToUpload = new File(getRealPathFromURI(this,uri));
            Log.i(TAG, "lolwa imagewa uri fileToUpload "+fileToUpload);

            setFileToUpload(saveBitmapToFile(fileToUpload));

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

    private File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);


            Log.i("JPEG IMAGE","===================");
            return file;
        } catch (Exception e) {
            return null;
        }
    }



    private void displayPaginatedMessage(JSONObject jsonMsg) {
        try {

            String user_id = null;
            String body = null;
            String timetoken = null;
            ChatMessageUserType userType = null;
            ChatMessageUserSubtype userSubtype = null;
            String imageUrl = null;
            String imageName = null;
            String FROM = null;
            String msgStatus = null;
            final ChatMessage message = new ChatMessage();
            String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";
            String userID = null;

            if(General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("yes"))
            {
                userID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
            }
            else
                userID = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);



            Log.i(TAG,"inside displaymessge =====  jsonMSG "+jsonMsg);

            if(jsonMsg.has("from"))
                FROM = jsonMsg.getString("from");
            else {
                FROM = jsonMsg.getString("_from");
            }

            Log.i(TAG," =====  _from "+msgStatus);

            if(jsonMsg.has("status"))
                msgStatus = jsonMsg.getString("status");

            /*else if(jsonMsg.getString("message").contains("https://"))
                msgStatus = "IMG";*/
            else
                msgStatus = null;


            if(jsonMsg.has("timetoken"))
                timetoken = jsonMsg.getString("timetoken");

            else
                timetoken = null;


            Log.i(TAG," after assigning =====  _from "+msgStatus);
            if (jsonMsg.has("message") && FROM != null && jsonMsg.has("to")) {

                body = jsonMsg.getString("message");


                if (msgStatus.equalsIgnoreCase("DEFAULT") || msgStatus.equalsIgnoreCase("SYSTEM") || msgStatus.equalsIgnoreCase("OYES") || msgStatus.equalsIgnoreCase("OKS")){
                    Log.i("CONVER", "DEFAULT set");
                    if(msgStatus.equalsIgnoreCase("OKS") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        return;
                    if(msgStatus.equalsIgnoreCase("OYES") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                        return;
                    if(msgStatus.equalsIgnoreCase("DEFAULT") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                        return;
                    if(msgStatus.equalsIgnoreCase("SYSTEM") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                        return;
                    userType = ChatMessageUserType.DEFAULT;
                }

                else if (msgStatus.equalsIgnoreCase("IMG") || msgStatus.equalsIgnoreCase("OYEI") || msgStatus.equalsIgnoreCase("OKI")){

                    Log.i("TAG","IMAGE ===================== %%%%%%%%%%%%%%%%%%%%%%%%%%");

                    //userType = ChatMessageUserType.IMG;

                    imageUrl = jsonMsg.getString("message");

                    imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

                    Log.i("grrrr IMG IMAGE NAME"," "+imageName);
                    Log.i(TAG, "calipso" + userSubtype);

                    if(userID.equalsIgnoreCase(FROM))
                    {
                        userType = ChatMessageUserType.IMGSELF;
                        userSubtype = ChatMessageUserSubtype.SELF;
                    }
                    else
                    {
                        userType = ChatMessageUserType.IMGOTHER;
                        userSubtype = ChatMessageUserSubtype.OTHER;
                    }


                }

                else if (msgStatus.equalsIgnoreCase("LISTING")){

                    userType = ChatMessageUserType.LISTING;


                }


                else{

                    if(userID.equalsIgnoreCase(FROM) )
                    {
                        userType = ChatMessageUserType.SELF;
                    }
                    else
                    {
                        userType = ChatMessageUserType.OTHER;
                    }

                }

//
                Log.i("TAG","NULL ===================== %%%%%%%%%%%%%%%%%%%%%%%%%%   "+msgStatus);
                Log.i(TAG, "calipso yo" + roleOfUser);
                Log.i(TAG, "calipso yo" + userType);
                Log.i(TAG,"calipso yo message "+body);

                Log.i("TAG","narcos img   "+userSubtype+" "+body+" "+userType);


                message.setUserName(roleOfUser);
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(body);
                message.setUserType(userType);
                message.setUserSubtype(userSubtype);
                message.setImageUrl(imageUrl);
                message.setUser_id(userID);
                message.setImageName(imageName);
                Log.i(TAG,"timestap "+Long.valueOf(jsonMsg.getString("timetoken"))/10000);
                message.setMessageTime(Long.valueOf(jsonMsg.getString("timetoken"))/10000);
                chatMessages.add(message);

                    /*chatMessagesCopy.clear();
                    chatMessagesCopy.addAll(chatMessages);
                    chatMessages.clear();
                    chatMessages.add(message);
                    chatMessages.addAll(chatMessagesCopy);*/


                Log.i(TAG, "message after adding to chatMessages" + message.getUserType());




            }




        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "calipso caught in display message1 debug" + e);
        }

    }



    /**
     * display incoming messages through pubnub
     * @param jsonMsg
     */


    private void displayMessage(JSONObject jsonMsg) {
        try {

        String user_id = null;
        String body = null;
        String timetoken = null;
        ChatMessageUserType userType = null;
        ChatMessageUserSubtype userSubtype = null;
        String imageUrl = null;
        String imageName = null;
        String FROM = null;
        String msgStatus = null;
        final ChatMessage message = new ChatMessage();
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";
        String userID = null;

        if(General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("yes"))
        {
            userID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
        }
        else
            userID = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);



            Log.i(TAG,"inside displaymessge =====  jsonMSG "+jsonMsg);

            if(jsonMsg.has("from"))
                FROM = jsonMsg.getString("from");
            else {
                FROM = jsonMsg.getString("_from");
            }

            Log.i(TAG," =====  _from "+msgStatus);

            if(jsonMsg.has("status"))
                msgStatus = jsonMsg.getString("status");

            /*else if(jsonMsg.getString("message").contains("https://"))
                msgStatus = "IMG";*/
            else
                msgStatus = null;


            if(jsonMsg.has("timetoken"))
                timetoken = jsonMsg.getString("timetoken");

            else
                timetoken = null;


            Log.i(TAG," after assigning =====  _from "+msgStatus);
            if (jsonMsg.has("message") && FROM != null && jsonMsg.has("to")) {

                body = jsonMsg.getString("message");


                if (msgStatus.equalsIgnoreCase("DEFAULT") || msgStatus.equalsIgnoreCase("SYSTEM") || msgStatus.equalsIgnoreCase("OYES") || msgStatus.equalsIgnoreCase("OKS")){
                    Log.i("CONVER", "DEFAULT set");
                    if(msgStatus.equalsIgnoreCase("OKS") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        return;
                    if(msgStatus.equalsIgnoreCase("OYES") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                        return;
                    if(msgStatus.equalsIgnoreCase("DEFAULT") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                        return;
                    if(msgStatus.equalsIgnoreCase("SYSTEM") &&  General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                        return;
                    userType = ChatMessageUserType.DEFAULT;
                }

                else if (msgStatus.equalsIgnoreCase("IMG") || msgStatus.equalsIgnoreCase("OYEI") || msgStatus.equalsIgnoreCase("OKI")){

                    Log.i("TAG","IMAGE ===================== %%%%%%%%%%%%%%%%%%%%%%%%%%");

                    //userType = ChatMessageUserType.IMG;

                    imageUrl = jsonMsg.getString("message");

                    imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

                    Log.i("grrrr IMG IMAGE NAME"," "+imageName);
                    Log.i(TAG, "calipso" + userSubtype);

                     if(userID.equalsIgnoreCase(FROM))
                     {
                         userType = ChatMessageUserType.IMGSELF;
                         userSubtype = ChatMessageUserSubtype.SELF;
                     }
                     else
                     {
                         userType = ChatMessageUserType.IMGOTHER;
                         userSubtype = ChatMessageUserSubtype.OTHER;
                     }


                 }

                else if (msgStatus.equalsIgnoreCase("LISTING")){

                    userType = ChatMessageUserType.LISTING;


                }


                else{

                    if(userID.equalsIgnoreCase(FROM) )
                    {
                        userType = ChatMessageUserType.SELF;
                    }
                    else
                    {
                        userType = ChatMessageUserType.OTHER;
                    }

                }

//
                Log.i("TAG","NULL ===================== %%%%%%%%%%%%%%%%%%%%%%%%%%   "+msgStatus);
                Log.i(TAG, "calipso yo" + roleOfUser);
                Log.i(TAG, "calipso yo" + userType);
                Log.i(TAG,"calipso yo message "+body);

                Log.i("TAG","narcos img   "+userSubtype+" "+body+" "+userType);


                message.setUserName(roleOfUser);
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(body);
                message.setUserType(userType);
                message.setUserSubtype(userSubtype);
                message.setImageUrl(imageUrl);
                message.setUser_id(userID);
                message.setImageName(imageName);
                Log.i(TAG,"timestap "+Long.valueOf(jsonMsg.getString("timetoken"))/10000);
                message.setMessageTime(Long.valueOf(jsonMsg.getString("timetoken"))/10000);


    chatMessages.add(message);


                Log.i(TAG, "message after adding to chatMessages" + message.getUserType());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        Log.i(TAG, "message runOnUiThread" + listAdapter);

                        if (listAdapter != null) {
                            Log.i(TAG, "message runOnUiThread  not null");
                            Log.i(TAG, "calipso yo notify" );
                           listAdapter.notifyDataSetChanged();
                        }

                        messageTyped = "";

//                        Log.i(TAG, "message runOnUiThread edtTypeMsg2");
                    }
                });


            }


        chatListView.post(new Runnable() {
            @Override
            public void run() {
                chatListView.setSelection(listAdapter.getCount());
            }
        });

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "calipso caught in display message1 debug" + e);
        }

    }


    private void displayMessage(){

        Log.i(TAG, "until toro foro loro displayMessage ");
        myRealm = General.realmconfig(this);
        String body = null;
        String timetoken = null;
        String imageUrl = null;
        String user_id = null;
        ChatMessageUserSubtype userSubtype = null;
        ChatMessageUserType userType = null;
        ChatMessage message;
        String userID = null;
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";
        if(General.getSharedPreferences(getApplicationContext(), AppConstants.IS_LOGGED_IN_USER).equals("yes"))
        {
            userID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
        }
        else
            userID = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);
        String channelName;  // local

        Log.i("TAG","INSIDE DISPLAYMESSAGE =========================");

        if(channel_name.equalsIgnoreCase(AppConstants.SUPPORT_CHANNEL_NAME))
        {
            // Replace my_channel with demo_id
            channelName = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);
        }
        else
            channelName = channel_name;


        try {
if(myRealm.isInTransaction())
    myRealm.cancelTransaction();
            myRealm.beginTransaction();
            RealmResults<Message> results1 =
                    myRealm.where(Message.class).equalTo(AppConstants.OK_ID, channelName).findAll();
            Log.i(TAG, "until toro foro iko "+results1);
            for (Message c : results1) {
                Log.i(TAG, "until toro foro "+c.getStatus());
                Log.i(TAG, "until insideroui3 " + c.getFrom());
//                Log.i(TAG, "until insideroui4 " + c.getTimestamp());
//                Log.i(TAG, "until insideroui4 " + c.getMessage());
//                Log.i(TAG, "until insideroui4 " + c.getFrom());
//                Log.i(TAG, "until insideroui4 " + c.getTo());
//                Log.i(TAG, "until insideroui4 " + c.getImageUrl());

                if (c.getStatus().equalsIgnoreCase("DEFAULT") || c.getStatus().equalsIgnoreCase("SYSTEM") || c.getStatus().equalsIgnoreCase("OYES")){
                    Log.i("CONVER", "DEFAULT set");
                    userType = ChatMessageUserType.DEFAULT;
                }

                else if (c.getStatus().equalsIgnoreCase("IMG") || c.getStatus().equalsIgnoreCase("OYEI") || c.getStatus().equalsIgnoreCase("OKI")){

                    Log.i("TAG","IMAGE ===================== %%%%%%%%%%%%%%%%%%%%%%%%%%");

                    //userType = ChatMessageUserType.IMG;

                    imageUrl = c.getMessage();

                    imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

                    Log.i("grrrr IMG IMAGE NAME"," "+imageName);
                    Log.i(TAG, "calipso" + userSubtype);

                    if(userID.equalsIgnoreCase(c.getFrom()))
                    {
                        userType = ChatMessageUserType.IMGSELF;
                        userSubtype = ChatMessageUserSubtype.SELF;
                    }
                    else
                    {
                        userType = ChatMessageUserType.IMGOTHER;
                        userSubtype = ChatMessageUserSubtype.OTHER;
                    }

                }

               else if (c.getStatus().equalsIgnoreCase("LISTING")){



                    userType = ChatMessageUserType.LISTING;


                }

                else{

                    if (userID.equalsIgnoreCase(c.getFrom())) {
                        userType = ChatMessageUserType.SELF;
                    } else {
                        userType = ChatMessageUserType.OTHER;
                    }
                }



                Log.i(TAG, "until toro foro loro abcs "+Long.valueOf(c.getTimestamp())/10000);

                message = new ChatMessage();
                message.setUserName(roleOfUser);
                message.setMessageStatus(ChatMessageStatus.SENT);
                message.setMessageText(c.getMessage());
                message.setUserType(userType);
                message.setImageUrl(imageUrl);
                message.setUser_id(user_id);
                message.setUserSubtype(userSubtype);
                message.setMessageTime(Long.valueOf(c.getTimestamp())/10000);
                chatMessages.add(message);

                Log.i(TAG, "cache yo  message after adding to chatMessages disp" + chatMessages);


            }

            listAdapter.notifyDataSetChanged();

        }catch(Exception e){
            Log.i(TAG,"Caught in the exception reading cache from realm "+e);
        }

        finally {
            myRealm.commitTransaction();
            Log.i(TAG,"until fitra 4 "+myRealm.isInTransaction());
        }
//        chatListView.post(new Runnable() {
//            @Override
//            public void run() {
//                chatListView.setSelection(listAdapter.getCount());
//            }
//        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                        Log.i(TAG, "message runOnUiThread" + listAdapter);

                if (listAdapter != null) {
                    Log.i(TAG, "message runOnUiThread  not null");
                    Log.i(TAG, "calipso yo notify" );
                    listAdapter.notifyDataSetChanged();
                }

//                edtTypeMsg.setText("");

//                        Log.i(TAG, "message runOnUiThread edtTypeMsg2");
            }
        });

    }

    /**
     * send message through pubnub
     * @param messageText
     */





    private void sendMessage(final String messageText)
    {

        String channel;
        Log.i(TAG, "Inside send message" +messageTyped);


        if(messageText != null)
        {
            if(messageText.trim().length()==0)
                return;
        }
        else
        return;


        // Rt find to?
        String To = "support";
        String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);


        try {

            if (channel_name.equalsIgnoreCase("my_channel")){

                channel = General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI);

            }
            else
                channel = channel_name;

            JSONObject jsonMsg = new JSONObject();

            if(General.getSharedPreferences(this ,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("yes"))
                 jsonMsg.put("_from", General.getSharedPreferences(this ,AppConstants.USER_ID));
            else
                jsonMsg.put("_from", General.getSharedPreferences(this ,AppConstants.TIME_STAMP_IN_MILLI));

           // jsonMsg.put("name", dbHelper.getValue(DatabaseConstants.name));
//changed after removing sqldb sushil
            jsonMsg.put("name", General.getSharedPreferences(getBaseContext(),AppConstants.NAME));

            jsonMsg.put("to", channel);
            jsonMsg.put("message", messageText);
            if(role.equals("broker"))
                jsonMsg.put("status", "OKU");
            else if(role.equals("client"))
                jsonMsg.put("status", "OYEU");

            jsonMsgtoWhereNow = jsonMsg;






            sendNotification(jsonMsg);
            //displayMessage(jsonMsg);    // Dont uncomment (avoid redundant messages in list)

            lastMessageTime = String.valueOf(System.currentTimeMillis());



            pubnub.auditPushChannelProvisions()
                    .deviceId(General.getSharedPreferences(this,AppConstants.GCM_ID))
                    .pushType(PNPushType.GCM)
                    .async(new PNCallback<PNPushListProvisionsResult>() {
                        @Override
                        public void onResponse(PNPushListProvisionsResult result, PNStatus status) {
                            try {
                                Log.i("TEST", "auditPushChannelProvisions" + result.getChannels() + " " + status.isError());
                            }catch(Exception e){}

                        }
                    });

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

   /* private void pubnubWhereNow(final String UUID){

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());

                Log.i("TEST", "pubnubWhereNow ======" + response.toString());

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

*//*<<<<<<< HEAD
//                        sendNotification("my_channel");
=======
                       sendNotification("my_channel",jsonMsgtoWhereNow);
>>>>>>> 609773f27c3c8c5863a88896a07ded5037c98d9d*//*

                        Log.i("Pubnub push","inside pubnub where now its first msg ");
                        pubnub.publish("my_channel", jsonMsgtoWhereNow, true, new Callback() {
                        });

                        lastMessageTime = String.valueOf(System.currentTimeMillis());

                        Log.i("TEST","first message" +firstMessage);
                    }

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

*/
    /**
     * load pubnub history
     */


    private void loadHistorypaginated(String channel_name, Long starttime){

        pubnub.history()
                .start(starttime)
                .channel(channel_name) // where to fetch history from
                .count(AppConstants.MSG_COUNT)// how many items to fetch
                .includeTimetoken(true)// include timetoken with each entry
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {

                        try {
                            timetoken = result.getStartTimetoken();
                            Log.i(TAG, "history is tha 209 " + result.getEndTimetoken()+"  "+result.getStartTimetoken());
                            if(!result.getMessages().isEmpty()) {
                                chatMessagesCopy.clear();
                                chatMessagesCopy.addAll(chatMessages);
                                chatMessages.clear();
                                int i =0;
                               // chatMessages.clear();
                                for (PNHistoryItemResult historyItemResult : result.getMessages()) {
                                    i++;
                                    Log.i(TAG, "history is tha 2 " + historyItemResult);
                                    try {
                                        if (historyItemResult.getEntry().has("pn_gcm")) {
                                            Log.i(TAG, "history is tha 33 " + historyItemResult.getTimetoken());
                                            // String content = jsonNode.get("data").textValue();

                                            String j = historyItemResult.getEntry().get("pn_gcm").get("data").toString();
                                            // JSONObject j = new JSONObject(historyItemResult.getEntry().get("pn_gcm").toString());
                                            Log.i(TAG, "history is tha 75 " + historyItemResult.getEntry().get("pn_gcm").get("data").toString());
                                            Log.i(TAG, "history is tha 70 " + j);
                                            JSONObject jsonObj = new JSONObject(j);

                                            jsonObj.put("timetoken", historyItemResult.getTimetoken().toString());
                                            Log.i(TAG, "history is tha 39 " + jsonObj);

                                            displayPaginatedMessage(jsonObj);


                                        }

                                    } catch (Exception e) {
                                        Log.i(TAG, "PUBNUB history Caught in exception recieved message not proper " + e);
                                    }

                                }

                                final int count = i;

                                if(i==AppConstants.MSG_COUNT) {
                                    Log.i(TAG,"pagination 1");
                                    paginate = true;
                                    i = 0;
                                }else{
                                    Log.i(TAG,"pagination 2  "+i);
                                }



                                chatMessages.addAll(chatMessagesCopy);


                                chatListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        chatListView.setSelection(count);
                                    }
                                });


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

//                        Log.i(TAG, "message runOnUiThread" + listAdapter);

                                        if (listAdapter != null) {
                                            Log.i(TAG, "message runOnUiThread  not null");
                                            Log.i(TAG, "calipso yo notify" );
                                            listAdapter.notifyDataSetChanged();
                                        }



//
                                    }
                                });

                            }else {
                                Log.i(TAG, "perser dont load pagination anymore ");


                            }
                        }
                        catch (Exception e) {
                            Log.i(TAG,"PUBNUB history Caught in exception 56 "+e);
                        }

                    }
                });
    }

    private void loadHistoryFromPubnub(final String channel_name) throws JSONException {
        Log.i(TAG,"Load history from pubnub called");

        /*if(channel_name.equalsIgnoreCase("my_channel"))
            channel_name = General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI);
*/

        pubnub.history()

                .channel(channel_name) // where to fetch history from
                .count(AppConstants.MSG_COUNT)// how many items to fetch
                .includeTimetoken(true)// include timetoken with each entry
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {

                        try {

                            timetoken = result.getStartTimetoken();
                            Log.i(TAG, "history is tha 2090 " + result.getEndTimetoken()+"  "+result.getStartTimetoken());
                            if(!result.getMessages().isEmpty()) {
                                Log.i(TAG, "perser 48 "+isUnverified);
                                chatMessages.clear();
                                int i = 0;
                                for (PNHistoryItemResult historyItemResult : result.getMessages()) {
                                    Log.i(TAG, "history is tha 2 " + historyItemResult);
                                    i++;
                                    try {
                                        if (historyItemResult.getEntry().has("pn_gcm")) {
                                            Log.i(TAG, "history is tha 33 " + historyItemResult.getTimetoken());
                                            // String content = jsonNode.get("data").textValue();

                                            String j = historyItemResult.getEntry().get("pn_gcm").get("data").toString();
                                            // JSONObject j = new JSONObject(historyItemResult.getEntry().get("pn_gcm").toString());
                                            Log.i(TAG, "history is tha 75 " + historyItemResult.getEntry().get("pn_gcm").get("data").toString());
                                            Log.i(TAG, "history is tha 70 " + j);
                                            JSONObject jsonObj = new JSONObject(j);

                                            jsonObj.put("timetoken", historyItemResult.getTimetoken().toString());
                                            Log.i(TAG, "history is tha 39 " + jsonObj);
                                            displayMessage(jsonObj);

                                        }

                                    } catch (Exception e) {
                                        Log.i(TAG, "PUBNUB history Caught in exception recieved message not proper " + e);
                                    }

                                }

if(i==AppConstants.MSG_COUNT) {
    Log.i(TAG,"pagination 3");
    paginate = true;
    i = 0;
}else{
    Log.i(TAG,"pagination 4");
}
                            }else {
                                Log.i(TAG, "perser 49 "+isUnverified);

                                if (isUnverified) {
                                    Log.i(TAG, "perser 4");
                                    displayDefaultMessageUnverified();
                                }
                                else  if (isDefault) {
                                    Log.i(TAG, "perser 4");
                                    displayDefaultMessage();
                                }
                            }
                        }
                        catch (Exception e) {
                            Log.i(TAG,"PUBNUB history Caught in exception 56 "+e);
                        }

                    }
                });


    }


    /**
     *  pubnub push notification
     */

    public void sendNotification(final JSONObject jsonMsg) throws JSONException {
String channel;
        if (channel_name.equalsIgnoreCase("my_channel")){

            channel = General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI);

        }
        else
            channel = channel_name;
        Log.i(TAG,"PUBNUB publish msg "+jsonMsg);
        Log.i(TAG,"PUBNUB publish channel "+channel);
        Map message = new HashMap();

        message.put("pn_gcm", new HashMap(){{put("data",new HashMap(){{put("message",jsonMsg.getString("message")); put("_from",jsonMsg.getString("_from"));put("to",jsonMsg.getString("to"));put("name",jsonMsg.getString("name")); put("status",jsonMsg.getString("status"));}});}});
        message.put("pn_apns", new HashMap(){{put("aps",new HashMap(){{put("alert",jsonMsg.getString("message")); put("_from",jsonMsg.getString("_from"));put("to",jsonMsg.getString("to"));put("name",jsonMsg.getString("name")); put("status",jsonMsg.getString("status"));}});}});


              Log.i(TAG,"Message is "+message);

        pubnub.publish()
                .message(message)
                .shouldStore(true)
                .usePOST(true)
                .channel(channel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            System.out.println("publish failed!");
                            Log.i(TAG,"PUBNUB publish "+status.getStatusCode());
                        } else {
                            System.out.println("push notification worked!");
                            System.out.println(result);
                        }
                    }
                });





        /*Log.i("INSIDE PUSH NOTIFY","============"+jsonMsg);
        PnGcmMessage gcmMessage = new PnGcmMessage();

        JSONObject json = jsonMsg; //new JSONObject();

        gcmMessage.setData(json);

        // Create APNS message

        PnApnsMessage apnsMessage = new PnApnsMessage();
        apnsMessage.setApsSound("melody");
        apnsMessage.setApsAlert(json.getString("message"));
        apnsMessage.setApsBadge(1);
        apnsMessage.put("from",json.get("_from"));
        apnsMessage.put("to",json.get("to"));
        apnsMessage.put("status",json.get("status"));


        PnMessage message = new PnMessage(pubnub, channel_name, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                Log.i("TRACE NOTIFICATION","pubnubgcm Successfullaaaa "+message);
            }

            @Override
            public void errorCallback(String channel,PubnubError error)
            {
                Log.i("TRACE NOTIFICATION","pubnubgcm yo Error "+error);
            }
        },apnsMessage,gcmMessage);

        //message.put("b","20");
        try {

            message.publish();
            Log.i("Pubnub push","inside send notif with msg published "+json);

        }
        catch (PubnubException e){
            Log.i("INSIDE PUSH NOTIFY","pubnubgcm ERROR============ "+e.getMessage());
             }*/

        }


   /* public static Callback callback = new Callback() {
        @Override
        public void successCallback(String channel, Object message) {
            Log.i(TAG, "Success on Channel " + channel + " : " + message);

        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.i(TAG, "Error On Channel " + channel + " : " + error);
        }

    };*/

    /**
     * all active userIds on channel
     */


    /*private void pubnubHereNow() {
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
    }*/

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
        String user_id = null;
        Log.i(TAG, "until cacheMessages called  "+jsonArrayHistory);
        //JSONArray jsonArrayHistory = loadFinalHistory();
        myRealm = General.realmconfig(this);


        try {

            int jsonArrayHistoryLength = jsonArrayHistory.length();

            Log.i(TAG, "until here is the cacheMessages 1 jsonArrayHistory "+jsonArrayHistory);
            Log.i(TAG, "until here is the cacheMessages 1 jsonArrayHistoryLength "+jsonArrayHistoryLength);
//            myRealm = General.realmconfig(this);

            myRealm = General.realmconfig(this);
            myRealm.beginTransaction();

            for (int i = 0; i < jsonArrayHistoryLength; i++) {
                JSONObject js = jsonArrayHistory.getJSONObject(i);
                Log.i(TAG, "until here is the cacheMessages 1 jsonMsg yo yo to " + js);

                try {

                    message = new Message();
                    Log.i(TAG, "until here is the cacheMessages 1 2 ithe " + myRealm.isInTransaction());
//
                    message = myRealm.createObject(Message.class); //new Message();
                    Log.i(TAG, "until here is the cacheMessages 1 2 Tithe");
                    message.setOk_id(channel_name);
                    message.setMessage(js.getString("message"));
                    message.setTimestamp(js.getString("timetoken"));
                    message.setFrom(js.getString("_from"));
                    message.setTo(js.getString("to"));
                    message.setStatus(js.getString("status"));

                    myRealm.copyToRealm(message);

                }catch(Exception e){}


            }




        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception in caching messages in Realm "+e);

        }
        finally{


            myRealm.commitTransaction();
            Log.i(TAG,"until fitra 3 "+myRealm.isInTransaction());
        }

       /* try {

//            Realm myRealm = General.realmconfig(this);
            RealmResults<Message> results1 =
                    myRealm.where(Message.class).equalTo(AppConstants.OK_ID, channel_name).findAll();

            for (Message c : results1) {
                Log.i(TAG, "until insiderou2 ");
                Log.i(TAG, "until insiderou3 " + c.getOk_id());
                Log.i(TAG, "until insiderou4 " + c.getTimestamp());
                Log.i(TAG, "until insiderou4 " + c.getMessage());
            }
        }catch(Exception e){
            Log.i(TAG,"Caught in the exception reading cache from realm "+e);
        }
*/

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
        Log.i(TAG,"Load final history called ");

try {


    pubnub.history()
            .channel(channel_name) // where to fetch history from
            .count(AppConstants.MSG_COUNT) // how many items to fetch
            .includeTimetoken(true) // include timetoken with each entry
            .async(new PNCallback<PNHistoryResult>() {
                @Override
                public void onResponse(PNHistoryResult result, PNStatus status) {
                    try {

                        for (PNHistoryItemResult historyItemResult : result.getMessages()) {
                            Log.i(TAG, "history is tha 2 " + historyItemResult.getEntry());
                            try {
                                if (historyItemResult.getEntry().has("pn_gcm")) {
                                    Log.i(TAG, "history is tha 3 " + historyItemResult.getEntry().toString());
                                    // String content = jsonNode.get("data").textValue();

                                    String j = historyItemResult.getEntry().get("pn_gcm").get("data").toString();
                                    // JSONObject j = new JSONObject(historyItemResult.getEntry().get("pn_gcm").toString());
                                    Log.i(TAG, "history is tha 7 " + j);
                                    JSONObject jsonObj = new JSONObject(j);
                                    jsonObj.put("timetoken", historyItemResult.getTimetoken().toString());


                                    // jsonA = new JSONArray();
                                    Log.i(TAG, "history is tha 10 jsonArrayHistory jsonObj " + jsonObj);
                                    jsonArrayHistory.put(jsonObj);
                                    Log.i(TAG, "history is tha 9 jsonArrayHistory " + jsonArrayHistory);

                                }

                            } catch (Exception e) {
                                Log.i(TAG, "PUBNUB history Caught in exception recieved message not proper " + e);
                            }
                        }
                        clearCache();
                        cacheMessages(jsonArrayHistory);


                       /* try {
                            JSONArray jsonArrayHistory;

                            JSONArray jsonArrayResponse = new JSONArray(result.toString());
                            Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayResponse "+jsonArrayResponse);
                            jsonArrayHistory = jsonArrayResponse.getJSONArray(0);
                            Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayHistory "+jsonArrayHistory);
                            clearCache();
                            cacheMessages(jsonArrayHistory);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    } catch (Exception e) {
                        Log.i(TAG, "Caught in exception loading final history " + e);
                    }
                }

            });


       /* Callback callback = new Callback() {
            JSONArray jsonArrayHistory;
            public void successCallback(String channel, Object response) {
                Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo");
                try {
                    JSONArray jsonArrayResponse = new JSONArray(response.toString());
                    Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayResponse "+jsonArrayResponse);
                    jsonArrayHistory = jsonArrayResponse.getJSONArray(0);
                    Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayHistory "+jsonArrayHistory);
                    clearCache();
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
        pubnub.history(channel_name,true, 15,callback);
        Log.i(TAG, "inside loadHistoryFromPubnub channel name is2 yo jsonArrayHistory 1 "+jsonArrayHistory);*/
}catch(Exception e){

}

    }


    private void displayCardView(){

        Log.i(TAG, "displayCardView called ");
        try {
            JSONObject jsonMsg = new JSONObject();
            //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
            jsonMsg.put("_from", General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));

            //jsonMsg.put("to", "client");

            jsonMsg.put("to", channel_name);
            jsonMsg.put("status","LISTING");
            Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));



                // prepare a default message now get ptype
               // String ptype = General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE);
          //  mainList.addAll(set);
            ArrayList<String> keys = new ArrayList<String>();
                   keys.addAll(listings.keySet());
            //ArrayList<String> keys = (ArrayList<String>) listings.keySet();  //get all keys

            Log.i(TAG,"message for cardview is "+keys.get(0)+"--"+listings.get(keys.get(0))+"--"+keys.get(1)+"--"+listings.get(keys.get(1))+"--"+keys.get(2)+"--"+listings.get(keys.get(2)));
               jsonMsg.put("message",keys.get(0)+"--"+listings.get(keys.get(0))+"--"+keys.get(1)+"--"+listings.get(keys.get(1))+"--"+keys.get(2)+"--"+listings.get(keys.get(2)));


            Log.i(TAG, "displayCardView msg is "+jsonMsg);
            displayMessage(jsonMsg);

            sendNotification(jsonMsg);
           // pubnub.publish(channel_name, jsonMsg, true, new Callback() {});

        }
        catch(Exception e){

            Log.i(TAG,"Caught in exception displayCardView "+e);
        }
        finally {
            //chatMessages.clear();   // clears chatmessages to avoid redundant messages after loadhistory called(Clears out cached msgs before loading actual)
        }

    }


    private void displayDefaultMessage(){
        Log.i(TAG, "displayDefaultMessage called ");
        try {
            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {

                JSONObject jsonMsg = new JSONObject();
           jsonMsg.put("_from", General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));
            jsonMsg.put("to",channel_name);
            jsonMsg.put("timetoken",String.valueOf(System.currentTimeMillis()));

            jsonMsg.put("name","");// SYSTEM as this would be welcome message

            Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));


                Realm myRealm = General.realmconfig(this);

                DefaultDeals result = myRealm.where(DefaultDeals.class).equalTo(AppConstants.OK_ID, channel_name).findFirst();



                /*final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                String json = gson.toJson(AppConstants.letsOye);

                JSONObject jsonResponse = new JSONObject(json);*/
                // Log.i(TAG, "role of user def 4 ");
                /*Log.i("TORO","froyo "+jsonResponse);*/
String furnishing = "Semi-Furnished";
                if(result.getFurnishing().equalsIgnoreCase("uf"))
                    furnishing = "Un-Furnished";
                else if (result.getFurnishing().equalsIgnoreCase("ff"))
                    furnishing = "Fully-Furnished";

                jsonMsg.put("message", "You have initiated enquiry for a "+furnishing+" "+ result.getP_type().substring(0,1).toUpperCase() + result.getP_type().substring(1) + " property (" + result.getPs_type() + ") by "+result.getPossation_date()+" within budget " + General.currencyFormat(result.getBudget()) + ".");


                jsonMsg.put("status","OYES");





/*  if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {

                // prepare a default message now get ptype
                String ptype = General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE);

                jsonMsg.put("message", "Client have initiated enquiry for " + General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE).substring(0, 1).toUpperCase() + General.getSharedPreferences(getApplicationContext(), AppConstants.PTYPE).substring(1)
                        + " property (" + General.getSharedPreferences(getApplicationContext(), AppConstants.PSTYPE) + ") within budget " + General.currencyFormat(General.getSharedPreferences(getApplicationContext(), AppConstants.PRICE)) + ".");

                jsonMsg.put("status","OKS");


            }*/
            Log.i(TAG,"message to def "+jsonMsg);

            final ChatMessage message = new ChatMessage();
            message.setUserName("self");
            message.setMessageStatus(ChatMessageStatus.SENT);
            message.setMessageText(jsonMsg.getString("message"));
            message.setUserType(ChatMessageUserType.DEFAULT);
            message.setMessageTime(System.currentTimeMillis());

            chatMessages.add(message);

            listAdapter.notifyDataSetChanged();
            setState("online","true");


            //displayMessage(jsonMsg);
            sendNotification(jsonMsg);

            }
        }

        catch(Exception e){
            Log.i(TAG,"Caught in exception showing default message "+e);
        }
        finally {
          //  chatMessages.clear();   // clears chatmessages to avoid redundant messages after loadhistory called(Clears out cached msgs before loading actual)
        }
    }


    private void displayDefaultMessageUnverified(){
        Log.i(TAG, "perser 5");
        Log.i(TAG, "displayDefaultMessage called ");
        try {
            JSONObject jsonMsg = new JSONObject();

            jsonMsg.put("_from", General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI));

            jsonMsg.put("to",channel_name);
            jsonMsg.put("name","");
            jsonMsg.put("timetoken",String.valueOf(System.currentTimeMillis()));


            jsonMsg.put("status","OYES"); // SYSTEM as this would be welcome message

            Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));
            String intent;
             if( specs.toLowerCase().contains("LL".toLowerCase()))
                 intent = "Renting";
            else if( specs.toLowerCase().contains("OR".toLowerCase()) && specs.toLowerCase().contains("AVL".toLowerCase()))
                 intent = "Buying";
            else
             intent = "Selling";

                jsonMsg.put("message", "You have initiated an enquiry for property in "+locality+" for "+intent+". ");


            Log.i(TAG,"display default message "+jsonMsg);
            displayMessage(jsonMsg);
            sendNotification(jsonMsg);

        }

        catch(Exception e){
            Log.i(TAG, "perser 6 exception "+e);
        }

    }
    private void displayImgMessage(String bucketName, String imgName){

        Log.i(TAG, "calipso inside displayimage msg"+bucketName +" "+imgName );
        Log.i(TAG, "displayImgMessage called ");

        String user_id = null;


         if(General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("yes"))
            user_id = General.getSharedPreferences(this,AppConstants.USER_ID);
        else
            user_id = General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI);



        try {
            JSONObject jsonMsg = new JSONObject();

            jsonMsg.put("timestamp",String.valueOf(System.currentTimeMillis()));

            //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);

            jsonMsg.put("_from", user_id);

            jsonMsg.put("to", channel_name);
            //jsonMsg.put("to", "client");
//            jsonMsg.put("imageUrl", "https://s3.ap-south-1.amazonaws.com/"+bucketName+"/"+imgName);
            if(General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                jsonMsg.put("status","OKI");
            else if(General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                jsonMsg.put("status","OYEI");

            jsonMsg.put("name","");

//            jsonMsg.put("imageName", imgName);
//            jsonMsg.put("user_id", user_id);

            Log.i(TAG, "role of user def " + General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));

//            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
//                            String ptype = General.getSharedPreferences(getApplicationContext(),AppConstants.PTYPE);
//                            Log.i(TAG, "role of user def 1 "+ptype);


                Log.i(TAG, "role of user def 6 ");

//            }
//            if (General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
//                jsonMsg.put("to", "client");
//                // prepare a default message now get ptype
//
//            }

            jsonMsg.put("message", "https://s3.ap-south-1.amazonaws.com/"+bucketName+"/"+imgName);

            Log.i("yoyoyo","urlo "+jsonMsg);
            Log.i(TAG, "calipso inside calling displayMsg "+jsonMsg );

           displayMessage(jsonMsg);
            sendNotification(jsonMsg);
//            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});


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
        imageName = "droid"+String.valueOf(System.currentTimeMillis())+".png";

        try {
            if (Environment.getExternalStorageState() == null) {
                //create new file directory object
                File directory = new File(Environment.getDataDirectory()
                        + "/oyeok/");


                // if no directory exists, create new directory
                if (!directory.exists()) {
                    directory.mkdir();
                }

                File destdir = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/" + imageName);
                Log.i(TAG, "dest diro " + destdir);
                copyFileUsingStream(fileToUpload, destdir);
                Log.i(TAG, "file after save ");

                // if phone DOES have sd card
            } else if (Environment.getExternalStorageState() != null) {
                // search for directory on SD card
                File directory = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/");

                // if no directory exists, create new directory to store test
                // results
                if (!directory.exists()) {
                    directory.mkdir();
                }
                File destdir = new File(Environment.getExternalStorageDirectory()
                        + "/oyeok/" + imageName);
                Log.i(TAG, "dest diro " + destdir);
                copyFileUsingStream(fileToUpload, destdir);
                Log.i(TAG, "file after save ");
            }
        }
        catch (Exception e)
        {

            Log.i(TAG, "Caught in exFception saving image /oyeok " + e);

        }


        displayImgMessage("oyeok-chat-images",imageName);

        TransferObserver transferObserver = transferUtility.upload(
                "oyeok-chat-images",     /* The bucket to upload to */
                imageName,    /* The key for the uploaded object */
                fileToUpload       /* The file where the data to upload exists */
        );

        transferObserverListener(transferObserver);
        //

    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {

        Log.i(TAG,"fila source "+source);
        Log.i(TAG,"fila dest "+dest);

        InputStream is = null;
        OutputStream os = null;

        try {

            is = new FileInputStream(source);
            os = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } finally {

            is.close();
            os.close();
        }
    }



    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     * @param
     **/
    public void setFileToDownload(String fileToD,File fileToDownload){
Log.i(TAG,"download image "+fileToD+" "+fileToDownload);
        TransferObserver transferObserver = transferUtility.download(
                "oyeok-chat-images",     /* The bucket to download from */
                fileToD,    /* The key for the object to download */
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
                   // sendMessage("https://s3.ap-south-1.amazonaws.com/oyeok-chat-images/"+imageName);
                    //displayImgMessage("oyeok-chat-images",imageName);


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



    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else
        {
           return true;
        }
    }

    private void clearNotifCount(){
        Log.i(TAG,"clear notification called ");
        try{
            Realm myRealm = General.realmconfig(this);
            NotifCount notifcount1 = myRealm.where(NotifCount.class).equalTo(AppConstants.OK_ID, channel_name).findFirst();
            if(notifcount1.getNotif_count()>0){
                myRealm.beginTransaction();
                notifcount1.setNotif_count(0);
                myRealm.commitTransaction();

            }

            //Log.i(TAG, "notif count is the " + notifcount1.getNotif_count());

        }
        catch(Exception e){
Log.i(TAG,"Caught in exception clearing notification count "+e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setState("online", "false");
        General.setSharedPreferences(this,AppConstants.CHAT_OPEN_OK_ID,null);

    Log.i(TAG,"back clicked");

        if(channel_name.equalsIgnoreCase(AppConstants.SUPPORT_CHANNEL_NAME)) {
            channel_name = General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI);
        }else {
            if(lastMessageTime != null) {
                //check if default deal or not
                try{
                    Realm myRealm = General.realmconfig(this);
                    DefaultDeals deal = myRealm.where(DefaultDeals.class).equalTo(AppConstants.OK_ID, channel_name).findFirst();
                    if(deal != null){
                        // update default deal time and return
                        myRealm.beginTransaction();
                        deal.setLastSeen(lastMessageTime);
                        myRealm.commitTransaction();

                    }
                    else{
                        // update server time
                        General.setDealStatus(this, "default", channel_name, lastMessageTime, "");
                    }

                }
                catch(Exception e){

                }


            }
        }
        storeDealTime();

        loadFinalHistory();

        clearNotifCount();


        if(!General.getSharedPreferences(this,AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
            if(General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                AppConstants.CLIENT_DEAL_FLAG = true;
                        else
                AppConstants.BROKER_DEAL_FLAG = true;
        }

        if(AppConstants.CLIENT_DEAL_FLAG == true){
            Log.i(TAG,"dealconv 1");


            Intent back = new Intent(this, ClientDealsListActivity.class); // to refresh adapter to display newly saved last message time
            back.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(back);
            finish();
            AppConstants.CLIENT_DEAL_FLAG = false;
        }else if(AppConstants.BROKER_DEAL_FLAG == true){
            Log.i(TAG,"dealconv 2");

            AppConstants.BROKER_DEAL_FLAG = false;
            Intent back = new Intent(this, ClientDealsListActivity.class); // to refresh adapter to display newly saved last message time
            back.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(back);
            finish();

        }
        else{
            Log.i(TAG,"onsuperp");
            super.onBackPressed();
        }

    }


    private void getDealStatus(Context c,String okId){
        Log.i(TAG,"getDealStatus 1 ");
        UpdateStatus updateStatus = new UpdateStatus();

        updateStatus.setOkId(okId);
        updateStatus.setRole(General.getSharedPreferences(this,AppConstants.ROLE_OF_USER));

        Log.i("getDealStatus ","getDealStactus okId "+General.getSharedPreferences(c,AppConstants.USER_ID)+" "+okId);

        Gson gson = new Gson();
        String json = gson.toJson(updateStatus);
        Log.i("magic","getDealStatus  json "+json);


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL_101)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.getStatus(updateStatus, new retrofit.Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("getStatus CALLED","getDealStatus success ");


                    JsonObject k = jsonElement.getAsJsonObject();
                    try {

                        Log.i("getStatus","getDealStatus success response "+response);


                        JSONObject ne = new JSONObject(k.toString());
                        Log.i("getStatus","getDealStatus success ne "+ne);

                        if(ne.getString("success").equalsIgnoreCase("true")){

                           if(ne.getJSONObject("responseData").getJSONObject("hdroom_status").getString("self_status").equalsIgnoreCase("blocked")) {
                               SnackbarManager.show(
                                       Snackbar.with(DealConversationActivity.this)
                                               .position(Snackbar.SnackbarPosition.TOP)
                                               .text("You have blocked this deal. Message will not be sent.")
                                               .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                           }
                           else if(ne.getJSONObject("responseData").getJSONObject("hdroom_status").getString("other_status").equalsIgnoreCase("blocked")){
                               SnackbarManager.show(
                                       Snackbar.with(DealConversationActivity.this)
                                               .position(Snackbar.SnackbarPosition.TOP)
                                               .text("Counter user have blocked this deal. Message will not be sent.")
                                               .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                           }
                            else{
                               Log.i(TAG,"message to send is the pro "+messageTyped);
                               sendMessage(messageTyped);
                            }

                        }
                        else if(ne.getString("success").equalsIgnoreCase("false")){
                            sendMessage(messageTyped);
                        }
                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("getStatus CALLED","Failed "+e.getMessage());
                        sendMessage(messageTyped);
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("get CALLED","update status failed "+error);
                    SnackbarManager.show(
                            Snackbar.with(DealConversationActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .text(error+"")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                }
            });

        }
        catch (Exception e){
            Log.e("TAG","Caught in get status " +e.getMessage());
        }

    }

    private void checkLocalBlockStatus(){
        Realm myRealm = General.realmconfig(this);
        DealStatusType dealStatusType = null;

        DealStatus dealStatus = myRealm.where(DealStatus.class).equalTo(AppConstants.OK_ID, channel_name).findFirst();
        Log.i(TAG, "Caught in exception notif insiderr cached msgs is the notifcount " + dealStatus);

        if (dealStatus != null && dealStatus.getStatus().equalsIgnoreCase(DealStatusType.BLOCKED.toString())) {

            SnackbarManager.show(
                    Snackbar.with(this)
                            .position(Snackbar.SnackbarPosition.TOP)
                            .text("You have blocked this deal. Message will not be sent.")
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
        }

        else{
            Log.i(TAG,"getDealStatus ");
            String demo_channel = General.getSharedPreferences(this,AppConstants.TIME_STAMP_IN_MILLI);

//            if(!channel_name.equalsIgnoreCase("my_channel") && !channel_name.equalsIgnoreCase(demo_channel))
                getDealStatus(this,channel_name);
//            else
//                sendMessage(messageTyped);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_EXTERNAL_STORAGE){
            if(permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Log.i("==== ","SELECT IMAGE ==============");
                selectImage();
            }
            else{
                //do nothing
            }
        }


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        switch(view.getId())
        {
            case R.id.chat_list_view:

                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.

                final int firstItem = firstVisibleItem;

                if(firstItem == 0 && paginate)
                { Log.i(TAG,"pagination 5");
                    paginate = false;
                    if(channel_name.equalsIgnoreCase(AppConstants.SUPPORT_CHANNEL_NAME))
                    loadHistorypaginated(General.getSharedPreferences(DealConversationActivity.this,AppConstants.TIME_STAMP_IN_MILLI), timetoken);
                    else
                    loadHistorypaginated(channel_name, timetoken);

                }
                else if(firstItem == 0)
                    Log.i(TAG,"pagination 6");
                else if(paginate)
                    Log.i(TAG,"pagination 7");
        }
    }
}