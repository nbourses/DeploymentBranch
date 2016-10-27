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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.adapters.myPortfolioAdapter;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;

import java.util.ArrayList;

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
    myPortfolioAdapter adapter;
    ArrayList<String> ids =new ArrayList<>(  );
    RealmResults<MyPortfolioModel> results1;
    EditText inputSearch;
    private String TT = "LL";
    LinearLayout add_build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_portfolio );

       final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar); only for above 21 API
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Portfolio");
        mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{this.getResources().getString(R.string.Rental), this.getResources().getString(R.string.Resale)}));
        mPhasedSeekBar.setListener((this));
        rental_list=(ListView) findViewById(R.id.Rental_listview);
        inputSearch=(EditText) findViewById( R.id.inputSearch1);

        add_build=(LinearLayout)findViewById(R.id.add_build);
        add_build.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(getBaseContext(),ClientMainActivity.class);
                in.putExtra("add","portfolio");
                startActivity(in);

            }
        });

        adapter = new myPortfolioAdapter(this,1);
        rental_list.setAdapter(adapter);
        rental_list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i( "portfolio1","onItemClick   : "+parent+" "+position+" "+id);

            }
        } );

                rental_list.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE_MODAL);
                   rental_list.setMultiChoiceModeListener( new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                        // Prints the count of selected Items in title
                        mode.setTitle(rental_list.getCheckedItemCount() + " Selected");


                        Log.i( "portfolio1","onItemCheckedStateChanged   : "+position+" "+id);
                        ids.add(adapter.getItem(position).getId());

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
                        if (item.getItemId() == R.id.delete){
                                adapter.removeSelection(ids);
                            ids.clear();
                            mode.finish();
                            return true;
                        }else{
                              for (int i=0;i<adapter.getCount();i++) {
                                  ids.add( adapter.getItem( position ).getId() );
                              }

                        }

                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
//                        toolbar.setVisibility(View.VISIBLE);
                    }
       });


        realm = General.realmconfig(this);
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



}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    @Override
    public void onPositionSelected(int position, int count) {
        if(position == 0) {
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
        }

    }




    private void loadFragment(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(title);
        Log.i("SIGNUP_FLAG","SIGNUP_FLAG=========  loadFragment client "+getFragmentManager().getBackStackEntryCount());
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


