package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nbourses.oyeok.R;
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
    protected  float DPTOPX_SCALE =0;


    public void setAdapter(JSONArray chats,boolean chat) {
        // Required empty public constructor
        mChats = chats;
        this.chat = chat;
    }

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

        mChatLayout = (LinearLayout) v.findViewById(R.id.chatlayout);
        mChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mitemClickListener.onClicked(-1,"hide");
            }
        });
        Bundle args = getArguments();
        if(args != null)
        {
            try {
                mChats = new JSONArray(args.getString("array"));
                chat  = args.getBoolean("flag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setupview();
        }



        return v;
    }

    public void setupview()
    {
        mChatLayout.removeAllViews();
        for(int i = 0; i<mChats.length();i++)
        {
            JSONObject chat_item = null;
            try {
                chat_item = mChats.getJSONObject(i);
                String direction = chat_item.get("direction").toString();
                String text = chat_item.get("text").toString();
                final View inside_view = getActivity().getLayoutInflater().inflate(R.layout.droom_chat_item, null);
                final RelativeLayout background = (RelativeLayout) inside_view.findViewById(R.id.background);
                final LinearLayout showHide = (LinearLayout) inside_view.findViewById(R.id.showHide);
                ImageView like = (ImageView) inside_view.findViewById(R.id.like);
                ImageView dislike = (ImageView) inside_view.findViewById(R.id.dislike);


                final int pos = i;
                like.setOnClickListener(new View.OnClickListener() {
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


                TextView tv = (TextView) inside_view.findViewById(R.id.item);
                tv.setText(text);

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
                    //params1.gravity = Gravity.RIGHT;
                    //background.setLayoutParams(params1);
                    background.setBackgroundResource(R.drawable.bubble_green);
                }
                inside_view.setLayoutParams(params);

                if(chat)
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
                }



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
        final LinearLayout showHide = (LinearLayout) inside_view.findViewById(R.id.showHide);
        ImageView like = (ImageView) inside_view.findViewById(R.id.like);
        ImageView dislike = (ImageView) inside_view.findViewById(R.id.dislike);


        final int pos = mChats.length();
        like.setOnClickListener(new View.OnClickListener() {
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
        });


        TextView tv = (TextView) inside_view.findViewById(R.id.item);
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
        final LinearLayout showHide = (LinearLayout) inside_view.findViewById(R.id.showHide);
         showHide.setVisibility(View.GONE);
        TextView tv = (TextView) inside_view.findViewById(R.id.item);
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
