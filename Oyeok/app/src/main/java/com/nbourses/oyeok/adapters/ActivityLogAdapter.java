package com.nbourses.oyeok.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.models.ActivityLog;

import java.util.List;

/**
 * Created by RiteshWarke on 30/01/17.
 */

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.MyViewHolder> {



        private List<ActivityLog> logList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, action;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                action = (TextView) view.findViewById(R.id.action);
              /*  year = (TextView) view.findViewById(R.id.year);*/
            }
        }


        public ActivityLogAdapter(List<ActivityLog> logList) {
            this.logList = logList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_log_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ActivityLog item = logList.get(position);
            holder.name.setText(item.getName());
            holder.action.setText(item.getAction());
        }

        @Override
        public int getItemCount() {
            return logList.size();
        }



}
