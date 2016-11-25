package com.nbourses.oyeok.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.models.loadBuildingDataModel;

import java.util.ArrayList;

/**
 * Created by sushil on 04/11/16.
 */


public class searchBuilding extends ArrayAdapter<loadBuildingDataModel> implements View.OnClickListener{

        private ArrayList<loadBuildingDataModel> dataSet;
        Context mContext;

        // View lookup cache
        private static class ViewHolder {
            TextView txtName;
            TextView txtlocality;
            ImageView info;
        }

        public searchBuilding(ArrayList<loadBuildingDataModel> data, Context context) {
            super(context, R.layout.add_building_row, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {

            int position=(Integer) v.getTag();
            Object object= getItem(position);
            loadBuildingDataModel dataModel=(loadBuildingDataModel)object;

            /*switch (v.getId())
            {
                case R.id.item_info:
                    Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    break;
            }*/
        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            loadBuildingDataModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.add_building_row, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.b_name);
                viewHolder.txtlocality = (TextView) convertView.findViewById(R.id.locality);
//                viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
                viewHolder.info = (ImageView) convertView.findViewById(R.id.building_img);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.slide_up : R.anim.slide_down);
            /*result.startAnimation(animation);*/
            lastPosition = position;

            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.txtlocality.setText(dataModel.getLocality());
//            viewHolder.txtVersion.setText(dataModel.getVersion_number());
            /*viewHolder.info.setOnClickListener(this);*/
            viewHolder.info.setTag(position);
            viewHolder.info.setBackground(getContext().getResources().getDrawable(R.drawable.asset_add_listing));
            // Return the completed view to render on screen
            return convertView;
        }
}
