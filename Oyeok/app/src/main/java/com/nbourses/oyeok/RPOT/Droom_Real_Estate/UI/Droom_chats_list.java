package com.nbourses.oyeok.RPOT.Droom_Real_Estate.UI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Firebase.ChatList;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.MainActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Droom_chats_list extends Fragment implements ChatList {


    //private List<ApplicationInfo> mAppList;
    private ArrayList<Title> pObj=new ArrayList<>();
    private AppAdapter mAdapter;
    private String okId="",userId1="",userId2="",listString="";
    DBHelper dbHelper;
    DroomChatFirebase droomChatFirebase;
    HashMap<String,HashMap<String,String>> list;
    HashMap<String,HashMap<String,String>> listBundle;
    JSONObject jsonObject;
    SwipeMenuListView listView;
    String lastFragment="null";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_droom_chats_list, container, false);
        listView= (SwipeMenuListView) rootView.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        dbHelper=new DBHelper(getActivity());
        droomChatFirebase=new DroomChatFirebase(DatabaseConstants.firebaseUrl,getActivity());

        Log.i("tb getDroomList", String.valueOf(System.currentTimeMillis()));
        Bundle b = getArguments();
        try {
             lastFragment = b.getString("lastFragment");
        }catch (Exception e){
            Log.i("last f","exception null");
        }
        if(!lastFragment.equalsIgnoreCase("null"))
            lastFragment=b.getString("lastFragment");
        listBundle = (HashMap<String, HashMap<String, String>>) b.getSerializable("HashMap");
        if(listBundle!=null) {
            Log.i("chatlistdata=",listBundle.toString());
            setChatListData(listBundle);
        }
        else {
            droomChatFirebase.getDroomList(dbHelper.getValue(DatabaseConstants.userId), getActivity());
            droomChatFirebase.setmCallBack(Droom_chats_list.this);
        }

        ((MainActivity)getActivity()).changeDrawerToggle(false,"Deals");


        //Log.i("Munni","In"+"   "+dbHelper.getValue(DatabaseConstants.userId)+"  "+list.toString());





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
                /*if(index==0)
                {
                    pObj.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    Title title = new Title();
                    title = mAdapter.getItem(position);
                    Fragment fragment = new Droom_Chat_New();
                    Bundle bundle = new Bundle();
                    bundle.putString("UserId1", dbHelper.getValue(DatabaseConstants.userId));
                    bundle.putString("OkId", title.getOkId());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }*/
                switch (index) {
                    case 0:
                        Title title = new Title();
                        title = mAdapter.getItem(position);
                        Fragment fragment = new Droom_Chat_New();
                        Bundle bundle = new Bundle();
                        bundle.putString("UserId1", dbHelper.getValue(DatabaseConstants.userId));
                        bundle.putString("OkId", title.getOkId());
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commitAllowingStateLoss();
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


        ((MainActivity)getActivity()).changeDrawerToggle(true, "ChatRoom");



        return rootView;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void sendData(HashMap<String, HashMap<String, String>> hashMap) {

        list=hashMap;
        Iterator it = list.entrySet().iterator();
        Log.i("time befor iteration", String.valueOf(System.currentTimeMillis()));
        while (it.hasNext()) {
            Title t=new Title();
            Map.Entry pair = (Map.Entry)it.next();
            Map<String,String> temp= new HashMap<String,String>();
            t.setOkId((String) pair.getKey());
            temp=(HashMap)pair.getValue();
            Iterator tt = temp.entrySet().iterator();
            while (tt.hasNext()) {
                Map.Entry pair2 = (Map.Entry) tt.next();
                Log.i("Munni",pair2.toString());
                if(pair2.getKey().equals("title"))
                    t.setTitle((String) pair2.getValue());

                if (pair2.getKey().equals("lastMessage"))
                    {t.setLastMessage((String) pair2.getValue());}

                tt.remove();
            }
            it.remove(); // avoids a ConcurrentModificationException
            Log.i("Munni", t.getTitle());
            pObj.add(t);
        }
        if(lastFragment.equalsIgnoreCase("oyeIntentSpecs")){
            Title p = new Title();
            p.setOkId("");
            p.setLastMessage("we are looking for a broker for you");
            p.setTitle("recent oye published");
            pObj.add(p);
        }
        Log.i("time after iteration", String.valueOf(System.currentTimeMillis()));
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void setChatListData(HashMap<String, HashMap<String, String>> hashMap){
        list=hashMap;
        Iterator it = list.entrySet().iterator();
        Log.i("time befor iteration", String.valueOf(System.currentTimeMillis()));
        while (it.hasNext()) {
            Title t=new Title();
            Map.Entry pair = (Map.Entry)it.next();
            Map<String,String> temp= new HashMap<String,String>();
            t.setOkId((String) pair.getKey());
            temp=(HashMap)pair.getValue();
            Iterator tt = temp.entrySet().iterator();
            while (tt.hasNext()) {
                Map.Entry pair2 = (Map.Entry) tt.next();
                Log.i("Munni",pair2.toString());
                if(pair2.getKey().equals("title"))
                    t.setTitle((String) pair2.getValue());

                if (pair2.getKey().equals("lastMessage"))
                {t.setLastMessage((String) pair2.getValue());}

                tt.remove();
            }
            it.remove(); // avoids a ConcurrentModificationException
            Log.i("Munni", t.getTitle());
            pObj.add(t);
        }
        if(lastFragment.equalsIgnoreCase("oyeIntentSpecs")){
            Title p = new Title();
            p.setOkId("");
            p.setLastMessage("we are looking for a broker for you");
            p.setTitle("recent oye published");
            pObj.add(p);
        }
        Log.i("time after iteration", String.valueOf(System.currentTimeMillis()));
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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

        public String getOkId() {
            return okId;
        }

        public void setOkId(String okId) {
            this.okId = okId;
        }

        private String okId;



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
