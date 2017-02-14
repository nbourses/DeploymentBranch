package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sushil on 07/11/16.
 */
/*
public class porfolioAdapter {
}*/


public class porfolioAdapter extends BaseAdapter {
    private ArrayList<portListingModel> portListing;
    private Context context;
    portListingModel listing;
    private LayoutInflater inflater;
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static Bitmap finalimage = null;
    private static Uri uri_image ;
    public porfolioAdapter(Context context, ArrayList<portListingModel> portListing) {
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
        View v = null;
        Holder holder;
        Holder1 holder1;
        listing=portListing.get(position);

        if(listing.getTt() == null&&listing.getDisplay_type()!=null&&listing.getDisplay_type().equalsIgnoreCase("watchlist")){
            v = convertView;
            if (v == null) {

                v = inflater.inflate(R.layout.watchlist_listview_row, null);
                holder1 = new Holder1(v);

                v.setTag(holder1);
            } else {
                holder1 = (Holder1) v.getTag();
            }


            try {
                uri_image=Uri.parse(listing.getImageUri());
                showimage(holder1 ,uri_image);
            } catch (Exception e) {
                e.printStackTrace();
            }


            holder1.watch_name.setText(listing.getName());
            //holder1.watchlist_dp.setImageResource(finalimage);





        }else {

            v = convertView;
            if (v == null) {
                v = inflater.inflate(R.layout.rental_listview_row, null);
                holder = new Holder(v);
            /*holder.tvPersonName = (TextView) v.findViewById(R.id.tvPersonName);
            holder.ivEditPesonDetail=(ImageView)v.findViewById(R.id.ivEditPesonDetail);
            holder.ivDeletePerson=(ImageView)v.findViewById(R.id.ivDeletePerson);*/
                v.setTag(holder);
            } else {

                holder = (Holder) v.getTag();
            }
            if (listing.getTt() != null && listing.getTt().equalsIgnoreCase("ll") && listing.getDisplay_type() == null) {
                holder.heading.setText(listing.getName() + " (" + listing.getConfig() + ")");
//            String text;
//            text="<html><sup></html>";
                holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(General.getFormatedPrice(listing.getConfig(), listing.getLl_pm()) + "") + "/m");
                if (listing.getTransaction() == null) {
                    holder.B_image.setImageResource(R.drawable.asset_add_listing);
                    holder.B_image.setBackground(null);

                } else {
                    holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                    holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

                }
                holder.description.setText(listing.getLocality());
                setIcon(listing, holder);
            } else if (listing.getTt() != null && listing.getTt().equalsIgnoreCase("or") && listing.getDisplay_type() == null) {
                holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(listing.getOr_psf() + "") + "/sq-ft");
                holder.heading.setText(listing.getName() + " (" + listing.getConfig() + ")");
                if (listing.getTransaction() == null) {
                    holder.B_image.setImageResource(R.drawable.asset_add_listing);
                    holder.B_image.setBackground(null);
                } else {
                    holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
                    holder.B_image.setBackground(context.getResources().getDrawable(R.drawable.custom_img_bg));

                }
                holder.description.setText(listing.getLocality());
                setIcon(listing, holder);
            } else if (listing.getDisplay_type().equalsIgnoreCase("LOCALITIES")) {
                holder.heading.setText(listing.getName());
                holder.description.setText(listing.getLocality());
                if (listing.getLl_pm() == 0)
                    holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(listing.getOr_psf() + "") + "/sq-ft");
                else
                    holder.ActualPrice.setText(General.currencyFormatWithoutRupeeSymbol(listing.getLl_pm() + "") + "/m");

                float scale = context.getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (10 * scale + 0.5f);
                holder.B_image.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                holder.B_image.setImageResource(R.drawable.s_marker);
                holder.B_image.setBackground(null);
                setIcon(listing, holder);

            } else {
                holder.heading.setText(listing.getName() + " (" + listing.getConfig() + ")");
                holder.ActualPrice.setText("Rates will be updated soon.");
                holder.B_image.setImageResource(R.drawable.asset_add_listing);
                holder.B_image.setBackground(null);
                holder.description.setText(listing.getLocality());
                setIcon(listing, holder);
            }

            Log.i("namerate", "grothrate " + listing.getGrowth_rate() + " name" + listing.getName() + "listing.getLl_pm() " + listing.getLl_pm() + " listing.getor_psf() " + listing.getOr_psf() + " " + listing.getLocality());

        }



        return v;
    }


    public void setIcon(portListingModel item, porfolioAdapter.Holder holder) {

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

    class Holder1 {

        public final TextView watch_name;
        public final ImageView watchlist_dp;

        public Holder1(View itemView) {

            this.watch_name = (TextView) itemView.findViewById(R.id.watch_name);
            this.watchlist_dp =(ImageView) itemView.findViewById(R.id.watchlist_dp);



        }
    }







    void showimage(porfolioAdapter.Holder1 holder1,Uri mImageUri) {

        /*try {
            Image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mImageUri);
            if (getOrientation(context, mImageUri) != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(getOrientation(context, mImageUri));
                if (rotateImage != null)
                    rotateImage.recycle();
                rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix, true);
                Log.i("imageuri", "onActivityResult imageuri : "+Image+" mImageUri : "+mImageUri);
                //return rotateImage;
                holder1.watchlist_dp.setImageBitmap(rotateImage);
            } else {
               // Log.i("imageuri", "onActivityResult imageuri : "+Image+" mImageUri : "+mImageUri);
                holder1.watchlist_dp.setImageBitmap(Image);
//                return Image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/


//Add image from local folder oyeok
        if (Environment.getExternalStorageState() == null) {
            Log.i("IMGURL","image exists 11 ");
            //create new file directory object
            File image = new File(Environment.getDataDirectory()
                    + ""+mImageUri);

            // if no directory exists, create new directory
            if (image.exists()) {
                Log.i("IMGURL","image exists 1 ");
                Bitmap bmp = BitmapFactory.decodeFile(Environment.getDataDirectory()
                        + ""+mImageUri);
                Log.i("IMGURL","image exists 1 bmp "+bmp);
                holder1.watchlist_dp.setImageBitmap(bmp);


            }

            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            Log.i("IMGURL","image exists 22 ");
            // search for directory on SD card
            File image = new File(Environment.getExternalStorageDirectory()
                    + ""+mImageUri);

            Log.i("IMGURL","image exists 223 "+image);

            // if no directory exists, create new directory to store test
            // results
            if (image.exists()) {
                Log.i("IMGURL","image exists 2 ");
                Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                        + ""+mImageUri);


                holder1.watchlist_dp.setImageBitmap(bmp);

            }
        }



















    }






    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
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