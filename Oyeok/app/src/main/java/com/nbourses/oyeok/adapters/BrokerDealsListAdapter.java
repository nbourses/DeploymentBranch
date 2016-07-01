package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.BrokerDeals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by rohit on 03/02/16.
 */
public class BrokerDealsListAdapter extends BaseAdapter {
    private ArrayList<BrokerDeals> dealses;
    private ArrayList<String> deals;
    private boolean default_deal;

    private Context context;
    private BrokerDeals deal;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("h:mm a");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT1 = new SimpleDateFormat("DD:mm:yyyy");

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

                   v = LayoutInflater.from(context).inflate(R.layout.hdroom_chat_item, parent, false);
               holder = new ViewHolder();

                holder.txtTitle = (TextView) v.findViewById(R.id.txtTitle);
                holder.dealPtype =(ImageView) v.findViewById(R.id.dealPtype);
                holder.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
                holder.txtTime = (TextView) v.findViewById(R.id.txtTime);
                holder.txtFirstChar = (TextView) v.findViewById(R.id.txtFirstChar);
                holder.locality = (TextView) v.findViewById(R.id.locality);

                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }
        if(dealses.size()>0) {

            //this block handles both default deals and hd rooms

            deal = dealses.get(position);


            Log.i("HDROOMS CRASH", "deal.getSpecCode" + deal.getSpecCode());


//            String userName = (!deal.getSpecCode().equals("")) ? deal.getSpecCode() : "None";
//
//            holder.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());
//
//            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());
//            holder.txtTitle.setText(name);
//
//            holder.txtDescription.setText(deal.getMobileNo());  //this sets nothing for default deal
//
//            holder.txtTime.setText("23.11");


            try {
                String name = deal.getName().toUpperCase();

                String spec = (!deal.getSpecCode().equals("")) ? deal.getSpecCode() : "None";

                Log.i("spec code is", "spec hd rooms res " + spec);
                Log.i("spec code ok id is", "ok id is " + deal.getOkId());

                String[] split = spec.split("-");
                //StringBuilder sb = new StringBuilder();
                String intend = split[0];
                String tt = split[1].toUpperCase();

                String ptype = split[2];
                String pstype = split[3];
                String price = split[4];

                if (tt.equalsIgnoreCase("LL")) {
                    tt = "Rent";
                } else if (tt.equalsIgnoreCase("OR")) {
                    tt = "Sale";
                }

                if (intend.equalsIgnoreCase("REQ")) {
                    intend = "required at";
                } else if (intend.equalsIgnoreCase("AVL")) {
                    intend = "available for";
                }


//            String ptype = null;

//            if(pstype.equalsIgnoreCase("1bhk") || pstype.equalsIgnoreCase("2bhk") || pstype.equalsIgnoreCase("3bhk") || pstype.equalsIgnoreCase("4bhk") || pstype.equalsIgnoreCase("4+bhk")){
//                ptype = "home";
//            }
//            else if(pstype.equalsIgnoreCase("retail outlet") || pstype.equalsIgnoreCase("food outlet") || pstype.equalsIgnoreCase("bank")){
//                ptype = "shop";
//            }
//            else if(pstype.equalsIgnoreCase("cold storage") || pstype.equalsIgnoreCase("kitchen") || pstype.equalsIgnoreCase("manufacturing") || pstype.equalsIgnoreCase("warehouse") || pstype.equalsIgnoreCase("workshop")){
//                ptype = "industrial";
//            }
//            else if(pstype.equalsIgnoreCase("<15") || pstype.equalsIgnoreCase("<35") || pstype.equalsIgnoreCase("<50") || pstype.equalsIgnoreCase("<100") || pstype.equalsIgnoreCase("100+")){
//                ptype = "office";
//                pstype = pstype+" seater";
//            }


//            Iterator<BrokerDeals> it = dealses.iterator();
//
//          //  listBrokerDeals_new = new ArrayList<BrokerDeals>();
//            while (it.hasNext()) {
//                BrokerDeals deals = it.next();
//
//                Log.i("CHAT","deals.are"+deals);
//                Log.i("CHAT","deals.ok_id"+deals.getdefaultDeal());
////                if(!(deals.getOkId() == null))
////                {   if(deals.getSpecCode().contains(TT+"-")) {
////                    Log.i("DEALREFRESHPHASESEEKBA","deal spec code "+deals.getSpecCode()+" for "+TT);
////
////
////
////                }
//                }

                Log.i("CHAT", "default deal flag is " + default_deal);


                String description = ptype.substring(0, 1).toUpperCase() + ptype.substring(1) + " property (" + pstype + ") " + intend + " " + General.currencyFormat(price) + ".";

                Log.i("Deal data", "Deal data is" + deal.getName());

                //         holder.txtFirstChar.setText(name.substring(0, 1).toUpperCase());

                Log.i("Deal data", "Deal data is" + deal.getName());

                // String specs = String.valueOf(spec.charAt(0)).toUpperCase() + spec.subSequence(1, spec.length());


                if (General.getSharedPreferences(context, AppConstants.NAME).equalsIgnoreCase(name) || General.getSharedPreferences(context, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
                    holder.txtTitle.setText(name);
                else
                    holder.txtTitle.setText("Broker " + name);

                if (ptype.equalsIgnoreCase("office"))
                    holder.dealPtype.setImageResource(R.drawable.office);
                else if (ptype.equalsIgnoreCase("home"))
                    holder.dealPtype.setImageResource(R.drawable.home);
                else if (ptype.equalsIgnoreCase("shop"))
                    holder.dealPtype.setImageResource(R.drawable.shop);
                else if (ptype.equalsIgnoreCase("industrial"))
                    holder.dealPtype.setImageResource(R.drawable.industry);


                //  holder.txtDescription.setText(deal.getMobileNo());

                holder.txtDescription.setText(description);

                // get time from shared if not available then assign random date from last few months
                String time = fetchTime();


                if (time != null) {
                    Date date = new Date(Long.parseLong(time));
                    Calendar c1 = Calendar.getInstance(); // today
                    c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(date); // your date

                    if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                            && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
                        holder.txtTime.setText("Yesterday");
                    }
                    if (c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)
                            && c1.get(Calendar.DAY_OF_YEAR) > c2.get(Calendar.DAY_OF_YEAR)) {
                        holder.txtTime.setText(SIMPLE_DATE_FORMAT1.format(Long.parseLong(time)));
                    }

                    holder.txtTime.setText(SIMPLE_DATE_FORMAT.format(Long.parseLong(time)));
                } else
                    holder.txtTime.setText("Last month");
                // holder.txtTime.setText(dfDateTime.format(gc.getTime()));

                Log.i("HDROOM locality", "locality is " + deal.getLocality());
                try {
                    if ((!deal.getLocality().isEmpty())) {
                        Log.i("HDROOM locality", "locality is 1 " + deal.getLocality());
                        holder.locality.setText(deal.getLocality());
                    } else {
                        holder.locality.setText("Mumbai");
                    }
                } catch (Exception e) {
                }


            }
            catch (Exception e) {
                Log.i("brokerDealsListAdapter","deals spec code may not be proper "+e);
            }

        }
        else if(this.default_deal)
        {

            // currently this block isnt executing
            // Need to be worked on
            Log.i("inside BrokersDeals","--------");
            String userName = "A";
            holder.txtFirstChar.setText(userName.substring(0, 1).toUpperCase());

            String name = String.valueOf(userName.charAt(0)).toUpperCase() + userName.subSequence(1, userName.length());
            holder.txtTitle.setText(name);

            holder.txtDescription.setText("");

            holder.txtTime.setText("23:11");
        }

        return v;
    }

    private class ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtTime;
        public TextView txtFirstChar;
        public ImageView dealPtype;
        public TextView locality;

    }

    public int dp2px(int dp) {
        Log.i("TRACE1","dp"+" "+dp);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }











    private String fetchTime(){

        String time = null;
            String dealTime;
            HashMap<String, String> dealTime1;
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            dealTime = General.getDealTime(context);


            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();

                dealTime1 = gson.fromJson(dealTime, type);

        if (dealTime1 == null) {
            dealTime1 = new HashMap<String, String>();

        }


        Iterator<Map.Entry<String,String>> iter = dealTime1.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            if(deal.getOkId().equalsIgnoreCase(entry.getKey())){
                time = entry.getValue();

            }

        }
        return time;



    }


}

