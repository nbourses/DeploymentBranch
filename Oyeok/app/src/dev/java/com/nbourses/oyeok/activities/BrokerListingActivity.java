package com.nbourses.oyeok.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.BrokerListingListView;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;
import com.nbourses.oyeok.realmModels.addBuildingRealm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class BrokerListingActivity extends BrokerMainPageActivity implements CustomPhasedListener {

    @Bind(R.id.btnMyDeals)
    Button btnMyDeals;

    @Bind(R.id.btnMyDeals1)
    Button btnMyDeals1;

    @Bind(R.id.container_Signup1)
    FrameLayout container_Signup1;

    @Bind(R.id.confirm_screen_title)
    TextView confirm_screen_title;

    @Bind(R.id.rentalCount)
    TextView rentalCount;

    @Bind(R.id.resaleCount)
    TextView resaleCount;

    CustomPhasedSeekBar  mPhasedSeekBar;
    int position=0;
    ViewPager viewPager;
    private Realm realm;
    ListView rental_list;
    MyPortfolioModel results;
    //    myPortfolioAdapter adapter;
    BrokerListingListView adapter;
    ArrayList<String> ids =new ArrayList<>(  );
    /*private static ArrayList<portListingModel> myPortfolioOR=new ArrayList<>();
    //    private static ArrayList<addBuildingRealm> addBuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> myPortfolioLL=new ArrayList<>();*/
    private static ArrayList<portListingModel> portListing=new ArrayList<>();
    private static ArrayList<portListingModel> portListingCopy=new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingOR=new ArrayList<>();
    private static ArrayList<portListingModel> deletelist=new ArrayList<>();
    private static ArrayList<portListingModel> item=new ArrayList<>();

    RealmResults<addBuildingRealm> results2;

    EditText inputSearch;
    private String TT = "LL";
    LinearLayout add_build;
    private String matchedId;
    TextView usertext,add_create;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_broker_listing);
        LinearLayout dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);

//        NestedScrollView dynamicContent = (NestedScrollView) findViewById(R.id.myScrollingContent);
        // assuming your Wizard content is in content_wizard.xml myScrollingContent
        View wizard = getLayoutInflater().inflate(R.layout.activity_broker_listing, null);

        // add the inflated View to the layout
        dynamicContent.addView(wizard);

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.listing);
        rb.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_select_listing) , null, null);
        rb.setTextColor(Color.parseColor("#2dc4b6"));
        ButterKnife.bind(this);

        if(portListing != null)
            portListing.clear();
        /*if(myPortfolioLL != null)
            myPortfolioLL.clear();
        if(myPortfolioOR != null)
            myPortfolioOR.clear();*/
        if(addbuildingLL != null)
            addbuildingLL.clear();
        if(addbuildingOR != null)
            addbuildingOR.clear();
        if(portListingCopy != null)
            portListingCopy.clear();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar); only for above 21 API
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm_screen_title.setVisibility(View.VISIBLE);
        btnMyDeals.setVisibility(View.GONE);
        btnMyDeals1.setVisibility(View.VISIBLE);
        btnMyDeals1.setBackground(getResources().getDrawable(R.drawable.snapshot));

        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener(this);


        /*BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.matching).setChecked(false);
        menu.findItem(R.id.listing).setChecked(true);
        menu.findItem(R.id.listing).setIcon(R.drawable.ic_select_listing);*/
//        if (menu.findItem(R.id.listing).isChecked()) menu.findItem(R.id.listing).setChecked(false);
//        else menu.findItem(R.id.listing).setChecked(true);


        rental_list=(ListView) findViewById(R.id.Rental_listview);
        inputSearch=(EditText) findViewById( R.id.inputSearch1);
        add_build=(LinearLayout)findViewById(R.id.add_build);
        usertext=(TextView)findViewById(R.id.usertext);
        add_create=(TextView)findViewById(R.id.add_create);


        /*if ((General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL) > 0) ) {
            rentalCount.setVisibility(View.VISIBLE);
            rentalCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.ADDB_COUNT_LL)));

        }
        if ((General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR) > 0)) {
            resaleCount.setVisibility(View.VISIBLE);
            resaleCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.ADDB_COUNT_OR)));

        }*/

        add_build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (General.getSharedPreferences(getBaseContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {

//                    General.setSharedPreferences(this, AppConstants.ROLE_OF_USER, "client");
                    SignUpFragment d = new SignUpFragment();
                    Bundle bundle = new Bundle();
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        bundle.putString("lastFragment", "clientDrawer");
                    else
                        bundle.putString("lastFragment", "brokerDrawer");
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

                }/*else if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")){
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                *//*in.putExtra("data","portfolio");
                in.putExtra("role","");*//*
                    startActivity(in);
                }*/else{
                    General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, "PC");
                    Intent in = new Intent(getBaseContext(), BrokerMap.class);
                /*in.putExtra("data","portfolio");
                in.putExtra("role","");*/
                    startActivity(in);
                }

            }
        });

        realm= General.realmconfig( getBaseContext() );
        adapter=new BrokerListingListView(getBaseContext(),portListing);
        rental_list.setAdapter(adapter);
       /* RealmResults<MyPortfolioModel> results= realm.where(MyPortfolioModel.class).equalTo("tt", "ll").findAllSorted("timestamp",false);

        for(MyPortfolioModel c :results){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null,"ll");
            myPortfolioLL.add(portListingModel);


        }


        RealmResults<MyPortfolioModel> results1= realm.where(MyPortfolioModel.class).equalTo("tt", "or").findAllSorted("timestamp",false);
        for(MyPortfolioModel c :results1){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getName(),c.getLocality(),c.getRate_growth(),c.getLl_pm(),c.getOr_psf(),c.getTimestamp(),c.getTransactions(),c.getConfig(),null,"or");

            myPortfolioOR.add(portListingModel);


        }*/


        RealmResults<addBuildingRealm> result11= realm.where(addBuildingRealm.class).equalTo("display_type","both").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result11){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),null);

            addbuildingLL.add(portListingModel);


        }

        RealmResults<addBuildingRealm> result1= realm.where(addBuildingRealm.class).equalTo("tt", "ll").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result1){

            Log.i("getLocality","getLocality   : "+c.getLocality());
            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),c.getLl_pm(),0,c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),"ll",c.getReq_avl(),c.getFurnishing());

            addbuildingLL.add(portListingModel);


        }



//    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config) {
        RealmResults<addBuildingRealm> result22= realm.where(addBuildingRealm.class).equalTo("display_type", "both").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result22){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),null);

            addbuildingOR.add(portListingModel);


        }
        RealmResults<addBuildingRealm> result2= realm.where(addBuildingRealm.class).equalTo("tt", "or").findAllSorted("timestamp",false);
        for(addBuildingRealm c :result2){


            portListingModel portListingModel = new  portListingModel(c.getId(),c.getBuilding_name(),c.getSublocality(),c.getGrowth_rate(),0,c.getOr_psf(),c.getTimestamp(),null,c.getConfig(),c.getDisplay_type(),"or",c.getReq_avl(),c.getFurnishing());

            addbuildingOR.add(portListingModel);


        }

        //Log.i("dataritesh","myPortfolioLL"+myPortfolioLL);
        portListing.addAll(addbuildingLL);
       // portListing.addAll(myPortfolioLL);
        portListingCopy.addAll(portListing);

            adapter.notifyDataSetChanged();
            getSupportActionBar().setTitle("");
            confirm_screen_title.setText("My Listings");
            inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
            usertext.setHint("\"My Listing\"");
            add_create.setText("Create");


       /* rental_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(item != null)
                    item.clear();
                item.add((portListingModel)adapter.getItem(position));
                String ids=((portListingModel) adapter.getItem(position)).getId();
                Log.i( "portfolio1","portListingModel   : "+position+" "+ids);

                RealmResults<MyPortfolioModel> result= realm.where(MyPortfolioModel.class).findAll();
                for(MyPortfolioModel c:result){
                    Log.i( "portfolio1","portListingModel inside for loop  : "+position+" "+ids+" "+c.getId());
                    if(ids.equalsIgnoreCase(c.getId())){
                        Log.i( "portfolio1","portListingModel inside if  : "+position+" "+ids);
                        if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                            General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, ids);
                            Intent in = new Intent(getBaseContext(), ClientMainActivity.class);
                            in.putExtra("id",ids);
                            in.putExtra("Cmarkerflag","true");
                            in.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP );*//*|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);*//*
                            startActivity(in);

                            break;
                        }else{
                            General.setSharedPreferences(getBaseContext(), AppConstants.CALLING_ACTIVITY, ids);
                            Intent in = new Intent(getBaseContext(), BrokerMap.class);
                            in.putExtra("id",ids);
                            in.putExtra("Bmarkerflag","true");
                            in.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP );*//*|
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);*//*
                            startActivity(in);
                            break;
                        }
                    }
                }

            }
        });*/


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
                        /*if (item.getItemId() == R.id.delete){
                                adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                              for (int i=0;i<adapter.getCount();i++) {
                                  ids.add( adapter.getItem( position ).getId() );
                              }

                        }*/
                if (item.getItemId() == R.id.delete){

                    for (final portListingModel d : deletelist) {

                        // Here your room is available
                        Log.i("portfolio1","deletelist"+portListing.contains(d)+" "+d.getName());
                        Log.i("addbuildingOR","addbuildingOR 1"+addbuildingOR.contains(d));
                        portListing.remove(d);
                       // myPortfolioLL.remove(d);
                       // myPortfolioOR.remove(d);
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
                            Realm myRealm = General.realmconfig(BrokerListingActivity.this);
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
                            Realm myRealm = General.realmconfig(BrokerListingActivity.this);
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
                        usertext.setHint("Your Building");
                        add_create.setText("Add");

                    }
                    return true;

                            /*for(deletelist.size()
                            adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                            for (int i=0;i<adapter.getCount();i++) {
                                ids.add( adapter.getItem( position ).getId() );
                            }*/

                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
//                        toolbar.setVisibility(View.VISIBLE);
            }
        });



        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString().trim();
                Log.i("searcho","s "+searchQuery.length());
                Log.i("searcho","sb "+portListingCopy+" ============ : "+inputSearch.getText().toString().equalsIgnoreCase(""));


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
          /*if(portListing != null)
              portListing.clear();
              portListing.addAll(portListingCopy);*/
                Log.i("search12","sb outside "+s+" ============ : "+inputSearch.getText().toString().equalsIgnoreCase(""));
                String s1="\""+s+"\"";
                if(s.toString().equalsIgnoreCase(""))
                {
                    usertext.setText(s);
                    Log.i("search12","sb inside "+s+" ============ : ");
                    if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                        usertext.setHint("Your Building");
                    else
                        usertext.setHint("My Listing");
                }

                else {
                    usertext.setText(s1);

                }
            }
        });





















    }



    @OnClick(R.id.btnMyDeals1)
    public void onBtnMyDealsClick(View v) {

        // if(btnMyDeals.getText().toString().equalsIgnoreCase("share")) {

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Log.i(TAG,"persy 12345");
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            //dashboardClientFragment.screenShot();

            if(portListing.size()==0) {
                new AlertDialog.Builder(this)
                        .setTitle("Empty List")
                        .setMessage("Please add building to take Snapshot!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                            }
                        })
                        /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })*/
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }else
                takeScreenshot();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;

    }

    @Override
    public void onBackPressed() {
        if(AppConstants.SIGNUP_FLAG){
            //Log.i(TAG,"flaga isa 6 ");

            if(AppConstants.REGISTERING_FLAG){}else{
                // getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.container_Signup1)).commit();
                AppConstants.SIGNUP_FLAG=false;
            }
            Log.i("sushil123"," main activity =================== SIGNUP_FLAGffffffff");

        }else
        if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
            Intent in = new Intent(getBaseContext(),BrokerMainActivity.class);
            in.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } else
            super.onBackPressed();
    }




    @Override
    public void onPositionSelected(int position, int count){
        inputSearch.setText("");
        if (position == 0) {
            TT = "LL";
            portListing.clear();
            portListing.addAll(addbuildingLL);
//            portListing.addAll(myPortfolioLL);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            AppConstants.TT_TYPE = "ll";
            adapter.notifyDataSetChanged();
            if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
                usertext.setText("");
                usertext.setHint("\"My Listing\"");
                add_create.setText("Create");
            }else{
                inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
                usertext.setText("");
                usertext.setHint("Your Building");
                add_create.setText("Add");

            }
            General.setBadgeCount(this, AppConstants.ADDB_COUNT_LL, 0);
            rentalCount.setVisibility(View.GONE);
        } else {
            TT = "OR";
            AppConstants.TT_TYPE = "or";
            Log.i("addbuildingOR", "addbuildingOR 3" + addbuildingOR);
            portListing.clear();
            portListing.addAll(addbuildingOR);
//            portListing.addAll(myPortfolioOR);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            adapter.notifyDataSetChanged();
            if(General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
                inputSearch.setHint("Search "+portListing.size()+" Building in Listings");
                usertext.setText("");
                usertext.setHint("\"My Listing\"");
                add_create.setText("Create");
            }else{
                inputSearch.setHint("Search "+ portListing.size()+" Building in Watchlist");
                usertext.setText("");
                usertext.setHint("Your Building");
                add_create.setText("Add");

            }
            General.setBadgeCount(this, AppConstants.ADDB_COUNT_OR, 0);
            resaleCount.setVisibility(View.GONE);
        }




       /* if(position == 0) {
            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);
            TT = "LL";

            Log.i( "portfolio","position : "+position );
           adapter = new myPortfolioAdapter(this,1);
            rental_list.setAdapter(adapter);
            adapter.setResults(realm.where(MyPortfolioModel.class).notEqualTo("ll_pm", 0).findAll());

        }
        else{
            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RESALE);
            TT = "OR";
            Log.i( "portfolio","position : "+position );
            adapter = new myPortfolioAdapter(this,2);
            rental_list.setAdapter(adapter);
            adapter.setResults(realm.where(MyPortfolioModel.class).notEqualTo("or_psf", 0).findAll());
        }*/

    }





    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }



    private void openScreenshot(File imageFile) {
        // Log.i(TAG,"persy 1234");
        int permission = ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

       /* if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"persy 12345");
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }*/
        // Log.i(TAG,"persy 12346");

        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg/text/html");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        //intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Hey, please check out these property rates I found out on this super amazing app Oyeok.</p><p><a href=\"https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/\">Download Oyeok for android</a></p>"));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, please check out these property rates I found out on this super amazing app Oyeok. \n \n  https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/");
        startActivity(Intent.createChooser(intent, "Share Image"));

//        Spanned spanned = Html.fromHtml(code, this, null);
    }


















}
