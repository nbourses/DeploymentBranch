package com.nbourses.oyeok.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserType;
import com.nbourses.oyeok.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rohit on 17/02/16.
 */
public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");

    public ChatListAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;

    }


    @Override
    public int getCount() {
        Log.i("TRACE","in get count"+chatMessages.size());
        return chatMessages.size();
    }

    @Override
    public Object getItem(int position) {

        Log.i("TRACE","in get posi"+chatMessages.get(position));
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i("TRACE","in get itemid"+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ChatMessage message = chatMessages.get(position);
        ViewHolder1 holder1;
        ViewHolder2 holder2;
        ViewHolder3 holder3;
        if (message.getUserType() == ChatMessageUserType.DEFAULT) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.default_chat, null, false);
                holder3 = new ViewHolder3();
            //    holder3.spinnerProgress = (ProgressBar) v.findViewById(R.id.spinnerProgress);
                holder3.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder3.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder3.chatReplyAuthor = (TextView) v.findViewById(R.id.chat_reply_author);
                holder3.txtFirstChar = (TextView) v.findViewById(R.id.txt_first_char);

                v.setTag(holder3);
            }
            else {
                v = convertView;
                holder3 = (ViewHolder3) v.getTag();
            }

            String userName = message.getUserName();
            //String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());

            holder3.messageTextView.setText(message.getMessageText());
            holder3.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            holder3.chatReplyAuthor.setText("Welcome "+userName );
            //holder3.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());
            holder3.txtFirstChar.setText("O");

        }


        if (message.getUserType() == ChatMessageUserType.SELF) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user1_item, null, false);
                holder1 = new ViewHolder1();

                holder1.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder1.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder1.chatReplyAuthor = (TextView) v.findViewById(R.id.chat_reply_author);
                holder1.txtFirstChar = (TextView) v.findViewById(R.id.txt_first_char);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (ViewHolder1) v.getTag();

            }

            String userName = message.getUserName();
            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());

            holder1.messageTextView.setText(message.getMessageText());
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            holder1.chatReplyAuthor.setText(name);
            holder1.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());
        }
        else if (message.getUserType() == ChatMessageUserType.OTHER) {

            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.chat_user2_item, null, false);

                holder2 = new ViewHolder2();

                holder2.messageTextView = (TextView) v.findViewById(R.id.message_text);
                holder2.timeTextView = (TextView) v.findViewById(R.id.time_text);
                holder2.messageStatus = (ImageView) v.findViewById(R.id.user_reply_status);
                v.setTag(holder2);

            } else {
                v = convertView;
                holder2 = (ViewHolder2) v.getTag();

            }

            holder2.messageTextView.setText(message.getMessageText());
            //holder2.messageTextView.setText(message.getMessageText());
            holder2.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));

            if (message.getMessageStatus() == ChatMessageStatus.DELIVERED) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick));
            } else if (message.getMessageStatus() == ChatMessageStatus.SENT) {
                holder2.messageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_single_tick));

            }
        }
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessages.get(position);
        return message.getUserType().ordinal();
    }

    private class ViewHolder1 {
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView chatReplyAuthor;
        public TextView txtFirstChar;
    }

    private class ViewHolder2 {
        public ImageView messageStatus;
        public TextView messageTextView;
        public TextView timeTextView;

    }

    private class ViewHolder3 {
     //   public ProgressBar spinnerProgress;
        public TextView messageTextView;
        public TextView timeTextView;
        public TextView chatReplyAuthor;
        public TextView txtFirstChar;
    }
}
