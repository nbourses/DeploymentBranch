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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

  //  @Bind(R.id.spinnerProgress)
   // ProgressBar spinnerProgress;

    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
  //  @Bind(R.id.texRating)
  //  ProgressBar texrating;
  private TextView texrating;

    private static final String TAG = "DealConversationActivit";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private Pubnub pubnub;
    private String channel_name = "";
    private ChatListAdapter listAdapter;
    private ArrayList<ChatMessage> chatMessages;


    private String userRole = "client";
    private static final String[] suggestionsForClientArray = {"How can I use this app?", "How can I find property?", "Is it free of cost to get broker?"};
    private static final String[] suggestionsForBrokerArray = {"How can I use this app?", "How can I find client?", "Can I post property?"};

    private final TextWatcher edtTypeMsgListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Log.i(TAG,"before edtTypeMsg changed");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Log.i(TAG,"on edtTypeMsg changed");
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
            Log.i(TAG,"after edtTypeMsg changed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deal_conversation);
        ButterKnife.bind(this);
       texrating = (TextView) findViewById(R.id.texRating);
        ratingBar.setOnRatingBarChangeListener(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!channel_name.equals(""))
            Log.i(TAG, "chanel name was null");
            setupPubnub();
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
        Log.i(TAG,"chatMessages are"+chatMessages);

        listAdapter = new ChatListAdapter(chatMessages, this);
        chatListView.setAdapter(listAdapter);

        Bundle bundle  = getIntent().getExtras();
        channel_name = bundle.getString(AppConstants.OK_ID);
        Log.i(TAG,"channel_name"+channel_name);
        userRole = bundle.getString("userRole");
        Log.i(TAG,"userRole is"+userRole);

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
    private void setupPubnub() {
        chatMessages.clear();
        listAdapter.notifyDataSetChanged();

        try {
            pubnub = new Pubnub(AppConstants.PUBNUB_PUBLISH_KEY, AppConstants.PUBNUB_SUBSCRIBE_KEY);

            Log.i(TAG,"before loadHistoryFromPubnub");
            loadHistoryFromPubnub();

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
                                JSONObject jsonMsg = (JSONObject) message;
                               Log.i(TAG,"pubnub setup success" +jsonMsg);

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
        Log.i(TAG,"inside displayMessage"+ jsonMsg);

        String body = null;
        ChatMessageUserType userType = null;
        final ChatMessage message = new ChatMessage();
        String roleOfUser = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") ? "broker" : "client";

        try {
            if (jsonMsg.has("message") && jsonMsg.has("from") && jsonMsg.has("to")) {
                body = jsonMsg.getString("message");
                if (jsonMsg.getString("from").equalsIgnoreCase(General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER))){
                    userType = ChatMessageUserType.OTHER;
                }

               else if (jsonMsg.getString("from").equalsIgnoreCase(General.getSharedPreferences(getApplicationContext(), "DEFAULT"))){
                    userType = ChatMessageUserType.DEFAULT;
                }

                else {
                    userType = ChatMessageUserType.SELF;
                }

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
                        Log.i(TAG, "message runOnUiThread"+listAdapter);
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
            Log.i(TAG,"caught in display message" +e);
        }
    }

    /**
     * send message through pubnub
     * @param messageText
     */
    private void sendMessage(final String messageText)
    {
        Log.i(TAG,"Insidde send message");
        if(messageText.trim().length()==0)
            return;

        // Ritesh find to?
        String To = null;
        String role = General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER);
        if(role.equals("broker"))
            To = "client";
        else if(role.equals("client"))
            To = "broker";
        else
             Log.i(TAG,"Role is not properly saved in shared preferences"+ role);



        try {
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("from", General.getSharedPreferences(getApplicationContext(), AppConstants.ROLE_OF_USER));
            //jsonMsg.put("to", "client");
            jsonMsg.put("to", To);
            jsonMsg.put("message", messageText);

            Log.i("TRACE","messageText is "+messageText);
            Log.i(TAG,"messageText is"+messageText);

            //publish message
            pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load pubnub history
     */
    private void loadHistoryFromPubnub() {
        Log.i(TAG,"inside loadHistoryFromPubnub");
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                try {
                    JSONArray jsonArrayResponse = new JSONArray(response.toString());
                    JSONArray jsonArrayHistory = jsonArrayResponse.getJSONArray(0);
                    int jsonArrayHistoryLength = jsonArrayHistory.length();
                    if (jsonArrayHistory.length() > 0) {
                        for (int i = 0; i < jsonArrayHistoryLength; i++) {
                            JSONObject jsonMsg = jsonArrayHistory.getJSONObject(i);

                            Log.i(TAG, "jsonMsg is success loadHistoryFromPubnub" + jsonMsg);
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
                        pubnub.publish(channel_name, jsonMsg, true, new Callback() {});
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
        pubnub.history(channel_name, 10, false, callback);
    }

    /**
     * all active userIds on channel
     */
    private void pubnubHereNow() {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println("pubnubHereNow "+response.toString());
                Log.i(TAG,"pubnubHereNow"+response.toString());
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
        texrating.setText("Rating: "+rating);

    }
}
