package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.MatchListing;
import com.nbourses.oyeok.realmModels.BuildingCacheRealm;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.List;

/**
 * Created by Ritesh Warke on 17/02/17.
 */

public class MatchListingAdapter extends RecyclerView.Adapter<MatchListingAdapter.MyViewHolder>  {


    private final View f;
    private Context c;
    private List<MatchListing> matchList;
    public int selected = 0;
    Animation slide_arrow;






    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView broker_name, config;
        public CardView matchlisting;
        public ImageView tick;
        public TextView date;
        public TextView match_price;
        public TextView match_locality;
        public ImageView match_icon;
        public ImageView match_growth_image;
        public TextView match_growth_rate;


        public MyViewHolder(View view) {
            super(view);
            broker_name = (TextView) view.findViewById(R.id.broker_name);
            config = (TextView) view.findViewById(R.id.config);
            matchlisting = (CardView) view.findViewById(R.id.matchlisting);
            slide_arrow=(AnimationUtils.loadAnimation(c, R.anim.slide_ltor_repeat));
            tick = (ImageView) view.findViewById(R.id.tick);
            date = (TextView) view.findViewById(R.id.date);
            match_price = (TextView) view.findViewById(R.id.match_price);
            match_growth_rate = (TextView) view.findViewById(R.id.match_growth_rate);
            match_locality = (TextView) view.findViewById(R.id.match_locality);
            match_icon = (ImageView) view.findViewById(R.id.match_icon);
            match_growth_image = (ImageView) view.findViewById(R.id.match_growth_image);
            match_growth_rate = (TextView) view.findViewById(R.id.match_growth_rate);
            match_icon = (ImageView) view.findViewById(R.id.match_icon);
              /*  year = (TextView) view.findViewById(R.id.year);*/
        }
    }


    public MatchListingAdapter(Context c, View f, List<MatchListing> matchList) {
        this.matchList = matchList;
        this.f = f;
        this.c = c;
    }





    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MatchListing item = matchList.get(position);
        holder.broker_name.setText(item.getBroker_name());
        holder.config.setText(item.getConfig());


        try {
            holder.date.setText(General.timestampToString(Long.parseLong(item.getDate())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            holder.date.setText(item.getDate());
            Log.i("TAG", "invalid date handled "+item.getDate()+" "+item.getConfig());
        }

        holder.match_locality.setText(item.getLocality());
        holder.match_price.setText(General.currencyFormat(item.getPrice()));
        setIcon(holder,item.getGrowth_rate());

        if(item.getReq_avl().equalsIgnoreCase("AVL")) {

            holder.match_icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.svg_icon_req));


        }
        else {
            if(item.getProperty_type().equalsIgnoreCase("home"))
                holder.match_icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_home));
            else if(item.getProperty_type().equalsIgnoreCase("office"))
                holder.match_icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_office));
            else if(item.getProperty_type().equalsIgnoreCase("shop"))
                holder.match_icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_shop));
            else if(item.getProperty_type().equalsIgnoreCase("office"))
                holder.match_icon.setImageDrawable(ContextCompat.getDrawable(c, R.drawable.ic_office));
        }

        final TextView vi = (TextView) f.findViewById(R.id.matching_text);

        holder.matchlisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getUser_id().equalsIgnoreCase(General.getSharedPreferences(c,AppConstants.USER_ID))) {
                    SnackbarManager.show(
                            com.nispok.snackbar.Snackbar.with(c)
                                    .position(Snackbar.SnackbarPosition.BOTTOM)
                                    .text("Self Matching cannot be Selected")
                                    .color(Color.parseColor("#696969")));
                }
else{
                    if (selected != 3 || item.isSelected()) {
                        if (item.isSelected()) {
                            selected--;
                            vi.setText("Select Matchings (" + selected + ")");
                            AppConstants.oyeIdsForOk.remove(item.getOye_id());
                        } else {
                            selected++;
                            vi.setText("Select Matchings (" + selected + ")");
                            AppConstants.oyeIdsForOk.add(item.getOye_id());
                        }
                        item.setSelected(!item.isSelected());
                        holder.matchlisting.setCardBackgroundColor(item.isSelected() ? Color.parseColor("#b2ffb2") : Color.WHITE);
                        holder.tick.setBackground(item.isSelected() ? ContextCompat.getDrawable(c, R.drawable.ic_checked_greenish_blue) : ContextCompat.getDrawable(c, R.drawable.ic_checked_grey));

                        View v = f.findViewById(R.id.ok);
                        v.setAnimation(slide_arrow);



                    /*if (selected == 3) {
                        v.setVisibility(View.VISIBLE);
                        v.setAnimation(slide_arrow);
                    }
                    else {
                        v.clearAnimation();
                        v.setVisibility(View.GONE);

                    }*/
                    }

                }
            }
        });
        Log.i("MatchListing","matchwa item.getTitle() "+item.getTitle());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_listing_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public void setIcon(MyViewHolder holder, String growth_rate) {
        if (Integer.parseInt(growth_rate) < 0) {

            holder.match_growth_image.setImageResource( R.drawable.sort_down_red );
            holder.match_growth_rate.setTextColor( Color.parseColor("#ffb91422"));
            holder.match_growth_rate.setText( (growth_rate).subSequence( 1, (growth_rate).length() ) + "%" );

        } else if (Integer.parseInt( growth_rate ) > 0) {

            holder.match_growth_image.setImageResource( R.drawable.sort_up_green );
            holder.match_growth_rate.setTextColor( Color.parseColor("#2dc4b6"));
            holder.match_growth_rate.setText( (growth_rate).subSequence( 1, (growth_rate).length() ) + "%" );

        } else {
            holder.match_growth_image.setImageResource( R.drawable.sort_up_black );
            holder.match_growth_rate.setTextColor( Color.parseColor("black") );
            holder.match_growth_rate.setText( growth_rate + "%" );
        }

    }



}

