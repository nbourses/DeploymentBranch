package com.nbourses.oyeok.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private Boolean isDefaultDeal;
    private Boolean firstMessage = false;
    private Boolean present = true;
    private JSONObject jsonMsgtoWhereNow = new JSONObject();
    private String specCode;
    private ArrayList<JSONObject> cachedmsgs = new ArrayList<JSONObject>();
    String[] bNames = new String[3];
    int[] bPrice = new int[3];
    Bundle b;


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

        dbHelper = new DBHelper(getBaseContext());

//        IntentFilter filter = new IntentFilter("shine");
//        LocalBroadcastManager.getInstance(this).registerReceiver(handlePushNewMessage, filter);
        UUID = General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID);
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
        if (pubnub != null)
            pubnub.unsubscribeAll();

    }



    private void init() {



        edtTypeMsg.addTextChangedListener(edtTypeMsgListener);

        chatMessages = new ArrayList<>();

        Log.i(TAG, "chatMessages are" + chatMessages);


        Bundle bundle  = getIntent().getExtras();
        channel_name = bundle.getString(AppConstants.OK_ID); //my_channel if came from root item
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
            String[] bNames = new String[]{"Building1","Building2","Building3"};
            int[] bPrice = new int[]{35000,50000,65000};
            Log.i("Listing card data","card data "+bNames+" "+bPrice+"channel_name "+channel_name);
        try{
            JSONObject jsonMsg = new JSONObject();
            //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
            jsonMsg.put("from", "LISTING");
            //jsonMsg.put("to", "client");

            jsonMsg.put("to", "client");


            jsonMsg.put("message",String.valueOf(bNames[0]+"--"+bPrice[0]+"--"+bNames[1]+"--"+bPrice[1]+"--"+bNames[2]+"--"+bPrice[2]));

            //Log.i("TRACE","messageText is "+messageText);
            //Log.i(TAG,"messageText is"+messageText);
            //publish message
            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
            Log.i(TAG, "Listing message published");
        }
        catch(Exception e){
            Log.i(TAG, "caught in listing msg exception "+e);

        }





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



        General.setSharedPreferences(this,AppConstants.GCM_ID,dbHelper.getValue(DatabaseConstants.gcmId));


        General.setSharedPreferences(this,AppConstants.GCM_ID,SharedPrefs.getString(getApplicationContext(),SharedPrefs.MY_GCM_ID));
        if (!channel_name.equals("")) {
            Log.i(TAG, "chanel name was not null" + channel_name);

            // subscribe a channel for Pubnub push notifications start

            Log.i("Pubnub push","channel_name is "+channel_name);
            Log.i("Pubnub push","GCM id is "+General.getSharedPreferences(this,AppConstants.GCM_ID));

            pubnub.enablePushNotificationsOnChannel(
                    channel_name,
                    General.getSharedPreferences(this,AppConstants.GCM_ID));

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
        else if(specCode.contains("LL")){
            if(specCode.contains("REQ")){
                adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsLLReq);
            }
            else{
                adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsLLAvl);
            }

        }
        else{
            if(specCode.contains("REQ")){
                adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsORReq);
            }
            else{
                adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsORAvl);
            }


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

            String channel = channel_name + "-pndebug";
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
            );
        }
        catch (PubnubException e) {
            e.getMessage();
            Log.i("WHERENOW", "6 ");
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DealConversationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * display incoming messages through pubnub
     * @param jsonMsg
     */
    private void displayMessage(JSONObject jsonMsg) {
        Log.i("WHERENOW", "5 ");
        Log.i("CONVER", "jsonMsg" + jsonMsg);
        Log.i(TAG, "inside displayMessage" + jsonMsg);
        //Read cachedmsgs from shared
if(cachedmsgs.size() < 10) {
    cachedmsgs.add(jsonMsg);
}
        else{
    cachedmsgs.remove(0);
    cachedmsgs.add(jsonMsg);
}

        Log.i("CACHE", "cachedmsgs after add" + cachedmsgs);


        String body = null;
        String timetoken = null;
        ChatMessageUserType userType = null;
        final ChatMessage message = new ChatMessage();
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";


        try {
            if (jsonMsg.has("message")) {
                JSONObject j = jsonMsg.getJSONObject("message");
                timetoken = jsonMsg.getString("timetoken");
                Log.i("CHAT","samosa1 "+jsonMsg.getString("message"));

                if (j.has("message") && j.has("from") && j.has("to")) {
                    Log.i("CHAT","samosa");
                    body = j.getString("message");
                    Log.i("CHAT","papdi "+body);

                    if (j.getString("from").equalsIgnoreCase("DEFAULT")){
                        Log.i("CONVER", "DEFAULT set");
                        userType = ChatMessageUserType.DEFAULT;
                    }
                    else if (j.getString("from").equalsIgnoreCase("LISTING")){
                        Log.i("CONVER", "LISTING set");
                        userType = ChatMessageUserType.LISTING;
                    }
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
                    message.setMessageTime(new Date().getTime());
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
        String To = null;
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
                channel_name = General.getSharedPreferences(this, AppConstants.USER_ID);
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
// if channel name is ok id dont call pubnubwhere now
            pubnubWhereNow(General.getSharedPreferences(this, AppConstants.USER_ID));

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
                        Log.i(TAG, "loadhistory not empty");
                        for (int i = 0; i < jsonArrayHistoryLength; i++) {
                            JSONObject jsonMsg = jsonArrayHistory.getJSONObject(i);

                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub jsonArrayResponse" + jsonArrayResponse);
                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub jsonArrayHistory" + jsonArrayHistory);
                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub" + jsonMsg);
                            displayMessage(jsonMsg);

                        }
                    }
                    else{
                        Log.i(TAG, "loadhistory empty");


                        JSONObject jsonMsg = new JSONObject();
                        //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
                        jsonMsg.put("from", "DEFAULT");
                        //jsonMsg.put("to", "client");



                        jsonMsg.put("to", "client");


                        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        String json = gson.toJson(AppConstants.letsOye);
                        JSONObject jsonResponse = new JSONObject(json);


                        Log.d(TAG, "AppConstants.letsOye " + jsonResponse.getString("property_subtype"));



                        jsonMsg.put("message", "You have initiated enquiry for requested "+jsonResponse.getString("property_subtype")+", "+jsonResponse.getString("property_type")+" property within budget "+General.currencyFormat(jsonResponse.getString("price"))+" Within a moment we are connecting you to our top 3 brokers.");
                        //Log.i("TRACE","messageText is "+messageText);
                        //Log.i(TAG,"messageText is"+messageText);
                        //publish message
                        pubnub.publish(channel, jsonMsg, true, new Callback() {});
                        Log.i(TAG, "Default message published");


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
        pubnub.history(channel_name,true, 10,callback);
    }


    /**
     *  pubnub push notification
     */

    public void sendNotification(String channel_name) throws JSONException {

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

    private void cacheMessages(JSONArray msgs){
//        JSONObject jo = new JSONObject();
//
//// populate the array
//        try {
//            jo.put("msg",msgs);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        General.setSharedPreferences(this,channel_name+AppConstants.CACHE,msgs.toString());
        Log.i("CHAT","cache "+General.getSharedPreferences(this,channel_name+AppConstants.CACHE));

    }

    @Override
    public void onBackPressed() {

        General.setSharedPreferences(this,channel_name+AppConstants.CACHE,cachedmsgs.toString());
        Log.i("CHAT","cache "+General.getSharedPreferences(this,channel_name+AppConstants.CACHE));

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



        super.onBackPressed();
        }




}