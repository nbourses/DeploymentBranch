package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.graphics.Color;
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
 * Created by sushil on 20/12/16.
 */

public class MainScreenListviewAdapter extends BaseAdapter {
    private ArrayList<portListingModel> portListing;
    private Context context;
    portListingModel listing;
    private LayoutInflater inflater;
    public MainScreenListviewAdapter(Context context, ArrayList<portListingModel> portListing) {
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
        MainScreenListviewAdapter.Holder holder;
        listing=portListing.get(position);
        if (v == null) {
            v = inflater.inflate(R.layout.rental_listview_row, null);
            holder = new MainScreenListviewAdapter.Holder(v);
            /*holder.tvPersonName = (TextView) v.findViewById(R.id.tvPersonName);
            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);*/
            v.setTag(holder);
        } else {
            holder = (MainScreenListviewAdapter.Holder) v.getTag();
        }
        if(listing.getLl_pm()!=0&&listing.getDisplay_type()==null){
            holder.heading.setText(listing.getName()+" ("+listing.getConfig()+")");
//            String text;
//            text="<html><sup></html>";
            holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(listing.getLl_pm()+"")+"/month");
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
        }else if(listing.getOr_psf()!=0 && listing.getDisplay_type()==null){
            holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(listing.getOr_psf()+"") + "/sq-ft" );
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
            holder.ActualPrice.setText("Rates will be updated soon.");
            holder.B_image.setImageResource(R.drawable.asset_add_listing);
            holder.B_image.setBackground(null);
            holder.description.setText(listing.getLocality());
            setIcon(listing, holder);
        }

        Log.i("namerate","grothrate "+listing.getGrowth_rate()+" name"+listing.getName()+"listing.getLl_pm() "+listing.getLl_pm()+" listing.getor_psf() "+listing.getOr_psf()+" "+listing.getLocality());


        return v;
    }


    public void setIcon(portListingModel item, MainScreenListviewAdapter.Holder holder) {

//        Log.i("namerate","grothrate 123 :  "+(item.getGrowth_rate()).subSequence( 1, (item.getGrowth_rate()).length() )+" name"+item.getName());

        try {
            if(item.getGrowth_rate() !=null) {
                if (Integer.parseInt(item.getGrowth_rate()) < 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_down_red);
                    holder.ActualPrice.setTextColor(Color.parseColor("#ffb91422"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");

                } else if (Integer.parseInt(item.getGrowth_rate()) > 0) {

                    holder.RateIndicator.setImageResource(R.drawable.sort_up_green);
                    holder.ActualPrice.setTextColor(Color.parseColor("#2dc4b6"));
                    holder.percentChange.setText((item.getGrowth_rate()).subSequence(1, (item.getGrowth_rate()).length()) + "%");

                } else {
                    holder.RateIndicator.setImageResource(R.drawable.sort_up_black);
                    holder.ActualPrice.setTextColor(Color.parseColor("black"));
                    holder.percentChange.setText(item.getGrowth_rate() + "%");
                }
            }else{
                holder.RateIndicator.setVisibility(View.GONE);

//               holder.ActualPrice.setText("Rates will be updated soon.");
//               holder.ActualPrice.setTextColor(Color.parseColor("#000000"));
                holder.ActualPrice.setTextColor(Color.parseColor("#000000"));
                holder.percentChange.setText("");
            }
        }catch (Exception e){}
    }

    class Holder {
        public final TextView heading;
        public final ImageView B_image;
        public final  TextView description;
        public final  TextView percentChange ;
        public final  TextView ActualPrice;
        public final  ImageView RateIndicator;
        /*public SparseBooleanArray mSelectedItemsIds;
        LinearLayout rows;*/


        public Holder(View itemView) {
//            super(itemView);
            this.heading = (TextView) itemView.findViewById(R.id.heading);
            this.B_image =(ImageView) itemView.findViewById(R.id.B_img);
            this.description=(TextView) itemView.findViewById(R.id.description);
            this.percentChange=(TextView)itemView.findViewById(R.id.percentChange);
            this.ActualPrice  =(TextView)itemView.findViewById(R.id.ActualPrice);
            this.RateIndicator =(ImageView)itemView.findViewById(R.id.RateIndicator);
            /*this.rows=(LinearLayout)itemView.findViewById( R.id.rows ) ;
            mSelectedItemsIds = new SparseBooleanArray();*/


        }
    }
    /*public static void ShowConfirmDialog(Context context,final int personId,final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this record?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        MainActivity.getInstance().deletePerson(personId,position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

}
