package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.models.BrokerDeals;

import java.util.ArrayList;


/**
 * Created by rohit on 03/02/16.
 */
public class BrokerDealsListAdapter extends BaseAdapter {
    private ArrayList<BrokerDeals> dealses;
    private Context context;

    public BrokerDealsListAdapter(ArrayList<BrokerDeals> dealses, Context context) {
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
        BrokerDeals deal = dealses.get(position);

        if (convertView == null) {
            v = LayoutInflater.from(context).inflate(R.layout.broker_deal_item, parent, false);
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

        String userName = (!deal.getSpecCode().equals("")) ? deal.getSpecCode() : "None";
        holder.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());

        String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());
        holder.txtTitle.setText(name);

        holder.txtDescription.setText(deal.getMobileNo());

        holder.txtTime.setText("23.11");

        return v;
    }

    private class ViewHolder {
        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtTime;
        public TextView txtFirstChar;
    }
}

