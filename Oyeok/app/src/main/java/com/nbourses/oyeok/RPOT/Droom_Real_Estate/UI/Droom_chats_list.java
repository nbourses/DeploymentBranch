package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nbourses.oyeok.R;

import java.util.ArrayList;


public class Droom_chats_list extends Fragment {


    //private List<ApplicationInfo> mAppList;
    private ArrayList<Title> pObj=new ArrayList<>();
    private AppAdapter mAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_droom_chats_list, container, false);
        SwipeMenuListView listView= (SwipeMenuListView) rootView.findViewById(R.id.listView);
        for(int i=0;i<5;i++){
            Title t = new Title();
            t.setLastMessage("hi");
            t.setTitle("title"+i);
            pObj.add(i,t);
        }
        mAdapter = new AppAdapter();
        listView.setAdapter(mAdapter);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        pObj.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });



        return rootView;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    class AppAdapter extends BaseAdapter {


        @Override
        public int getViewTypeCount() {
            // menu type count
            return 1;
        }

        @Override
        public int getCount() {
            return pObj.size();
        }

        @Override
        public Title getItem(int position) {
            return pObj.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(),
                        R.layout.chat_list_row, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Title item = getItem(position);
            holder.title.setText(item.getTitle());
            holder.lastMessage.setText(item.getLastMessage());
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            // current menu type
            return 1;
        }

        class ViewHolder {
            TextView title;
            TextView lastMessage;

            public ViewHolder(View view) {
                title = (TextView) view.findViewById(R.id.chat_title);
                lastMessage = (TextView) view.findViewById(R.id.chat_last_message);
                view.setTag(this);
            }
        }

    }

    public class Title{
        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        String lastMessage;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        String title;

    }




}
