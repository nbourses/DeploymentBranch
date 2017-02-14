package com.nbourses.oyeok.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.models.loadBuildingDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushil on 04/02/17.
 */

public class watchlistAdapter extends BaseAdapter {


    private Context activity;
    private ArrayList<loadBuildingDataModel> data=new ArrayList<>();
    private static LayoutInflater inflater = null;
    private View vi;
    private ViewHolder viewHolder;

    public watchlistAdapter( ArrayList<loadBuildingDataModel>items,Context context) {
        this.activity = context;
        this.data = items;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        vi = view;
        //Populate the Listview
        final int pos = position;
        loadBuildingDataModel items = data.get(pos);
        if(view == null) {
            vi = inflater.inflate(R.layout.watchlist_explorer_row, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) vi.findViewById(R.id.checkBox);
            viewHolder.name = (TextView) vi.findViewById(R.id.b_name);

            viewHolder.txtlocality = (TextView) vi.findViewById(R.id.locality);
//                viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            viewHolder.info = (ImageView) vi.findViewById(R.id.building_img);


            vi.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();
        viewHolder.name.setText(items.getName());
        viewHolder.txtlocality.setText(items.getLocality());
        viewHolder.info.setBackground(activity.getResources().getDrawable(R.drawable.asset_add_listing));



        if(items.isCheckbox()){
            viewHolder.checkBox.setChecked(true);
        }
        else {
            viewHolder.checkBox.setChecked(false);
        }
        return vi;
    }
    public ArrayList<loadBuildingDataModel> getAllData(){
        return data;
    }
    public void setCheckBox(int position){
        //Update status of checkbox
        loadBuildingDataModel items = data.get(position);
        items.setCheckbox(!items.isCheckbox());
        notifyDataSetChanged();
    }

    public class ViewHolder{

        TextView txtlocality;
        ImageView info;
        TextView name;
        CheckBox checkBox;
    }

    public void DeleteAllData(){
        data.clear();

    }

}
