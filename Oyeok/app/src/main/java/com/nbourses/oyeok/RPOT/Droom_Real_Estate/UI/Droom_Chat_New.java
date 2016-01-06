package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.Firebase.DroomDetails;
import com.nbourses.oyeok.R;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class Droom_Chat_New extends Fragment  {

    private JSONArray mChats;
    private LinearLayout mChatLayout;
    private boolean chat;
    private itemClickListener mitemClickListener;
    private Button sendMessageButton;
    protected  float DPTOPX_SCALE =0;
    DBHelper dbHelper;
    private EditText sendMessageEditText;
    ScrollView scroll;
    String okId="",userId1="",userId2="";



    Callback callback = new Callback() {
        public void successCallback(String channel, Object response) {
            //System.out.println(response.toString());
            Log.i("chat history",response.toString());
            String data= response.toString();
            try {
                //JSONObject rData= new JSONObject(data);
                JSONArray aData= new JSONArray(data).getJSONArray(0);

                for(int i=0;i<aData.length();i++) {
                    JSONObject rData = aData.getJSONObject(i);
                    Log.i("object"+i,rData.toString());
                }
                setAdapter(aData,true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void errorCallback(String channel, PubnubError error) {
            System.out.println(error.toString());
        }
    };





    //pubnub.history("demo_tutorial1", 100, true, callback);

    public void setAdapter(JSONArray chats,boolean chat) {
        // Required empty public constructor
        mChats = chats;
        this.chat = chat;
        if(isAdded())
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupview();
                }
            });}



    public Droom_Chat_New.itemClickListener getItemClickListener() {
        return mitemClickListener;
    }

    public void setItemClickListener(Droom_Chat_New.itemClickListener itemClickListener) {
        this.mitemClickListener = itemClickListener;
    }

    public interface itemClickListener
    {
        public void onClicked(int position, String status);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DPTOPX_SCALE = getResources().getDisplayMetrics().density;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.droom_chat_layout, container, false);
        final DBHelper dbHelper= new DBHelper(getActivity());
        mChatLayout = (LinearLayout) v.findViewById(R.id.chatlayout);
        sendMessageButton = (Button) v.findViewById(R.id.bt_send_message);
        sendMessageEditText= (EditText)v.findViewById(R.id.et_send_message);
        scroll= (ScrollView) v.findViewById(R.id.chat_scroll_view);

        Bundle b=getArguments();
        DroomDetails droomDetails=new DroomDetails();
        DroomChatFirebase droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl);
        okId= (String) b.get("OkId");
        userId1= (String) b.get("UserId1");
        userId2= (String) b.get("UserId2");
       /* droomDetails=droomChatFirebase.getChatRoom(okId,userId1);*/
        final Pubnub pubnub = new Pubnub("pub-c-da891650-b0d6-4cfc-901c-60ca47bfcf90", "sub-c-c85c5622-b36d-11e5-bd0b-02ee2ddab7fe");
        pubnub.history(okId, 100, true, callback);


        /*mChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mitemClickListener.onClicked(-1, "hide");
            }
        });*/
        Bundle args = getArguments();
        /*if(args != null)
        {
            try {
                mChats = new JSONArray(args.getString("array"));
                chat  = args.getBoolean("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setupview();
        }*/


        try {
            pubnub.subscribe(okId, new Callback() {
                public void successCallback(String channel, Object message) {
                    //Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_LONG).show();
                    try {
                        mChats.put(mChats.length(),message.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setAdapter(mChats,true);
                }

                public void errorCallback(String channel, PubnubError error) {
                    Toast.makeText(getActivity(), error.getErrorString(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject o = new JSONObject();
                if (!sendMessageEditText.getText().toString().equalsIgnoreCase("")){
                    try {
                        //o.put("sender_id",dbHelper.getValue(DatabaseConstants.userId));
                        o.put("sender_id", userId1);
                        o.put("message", sendMessageEditText.getText().toString());
                        o.put("receiver_id", userId2);
                    } catch (Exception e) {
                        Log.i("exception in", "publish message");
                    }
                }

                //o.put("receiver_id",);
                pubnub.publish(okId, o, new Callback() {
                });
                try {
                    mChats.put(mChats.length(),o);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setAdapter(mChats, true);
                sendMessageEditText.setText("");
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
                hideSoftKeyboard(getActivity());
            }
        });

        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
        return v;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupview()
    {
        mChatLayout.removeAllViews();
        for(int i = 0; i<mChats.length();i++)
        {
            JSONObject chat_item = null;
            try {
                chat_item = mChats.getJSONObject(i);
                //String direction = chat_item.get("direction").toString();
                //String text = chat_item.get("text").toString();

                String senderId=chat_item.getString("sender_id");
                String direction;
                if(senderId.equalsIgnoreCase(userId2))
                    direction ="left";
                else
                    direction="right";
                String receiverId=chat_item.getString("receiver_id");
                String message=chat_item.getString("message");
                final View inside_view = getActivity().getLayoutInflater().inflate(R.layout.droom_chat_item, null);
                final RelativeLayout background = (RelativeLayout) inside_view.findViewById(R.id.background);
                //final LinearLayout showHide = (LinearLayout) inside_view.findViewById(R.id.showHide);
                /*ImageView like = (ImageView) inside_view.findViewById(R.id.like);
                ImageView dislike = (ImageView) inside_view.findViewById(R.id.dislike);*/


                final int pos = i;
                /*like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      mitemClickListener.onClicked(pos,"liked");
                        JSONArray shiftArray = new JSONArray();
                        for(int i= 0 ;i<mChats.length();i++)
                        {
                            if(i != pos)
                            {
                                try {
                                    shiftArray.put(mChats.get(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        mChats = shiftArray;
                        setupview();
                        //mChatLayout.removeView(inside_view);


                    }
                });

                dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      mitemClickListener.onClicked(pos,"disliked");
                        JSONArray shiftArray = new JSONArray();
                        for(int i= 0 ;i<mChats.length();i++)
                        {
                            if(i != pos)
                            {
                                try {
                                    shiftArray.put(mChats.get(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        mChats = shiftArray;
                        setupview();
                        //mChatLayout.removeView(inside_view);
                    }
                });
*/

                TextView tv = (TextView) inside_view.findViewById(R.id.itemChat);
                tv.setText(message);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(200*DPTOPX_SCALE), LinearLayout.LayoutParams.WRAP_CONTENT);
                if(direction.equals("left")) {
                    params.gravity = Gravity.LEFT;
                    //params1.gravity = Gravity.LEFT;
                    //background.setLayoutParams(params1);
                    if(chat)
                    {
                        background.setBackgroundResource(R.drawable.bubble_yellow);
                    }else
                    {
                        background.setBackgroundResource(R.drawable.bubble_green);
                    }
                    //inside_view.setGravity(Gravity.LEFT);
                }else {
                    // inside_view.setGravity(Gravity.RIGHT);
                    params.gravity = Gravity.RIGHT;
                    FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params1.gravity = Gravity.RIGHT;
                    background.setLayoutParams(params1);
                    background.setBackgroundResource(R.drawable.bubble_green);
                }
                inside_view.setLayoutParams(params);

                /*if(chat)
                {
                    inside_view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(showHide.getVisibility() == View.VISIBLE)
                            {
                                showHide.setVisibility(View.GONE);
                            }else {
                                showHide.setVisibility(View.VISIBLE);
                            }
                            return true;
                        }
                    });
                }*/



                mChatLayout.addView(inside_view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addvalue(String m)
    {

        final View inside_view = getActivity().getLayoutInflater().inflate(R.layout.droom_chat_item, null);
        final RelativeLayout background = (RelativeLayout) inside_view.findViewById(R.id.background);
        //final LinearLayout showHide = (LinearLayout) inside_view.findViewById(R.id.showHide);
        /*ImageView like = (ImageView) inside_view.findViewById(R.id.like);
        ImageView dislike = (ImageView) inside_view.findViewById(R.id.dislike);*/


        final int pos = mChats.length();
        /*like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mitemClickListener.onClicked(pos,"liked");
                JSONArray shiftArray = new JSONArray();
                for(int i= 0 ;i<mChats.length();i++)
                {
                    if(i != pos)
                    {
                        try {
                            shiftArray.put(mChats.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mChats = shiftArray;
                mChatLayout.removeView(inside_view);


            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mitemClickListener.onClicked(pos, "disliked");
                JSONArray shiftArray = new JSONArray();
                for (int i = 0; i < mChats.length(); i++) {
                    if (i != pos) {
                        try {
                            shiftArray.put(mChats.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mChats = shiftArray;
                mChatLayout.removeView(inside_view);
            }
        });*/


        TextView tv = (TextView) inside_view.findViewById(R.id.itemChat);
        tv.setText(m);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(200*DPTOPX_SCALE), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        background.setBackgroundResource(R.drawable.bubble_green);

        inside_view.setLayoutParams(params);

        JSONObject obj = new JSONObject();
        try {
            obj.put("direction","right");
            obj.put("text",m);
            mChats.put(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mChatLayout.addView(inside_view);


    }

    public void addAcceptandRejectView(JSONArray m,String tag)
    {
        final View inside_view = getActivity().getLayoutInflater().inflate(R.layout.droom_chat_item, null);
        final RelativeLayout background = (RelativeLayout) inside_view.findViewById(R.id.background);
        //final LinearLayout showHide = (LinearLayout) inside_view.findViewById(R.id.showHide);
        //showHide.setVisibility(View.GONE);
        TextView tv = (TextView) inside_view.findViewById(R.id.itemChat);
        String text = "";
        if(tag.equals("rejected"))
        {

            text = "Here are the params that are rejected by other party "+"\n";


        }else
        {
            text = "Here are the params that are Accepted by other party "+"\n";

        }

        text = text + "Start : *************************************"+"\n";

        for(int i=0;i<m.length();i++)
        {
            try {
                JSONObject object = m.getJSONObject(i);
                String tex = object.getString("text");
                text = text + tex + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        text = text + "End   :  *************************************"+"\n";
        tv.setText(text);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(200*DPTOPX_SCALE), LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        background.setBackgroundResource(R.drawable.bubble_green);

        inside_view.setLayoutParams(params);
        mChatLayout.addView(inside_view);

    }

    public void addEmoji(Emojicon emoji)
    {
        LinearLayout li = new LinearLayout(getActivity());
        //LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        li.setBackgroundResource(R.drawable.bubble_green);
        EmojiconTextView tv = new EmojiconTextView(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        tv.setText(emoji.getEmoji());

        li.addView(tv);
        li.setLayoutParams(params);
        mChatLayout.addView(li);

    }


}
