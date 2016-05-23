package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
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
    private ArrayList<String> deals;
    private boolean default_deal;
    private Context context;

    public BrokerDealsListAdapter(ArrayList<BrokerDeals> dealses, Context context) {
        this.dealses = dealses;
        this.context = context;

    }
   /* public BrokerDealsListAdapter(boolean flag , Context context) throws JSONException {
        Log.i("default deal ","value "+flag);
        this.default_deal = true;

        ArrayList<BrokerDeals> Al = new ArrayList<BrokerDeals>();
        Al.set(0,new BrokerDeals());
        this.dealses = Al;
        this.context = context;

    }
*/

    public BrokerDealsListAdapter(Context context,boolean flag,ArrayList<String> deals){
        this.deals =deals;
        this.context =context;
        default_deal = flag;

    }



    @Override
    public int getCount() {
        if(!default_deal)
            return dealses.size();

        return deals.size();
    }

    @Override
    public Object getItem(int position) {
        if(default_deal)
            return deals.get(position);

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

Log.i("inside brokerdeals view","flag check "+this.default_deal);
            if (convertView == null) {

                   v = LayoutInflater.from(context).inflate(R.layout.broker_deal_item, parent, false);
               holder = new ViewHolder();

                holder.txtTitle = (TextView) v.findViewById(R.id.txtTitle);
                holder.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
                holder.txtTime = (TextView) v.findViewById(R.id.txtTime);
                holder.txtFirstChar = (TextView) v.findViewById(R.id.txtFirstChar);

                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }
        if(dealses.size()>0) {

            BrokerDeals deal = dealses.get(position);


            Log.i("HDROOMS CRASH","deal.getSpecCode"+deal.getSpecCode());



            String userName = (!deal.getSpecCode().equals("")) ? deal.getSpecCode() : "None";

            holder.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());

            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());
            holder.txtTitle.setText(name);

            holder.txtDescription.setText(deal.getMobileNo());

            holder.txtTime.setText("23.11");
        }
        else if(this.default_deal)
        {
            Log.i("inside BrokersDeals","--------");
            String userName = "A";
            holder.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());

            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());
            holder.txtTitle.setText(name);

            holder.txtDescription.setText("");

            holder.txtTime.setText("23.11");
        }

        return v;
    }

    private class ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtTime;
        public TextView txtFirstChar;

    }

    public int dp2px(int dp) {
        Log.i("TRACE1","dp"+" "+dp);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}

