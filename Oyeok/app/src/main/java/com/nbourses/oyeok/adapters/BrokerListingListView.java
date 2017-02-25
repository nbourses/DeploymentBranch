package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
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
import com.nbourses.oyeok.realmModels.ListingCatalogRealm;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by sushil on 21/01/17.
 */

public class BrokerListingListView extends BaseAdapter{
    private ArrayList<portListingModel> portListing;
    private Context context;
    portListingModel listing;
    private LayoutInflater inflater;
    private Holder1 holder1;
    private Holder holder;
    private static String uri_image ;


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
//        View v = convertView;
       // BrokerListingListView.Holder holder;
       // listing=portListing.get(position);


        View v = null;

        listing=portListing.get(position);

        if(listing.getTt() != null&&listing.getDisplay_type()!=null&&listing.getDisplay_type().equalsIgnoreCase("catalog")){
        // v = null;
            if (v == null) {


                v = inflater.inflate(R.layout.watchlist_listview_row, null);
                holder1 = new Holder1(v);

                v.setTag(holder1);
            } else {
                v = convertView;
                holder1 = (Holder1) v.getTag();
            }


            try {
                holder1.Catalog_dp.setImageResource(R.drawable.listing_catalog_icon_home);
                if(!listing.getImageUri().equalsIgnoreCase("")&&listing.getImageUri()!=null) {
                    uri_image = listing.getImageUri();
                    showimage(holder1, uri_image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            holder1.Catalog_name.setText(listing.getName());
            Realm myRealm = General.realmconfig(context);
            ListingCatalogRealm result = myRealm.where(ListingCatalogRealm.class).equalTo("catalog_id", listing.getCatalog_id()).findFirst();
            holder1.count.setText(result.getListingids().size()+"");

            //holder1.watchlist_dp.setImageResource(finalimage);


        }else {

            v = null;

                if (v == null) {
                    v = inflater.inflate(R.layout.listing_row, null);
                    holder = new Holder(v);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (Holder) v.getTag();
                }


                if (listing.getTt() != null && listing.getTt().equalsIgnoreCase("ll") && listing.getDisplay_type() == null) {
                    String price = General.numToVal(listing.getLl_pm());
                    Log.i("namerate1", "grothrate " + price + "  " + listing.getLl_pm() + "listing.getMarket_rate()   " + listing.getMarket_rate() + "getGrowth_rate " + listing.getGrowth_rate() + "  listing.getPossession_date()   " + listing.getPossession_date());
                    holder.spec_code.setText(listing.getReq_avl() + " (" + listing.getConfig() + ") " + listing.getFurnishing() + " @ ₹ " + price + "/m");
                    holder.heading.setText(listing.getName());//+" ("+listing.getConfig()+")"
//            String text;
//            text="<html><sup></html>";
                    int m_rate = listing.getMarket_rate() * General.getFormatedarea(listing.getConfig());
                    holder.marketRate.setText(General.currencyFormat(m_rate + "") + "/m");
                    if (listing.getTransaction() == null) {
                        holder.B_image.setImageResource(R.drawable.asset_add_listing);
                        holder.B_image.setBackground(null);

                    } else {
                        holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                        holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

                    }
                    holder.list_possestion_date.setText(listing.getPossession_date());
                    holder.description.setText(listing.getLocality());
                    setIcon(listing, holder);
                } else if (listing.getTt() != null && listing.getTt().equalsIgnoreCase("or") && listing.getDisplay_type() == null) {
                    String price = General.numToVal(listing.getOr_psf());
                    Log.i("namerate1", "grothrate or " + price + "  " + listing.getOr_psf() + "listing.getMarket_rate() ++  " + listing.getMarket_rate());
                    holder.spec_code.setText(listing.getReq_avl() + " (" + listing.getConfig() + ") " + listing.getFurnishing() + " @ ₹ " + price);
                    int m_rate = listing.getMarket_rate() * General.getFormatedarea(listing.getConfig());
                    Log.i("namerate1", "grothrate or " + price + "  " + listing.getOr_psf() + "getGrowth_rate " + listing.getGrowth_rate());
                    price = General.numToVal(m_rate);
                    holder.marketRate.setText(General.currencyFormat(m_rate + ""));
                    holder.heading.setText(listing.getName());//+" ("+listing.getConfig()+")"
                    if (listing.getTransaction() == null) {
                        holder.B_image.setImageResource(R.drawable.asset_add_listing);
                        holder.B_image.setBackground(null);
                    } else {
                        holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                        holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

                    }
                    holder.list_possestion_date.setText(listing.getPossession_date());
                    holder.description.setText(listing.getLocality());
                    setIcon(listing, holder);
                } else {
                    holder.heading.setText(listing.getName());//" ("+listing.getConfig()+")"
                    holder.marketRate.setText("updated soona");
                    holder.B_image.setImageResource(R.drawable.asset_add_listing);
                    holder.list_possestion_date.setText("");
                    holder.B_image.setBackground(null);
                    holder.description.setText(listing.getLocality());
                    setIcon(listing, holder);
                }

                // Log.i("namerate","grothrate "+listing.getGrowth_rate()+" name"+listing.getName()+"listing.getLl_pm() "+listing.getLl_pm()+" listing.getor_psf() "+listing.getOr_psf()+" "+listing.getLocality());
            }
        return v;
    }


    public void setIcon(portListingModel item, BrokerListingListView.Holder holder) {

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

            /*this.rows=(LinearLayout)itemView.findViewById( R.id.rows ) ;  m_rate_indicator
            mSelectedItemsIds = new SparseBooleanArray();*/


        }
    }

        class Holder1 {

            public final TextView Catalog_name;
            public final ImageView Catalog_dp;
            public final TextView count;

            public Holder1(View itemView) {

                this.Catalog_name = (TextView) itemView.findViewById(R.id.watch_name);
                this.Catalog_dp =(ImageView) itemView.findViewById(R.id.watchlist_dp);
                this.count = (TextView) itemView.findViewById(R.id.count);


            }
        }


    void showimage(Holder1 holder1,String mImageUri) {


        if (Environment.getExternalStorageState() == null) {
            Log.i("IMGURL","image exists 11 ");
            //create new file directory object
            File image = new File(Environment.getDataDirectory()
                    + "/oyeok/watchlist/"+mImageUri);
            // if no directory exists, create new directory
            if (image.exists()) {
                Log.i("IMGURL","image exists 1 ");
                Bitmap bmp = BitmapFactory.decodeFile(Environment.getDataDirectory()
                        + "/oyeok/watchlist/"+mImageUri);
                Log.i("IMGURL","image exists 1 bmp "+bmp);
                holder1.Catalog_dp.setImageBitmap(bmp);



               // stopDownloadImage5 = true;

            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            Log.i("IMGURL","image exists 22 ");
            // search for directory on SD card
            File image = new File(Environment.getExternalStorageDirectory()
                    + "/oyeok/watchlist/"+mImageUri);

            Log.i("IMGURL","image exists 223 "+image);

            // if no directory exists, create new directory to store test
            // results
            if (image.exists()) {
                Log.i("IMGURL","image exists 2 ");
                Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                        + "/oyeok/watchlist/"+mImageUri);


                holder1.Catalog_dp.setImageBitmap(bmp);



                Log.i("IMGURL","image exists 2 bmp "+bmp);
                //holder5.imageView.setImageBitmap(bmp);
                //stopDownloadImage5 = true;
            }
        }
       // Log.i("TAG","stopDownloadImage "+stopDownloadImage5+" :: "+mImageUri);
        /*if(!stopDownloadImage5) {
            Log.i("TAG","stopDownloadImage1 "+stopDownloadImage5+"  ::  "+mImageUri);
            try {
                new porfolioAdapter.DownloadImageTask5(holder1.watchlist_dp).execute("https://s3.ap-south-1.amazonaws.com/oyeok-watchlist-images/"+mImageUri);

            } catch (Exception e) {
                Log.i("IMGURL", "image url is e " + e);
            }
        }*/



















    }










}
