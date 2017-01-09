package com.nbourses.oyeok.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.adapters.myPortfolioAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.helpers.OnSwipeTouchListener;
import com.nbourses.oyeok.realmModels.BuildingCacheRealm;

import java.util.ArrayList;

import io.realm.Realm;

import static com.facebook.FacebookSdk.getApplicationContext;


public class MainScreenPropertyListing extends Fragment {

    private OnFragmentInteractionListener mListener;

    Realm realm;
    private View view,view1;
    EditText Searchlist;
    ListView listview;
    LinearLayout dragablelistview;
    ImageView pullup_button;
    Animation cust_slideup,cust_slide_down,bounce;
    FrameLayout.LayoutParams params;
    private GestureDetector gestureDetector;
    myPortfolioAdapter adapter;
    View v;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    private long lastTouched = 0, start = 0;
    private static final long SCROLL_TIME = 200L;
    long count;
    ArrayList<String> ids =new ArrayList<>(  );
    DisplayMetrics displaymetrics;
    int height,screenheight;
    int bottom,top ,slideby=100;//=(int)dipToPixels(getContext(),75);
    Bundle data;
    public MainScreenPropertyListing() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_mainscreen_listview, container, false);


        listview = (ListView) view.findViewById(R.id.listview);
        dragablelistview = (LinearLayout) view.findViewById(R.id.dragablelistview);
        cust_slideup = (AnimationUtils.loadAnimation(getContext(), R.anim.cust_slideup));
        cust_slide_down = (AnimationUtils.loadAnimation(getContext(), R.anim.cust_slide_down));
        bounce=(AnimationUtils.loadAnimation(getContext(), R.anim.slideup_plus_bounce));
        pullup_button = (ImageView) view.findViewById(R.id.pullup_button);
        Searchlist =(EditText) view.findViewById(R.id.Searchlist);
       // General.setSharedPreferences(getContext(),AppConstants.AUTO_TT_CHANGE,"or");
        displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
       // width  = displaymetrics.widthPixels;
         adapter = new myPortfolioAdapter(getContext(), 1);

        listview.setAdapter(adapter);
        realm = General.realmconfig(getContext());

        adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSortedAsync("timestamp",false));
        adapter.notifyDataSetChanged();
        data=getArguments();
        String height1=data.getString( "height1" );
        height=  Integer.parseInt(height1);
        params = (FrameLayout.LayoutParams) dragablelistview.getLayoutParams();
        bottom=height-(int)dipToPixels(getContext(),55);
        top=(int)dipToPixels(getContext(),55);
        slideby=(int)dipToPixels(getContext(),80);
        screenheight-=(height+100);
        params.topMargin = bottom;

        //params.topMargin = params.bottomMargin-(params.topMargin);
        dragablelistview.startAnimation(cust_slideup);
        Log.i("touchcheck", "ACTION_MOVE on create" + params.bottomMargin+"   "+params.topMargin+ "   :  height "+params.height+"  "+params.width+"  :"+height1+" screenheight "+screenheight+"dipToPixels(getContext(),110) "+dipToPixels(getContext(),110));

        count = realm.where(BuildingCacheRealm.class).count();
        Searchlist.setHint("Search from "+ count +" Building");
       /* realm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                if(params.topMargin<bottom) {
                    count = realm.where(BuildingCacheRealm.class).count();
                    Searchlist.setHint("Search from" + count + " Building");
                    Log.i("sliding111","==============refreshListView==========onChange========  : "+General.getSharedPreferences(getApplicationContext(), AppConstants.AUTO_TT_CHANGE));

                    if (General.getSharedPreferences(getApplicationContext(), AppConstants.AUTO_TT_CHANGE).equalsIgnoreCase("ll")) {
                        Log.i("sliding111", "==============refreshListView=======ll===========");
                        // realm.addChangeListener(new chan);
                        adapter = new myPortfolioAdapter(getContext(), 1);
                        listview.setAdapter(adapter);
                        adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSortedAsync("timestamp", false));
                        // adapter.notifyDataSetChanged();

                    } else if (General.getSharedPreferences(getContext(), AppConstants.AUTO_TT_CHANGE).equalsIgnoreCase("or")) {
                        adapter = new myPortfolioAdapter(getContext(), 2);
                        Log.i("sliding111", "==============refreshListView=========or=========");
                        listview.setAdapter(adapter);
                        adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSortedAsync("timestamp", false));
                        // adapter.notifyDataSetChanged();
                    }
                }
            }
        });*/

        pullup_button.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            @Override
            public void onClick() {
                super.onClick();
                view=dragablelistview ;
                if (params.topMargin < bottom && params.topMargin >= top) {
                    params.topMargin = bottom;
                    dragablelistview.startAnimation(cust_slideup);
                    view.setLayoutParams(params);
                } else {
                    params.topMargin = top;
                    view.setLayoutParams(params);
                }
                // your on click here
            }
            @Override
            public boolean onTouch( View view, final MotionEvent event) {
                //View v1=view;
               // dragablelistview.clearAnimation();
                view=dragablelistview ;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        Log.i("touchcheck", "ACTION_MOVE" + event.getRawY());

                        params.topMargin = (int) event.getRawY()- screenheight;
                        Log.i("touchcheck", "ACTION_MOVE" + event.getRawY() + "   : " + params.topMargin + "   :  : " + params.bottomMargin+"   "+params.topMargin+ " "+event.getY()+" :screenheight "+screenheight);

                        view.setLayoutParams(params);
                        break;

                    case MotionEvent.ACTION_UP:
                        final long now = SystemClock.uptimeMillis();
                        if (now - lastTouched > SCROLL_TIME) {
                            if (params.topMargin < bottom)
                               params.topMargin = (int) event.getRawY()- screenheight;//- 310;//- (view.getHeight());
                                //params.topMargin =(int)event.getY();
                            else {
                                params.topMargin = bottom;
                                dragablelistview.startAnimation(cust_slideup);
                            }
                            if (params.topMargin < top) {
                                params.topMargin = top;
                                //  dragablelistview.startAnimation(cust_slide_down);
                            }
                            //params.bottomMargin=(int) event.getRawY();
                            Log.i("touchcheck", "ACTION_UP" + event.getRawY() + "   : " + params.topMargin + "   :  : " + params.bottomMargin+" === "+height+" "+event.getY());

                            //params.leftMargin = (int) event.getRawX() - (view.getWidth());
                            view.setLayoutParams(params);
                            break;
                        }else{
                            if (params.topMargin < bottom && params.topMargin >= top) {
                                params.topMargin = bottom;
                                view.setLayoutParams(params);
                            } else {
                                params.topMargin = top;
                                view.setLayoutParams(params);
                            }
                            break;
                        }

                    case MotionEvent.ACTION_DOWN:
                        Log.i("touchcheck", "ACTION_UP" + event.getRawY() + "   : " + params.topMargin + "   :  : " + params+" "+event.getY());
                        //params.topMargin==(int)1372;
                        //params.topMargin = 1300;
                        v=view;
                        lastTouched = SystemClock.uptimeMillis();
                        params.topMargin = (int) event.getRawY()- screenheight  ;//- 310;
                        //params.topMargin =(int)event.getY();
                        view.setLayoutParams(params);
                        break;
                }
                // return gestureDetector.onTouchEvent(event);
                return false;
            }

        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i( "listview","onItemClick  LL : "+adapter.getItemId(position)+"  : "+position );
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Searchlist.getWindowToken(), 0);
                ids.add(adapter.getItem(position).getId());
                General.setSharedPreferences(getContext(), AppConstants.BUILDING_ID,adapter.getItem(position).getId());
                params.topMargin = bottom;
                v.setLayoutParams(params);

               // ((DashboardClientFragment)getContext())
            }
        });
        Searchlist.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
               // if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );
                if(cs.equals(""))
                    Searchlist.setHint("Search from " + count + " Building");

                    adapter.setResults( realm.where(BuildingCacheRealm.class)
                              //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                /*}else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }*/

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        try {
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

                    if (key.equals(AppConstants.AUTO_TT_CHANGE)) {
try {
    if (General.getSharedPreferences(getApplicationContext(), AppConstants.AUTO_TT_CHANGE).equalsIgnoreCase("ll")) {
        adapter = new myPortfolioAdapter(getContext(), 1);
        listview.setAdapter(adapter);
        adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSorted("timestamp",false));
       // adapter.notifyDataSetChanged();

    } else if (General.getSharedPreferences(getContext(), AppConstants.AUTO_TT_CHANGE).equalsIgnoreCase("or")) {
        adapter = new myPortfolioAdapter(getContext(), 2);
        listview.setAdapter(adapter);
        adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSorted("timestamp",false));
       // adapter.notifyDataSetChanged();
    }/*else{
                            adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSorted("timestamp"));
                            adapter.notifyDataSetChanged();
                            //dragablelistview.startAnimation(bounce);
                        }*/
    Log.i("builds", "OUTO_CLICK_MARKER  : " + General.getSharedPreferences(getContext(), AppConstants.AUTO_TT_CHANGE));
}catch (Exception e){}

                    }
                }


            };
            prefs.registerOnSharedPreferenceChangeListener(listener);

        }
        catch (Exception e){
            Log.e("loc", e.getMessage());
        }

/*
        if(portListing != null)
            portListing.clear();
        if(myPortfolioLL != null)
            myPortfolioLL.clear();
        if(myPortfolioOR != null)
            myPortfolioOR.clear();
        if(addbuildingLL != null)
            addbuildingLL.clear();
        if(addbuildingOR != null)
            addbuildingOR.clear();
        if(portListingCopy != null)
            portListingCopy.clear();

        Log.i("port","portListing "+portListing);
        Log.i("port","portListing "+portListing);
        Log.i("port","myPortfolioLL "+myPortfolioLL);
        Log.i("port","addbuildingLL "+addbuildingLL);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar); only for above 21 API
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm_screen_title.setVisibility(View.VISIBLE);
        btnMyDeals.setBackground(getResources().getDrawable(R.drawable.share_btn_background));
        btnMyDeals.setText("Share");
        //Phased seekbar initialisation
        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener((this));
        rental_list=(ListView) findViewById(R.id.Rental_listview);
        inputSearch=(EditText) findViewById( R.id.inputSearch1);
        add_build=(LinearLayout)findViewById(R.id.add_build);
        usertext=(TextView)findViewById(R.id.usertext);
        add_create=(TextView)findViewById(R.id.add_create);


        add_build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {

//                    General.setSharedPreferences(this, AppConstants.ROLE_OF_USER, "client");
                    SignUpFragment d = new SignUpFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("lastFragment", "clientDrawer");
//                    loadFragment(signUpFragment, bundle, R.id.container_Signup1, "");
//                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup)).commit();
                    d.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);

                    fragmentTransaction.addToBackStack("cardSignUp1");
                    container_Signup1.setVisibility(View.VISIBLE);
                    fragmentTransaction.replace(R.id.container_Signup1, d);
//                    signUpCardFlag = true;
                    fragmentTransaction.commitAllowingStateLoss();
//                    AppConstants.SIGNUP_FLAG = true;

                }else {
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                *//*in.putExtra("data","portfolio");
                in.putExtra("role","");*//*
                    startActivity(in);
                }

            }
        });

        realm= General.realmconfig( getBaseContext() );
        adapter=new porfolioAdapter(getBaseContext(),portListing);
        rental_list.setAdapter(adapter);
        RealmResults<MyPortfolioModel> results= realm.where(MyPortfolioModel.class).notEqualTo("ll_pm", 0).findAllSorted("timestamp",false);

        for(MyPortfolioModel c :results){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null);

            myPortfolioLL.add(portListingModel);


        }


        RealmResults<MyPortfolioModel> results1= realm.where(MyPortfolioModel.class).notEqualTo("or_psf", 0).findAllSorted("timestamp",false);
        for(MyPortfolioModel c :results1){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null);

            myPortfolioOR.add(portListingModel);


        }


        RealmResults<addBuildingRealm> result11= realm.where(addBuildingRealm.class).equalTo("display_type","both").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result11){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type());

            addbuildingLL.add(portListingModel);


        }

        RealmResults<addBuildingRealm> result1= realm.where(addBuildingRealm.class).notEqualTo("ll_pm", 0).findAllSorted("timestamp",false);
        for(addBuildingRealm c :result1){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type());

            addbuildingLL.add(portListingModel);


        }



//    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config) {
        RealmResults<addBuildingRealm> result22= realm.where(addBuildingRealm.class).equalTo("display_type", "both").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result22){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type());

            addbuildingOR.add(portListingModel);


        }
        RealmResults<addBuildingRealm> result2= realm.where(addBuildingRealm.class).notEqualTo("or_psf", 0).findAllSorted("timestamp",false);
        for(addBuildingRealm c :result2){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type());

            addbuildingOR.add(portListingModel);


        }

        Log.i("dataritesh","myPortfolioLL"+myPortfolioLL);
        portListing.addAll(addbuildingLL);
        portListing.addAll(myPortfolioLL);
        portListingCopy.addAll(portListing);

        adapter.notifyDataSetChanged();
        if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
            getSupportActionBar().setTitle("");
            confirm_screen_title.setText("My Advertised \nListings");
            inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
            usertext.setHint("\"My Listing\"");
            add_create.setText("Create");
        }else{
            getSupportActionBar().setTitle("");
            confirm_screen_title.setText("My WatchList");
            inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
            usertext.setHint("My Watchlist");
            add_create.setText("Add");

        }
        // inputSearch.setHint("Search My "+portListing.size()+" Listings");
        Log.i("dataritesh","myPortfolioLL"+portListing);
//        portListing.addAll(myPortfolioLL);

       *//* adapter = new myPortfolioAdapter(this,1);
        rental_list.setAdapter(adapter);
        rental_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i( "portfolio1","onItemClick   : "+parent+" "+position+" "+id);

            }
        } );*//*

        rental_list.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE_MODAL);
        rental_list.setMultiChoiceModeListener( new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Prints the count of selected Items in title
                mode.setTitle(rental_list.getCheckedItemCount() + " Selected");


                Log.i( "portfolio1","onItemCheckedStateChanged   : "+position+" "+id+" "+portListing.contains(adapter.getItem(position)));
//                        portListing.contains(adapter.getItem(position));
//                        ids.add(adapter.getItem(position));

                deletelist.add((portListingModel) adapter.getItem(position));

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                        toolbar.setVisibility(View.GONE);
                mode.getMenuInflater().inflate(R.menu.main1, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        *//*if (item.getItemId() == R.id.delete){
                                adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                              for (int i=0;i<adapter.getCount();i++) {
                                  ids.add( adapter.getItem( position ).getId() );
                              }

                        }*//*
                if (item.getItemId() == R.id.delete){

                    for (final portListingModel d : deletelist) {

                        // Here your room is available
                        Log.i("portfolio1","deletelist"+portListing.contains(d)+" "+d.getName());
                        Log.i("addbuildingOR","addbuildingOR 1"+addbuildingOR.contains(d));
                        portListing.remove(d);
                        myPortfolioLL.remove(d);
                        myPortfolioOR.remove(d);
                        portListingCopy.remove(d);
                        if(addbuildingLL.contains(d)){
                            addbuildingLL.remove(d);
                            matchedId = d.getId();
                            Log.i("portfolio1","deletelist 23 "+matchedId);


                            for (final portListingModel l : addbuildingOR) {
                                Log.i("portfolio1","deletelist 25 "+l.getId());
                                if(l.getId().equalsIgnoreCase(matchedId)){
                                    addbuildingOR.remove(l);
                                    break;
                                }
                            }



                        }

                        if(addbuildingOR.contains(d)){
                            addbuildingOR.remove(d);
                            matchedId = d.getId();
                            Log.i("portfolio1","deletelist 33 "+matchedId);


                            for (final portListingModel l : addbuildingLL) {
                                Log.i("portfolio1","deletelist 35 "+l.getId());
                                if(l.getId().equalsIgnoreCase(matchedId)){
                                    addbuildingLL.remove(l);
                                    break;
                                }
                            }



                        }


                        Log.i("addbuildingOR","addbuildingOR 2"+addbuildingOR);


                        try {
                            Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                            MyPortfolioModel result = myRealm.where(MyPortfolioModel.class).equalTo("id", d.getId()).findFirst();
                            if(myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 21 "+e);
                        }
                        try {
                            Realm myRealm = General.realmconfig(MyPortfolioActivity.this);
                            addBuildingRealm result = myRealm.where(addBuildingRealm.class).equalTo("id", d.getId()).findFirst();
                            if(myRealm.isInTransaction())
                                myRealm.cancelTransaction();
                            myRealm.beginTransaction();
                            result.removeFromRealm();
                            myRealm.commitTransaction();
                        } catch (Exception e) {
                            Log.i("TAG", "caught in exception deleting default droom 31 "+e);
                        }




                    }
                    mode.finish();
                    adapter.notifyDataSetChanged();
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                        inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
                        usertext.setText("");
                        usertext.setHint("\"My Listing\"");
                        add_create.setText("Create");
                    }else{
                        inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
                        usertext.setText("");
                        usertext.setHint("My Watchlist");
                        add_create.setText("Add");

                    }
                    return true;

                            *//*for(deletelist.size()
                            adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                            for (int i=0;i<adapter.getCount();i++) {
                                ids.add( adapter.getItem( position ).getId() );
                            }*//*

                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
//                        toolbar.setVisibility(View.VISIBLE);
            }
        });




        *//*realm = General.realmconfig(this);
        adapter.setResults(realm.where(MyPortfolioModel.class).greaterThan( "ll_pm",0 ).findAll());

  inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );
                adapter.setResults( realm.where(MyPortfolioModel.class)
                        .greaterThan("ll_pm", 0)  //implicit AND
                        .beginGroup()
                        .contains("name", cs.toString(),false)
                        .endGroup()
                        .findAll() );
                }else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(TT=="LL"){
                    Log.i( "portfolio","onTextChanged  LL : "+cs );
                adapter.setResults( realm.where(MyPortfolioModel.class)
                        .greaterThan("ll_pm", 0)  //implicit AND
                        .beginGroup()
                        .contains("name", cs.toString(),false)
                        .endGroup()
                        .findAll() );
                }else{

                    adapter.setResults( realm.where(MyPortfolioModel.class)
                            .greaterThan("or_psf", 0)  //implicit AND
                            .beginGroup()
                            .contains("name", cs.toString(),false)
                            .endGroup()
                            .findAll() );
                    Log.i( "portfolio","onTextChanged  LL : ");

                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*//*

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                Log.i("searcho","s "+searchQuery.length());
                Log.i("searcho","sb "+portListingCopy);
                String s1="\""+s+"\"";
                if(!s1.equalsIgnoreCase(""))
                    usertext.setText(s1);
                else {
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        usertext.setHint("My Watchlist");
                    else
                        usertext.setHint("My Listing");

                }
                if(portListing != null)
                    portListing.clear();
                portListing.addAll(portListingCopy);
                Log.i("searcho","sc "+portListing);
                for(portListingModel c :portListingCopy){
                    Log.i("searcho", "sd " + c.getLl_pm() + " "+ c.getOr_psf() +" ");
                    if(!c.getName().toLowerCase().contains(searchQuery.toLowerCase())){
                        portListing.remove(c);
                    } else if(c.getLl_pm() != 0 && c.getOr_psf() != 0) {
                        if (TT.equalsIgnoreCase("LL")) {
                            if (c.getLl_pm() == 0) {
                                portListing.remove(c);
                            }
                        } else if (TT.equalsIgnoreCase("OR")) {
                            if (c.getOr_psf() == 0) {
                                portListing.remove(c);
                            }
                        }



                    }
                    Log.i("searcho", "sd " + portListing);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
          *//*if(portListing != null)
              portListing.clear();
              portListing.addAll(portListingCopy);*//*
            }
        });*/








      //BuildingCacheRealm.setAdapter(sampleadapter);
        return view;
    }




   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = activity;
    }*/
    /*@Override
    public void onAttach(Activity activity){
        this.activity = activity;
    }*/
    @Override
    public void onDetach() {
        super.onDetach();
       /* mListener = null;*/
    }

    public interface OnFragmentInteractionListener {

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refreshListView);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshListView, new IntentFilter(AppConstants.REFRESH_LISTVIEW));

    }
    private BroadcastReceiver refreshListView=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          //  Log.i("sliding111","==============refreshListView==================  : "+General.getSharedPreferences(getApplicationContext(), AppConstants.AUTO_TT_CHANGE)+ "  slideby : "+slideby+"   params.topMargin  "+params.topMargin+"   :  === : "+bottom);
            count = realm.where(BuildingCacheRealm.class).count();
            Searchlist.setHint("Search from " + count + " Building");
            if(params.topMargin<bottom) {
                /*count = realm.where(BuildingCacheRealm.class).count();
                Searchlist.setHint("Search from " + count + " Building");*/
             //   Log.i("sliding111","==============refreshListView==========onChange========  : "+General.getSharedPreferences(getApplicationContext(), AppConstants.AUTO_TT_CHANGE));
                adapter.setResults(realm.where(BuildingCacheRealm.class).findAllSortedAsync("timestamp", false));

            }else{
               // params.topMargin=bottom-top;
                Log.i("sliding111", "==============animate=========or========= : "+params.topMargin);
                params.topMargin=bottom-slideby;
                view.setLayoutParams(params);
                Log.i("sliding111", "==============animate=========or========= : "+params.topMargin+" :;;;;;;;;;;;;;;;; : " +bottom);
                dragablelistview.startAnimation(bounce);
                 new CountDownTimer(500, 500) {

                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {
                   // if(General.getSharedPreferences(getContext(),AppConstants.MY_BASE_LOCATION).equalsIgnoreCase(""))
                    dragablelistview.clearAnimation();
                        params.topMargin=bottom;
                        view.setLayoutParams(params);
                }

            }.start();
                //params.topMargin=bottom-top;
            }
        }
    };
    private  class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

      @Override
      public boolean onDown(MotionEvent e) {
            /*it needs to return true if we don't want
            to ignore rest of the gestures*/
          return true;
      }
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
        return true;
            //onClick();
           // return super.onSingleTapUp(event);
        }

        /*public void onClick() {

        }*/
    }


    public static float dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

}
