package com.nbourses.oyeok.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.fragments.WatchlistDisplayBuilding;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.WatchlistDisplayBuildingModel;
import com.nbourses.oyeok.models.loadBuildingDataModel;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.WatchListRealmModel;
import com.nbourses.oyeok.realmModels.WatchlistBuildingRealm;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by sushil on 07/02/17.
 */

public class watchlistDisplayAdapter extends BaseAdapter {

    WatchlistDisplayBuildingModel listing;
    ArrayList<WatchlistDisplayBuildingModel> watchList=new ArrayList<>();
    Context context;
    Holder holder;
    WatchlistDisplayBuilding frag;
    //ArrayList<Boolean> check_visible=new ArrayList<>();
    Boolean check_visible=false;
    //public final CheckBox checkBox;
    private LayoutInflater inflater;
    public watchlistDisplayAdapter(Context context, ArrayList<WatchlistDisplayBuildingModel> watchList, WatchlistDisplayBuilding frag) {
        this.watchList=watchList;
        this.context=context;
        this.frag=frag;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*for(int i=0;i<watchList.size();i++){
            check_visible.add(i,false);
        }*/

    }










    @Override
    public int getCount() {
        return watchList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }




/*
    @Override
    public int getCount() {
        return watchList.size();
    }
    @Override
    public Object getItem(int position) {
        return watchList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;

        listing=watchList.get(position);
        Log.i("printdata","watchList data=============== : "+listing.getName());
        if (convertView == null) {
            v = inflater.inflate(R.layout.display_watchlist_row, null);
            holder = new Holder(v);

            v.setTag(holder);
        } else {
            v = convertView;
            holder = (Holder) v.getTag();
        }


            holder.heading.setText(listing.getName() + " (" + listing.getConfig() + ")");
//            String text;
//            text="<html><sup></html>";
            holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(General.getFormatedPrice(listing.getConfig(), listing.getLl_pm()) + "") + "/m");
            if (listing.getTransactions() == null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);

            } else {
                holder.B_image.setImageResource(R.drawable.ic_home_selected);
                holder.B_image.setBackground(null);
               // holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);

        if(check_visible){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkBox.setVisibility(View.GONE);
        }

        if(listing.isCheckbox()){
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("status check","=============== "+position);
                frag.checkboxStatus(position);
            }
        });

        return v;
    }









    public void setIcon(WatchlistDisplayBuildingModel item, watchlistDisplayAdapter.Holder holder) {

        try {
            if(item.getRate_growth() !=null) {
                if (Integer.parseInt(item.getRate_growth()) < 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_down_red);
                    holder.ActualPrice.setTextColor(Color.parseColor("#ffb91422"));
                    holder.percentChange.setText((item.getRate_growth()).subSequence(1, (item.getRate_growth()).length()) + "%");

                } else if (Integer.parseInt(item.getRate_growth()) > 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_up_green);
                    holder.ActualPrice.setTextColor(Color.parseColor("#2dc4b6"));
                    holder.percentChange.setText((item.getRate_growth()).subSequence(1, (item.getRate_growth()).length()) + "%");

                } else {
                    holder.RateIndicator.setImageResource(R.drawable.sort_up_black);
                    holder.ActualPrice.setTextColor(Color.parseColor("black"));
                    holder.percentChange.setText(item.getRate_growth() + "%");
                }
            }else{
                holder.RateIndicator.setVisibility(View.GONE);
                holder.ActualPrice.setTextColor(Color.parseColor("#000000"));
                holder.percentChange.setText("");
            }
        }catch (Exception e){}
    }


    public ArrayList<WatchlistDisplayBuildingModel> getAllData(){
        return watchList;
    }


    public void setCheckBox(int position){
        //Update status of checkbox
        WatchlistDisplayBuildingModel items = watchList.get(position);
        items.setCheckbox(!items.isCheckbox());
        notifyDataSetChanged();
    }

    public void Show_check(){
       // for(WatchlistDisplayBuildingModel c:watchList) {
        check_visible=true;
           // holder.checkBox.setVisibility(View.VISIBLE);
       // }
        notifyDataSetChanged();
    }

    public void Hide_check(){
       // for(WatchlistDisplayBuildingModel c:watchList) {
           // holder.checkBox.setVisibility(View.GONE);
        check_visible=false;
       // }
        notifyDataSetChanged();
    }

    class Holder {
        public final TextView heading;
        public final ImageView B_image;
        public final  TextView description;
        public final  TextView percentChange ;
        public final  TextView ActualPrice;
        public final  ImageView RateIndicator;
        public final CheckBox checkBox;


        public Holder(View itemView) {
//            super(itemView);
            this.heading = (TextView) itemView.findViewById(R.id.heading);
            this.B_image =(ImageView) itemView.findViewById(R.id.B_img);
            this.description=(TextView) itemView.findViewById(R.id.description);
            this.percentChange=(TextView)itemView.findViewById(R.id.percentChange);
            this.ActualPrice  =(TextView)itemView.findViewById(R.id.ActualPrice);
            this.RateIndicator =(ImageView)itemView.findViewById(R.id.RateIndicator);
            this.checkBox=(CheckBox) itemView.findViewById(R.id.checkBox);


        }
    }








}
