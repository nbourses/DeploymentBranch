package com.nbourses.oyeok.RPOT.Droom_Real_Estate;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.CustomKeyboard_Fragment;
import com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI.Droom_Chat_New;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Droom_Inside extends AppCompatActivity implements Droom_Chat_New.itemClickListener,CustomKeyboard_Fragment.onValueSelected,EmojiconsFragment.OnEmojiconBackspaceClickedListener,EmojiconGridFragment.OnEmojiconClickedListener {

    private TextView smalldetail;
    private FrameLayout chatContainer;
    private ImageView smileys;
    private ImageView compare;
    private ImageView history;
    private Button showCommands;
    private FrameLayout container;
    private boolean first = true;
    public static JSONArray mAccepted = new JSONArray();
    public static JSONArray mRejected = new JSONArray();
    private JSONArray mChatArray = new JSONArray();
    private Droom_Chat_New chatScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droom__inside);
        smalldetail = (TextView) findViewById(R.id.smalldetail);
        smalldetail.setSelected(true);

        chatContainer = (FrameLayout) findViewById(R.id.chatContainer);
        smileys       = (ImageView) findViewById(R.id.smileys);
        compare       = (ImageView) findViewById(R.id.compare);
        history       = (ImageView) findViewById(R.id.history);
        showCommands  = (Button) findViewById(R.id.showCommands);
        container     = (FrameLayout) findViewById(R.id.container);

        chatContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                container.setVisibility(View.GONE);
            }
        });

        smileys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, EmojiconsFragment.newInstance(false))
                            .commit();

                container.setVisibility(View.VISIBLE);
            }
        });
        showCommands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomKeyboard_Fragment f = new CustomKeyboard_Fragment();

                 f.setMonValueSelected(Droom_Inside.this);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, f)
                        .commit();


                container.setVisibility(View.VISIBLE);
            }
        });


        JSONObject x = new JSONObject();
        try {
            x.put("direction","left");
            x.put("text","Brokerage 20%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(x);


        JSONObject y = new JSONObject();
        try {
            y.put("direction","right");
            y.put("text", "Advance 20k");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(y);


        JSONObject z = new JSONObject();
        try {
            z.put("direction", "left");
            z.put("text", "NoticePeriod 3months");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mChatArray.put(z);


         chatScreen = new Droom_Chat_New();
        Bundle args = new Bundle();
        args.putString("array", mChatArray.toString());
        args.putBoolean("flag", true);
        chatScreen.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.chatContainer, chatScreen).commit();
        chatScreen.setItemClickListener(this);


    }


    @Override
    public void onClicked(int position, String status) {

        if(position != -1) {

            JSONObject moved_object = new JSONObject();
            JSONArray shift_array = new JSONArray();
            for (int i = 0; i < mChatArray.length(); i++) {
                if (i != position) {
                    try {
                        shift_array.put(mChatArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        moved_object = mChatArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            mChatArray = shift_array;


            if (status.equals("liked")) {
                mAccepted.put(moved_object);
            } else {
                mRejected.put(moved_object);
            }
        }else
        {
            container.setVisibility(View.GONE);
        }

    }

    @Override
    public void onValueSelected(String obj) {

        chatScreen.addvalue(obj);

    }

    @Override
    public void setValueFromCommand(String status) {

        if(status.equals("rejected"))
        {
            chatScreen.addAcceptandRejectView(mRejected,"rejected");
        }else
        {
            chatScreen.addAcceptandRejectView(mAccepted,"accepted");
        }

    }


    @Override
    public void onEmojiconBackspaceClicked(View v) {

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        chatScreen.addEmoji(emojicon);
    }
}
