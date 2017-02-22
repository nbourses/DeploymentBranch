package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.models.loadBuildingDataModel;

import java.util.ArrayList;

/**
 * Created by sushil on 03/02/17.
 */

public  class WatchlistExplorerAdapter extends ArrayAdapter<loadBuildingDataModel> {
    private boolean[] itemChecked;
    private ArrayList<loadBuildingDataModel> dataSet;
    loadBuildingDataModel dataModel;
    private Context mContext;
    private LayoutInflater inflater;
    int Currentposition;
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtlocality;
        ImageView info;
        CheckBox ck1;

    }

    public WatchlistExplorerAdapter(ArrayList<loadBuildingDataModel> data, Context context) {
       super(context, R.layout.watchlist_explorer_row, data);
        this.dataSet = data;
        this.mContext=context;
        this.itemChecked = new boolean[data.size()];
        //itemChecked = new boolean[data.size()];

    }

    public int getCount() {
        return dataSet.size();
    }



    public long getItemId(int position) {
        return 0;
    }
//    @Override
//    public void onClick(View v) {
//
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        loadBuildingDataModel dataModel=(loadBuildingDataModel)object;
//
//            switch (v.getId())
//            {
//                case R.id.item_info:
//                    Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                            .setAction("No action", null).show();
//                    break;
//            }
//    }

   private int lastPosition = -1;

    @Override
    public View getView( final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        dataModel = dataSet.get(position);
       // Currentposition=position;
        // Check if an existing view is being reused, otherwise inflate the view
       final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.watchlist_explorer_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.b_name);
            viewHolder.txtlocality = (TextView) convertView.findViewById(R.id.locality);
//                viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.building_img);
            //viewHolder.ck1=(CheckBox) convertView.findViewById(R.id.txt_title);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.slide_up : R.anim.slide_down);
            result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtlocality.setText(dataModel.getLocality());

        viewHolder.info.setTag(position);
        viewHolder.info.setBackground(mContext.getResources().getDrawable(R.drawable.asset_add_listing));
        // Return the completed view to render on screen

       /* viewHolder.ck1.setChecked(false);

        if (itemChecked[position])
            viewHolder.ck1.setChecked(true);
        else
            viewHolder.ck1.setChecked(false);

        viewHolder.ck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("insidecheck ","======================= inside  check  onClick =========================="+viewHolder.ck1.isChecked()+"position "+position);
                if (viewHolder.ck1.isChecked())
                    itemChecked[position] = true;
                else
                    itemChecked[position] = false;
            }
        });*/


        return result;
    }
}
