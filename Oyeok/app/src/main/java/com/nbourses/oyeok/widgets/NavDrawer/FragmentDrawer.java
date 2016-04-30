package com.nbourses.oyeok.widgets.NavDrawer;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;

import java.util.ArrayList;
import java.util.List;



public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private DBHelper dbHelper;
    String userType="";
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    public MDrawerListener mDrawerListener;
    List<NavDrawerItem> navDrawerItems;


    public void setmDrawerListener(MDrawerListener mDrawerListener) {
        this.mDrawerListener = mDrawerListener;
    }

    public interface MDrawerListener {
        public void drawerOpened();
    }

    public FragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        dbHelper=new DBHelper(getActivity());
        userType=dbHelper.getValue(DatabaseConstants.user);



        Log.d(TAG, "IS_LOGGED_IN_USER "+General.getSharedPreferences(getActivity(), AppConstants.IS_LOGGED_IN_USER));
        Log.d(TAG, "ROLE_OF_USER "+General.getSharedPreferences(getActivity(), AppConstants.ROLE_OF_USER));

        if(General.getSharedPreferences(getActivity(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
            titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_no_signup);
        }
        else {
            if (General.getSharedPreferences(getActivity(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_signup_broker);
            }
            else {
                titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_signup_client);
            }
        }

        /*if(userType.equals("Client"))
            titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_signup_client);
        else{
            if (userType.equals("Broker"))
                titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_signup_broker);
            else
                titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels_no_signup);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        navDrawerItems = getData();
        adapter = new NavigationDrawerAdapter(getActivity(), navDrawerItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position, navDrawerItems.get(position).getTitle());
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                if(mDrawerListener != null ) {
                    mDrawerListener.drawerOpened();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                if(mDrawerListener != null ) {
                    mDrawerListener.drawerOpened();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public void setmDrawerToggle(boolean string)
    {
        mDrawerToggle.setDrawerIndicatorEnabled(string);
    }

    public boolean handle(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    public static interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position, String itemTitle);
    }
}
