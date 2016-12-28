package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.nbourses.oyeok.realmModels.BuildingCacheRealm;

/**
 * Created by sushil on 29/09/16.
 */

public  class myPortfolioAdapter extends realmAdapter<BuildingCacheRealm,myPortfolioAdapter.ViewHolder> implements View.OnLongClickListener{
    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public static class ViewHolder extends realmAdapter.ViewHolder {
       public final TextView heading;
       public final ImageView B_image;
       public final  TextView description;
       public final  TextView percentChange ;
       public final  TextView ActualPrice;
       public final  ImageView RateIndicator;
        public SparseBooleanArray mSelectedItemsIds;
        LinearLayout rows;


        public ViewHolder(View itemView) {
            super(itemView);
            this.heading = (TextView) itemView.findViewById(R.id.heading);
            this.B_image =(ImageView) itemView.findViewById(R.id.B_img);
            this.description=(TextView) itemView.findViewById(R.id.description);
            this.percentChange=(TextView)itemView.findViewById(R.id.percentChange);
            this.ActualPrice  =(TextView)itemView.findViewById(R.id.ActualPrice);
            this.RateIndicator =(ImageView)itemView.findViewById(R.id.RateIndicator);
            this.rows=(LinearLayout)itemView.findViewById( R.id.rows ) ;
            mSelectedItemsIds = new SparseBooleanArray();


        }
    }


    public static interface OnItemClickListener {
        public void onItemClick(BuildingCacheRealm myPortfolioModel);
    }




    private final LayoutInflater inflater;
//    private  OnItemClickListener listener;
    private final Context context;
    private int    type=1;
    ListView listview;
    public SparseBooleanArray mSelectedItemsIds;
    public myPortfolioAdapter(Context context, int type) {
        this.inflater = LayoutInflater.from(context);
        this.type=type;
        this.context=context;
    }

    @Override
    public myPortfolioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myPortfolioAdapter.ViewHolder(inflater.inflate(R.layout.rental_listview_row, parent, false));
    }

    @Override
    public   void onBindViewHolder(final myPortfolioAdapter.ViewHolder holder, final int position) {
        final BuildingCacheRealm item = getItem(position);


        if(type==1){
        holder.ActualPrice.setText(General.currencyFormat(General.getFormatedPrice(item.getConfig(),item.getLl_pm())+"")+"/month");
        holder.heading.setText(item.getName()+" ("+item.getConfig()+")");
        holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
        holder.description.setText(item.getLocality());
        setIcon(item, holder);
        }
        else if(type==2){
            holder.ActualPrice.setText(General.currencyFormat(item.getOr_psf()+"") + "/sq-ft" );
            holder.heading.setText(item.getName()+" ("+item.getConfig()+")");
            holder.B_image.setImageResource(R.drawable.buildingiconbeforeclick);
            holder.description.setText(item.getLocality());
            setIcon(item, holder);
        }
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("listviewsushil","item is clicked !!!!! 1 : "+item.getId());
//                listener.onItemClick(item);
            }
        });*/


    }

public void setIcon(BuildingCacheRealm item, myPortfolioAdapter.ViewHolder holder) {
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

}
    /*public void removeSelection(ArrayList<String> ids) {
        Realm myRealm= General.realmconfig( context );
   for(int i=0;i<ids.size();i++) {
       try {
           MyPortfolioModel results = myRealm
                   .where( MyPortfolioModel.class )
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

    }*/

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


/*
public final class ExampleAdapter extends RealmAdapter<Example, ExampleAdapter.ViewHolder> {

    public static class ViewHolder extends RealmAdapter.ViewHolder {
        public final TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static interface OnItemClickListener {
        public void onItemClick(Example example);
    }

    private final LayoutInflater inflater;
    private final OnItemClickListener listener;

    public ExampleAdapter(Context context, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.example_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Example item = getItem(position);
        holder.title.setText(item.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

}*/
