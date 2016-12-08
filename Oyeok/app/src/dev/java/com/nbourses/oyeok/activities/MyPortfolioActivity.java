package com.nbourses.oyeok.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.porfolioAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.portListingModel;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;
import com.nbourses.oyeok.realmModels.addBuildingRealm;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sushil on 29/09/16.
 */

public class MyPortfolioActivity extends AppCompatActivity implements CustomPhasedListener {



    CustomPhasedSeekBar  mPhasedSeekBar;
    int position=0;
    ViewPager viewPager;
    private Realm realm;
    ListView rental_list;
    MyPortfolioModel results;
//    myPortfolioAdapter adapter;
     porfolioAdapter adapter;
    ArrayList<String> ids =new ArrayList<>(  );
    private static ArrayList<portListingModel> myPortfolioOR=new ArrayList<>();
//    private static ArrayList<addBuildingRealm> addBuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> myPortfolioLL=new ArrayList<>();
    private static ArrayList<portListingModel> portListing=new ArrayList<>();
    private static ArrayList<portListingModel> portListingCopy=new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingLL=new ArrayList<>();
    private static ArrayList<portListingModel> addbuildingOR=new ArrayList<>();
    private static ArrayList<portListingModel> deletelist=new ArrayList<>();

    RealmResults<MyPortfolioModel> results1;
    RealmResults<addBuildingRealm> results2;
    EditText inputSearch;
    private String TT = "LL";
    LinearLayout add_build;
    private String matchedId;

    @Bind(R.id.container_Signup1)
    FrameLayout container_Signup1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_portfolio );
        ButterKnife.bind(this);
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
        getSupportActionBar().setTitle("My WatchList");

        //Phased seekbar initialisation
        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener((this));
        rental_list=(ListView) findViewById(R.id.Rental_listview);
        inputSearch=(EditText) findViewById( R.id.inputSearch1);

        add_build=(LinearLayout)findViewById(R.id.add_build);
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
                /*in.putExtra("data","portfolio");
                in.putExtra("role","");*/
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
        Log.i("dataritesh","myPortfolioLL"+portListing);
//        portListing.addAll(myPortfolioLL);

       /* adapter = new myPortfolioAdapter(this,1);
        rental_list.setAdapter(adapter);
        rental_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i( "portfolio1","onItemClick   : "+parent+" "+position+" "+id);

            }
        } );*/

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




        /*realm = General.realmconfig(this);
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
        });*/

inputSearch.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {



    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchQuery = s.toString().trim();
        Log.i("searcho","s "+searchQuery.length());
        Log.i("searcho","sb "+portListingCopy);

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
    }
});


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
    public void onPositionSelected(int position, int count) {
        inputSearch.setText("");
        if(position==0){
            TT = "LL";
            portListing.clear();
            portListing.addAll(addbuildingLL);
            portListing.addAll(myPortfolioLL);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            adapter.notifyDataSetChanged();
        }else{
            TT = "OR";
            Log.i("addbuildingOR","addbuildingOR 3"+addbuildingOR);
            portListing.clear();
            portListing.addAll(addbuildingOR);
            portListing.addAll(myPortfolioOR);
            portListingCopy.clear();
            portListingCopy.addAll(portListing);
            adapter.notifyDataSetChanged();
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




    /*private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(title);
        Log.i("SIGNUP_FLAG","SIGNUP_FLAG=========  loadFragment client "+getFragmentManager().getBackStackEntryCount());
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }*/

    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }




}















   /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        TabLayout.Tab tab1=tabLayout.newTab();
        TabLayout.Tab tab2=tabLayout.newTab();
        tabLayout.addTab(tab1.setText("Rental"));
        tabLayout.addTab(tab2.setText("Buy/Sell"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

     /*   viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(adapter);*/


        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TabFragment1 tabFragment1=new TabFragment1();

                loadFragment(tabFragment1, null, R.id.container_map, "Client Dashboard");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/


