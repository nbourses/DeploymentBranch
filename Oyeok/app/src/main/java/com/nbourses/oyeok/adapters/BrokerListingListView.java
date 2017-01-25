package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.portListingModel;

import java.util.ArrayList;

/**
 * Created by sushil on 21/01/17.
 */

public class BrokerListingListView extends BaseAdapter{
    private ArrayList<portListingModel> portListing;
    private Context context;
    portListingModel listing;
    private LayoutInflater inflater;
    public BrokerListingListView(Context context, ArrayList<portListingModel> portListing) {
        this.context = context;
        this.portListing = portListing;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return portListing.size();
    }
    @Override
    public Object getItem(int position) {
        return portListing.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        BrokerListingListView.Holder holder;
        listing=portListing.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.listing_row, null);
            holder = new BrokerListingListView.Holder(v);
            /*holder.tvPersonName = (TextView) v.findViewById(R.id.tvPersonName);
            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);*/
            v.setTag(holder);
        } else {
            holder = (BrokerListingListView.Holder) v.getTag();
        }
        if(listing.getTt()!=null&&listing.getTt().equalsIgnoreCase("ll")&&listing.getDisplay_type()==null){
            String price=General.numToVal(listing.getLl_pm());
            Log.i("namerate1","grothrate "+price+"  "+listing.getLl_pm());
            holder.spec_code.setText(listing.getReq_avl()+" ("+listing.getConfig()+") "+listing.getFurnishing()+" @ "+General.currencyFormat(price));
            holder.heading.setText(listing.getName()+" ("+listing.getConfig()+")");
//            String text;
//            text="<html><sup></html>";
            holder.marketRate.setText(General.currencyFormat(listing.getLl_pm()+"")+"/m");
            if(listing.getTransaction()==null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);

            }
            else{
                holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }else if(listing.getTt()!=null&&listing.getTt().equalsIgnoreCase("or") && listing.getDisplay_type()==null){
            String price=General.numToVal(listing.getOr_psf());
            Log.i("namerate1","grothrate "+price+"  "+listing.getOr_psf());
            holder.spec_code.setText(listing.getReq_avl()+" ("+listing.getConfig()+") "+listing.getFurnishing()+" @ ₹ "+price);
            int m_rate=listing.getOr_psf()/General.getFormatedarea(listing.getConfig());
            Log.i("namerate1","grothrate "+price+"  "+listing.getOr_psf());

            holder.marketRate.setText(General.currencyFormat(m_rate+"") + "/sq-ft" );
            holder.heading.setText(listing.getName()+" ("+listing.getConfig()+")");
            if(listing.getTransaction()==null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);
            }
            else{
                holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }else{
            holder.heading.setText(listing.getName()+" ("+listing.getConfig()+")");
            holder.marketRate.setText("Rates will be updated soon.");
            holder.B_image.setImageResource(R.drawable.asset_add_listing);
            holder.B_image.setBackground(null);
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }

        Log.i("namerate","grothrate "+listing.getGrowth_rate()+" name"+listing.getName()+"listing.getLl_pm() "+listing.getLl_pm()+" listing.getor_psf() "+listing.getOr_psf()+" "+listing.getLocality());

        return v;
    }


    public void setIcon(portListingModel item, BrokerListingListView.Holder holder) {

//        Log.i("namerate","grothrate 123 :  "+(item.getGrowth_rate()).subSequence( 1, (item.getGrowth_rate()).length() )+" name"+item.getName());

        try {
            if(item.getGrowth_rate() !=null) {
                if (Integer.parseInt(item.getGrowth_rate()) < 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_down_red);
                    holder.marketRate.setTextColor(Color.parseColor("#ffb91422"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");

                } else if (Integer.parseInt(item.getGrowth_rate()) > 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_up_green);
                    holder.marketRate.setTextColor(Color.parseColor("#2dc4b6"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");

                } else {
                    holder.RateIndicator.setImageResource(R.drawable.sort_up_black);
                    holder.marketRate.setTextColor(Color.parseColor("black"));
                    holder.percentChange.setText(item.getGrowth_rate() + "%");
                }
            }else{
                holder.RateIndicator.setVisibility(View.GONE);

//               holder.ActualPrice.setText("Rates will be updated soon.");
//               holder.ActualPrice.setTextColor(Color.parseColor("#000000"));
                holder.marketRate.setTextColor(Color.parseColor("#000000"));
                holder.percentChange.setText("");
            }
        }catch (Exception e){}
    }

    class Holder {
        public final TextView spec_code;
        public final TextView heading;
        public final ImageView B_image;
        public final  TextView description;
        public final  TextView percentChange ;
        public final  TextView m_rate_indicator;
        public final  ImageView RateIndicator;
        public final  TextView marketRate;
        /*public SparseBooleanArray mSelectedItemsIds;
        LinearLayout rows;*/


        public Holder(View itemView) {
//            super(itemView);
            this.spec_code = (TextView) itemView.findViewById(R.id.spec_code);
            this.heading = (TextView) itemView.findViewById(R.id.b_name);//spec_code
            this.B_image =(ImageView) itemView.findViewById(R.id.B_img);
            this.description=(TextView) itemView.findViewById(R.id.list_locality);
            this.percentChange=(TextView)itemView.findViewById(R.id.growth_rate);
            this.marketRate  =(TextView)itemView.findViewById(R.id.list_price);
            this.RateIndicator =(ImageView)itemView.findViewById(R.id.growth_image);
            this.m_rate_indicator =(TextView)itemView.findViewById(R.id.m_rate_indicator);
            /*this.rows=(LinearLayout)itemView.findViewById( R.id.rows ) ;  m_rate_indicator
            mSelectedItemsIds = new SparseBooleanArray();*/


        }
    }
}
