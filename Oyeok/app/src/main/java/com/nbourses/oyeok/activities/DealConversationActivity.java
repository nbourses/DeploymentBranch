package com.nbourses.oyeok.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.adapters.ChatListAdapter;
import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserType;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ChatMessage;
import com.pubnub.api.Callback;
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
    private static final String[] suggestionsForClientArray = {"How can I use this app?", "How can I find property?", "Is it free of cost to get broker?"};
    private static final String[] suggestionsForBrokerArray = {"How can I use this app?", "How can I find client?", "Can I post property?"};
    private Boolean isDefaultDeal;
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


        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!channel_name.equals(""))
            i(TAG, "chanel name was not null" + channel_name);
        if(!channel_name.equals("my_channel"))
        {

            setupPubnub(channel_name);}  // DEals OK ID
        else {
            setupPubnub(UUID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pubnub != null)
            pubnub.unsubscribeAll();
    }

    private void init() {



        edtTypeMsg.addTextChangedListener(edtTypeMsgListener);

        chatMessages = new ArrayList<>();

        i(TAG, "chatMessages are" + chatMessages);


        Bundle bundle  = getIntent().getExtras();
       channel_name = bundle.getString(AppConstants.OK_ID); //my_channel if came from root item
       isDefaultDeal = bundle.getBoolean("isDefaultDeal");

        i("TRACE DEALS FLAG 3", "FLAG " + isDefaultDeal);



        if (!channel_name.equals(""))
            i(TAG, "chanel name was not null" + channel_name);




        try {
            pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);

            if(channel_name.equals("my_channel"))
            {
                Log.i("WHERENOW", "2 ");

                pubnub.subscribe(UUID, new Callback() {
                            @Override
                            public void connectCallback(String channel, Object message) {
                                Log.i("WHERENOW", "61 ");
                            }

                            @Override
                            public void disconnectCallback(String channel, Object message) {
                                Log.i("WHERENOW", "62 ");
                            }

                            public void reconnectCallback(String channel, Object message) {
                                Log.i("WHERENOW", "63 ");
                            }

                            @Override
                            public void successCallback(String channel, final Object message) {
                                try {
                                    Log.i("WHERENOW", "6 ");
                                    JSONObject jsonMsg = (JSONObject) message;
                                    i(TAG, "pubnub setup success" + jsonMsg);

                                   // displayMessage(jsonMsg);
                                } catch (Exception e) {
                                    Log.i("WHERENOW", "64 "+e);
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void errorCallback(String channel, PubnubError error) {
                                Log.i("WHERENOW", "error subscribing " + error);
                            }
                        }
                );
            }

            Log.i("WHERENOW", "UUID " + pubnub.getUUID());

            Callback callback = new Callback() {
                public void successCallback(String channel, Object response) {
                    System.out.println(response.toString());
                    Log.i("WHERENOW", "success " + response.toString());
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        String a = obj.getString("channels");
                        Log.i("WHERENOW", "successaa" +a);


                        JSONObject obje = new JSONObject(response.toString());
                        JSONArray arr = obje.getJSONArray("channels");
                        for (int i = 0; i < arr.length(); i++)
                           // System.out.println(arr.getString(i));
                        Log.i("WHERENOW", "successaab" + arr.getString(i));





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                public void errorCallback(String channel, PubnubError error) {
                    System.out.println(error.toString());
                    i("WHERENOW", "failure " + error.toString());

                }
            };
            pubnub.whereNow(UUID, callback);

        }
        catch (Exception e) {
            e.printStackTrace();
        }








        listAdapter = new ChatListAdapter(chatMessages,isDefaultDeal, this);
        chatListView.setAdapter(listAdapter);

//        Bundle bundle  = getIntent().getExtras();
//        channel_name = bundle.getString(AppConstants.OK_ID);

        i(TAG, "channel_name" + channel_name);
        userRole = bundle.getString("userRole");
        i(TAG, "userRole is" + userRole);

        ArrayAdapter<String> adapterSuggestions = null;
        //userRole.equals("other") then load simple_list_item_2
        if (userRole.equals("client"))
            adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsForClientArray);
        else
            adapterSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, suggestionsForBrokerArray);

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
        i("WHERENOW", "3 ");

        try {
            i("WHERENOW", "4 ");

            pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);

            i(TAG, "before loadHistoryFromPubnub");
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
                                i("WHERENOW", "6 ");
                                JSONObject jsonMsg = (JSONObject) message;
                               i(TAG, "pubnub setup success" + jsonMsg);

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
            i("WHERENOW", "6 ");
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
        i("WHERENOW", "5 ");
        i("CONVER", "jsonMsg" + jsonMsg);
        i(TAG, "inside displayMessage" + jsonMsg);

        String body = null;
        ChatMessageUserType userType = null;
        final ChatMessage message = new ChatMessage();
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";


        try {
            if (jsonMsg.has("message") && jsonMsg.has("from") && jsonMsg.has("to")) {
                body = jsonMsg.getString("message");

                if (jsonMsg.getString("from").equalsIgnoreCase("DEFAULT")){
                    i("CONVER", "DEFAULT set");
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
                i(TAG, "message before adding to chatMessages" + message);
                chatMessages.add(message);
                i(TAG, "message after adding to chatMessages" + chatMessages);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        i(TAG, "message runOnUiThread" + listAdapter);
                        if (listAdapter != null) {
                            i(TAG, "message runOnUiThread  not null");
                            listAdapter.notifyDataSetChanged();
                        }
                        i(TAG, "message runOnUiThread edtTypeMsg1");
                        edtTypeMsg.setText("");
                        i(TAG, "message runOnUiThread edtTypeMsg2");
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
            i(TAG, "caught in display message" + e);
        }
    }

    /**
     * send message through pubnub
     * @param messageText
     */





    private void sendMessage(final String messageText)
    {
        i(TAG, "Insidde send message");
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
             i(TAG, "Role is not properly saved in shared preferences" + role);



        try {
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("from", General.getSharedPreferences(this ,AppConstants.USER_ID));
            jsonMsg.put("name", dbHelper.getValue(DatabaseConstants.name));
            i(TAG, "name is " + dbHelper.getValue(DatabaseConstants.name));



            //.put("from", General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));
            //jsonMsg.put("to", "client");
            jsonMsg.put("to", To);
            jsonMsg.put("message", messageText);

            i("TRACE", "messageText is " + messageText);
            i(TAG, "messageText is" + messageText);
            i(TAG, "channel_name is" + channel_name);
            i(TAG, "jsonMsg" + jsonMsg);

            //publish message



            pubnub.publish(General.getSharedPreferences(this ,AppConstants.USER_ID), jsonMsg, true, new Callback() {});
            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load pubnub history
     */
    private void loadHistoryFromPubnub(String channel_name) {

        i(TAG, "inside loadHistoryFromPubnub");
        i(TAG, "inside loadHistoryFromPubnub channel name is" + channel_name);

        Callback callback = new Callback() {

            public void successCallback(String channel, Object response) {
                i(TAG, "inside loadHistoryFromPubnub channel name is2" + channel);
                try {
                    JSONArray jsonArrayResponse = new JSONArray(response.toString());
                    JSONArray jsonArrayHistory = jsonArrayResponse.getJSONArray(0);
                    int jsonArrayHistoryLength = jsonArrayHistory.length();
                    if (jsonArrayHistory.length() > 0) {
                        for (int i = 0; i < jsonArrayHistoryLength; i++) {
                            JSONObject jsonMsg = jsonArrayHistory.getJSONObject(i);

                            i(TAG, "jsonMsg is success loadHistoryFromPubnub" + jsonMsg);
                            displayMessage(jsonMsg);

                        }
                    }
                    else{


                        JSONObject jsonMsg = new JSONObject();
                        //String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
                        jsonMsg.put("from", "DEFAULT");
                        //jsonMsg.put("to", "client");



                        jsonMsg.put("to", "client");


                        final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        String json = gson.toJson(AppConstants.letsOye);
                        JSONObject jsonResponse = new JSONObject(json);


                        Log.d(TAG, "AppConstants.letsOye " + jsonResponse.getString("property_subtype"));



                        jsonMsg.put("message", "You have initiated inquiry for requested "+jsonResponse.getString("property_subtype")+", "+jsonResponse.getString("property_type")+" property within budget "+jsonResponse.getString("price")+" Within a moment we are connecting you to our top 3 brokers.");
                        //Log.i("TRACE","messageText is "+messageText);
                        //Log.i(TAG,"messageText is"+messageText);
                        //publish message
                        pubnub.publish(channel, jsonMsg, true, new Callback() {});
                        i(TAG, "Default message published");



                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void errorCallback(String channel, PubnubError error) {
            }
        };
        pubnub.history(channel_name, 10, false, callback);
    }

    /**
     * all active userIds on channel
     */
    private void pubnubHereNow() {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println("pubnubHereNow "+response.toString());
                i(TAG, "pubnubHereNow" + response.toString());
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







}
