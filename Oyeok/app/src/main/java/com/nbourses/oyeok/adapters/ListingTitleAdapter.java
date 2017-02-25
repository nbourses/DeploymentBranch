package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.portListingModel;

import java.util.ArrayList;

/**
 * Created by sushil on 23/02/17.
 */

public class ListingTitleAdapter extends BaseAdapter {
    private ArrayList<portListingModel> portListing;
    private Context context;
    portListingModel listing;
    private LayoutInflater inflater;
    public ListingTitleAdapter(Context context, ArrayList<portListingModel> portListing) {
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
        ListingTitleAdapter.Holder holder;
        listing=portListing.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.list_catalog_row, null);
            holder = new ListingTitleAdapter.Holder(v);
            /*holder.tvPersonName = (TextView) v.findViewById(R.id.tvPersonName);
            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);*/
            v.setTag(holder);
        } else {
            holder = (ListingTitleAdapter.Holder) v.getTag();
        }
        if(listing.getTt()!=null&&listing.getTt().equalsIgnoreCase("ll")&&listing.getDisplay_type()==null){
            String price= General.numToVal(listing.getLl_pm());
            Log.i("namerate1","grothrate "+price+"  "+listing.getLl_pm()+"listing.getMarket_rate()   "+listing.getMarket_rate()+"getGrowth_rate "+listing.getGrowth_rate()+"  listing.getPossession_date()   "+listing.getPossession_date());
            holder.spec_code.setText(listing.getReq_avl()+" ("+listing.getConfig()+") "+listing.getFurnishing()+" @ ₹ "+price+"/m");
            holder.heading.setText(listing.getName());//+" ("+listing.getConfig()+")"
//            String text;
//            text="<html><sup></html>";
            int m_rate=listing.getMarket_rate()*General.getFormatedarea(listing.getConfig());
            holder.marketRate.setText(General.currencyFormat(m_rate+"")+"/m");
            if(listing.getTransaction()==null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);

            }
            else{
                holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.list_possestion_date.setText(listing.getPossession_date());
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }else if(listing.getTt()!=null&&listing.getTt().equalsIgnoreCase("or") && listing.getDisplay_type()==null){
            String price=General.numToVal(listing.getOr_psf());
            Log.i("namerate1","grothrate or "+price+"  "+listing.getOr_psf()+"listing.getMarket_rate() ++  "+listing.getMarket_rate());
            holder.spec_code.setText(listing.getReq_avl()+" ("+listing.getConfig()+") "+listing.getFurnishing()+" @ ₹ "+price);
            int m_rate=listing.getMarket_rate()*General.getFormatedarea(listing.getConfig());
            Log.i("namerate1","grothrate or "+price+"  "+listing.getOr_psf()+"getGrowth_rate "+listing.getGrowth_rate());
            price=General.numToVal(m_rate);
            holder.marketRate.setText(General.currencyFormat(m_rate+""));
            holder.heading.setText(listing.getName());//+" ("+listing.getConfig()+")"
            if(listing.getTransaction()==null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);
            }
            else{
                holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.list_possestion_date.setText(listing.getPossession_date());
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }else{
            holder.heading.setText(listing.getName());//" ("+listing.getConfig()+")"
            holder.marketRate.setText("updated soona");
            holder.B_image.setImageResource(R.drawable.asset_add_listing);
            holder.list_possestion_date.setText("");
            holder.B_image.setBackground(null);
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }

        if(listing.isCheckbox()){
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }
        // Log.i("namerate","grothrate "+listing.getGrowth_rate()+" name"+listing.getName()+"listing.getLl_pm() "+listing.getLl_pm()+" listing.getor_psf() "+listing.getOr_psf()+" "+listing.getLocality());

        return v;
    }


    public void setIcon(portListingModel item, ListingTitleAdapter.Holder holder) {

//        Log.i("namerate","grothrate 123 :  "+(item.getGrowth_rate()).subSequence( 1, (item.getGrowth_rate()).length() )+" name"+item.getName());

        try {
            Log.i("namerate","grothrate inside "+item.getGrowth_rate()+ "  === "+Integer.parseInt(item.getGrowth_rate())+"    ======= =======  "+(item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()));

            if(item.getGrowth_rate() !=null) {

                if (Integer.parseInt(item.getGrowth_rate()) < 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_down_red);
                    holder.marketRate.setTextColor(Color.parseColor("#ffb91422"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");
                    holder.m_rate_indicator.setText("Below\nmarket rate");
                    holder.m_rate_indicator.setTextColor(Color.parseColor("#ffb91422"));
                } else if (Integer.parseInt(item.getGrowth_rate()) > 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_up_green);
                    holder.marketRate.setTextColor(Color.parseColor("#2dc4b6"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");
                    holder.m_rate_indicator.setText("Above\nmarket rate");
                    holder.m_rate_indicator.setTextColor(Color.parseColor("#2dc4b6"));
                } else {
                    holder.RateIndicator.setImageResource(R.drawable.sort_up_black);
                    holder.marketRate.setTextColor(Color.parseColor("black"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");
                    holder.m_rate_indicator.setText("");
                    holder.m_rate_indicator.setTextColor(Color.parseColor("#000000"));
                }
            }else{
                holder.RateIndicator.setVisibility(View.GONE);
                holder.m_rate_indicator.setText("");
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
        public final  TextView list_possestion_date;
        public final CheckBox checkBox;
        /*public SparseBooleanArray mSelectedItemsIds;list_possestion_date
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
            this.list_possestion_date =(TextView)itemView.findViewById(R.id.list_possestion_date);
            this.checkBox =(CheckBox)itemView.findViewById(R.id.list_checkBox);


            /*this.rows=(LinearLayout)itemView.findViewById( R.id.rows ) ;  m_rate_indicator
            mSelectedItemsIds = new SparseBooleanArray();*/


        }
    }
    public ArrayList<portListingModel> getAllData(){
        return portListing;
    }
    public void setCheckBox(int position){
        //Update status of checkbox
        portListingModel items = portListing.get(position);
        items.setCheckbox(!items.isCheckbox());
        notifyDataSetChanged();
    }
}
