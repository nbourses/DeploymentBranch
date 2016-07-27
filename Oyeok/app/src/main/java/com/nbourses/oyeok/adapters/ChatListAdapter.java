package com.nbourses.oyeok.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserType;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ChatMessage;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by rohit on 17/02/16.
 */
public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;
    private Boolean isDefaultDeal = false;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a");

    public ChatListAdapter(ArrayList<ChatMessage> chatMessages, Boolean isDefaultDeal, Context context) {
        this.chatMessages = chatMessages;
        this.isDefaultDeal = isDefaultDeal;
        this.context = context;

        Log.i("TRACE DEALS FLAG 1","FLAG "+isDefaultDeal);
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
        ViewHolder4 holder4;
        ViewHolder5 holder5;
        Log.i("uri","boyka "+message.getUserType());

        if (message.getUserType() == ChatMessageUserType.LISTING) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.cardview, null, false);
                holder4 = new ViewHolder4();
                holder4.spinnerProgress = (ProgressBar) v.findViewById(R.id.spinnerProgress);
                //holder4.messageTextView = (TextView) v.findViewById(R.id.message_text);
                //holder4.timeTextView = (TextView) v.findViewById(R.id.time_text);
               // holder4.chatReplyAuthor = (TextView) v.findViewById(R.id.chat_reply_author);
                holder4.txtFirstChar = (TextView) v.findViewById(R.id.txt_first_char);
                holder4.building1 = (TextView) v.findViewById(R.id.building1);
                holder4.building2 = (TextView) v.findViewById(R.id.building2);
                holder4.building3 = (TextView) v.findViewById(R.id.building3);
                holder4.price1 = (TextView) v.findViewById(R.id.price1);
                holder4.price2 = (TextView) v.findViewById(R.id.price2);
                holder4.price3 = (TextView) v.findViewById(R.id.price3);
                holder4.confirmListing = (Button) v.findViewById(R.id.confirmListing);
                holder4.laterListing = (Button) v.findViewById(R.id.laterListing);

                v.setTag(holder4);
            }
            else {
                v = convertView;
                holder4 = (ViewHolder4) v.getTag();
            }

            //String userName = message.getUserName(); // broker
//            String userName = General.getSharedPreferences(context, AppConstants.NAME);
//            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());

            Log.i("CONVER", "Chat message is Listing1" + message.getMessageText());


            Log.i("TRACE DEALS FLAG", "FLAG " + isDefaultDeal);
            if(!isDefaultDeal) {
                holder4.spinnerProgress.setVisibility(View.INVISIBLE);
                holder4.txtFirstChar.setVisibility(View.VISIBLE);
            }else{
                holder4.spinnerProgress.setVisibility(View.VISIBLE);
                holder4.txtFirstChar.setVisibility(View.INVISIBLE);
            }
           // holder3.messageTextView.setText(message.getMessageText());
//            holder4.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            //holder3.chatReplyAuthor.setText("Welcome "+name);
            //holder3.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());
            holder4.txtFirstChar.setText("L");

        }

        else if (message.getUserType() == ChatMessageUserType.IMG) {
            Log.i("image adapter","image adapter");
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.image, null, false);
                holder5 = new ViewHolder5();
                holder5.imageView = (ImageView) v.findViewById(R.id.imageView);
                //holder4.messageTextView = (TextView) v.findViewById(R.id.message_text);
                //holder4.timeTextView = (TextView) v.findViewById(R.id.time_text);
                // holder4.chatReplyAuthor = (TextView) v.findViewById(R.id.chat_reply_author);

                v.setTag(holder5);
            }
            else {
                v = convertView;
                holder5 = (ViewHolder5) v.getTag();
            }
            Log.i("IMGURL","image url is "+message.getImageUrl());


try {
    /*ImageFromUrl a = new ImageFromUrl();
     a.execute(message.getImageUrl());*/
    new DownloadImageTask(holder5.imageView).execute(message.getImageUrl());

    //holder5.imageView.setBackgroundResource(R.drawable.asset_oye_symbol_icon);
//    URL url = new URL(message.getImageUrl());
//    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//    holder5.imageView.setImageBitmap(bmp);
}
catch(Exception e){
    Log.i("IMGURL","image url is e "+e);
    // Image deleted default image
}


           /* Log.i("asakasa","asakasa "+General.getSharedPreferences(context,AppConstants.UPLOADED_IMAGE_PATH));
            File imageFile = new  File(General.getSharedPreferences(context,AppConstants.UPLOADED_IMAGE_PATH));
            if(imageFile.exists()){

                holder5.imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            }*/

            /*Bitmap bmp = BitmapFactory.decodeFile(General.getSharedPreferences(context,AppConstants.UPLOADED_IMAGE_PATH));

            holder5.imageView.setImageBitmap(bmp);
*/
            //String userName = message.getUserName(); // broker
//            String userName = General.getSharedPreferences(context, AppConstants.NAME);
//            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());
            Log.i("CONVER", "Chat message is Listing1" + message.getMessageText());


        }
        else if (message.getUserType() == ChatMessageUserType.DEFAULT) {
            if (convertView == null) {
                v = LayoutInflater.from(context).inflate(R.layout.default_chat, null, false);
                holder3 = new ViewHolder3();
                holder3.spinnerProgress = (ProgressBar) v.findViewById(R.id.spinnerProgress);
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

            //String userName = message.getUserName(); // broker
            String userName = General.getSharedPreferences(context, AppConstants.NAME);
            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());

            Log.i("CONVER", "Chat message is2" + message.getMessageText());


            Log.i("TRACE DEALS FLAG", "FLAG " + isDefaultDeal);
            if(!isDefaultDeal) {
                holder3.spinnerProgress.setVisibility(View.INVISIBLE);
                holder3.txtFirstChar.setVisibility(View.VISIBLE);
            }else{
                holder3.spinnerProgress.setVisibility(View.VISIBLE);
                holder3.txtFirstChar.setVisibility(View.INVISIBLE);
            }
            holder3.messageTextView.setText(message.getMessageText());
            holder3.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            holder3.chatReplyAuthor.setText("Welcome "+name);
            //holder3.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());
            holder3.txtFirstChar.setText("O");

        }


        else if (message.getUserType() == ChatMessageUserType.SELF) {
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

            Log.i("CONVER","Chat message is self3"+message.getMessageText());

            String userName = message.getUserName();
            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());

            holder1.messageTextView.setText(message.getMessageText());
            holder1.timeTextView.setText(SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
            holder1.chatReplyAuthor.setText(name);
            holder1.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());
            Log.i("CONVER","message time self "+message.getMessageTime() + "formated"  + SIMPLE_DATE_FORMAT.format(message.getMessageTime()));
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

            Log.i("CONVER","Chat message is other4"+message.getMessageText());
            Log.i("CONVER","message time other "+message.getMessageTime() + "formated"  + SIMPLE_DATE_FORMAT.format(message.getMessageTime()));


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
        return 5;
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
        public ProgressBar spinnerProgress;

        public TextView messageTextView;
        public TextView timeTextView;
        public TextView chatReplyAuthor;
        public TextView txtFirstChar;
    }

    private class ViewHolder4 {
        public ProgressBar spinnerProgress;

//        public TextView messageTextView;
//        public TextView timeTextView;
//        public TextView chatReplyAuthor;
        public TextView txtFirstChar;
        public TextView building1;
        public TextView building2;
        public TextView building3;
        public TextView price1;
        public TextView price2;
        public TextView price3;
        public Button confirmListing;
        public Button laterListing;

    }
    private class ViewHolder5 {

        public ImageView imageView;
    }






    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Log.i("flok","flokai 1");
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.i("flok","flokai 2");
            bmImage.setImageBitmap(result);
        }
    }


}
