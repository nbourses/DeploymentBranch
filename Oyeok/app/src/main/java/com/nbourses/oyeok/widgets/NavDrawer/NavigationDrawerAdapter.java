package com.nbourses.oyeok.widgets.NavDrawer;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public static int selected_item = 0;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        if(position == selected_item) {
            holder.title.setTextColor(Color.parseColor("#2dc4b6"));
         // holder.imgViewIcon.setBackgroundResource(R.drawable.deal_circle);
        } else {
            holder.title.setTextColor(Color.parseColor("#ffffff")); //actually you should set to the normal text color
            //holder.imgViewIcon.setBackgroundResource(0);
        }
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
       //holder.imgViewIcon.setImageResource(current.getIcon());




//        NavDrawerItem current = data.get(position);
//        holder.title.setText(current.getTitle());


        if(position == 2) {

            if(General.getBadgeCount(context,AppConstants.HDROOMS_COUNT)<=0)
                holder.supportCount.setVisibility(View.GONE);
            else {
                holder.supportCount.setVisibility(View.VISIBLE);
                holder.supportCount.setText(String.valueOf(General.getBadgeCount(context, AppConstants.HDROOMS_COUNT)));

            }

              }
        else
            holder.supportCount.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView supportCount;
        public ImageView imgViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            supportCount = (TextView) itemView.findViewById(R.id.supportCount);
            imgViewIcon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
