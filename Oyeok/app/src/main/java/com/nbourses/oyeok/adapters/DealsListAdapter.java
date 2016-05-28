package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.models.PublishLetsOye;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by rohit on 03/02/16.
 */
public class DealsListAdapter extends BaseAdapter {
    private List<PublishLetsOye> dealses;
    private Context context;

    public DealsListAdapter(List<PublishLetsOye> dealses, Context context) {
        this.dealses = dealses;
        this.context = context;

    }

    @Override
    public int getCount() {
        return dealses.size();
    }

    @Override
    public Object getItem(int position) {
        return dealses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ViewHolder holder;
        PublishLetsOye deal = dealses.get(position);

        if (convertView == null) {
            v = LayoutInflater.from(context).inflate(R.layout.support_chat_item, parent, false);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            holder.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            holder.txtTime = (TextView) v.findViewById(R.id.txtTime);
            holder.txtFirstChar = (TextView) v.findViewById(R.id.txtFirstChar);

            v.setTag(holder);
        }
        else {
            v = convertView;
            holder = (ViewHolder) v.getTag();
        }

        holder.txtFirstChar.setText(deal.getPropertyType().substring(0,1).toUpperCase());
        String propertyType = String.valueOf(deal.getPropertyType().charAt(0)).toUpperCase() + deal.getPropertyType().subSequence(1, deal.getPropertyType().length());
        holder.txtTitle.setText(propertyType+" (Looking for a Broker)");
        holder.txtDescription.setText("Property sub type: "+deal.getPropertySubType()
                                        +", Size: "+deal.getSize()
                                        +", Price: "+deal.getPrice());
        Log.i("CHAT","current time "+DateFormat.getDateTimeInstance().format(new Date()));

        holder.txtTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
       Log.i("CHAT","current time "+DateFormat.getDateTimeInstance());

        return v;
    }

    private class ViewHolder {
        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtTime;
        public TextView txtFirstChar;
    }
}

