package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.addBuilding;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by sushil on 20/10/16.
 */

public  class addBuildingAdapter extends realmAdapter<addBuilding,addBuildingAdapter.ViewHolder> implements View.OnLongClickListener{
    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public static class ViewHolder extends realmAdapter.ViewHolder {
        public final TextView B_name;
        public final ImageView building_img;
        public final  TextView locality;
        /*public final  TextView percentChange ;
        public final  TextView ActualPrice;
        public final  ImageView RateIndicator;*/
        public SparseBooleanArray mSelectedItemsIds;
        LinearLayout rows;


        public ViewHolder(View itemView) {
            super(itemView);
            this.B_name = (TextView) itemView.findViewById(R.id.b_name);
            this.building_img =(ImageView) itemView.findViewById(R.id.building_img);
            this.locality=(TextView) itemView.findViewById(R.id.locality);
            /*this.percentChange=(TextView)itemView.findViewById(R.id.percentChange);
            this.ActualPrice  =(TextView)itemView.findViewById(R.id.ActualPrice);
            this.RateIndicator =(ImageView)itemView.findViewById(R.id.RateIndicator);*/
            this.rows=(LinearLayout)itemView.findViewById( R.id.rows ) ;
            mSelectedItemsIds = new SparseBooleanArray();


        }
    }


    public static interface OnItemClickListener {
        public void onItemClick(addBuilding addBuilding);
    }




    private final LayoutInflater inflater;
    //    private  OnItemClickListener listener;
    private final Context context;
    private int    type=1;
    ListView listview;
    public SparseBooleanArray mSelectedItemsIds;
    public addBuildingAdapter(Context context, int type) {
        this.inflater = LayoutInflater.from(context);
        this.type=type;
        this.context=context;
    }

    @Override
    public addBuildingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new addBuildingAdapter.ViewHolder(inflater.inflate(R.layout.add_building_row, parent, false));
    }

    @Override
    public   void onBindViewHolder(final addBuildingAdapter.ViewHolder holder, final int position) {
        final addBuilding item = getItem(position);

        holder.building_img.setImageResource(R.drawable.buildingiconbeforeclick);
        holder.locality.setText(item.getLocality());
        holder.B_name.setText(item.getBuilding_name());

       /* if(type==1 && item.getLl_pm()>0){
            holder.ActualPrice.setText(General.currencyFormat(item.getLl_pm()+"")+"/month");
            holder.heading.setText(item.getName()+" ("+item.getConfig()+")");


            setIcon(item, holder);
        }
        else if(type==2 && item.getOr_psf()>0){
            holder.ActualPrice.setText(General.currencyFormat(item.getOr_psf()+"") + "/sq-ft" );
            holder.heading.setText(item.getName()+" ("+item.getConfig()+")");
            holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
            holder.description.setText(item.getLocality());
            setIcon(item, holder);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("listviewsushil","item is clicked !!!!! 1 : "+item.getId());
//                listener.onItemClick(item);
            }
        });*/


    }

   /* public void setIcon(addBuilding item, addBuildingAdapter.ViewHolder holder) {
        if (Integer.parseInt( item.getRate_growth() ) < 0) {

            holder.RateIndicator.setImageResource( R.drawable.sort_down_red );
            holder.ActualPrice.setTextColor( Color.parseColor( "#ffb91422" ) );
            holder.percentChange.setText( (item.getRate_growth()).subSequence( 1, (item.getRate_growth()).length() ) + "%" );

        } else if (Integer.parseInt( item.getRate_growth() ) > 0) {

            holder.RateIndicator.setImageResource( R.drawable.sort_up_green );
            holder.ActualPrice.setTextColor( Color.parseColor( "#2dc4b6" ) );
            holder.percentChange.setText( (item.getRate_growth()).subSequence( 1, (item.getRate_growth()).length() ) + "%" );

        } else {
            holder.RateIndicator.setImageResource( R.drawable.sort_up_black );
            holder.ActualPrice.setTextColor( Color.parseColor( "black" ) );
            holder.percentChange.setText( item.getRate_growth() + "%" );
        }

    }*/
    public void removeSelection(ArrayList<String> ids) {
        Realm myRealm= General.realmconfig( context );
        for(int i=0;i<ids.size();i++) {
            try {
                addBuilding results = myRealm
                        .where( addBuilding.class )
                        .equalTo( "id", ids.get( i ) )
                        //.notEqualTo( "" )

                        .findFirst();

                myRealm.beginTransaction();
                results.removeFromRealm();
                myRealm.commitTransaction();
                notifyDataSetChanged();
            }catch (Exception e){System.out.println(e);}
        }
//        mSelectedItemsIds = new SparseBooleanArray();

    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void toggleSelection(int position) {
        Log.i( "portfolio1","onItemCheckedStateChanged   : "+position+" ");

        selectView(position, !mSelectedItemsIds.get(position));
    }





}