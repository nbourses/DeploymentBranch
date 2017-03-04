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
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.ListingModel;
import com.nbourses.oyeok.models.portListingModel;

import java.util.ArrayList;

/**
 * Created by sushil on 24/02/17.
 */

public class CatalogDisplayAdapter extends BaseAdapter {

    private ArrayList<ListingModel> portListing;
    private Context context;
    ListingModel listing;
    private LayoutInflater inflater;
    private String Rate_growth;


    public CatalogDisplayAdapter(Context context, ArrayList<ListingModel> portListing) {
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
        CatalogDisplayAdapter.Holder holder;
        listing=portListing.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.list_catalog_row, null);
            holder = new CatalogDisplayAdapter.Holder(v);
            v.setTag(holder);
        } else {
            holder = (CatalogDisplayAdapter.Holder) v.getTag();
        }
        if((AppConstants.TT_TYPE).equalsIgnoreCase("ll")){
            //String price= General.numToVal(listing.getLl_pm());
            Rate_growth=General.percentageCalculator(listing.getListed_ll_pm(),listing.getReal_ll_pm());
            Log.i("namerate1","grothrate "+listing.getListed_ll_pm()+"listing.getMarket_rate()   "+listing.getReal_ll_pm()+"getRate_growth "+listing.getRate_growth()+"  listing.getPossession_date()   "+listing.getPossession_date());
            holder.spec_code.setText(listing.getReq_avl()+" ("+listing.getConfig()+") "+" @ ₹ "+General.currencyFormatWithoutRupeeSymbol(listing.getListed_ll_pm()+"")+"/m");
            holder.heading.setText(listing.getName());//+" ("+listing.getConfig()+")"
//            String text;
//            text="<html><sup></html>";
           // int m_rate=listing.getReal_ll_pm()*General.getFormatedarea(listing.getConfig());
            holder.marketRate.setText(General.currencyFormat(listing.getReal_ll_pm()+"")+"/m");
            if(listing.getTransactions()==null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);

            }
            else{
                holder.B_image.setImageResource(R.drawable.ic_home_selected);
               // holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.list_possestion_date.setText(General.getDate(Long.parseLong(listing.getPossession_date())));
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }else if((AppConstants.TT_TYPE).equalsIgnoreCase("or")){
            //String price=General.numToVal(listing.getOr_psf());
            Rate_growth=General.percentageCalculator(listing.getListed_or_psf(),listing.getReal_or_psf());
            Log.i("namerate1","grothrate or "+listing.getListed_or_psf()+"listing.getMarket_rate() ++  "+listing.getReal_or_psf());
            holder.spec_code.setText(listing.getReq_avl()+" ("+listing.getConfig()+") "+" @ ₹ "+General.currencyFormatWithoutRupeeSymbol(listing.getListed_or_psf()+""));
            int m_rate=listing.getReal_or_psf()*General.getFormatedarea(listing.getConfig());
            Log.i("namerate1","grothrate or "+listing.getListed_or_psf()+"getRate_growth "+listing.getRate_growth());
           // price=General.numToVal(m_rate);
            holder.marketRate.setText(General.currencyFormat(m_rate+""));
            holder.heading.setText(listing.getName());//+" ("+listing.getConfig()+")"
            if(listing.getTransactions()==null) {
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);
            }
            else{
                holder.B_image.setImageResource(R.drawable.ic_home_selected);
                //holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

            }
            holder.list_possestion_date.setText(General.getDate(Long.parseLong(listing.getPossession_date())));
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
        // Log.i("namerate","grothrate "+listing.getRate_growth()+" name"+listing.getName()+"listing.getLl_pm() "+listing.getLl_pm()+" listing.getor_psf() "+listing.getOr_psf()+" "+listing.getLocality());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckBox(position);
            }
        });
        return v;
    }


    public void setIcon(ListingModel item, CatalogDisplayAdapter.Holder holder) {

//        Log.i("namerate","grothrate 123 :  "+(item.getRate_growth()).subSequence( 1, (item.getRate_growth()).length() )+" name"+item.getName());

        try {
            Log.i("namerate","grothrate inside "+item.getRate_growth()+ "  === "+Integer.parseInt(item.getRate_growth())+"    ======= =======  "+(item.getRate_growth()).subSequence(1, (item.getRate_growth()).length()));

            if(Rate_growth !=null) {

                if (Integer.parseInt(Rate_growth) < 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_down_red);
                    holder.marketRate.setTextColor(Color.parseColor("#ffb91422"));
                    holder.percentChange.setText((Rate_growth).subSequence(1, (Rate_growth).length()) + "%");
                    holder.m_rate_indicator.setText("Below\nmarket rate");
                    holder.m_rate_indicator.setTextColor(Color.parseColor("#ffb91422"));
                } else if (Integer.parseInt(Rate_growth) > 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_up_green);
                    holder.marketRate.setTextColor(Color.parseColor("#2dc4b6"));
                    holder.percentChange.setText((Rate_growth).subSequence(1, (Rate_growth).length()) + "%");
                    holder.m_rate_indicator.setText("Above\nmarket rate");
                    holder.m_rate_indicator.setTextColor(Color.parseColor("#2dc4b6"));
                } else {
                    holder.RateIndicator.setImageResource(R.drawable.sort_up_black);
                    holder.marketRate.setTextColor(Color.parseColor("black"));
                    holder.percentChange.setText((Rate_growth).subSequence(1, (Rate_growth).length()) + "%");
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
    public ArrayList<ListingModel> getAllData(){
        return portListing;
    }
    public void setCheckBox(int position){
        //Update status of checkbox
        ListingModel items = portListing.get(position);
        items.setCheckbox(!items.isCheckbox());
        notifyDataSetChanged();
    }
}
