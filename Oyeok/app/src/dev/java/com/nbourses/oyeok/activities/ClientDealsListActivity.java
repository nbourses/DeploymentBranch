package com.nbourses.oyeok.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.deleteHDroom;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.adapters.BrokerDealsListAdapter;
import com.nbourses.oyeok.enums.DealStatusType;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.models.BrokerDeals;
import com.nbourses.oyeok.models.HdRooms;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.realmModels.DefaultDeals;
import com.nbourses.oyeok.realmModels.HalfDeals;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;

//import com.google.android.gms.appindexing.AppIndex;



public class ClientDealsListActivity extends AppCompatActivity implements CustomPhasedListener {


    private List<PublishLetsOye> publishLetsOyes;
    private static final String TAG = "DealsListActivity";

    @Bind(R.id.listViewDeals)
    SwipeMenuListView listViewDeals;

    @Bind(R.id.supportChat)
    LinearLayout supportChat;

    @Bind(R.id.dealItemRoot)
    RelativeLayout dealItemRoot;
    //    @Bind(R.id.fragment_container)
//    FrameLayout fragment_container;
    @Bind(R.id.fragment_container1)
    FrameLayout fragment_container1;

    @Bind(R.id.view)
    View view;

    @Bind(R.id.phasedSeekBar)
    CustomPhasedSeekBar mCustomPhasedSeekbar;

    @Bind(R.id.phaseSeekbar)
    LinearLayout phaseSeekBar;


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.signUpCardText)
    TextView signUpCardText;

    @Bind(R.id.rentalCount)
    TextView rentalCount;

    @Bind(R.id.resaleCount)
    TextView resaleCount;



   /* @Bind(R.id.search)
    Button search;
    @Bind(R.id.searchgone)
    Button searchgone;*/

    //    @Bind(R.id.searchView)
//    SearchView searchView;


//    @Bind(R.id.searchView)
//    SearchView searchView;


    private boolean default_deal_flag;
    private ArrayList<BrokerDeals> default_deals;
    private ArrayList<BrokerDeals> default_dealsLL;
    private ArrayList<BrokerDeals> default_dealsOR;
    private ArrayList<BrokerDeals> default_deals_copy;
    private ArrayList<BrokerDeals> unverifiedLL;
    private ArrayList<BrokerDeals> unverifiedOR;
    //private BrokerDeals deals;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private String deals;
    private Boolean RefreshDrooms = false;

    private BrokerDealsListAdapter listAdapter;
    private Boolean showbgtext = true;
    private HalfDeals halfDeals;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    // private ArrayList<dumpclass> listdata;

    public CustomPhasedSeekBar mPhasedSeekBar1;
    private String TT = "LL";
    private ArrayList<BrokerDeals> total_deals;
    private ArrayList<BrokerDeals> listBrokerDeals_new;
    private ArrayList<BrokerDeals> listBrokerDealsLL;
    private ArrayList<BrokerDeals> listBrokerDealsOR;
    private Set<String> mutedOKIds = new HashSet<String>();
    private int position;   //position of swipe menu item
    private Realm myRealm;
    private DefaultDeals defaultDeals;
    private ArrayList<String> defaultOkIds = new ArrayList<String>();
    private ArrayList<String> matchedOkIds;
    private ArrayList<BrokerDeals> reset;

    private TextView bgtxt;
    private LinearLayout bgtxtlayout;

    private ImageView txtPreviouslySelectedPropertyType;
    private static final String propertyTypeDefaultColor = "#FFFFFF";
    private String filterPtype = null;
    private String searchQuery = null;
    private SearchView searchView;
    private LinearLayout signUpCard;
    private Button signUp;
    private Boolean signUpCardFlag = false;

    private ArrayList<BrokerDeals> cachedDeals;
    private ArrayList<BrokerDeals> cachedDealsLL;
    private ArrayList<BrokerDeals> cachedDealsOR;

    Animation bounce;
    Animation slideUp;
    Animation slideDown;
    private ArrayList<BrokerDeals> copy;
    private String сolorString;


    private BroadcastReceiver networkConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            networkConnectivity();
        }
    };
    private BroadcastReceiver badgeCountBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TAG", "narcos 1 ");

            //loadDefaultDeals();
            //loadBrokerDeals();
            //refreshview to show badges
            try {
                Log.i("TAG", "narcos " + intent.getStringExtra("channel_name"));
                updateNewLastseen(intent.getStringExtra("channel_name"));
            } catch (Exception e) {

            }
            Collections.sort(total_deals);
            listAdapter.notifyDataSetChanged();



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myRealm = General.realmconfig(this);
        } catch (Exception e) {
        }


        IntentFilter filter = new IntentFilter("okeyed");
        LocalBroadcastManager.getInstance(this).registerReceiver(handlePushNewMessage, filter);
        setContentView(R.layout.activity_deals_list);


        listViewDeals = (SwipeMenuListView) findViewById(R.id.listViewDeals);
        supportChat = (LinearLayout) findViewById(R.id.supportChat);
        fragment_container1 = (FrameLayout) findViewById(R.id.fragment_container1);
        //  listViewDeals.setAdapter(new SearchingBrokersAdapter(this));
        signUpCard = (LinearLayout) findViewById(R.id.signUpCard);
        signUp = (Button) findViewById(R.id.signUp);
        bgtxt = (TextView) findViewById(R.id.bgtxt1);
        /*bgtxtlayout = (LinearLayout) findViewById(R.id.bgtxtlayout);
        bgtxtlayout.setVisibility(View.VISIBLE);
        bgtxt.setText("Go Back &,\nBroadcast yours needs\nto create New DEALs\nwith more Brokers");*/
        listAdapter = new BrokerDealsListAdapter(default_deals, getApplicationContext());
        supportChat.setVisibility(View.VISIBLE);
        listViewDeals.setVisibility(View.VISIBLE);


        ButterKnife.bind(this);

        Log.i("CHAT", "in client deals list activity " + DateFormat.getDateTimeInstance().format(new Date()));


        init();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {


                // create "More" item
                SwipeMenuItem MoreItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                MoreItem.setBackground(new ColorDrawable(getResources().getColor(R.color.grey)));
                // set item width
                MoreItem.setWidth(listAdapter.dp2px(90));
                Log.i("TRACE1", "dp" + " " + listAdapter.dp2px(90));
                // set item title
                MoreItem.setIcon(R.drawable.more);
                MoreItem.setTitle("More");
                // set item title fontsize
                MoreItem.setTitleSize(18);
                // set item title font color
                MoreItem.setTitleColor(R.color.orange);
                // add to more
                menu.addMenuItem(MoreItem);


        /*        // create "unmute" item
                SwipeMenuItem MuteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                MuteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.timestamp)));
                // set item width
                MuteItem.setWidth(listAdapter.dp2px(90));
                // set item title
                MuteItem.setIcon(R.drawable.unmute);
                MuteItem.setTitle("mute");
                // set item title fontsize
                MuteItem.setTitleSize(18);
                // set item title font color
                MuteItem.setTitleColor(Color.WHITE);
                // add to more
                menu.addMenuItem(MuteItem);*/


                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));
                // set item width
                deleteItem.setWidth(listAdapter.dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                deleteItem.setTitle("Delete");
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(R.color.black);
                // add to more
                menu.addMenuItem(deleteItem);


            }
        };
        // set creator
        listViewDeals.setMenuCreator(creator);
        listViewDeals.setCloseInterpolator(new BounceInterpolator());
        listViewDeals.setOpenInterpolator(new BounceInterpolator());

        // step 2. listener item click event

        listViewDeals.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int pos, SwipeMenu menu, int index) {
                //           ApplicationInfo item =  listAdapter.getItem(position);
                position = pos;
            /*    // mute or unmute toggle
                String muteStatus = "Mute notifications";
                String blockStatus = "Block deal";
                Log.i(TAG,"listbrokerdealsnew "+listBrokerDeals_new);
                Log.i(TAG,"listbrokerdealsnew  def "+default_deals);
                Log.i(TAG,"listbrokerdealsnew "+listBrokerDeals_new);
                try {
                    if (!(General.getMutedOKIds(ClientDealsListActivity.this) == null)) {
                        mutedOKIds.addAll(General.getMutedOKIds(ClientDealsListActivity.this));
                        if (mutedOKIds.contains(total_deals.get(position).getOkId())) {
                            muteStatus = "Unmute notifications";

                        }
                    }

                }catch(Exception e){
                    // handle this problem when no network and remove this try catch
                }

                try{
                    Realm myRealm = General.realmconfig(ClientDealsListActivity.this);

                    DealStatus dealStatus = myRealm.where(DealStatus.class).equalTo(AppConstants.OK_ID, total_deals.get(position).getOkId()).findFirst();
                    if (dealStatus != null && dealStatus.getStatus().equalsIgnoreCase(DealStatusType.BLOCKED.toString())) {
                        blockStatus = "Unblock deal";
                        Log.i(TAG,"Block deal Block deal "+blockStatus);
                    } else {
                        blockStatus = "Block deal";
                        Log.i(TAG,"Block deal Block deal "+blockStatus);
                    }
                }
                catch(Exception e){
                    Log.i(TAG,"caught in exception reading block status from realm "+e);
                }*/


                switch (index) {
                    case 0:
                        Log.i(TAG, "wadala default deals 2 you ");
                        Log.i(TAG, "wadala default deals 2 you default_deals " + default_deals);
                        Log.i(TAG, "wadala default deals 2 you default_deals " + listBrokerDeals_new);
                        Log.i(TAG, "wadala default deals 2 you default_deals " + total_deals);
                        if (General.isNetworkAvailable(ClientDealsListActivity.this)) {
                            if (/*default_deals != null && !*/listBrokerDeals_new.contains(total_deals.get(position))) {
                                Log.i(TAG, "tag me yo 2 " + total_deals.get(pos).getHDroomStatus().getSelfStatus());
                                final String muteStatus = (total_deals.get(pos).getHDroomStatus().getSelfStatus().equalsIgnoreCase("active")) ? "mute" : "unmute";
                                final String blockStatus = (total_deals.get(pos).getHDroomStatus().getOtherStatus().equalsIgnoreCase("blocked")) ? "unblock" : "block";
                                Log.i(TAG, "tag me yo 1 " + blockStatus);
                                final CharSequence[] items = {muteStatus, /*"Delete deal",*/blockStatus, "Cancel"};
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ClientDealsListActivity.this);
                                builder.setTitle("More!");
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (items[item].equals(muteStatus)) {

                                            if (muteStatus.equalsIgnoreCase("mute")) {
                                                try {
                                                    General.setDealStatus(ClientDealsListActivity.this, DealStatusType.MUTED.toString(), total_deals.get(position).getOkId(), "default", General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.USER_ID));
                                                    total_deals.get(pos).getHDroomStatus().setSelfStatus(DealStatusType.MUTED.toString());
                                                    SnackbarManager.show(
                                                            Snackbar.with(ClientDealsListActivity.this)
                                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                                    .text(total_deals.get(position).getSpecCode() + " muted!")
                                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                                } catch (Exception e) {

                                                }
                                            } else {
                                                try {
                                                    General.setDealStatus(ClientDealsListActivity.this, DealStatusType.ACTIVE.toString(), total_deals.get(position).getOkId(), "default", General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.USER_ID));
                                                    total_deals.get(pos).getHDroomStatus().setSelfStatus(DealStatusType.ACTIVE.toString());
                                                    SnackbarManager.show(
                                                            Snackbar.with(ClientDealsListActivity.this)
                                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                                    .text(total_deals.get(position).getSpecCode() + " unmuted!")
                                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                                                } catch (Exception e) {

                                                }


                                            }
                                        } else if (items[item].equals(blockStatus)) {

                                            if (blockStatus.equalsIgnoreCase("block")) {
                                                try {
                                                    General.setDealStatus(ClientDealsListActivity.this, DealStatusType.BLOCKED.toString(), total_deals.get(position).getOkId(), "default", total_deals.get(position).getOkUserId());
                                                    total_deals.get(pos).getHDroomStatus().setOtherStatus(DealStatusType.BLOCKED.toString());
                                                    SnackbarManager.show(
                                                            Snackbar.with(ClientDealsListActivity.this)
                                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                                    .text(total_deals.get(position).getSpecCode() + " blocked!")
                                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                                } catch (Exception e) {

                                                }
                                            } else {
                                                try {
                                                    General.setDealStatus(ClientDealsListActivity.this, DealStatusType.ACTIVE.toString(), total_deals.get(position).getOkId(), "default", total_deals.get(position).getOkUserId());
                                                    total_deals.get(pos).getHDroomStatus().setOtherStatus(DealStatusType.ACTIVE.toString());
                                                    SnackbarManager.show(
                                                            Snackbar.with(ClientDealsListActivity.this)
                                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                                    .text(total_deals.get(position).getSpecCode() + " active!")
                                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                                                } catch (Exception e) {

                                                }


                                            }

                                        } else if (items[item].equals("Cancel")) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.show();
                            } else if (/*default_deals != null && !*/default_deals.contains(total_deals.get(position))) {
                                SnackbarManager.show(
                                        Snackbar.with(ClientDealsListActivity.this)
                                                .position(Snackbar.SnackbarPosition.TOP)
                                                .text("Default deals cannot be blocked or muted.")

                                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                            }
                        } else {
                            SnackbarManager.show(
                                    Snackbar.with(ClientDealsListActivity.this)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text("Deals cannot be blocked or muted offline.")

                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                        }
                        break;

                    case 1:
                        if (General.isNetworkAvailable(ClientDealsListActivity.this)) {
                            // Log.i("DELETEHDROOM","position "+position+"menu "+menu+"index "+index);
                            Log.i(TAG, "wadala default deals 2 you ");
                            Log.i(TAG, "wadala default deals 2 you default_deals " + default_deals);
                            Log.i(TAG, "wadala default deals 2 you default_deals " + listBrokerDeals_new);
                            Log.i(TAG, "wadala default deals 2 you default_deals " + total_deals);

                            Log.i("deleteDR CALLED", "spec code " + total_deals.get(position).getSpecCode());
                            AlertDialog alertDialog = new AlertDialog.Builder(ClientDealsListActivity.this).create();
                            //alertDialog.setTitle("DELETE");
                            alertDialog.setMessage("Do you really want to delete this deal room.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();


                                            if (default_deals != null && default_deals.contains(total_deals.get(position))) {

                                                Log.i("deleteDR CALLED", "Its default deal " + total_deals.get(position).getSpecCode());


                                                try {
                                                    Realm myRealm = General.realmconfig(ClientDealsListActivity.this);
                                                    RealmResults<DefaultDeals> result = myRealm.where(DefaultDeals.class).equalTo(AppConstants.OK_ID, total_deals.get(position).getOkId()).findAll();

                                                    myRealm.beginTransaction();
                                                    result.clear();
                                                    myRealm.commitTransaction();
                                                } catch (Exception e) {
                                                    Log.i(TAG, "caught in exception deleting default droom");
                                                }

                                                RealmResults<DefaultDeals> results1 =
                                                        myRealm.where(DefaultDeals.class).findAll();

                                                Log.i(TAG, "until defaultDeals called ror 4 " + results1);

                                                for (DefaultDeals c : results1) {
                                                    Log.i(TAG, "until defaultDeals ror 1 " + c.getSpec_code());
                                                    Log.i(TAG, "until defaultDeals ror 2 " + c.getOk_id());
                                                    Log.i(TAG, "until defaultDeals ror 3 " + c.getLocality());
                                                }
                                                //deleteDealingroom("1",total_deals.get(position).getOkId(),total_deals.get(position).getSpecCode());
                                                if (default_dealsLL.contains(total_deals.get(position)))
                                                    default_dealsLL.remove(total_deals.get(position));
                                                else if (default_dealsOR.contains(total_deals.get(position)))
                                                    default_dealsOR.remove(total_deals.get(position));
                                                default_deals.remove(total_deals.get(position));
                                                total_deals.remove(position);
                                                listAdapter.notifyDataSetChanged();


                                            } else {

                                                if (!listBrokerDeals_new.isEmpty() && listBrokerDeals_new.contains(total_deals.get(position))) {

                                                    Log.i("deleteDR CALLED", "Its HDroom " + total_deals.get(position).getSpecCode());


                                                    deleteDealingroom("0", total_deals.get(position).getOkId(), total_deals.get(position).getSpecCode());
                                                    //on delete droom delete that room OK id from mutedOKIds

                                                    Log.i("MUTE", "muted from shared1" + General.getMutedOKIds(ClientDealsListActivity.this));
                                                    if (!(General.getMutedOKIds(ClientDealsListActivity.this) == null)) {
                                                        mutedOKIds.addAll(General.getMutedOKIds(ClientDealsListActivity.this));

                                                        if (mutedOKIds.contains(total_deals.get(position).getOkId()))
                                                            mutedOKIds.remove(total_deals.get(position).getOkId());

                                                    }

                                                    General.saveMutedOKIds(ClientDealsListActivity.this, mutedOKIds);

                                                    Log.i("MUTE", "muted from shared" + General.getMutedOKIds(ClientDealsListActivity.this));
                                                    if (listBrokerDealsLL.contains(total_deals.get(position)))
                                                        listBrokerDealsLL.remove(total_deals.get(position));
                                                    else if (listBrokerDealsOR.contains(total_deals.get(position)))
                                                        listBrokerDealsOR.remove(total_deals.get(position));
                                                    else if (unverifiedLL.contains(total_deals.get(position)))
                                                        unverifiedLL.remove(total_deals.get(position));
                                                    else if (unverifiedOR.contains(total_deals.get(position)))
                                                        unverifiedOR.remove(total_deals.get(position));
                                                    listBrokerDeals_new.remove(total_deals.get(position));
                                                    total_deals.remove(position);
                                                    Log.i("MUTE", "total_deals total_deals total_deals" + total_deals);
                                                    listAdapter.notifyDataSetChanged();

                                                } else {
                                                    SnackbarManager.show(
                                                            Snackbar.with(ClientDealsListActivity.this)
                                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                                    .text("Deals can not be deleted offline.")

                                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                                }

                                            }

                                        }
                                    });
                            alertDialog.show();

                        } else {
                            SnackbarManager.show(
                                    Snackbar.with(ClientDealsListActivity.this)
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .text("Deals cannot be deleted offline.")

                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                        }

//					delete(item);
//                        listAdapter.remove(position);
//                        listAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listViewDeals.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        listViewDeals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
//              Toast.makeText(getApplicationContext(), position + " long click", 0).show();
                return false;
            }
        });


//        touchListener = new ListViewSwipeGesture(listViewDeals, swipeListener, this);
//        touchListener.SwipeType	=	ListViewSwipeGesture.Double;    //Set two options at background of list item
//
//


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(badgeCountBroadcast, new IntentFilter(AppConstants.BADGE_COUNT_BROADCAST));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(networkConnectivity, new IntentFilter(AppConstants.NETWORK_CONNECTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(networkConnectivity);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(badgeCountBroadcast);

    }


    private void init() {
        int labelColor = getResources().getColor(R.color.greenish_blue);
        сolorString = String.format("%X", labelColor).substring(2);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.clearFocus();

        if ((General.getBadgeCount(this, AppConstants.HDROOMS_COUNT_UV) <= 0) || General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
            rentalCount.setVisibility(View.GONE);
            resaleCount.setVisibility(View.GONE);
        } else {
            if(General.getSharedPreferences(this,AppConstants.Card_TT).equalsIgnoreCase("LL")) {
                rentalCount.setVisibility(View.VISIBLE);
                rentalCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.HDROOMS_COUNT_UV)));
            }else{
                resaleCount.setVisibility(View.VISIBLE);
                resaleCount.setText(String.valueOf(General.getBadgeCount(this, AppConstants.HDROOMS_COUNT_UV)));
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query.trim();
                Log.i(TAG, "1111111111");
                //search(searchQuery);
                /*if(default_deals != null)
                    default_deals.clear();
                if(listBrokerDeals_new != null)
                    listBrokerDeals_new.clear();*/
                // loadDefaultDeals();
                // loadBrokerDeals();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText.trim();
                search(searchQuery);
                Log.i(TAG, "newText " + searchQuery);


                return true;
            }


        });


        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        //if user is logged in then make phase seek bar visible, view is already made GONE from layout, on safer side we will still make it gone initially programatically

        phaseSeekBar.setVisibility(View.GONE);

        // if (!General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).isEmpty()) {
        phaseSeekBar.setVisibility(View.VISIBLE);

        // }


        General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);

        // mPhasedSeekBar = (CustomPhasedSeekBar) findViewById(R.id.phasedSeekBar);
        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{getBaseContext().getResources().getString(R.string.Rental), getBaseContext().getResources().getString(R.string.Resale)
                }));

        mCustomPhasedSeekbar.setListener((this));
        if (total_deals == null) {
            total_deals = new ArrayList<BrokerDeals>();
        } else {
            total_deals.clear();
        }


        listAdapter = new BrokerDealsListAdapter(total_deals, getApplicationContext());
        listViewDeals.setAdapter(listAdapter);

        listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.i("TRACE", "default deals adapter clicked" + position);


                BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);
                AppConstants.CLIENT_DEAL_FLAG = true;
                Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                intent.putExtra("userRole", "client");
                intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                intent.putExtra(AppConstants.NAME, brokerDeals.getName());
                Log.i("TRACE", "ment" + AppConstants.OK_ID);

                startActivity(intent);
            }
        });

        Log.i("Phaseseekbar", "oncreate value sign " + General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER));
        if (General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")) {
            signUpCard.setVisibility(View.VISIBLE);
            if(General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
            signUpCardText.setText("Sign up to get more leads to create New DEALs with more Clients");
            else
                signUpCardText.setText("Sign up to broadcast your needs to create New DEALs with more Brokers");


        }

        //  if (!RefreshDrooms) {
        default_deals = new ArrayList<BrokerDeals>();


        default_dealsLL = new ArrayList<BrokerDeals>();
        default_dealsOR = new ArrayList<BrokerDeals>();
        listBrokerDealsLL = new ArrayList<BrokerDeals>();
        listBrokerDealsOR = new ArrayList<BrokerDeals>();
        listBrokerDeals_new = new ArrayList<BrokerDeals>();
        unverifiedLL = new ArrayList<BrokerDeals>();
        unverifiedOR = new ArrayList<BrokerDeals>();
        if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
            loadDefaultDealsNew();
        if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker") && General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){

        }
        else {
            loadCachedDeals();
            loadBrokerDeals();
        }

        // }
        setSupportActionBar(mToolbar);
       /* if (!(General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER)).equalsIgnoreCase("")) {

            getSupportActionBar().setTitle(Html.fromHtml(String.format("DEALING ROOMs <font color=\"#%s\">(Rental)</font>", сolorString)));

        } else*/
            getSupportActionBar().setTitle(Html.fromHtml(String.format("DEALING ROOMs <font color=\"#%s\">(Rental)</font>", сolorString)));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*private void dismissProgressBar() {
        *//*if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();*//*
        *//*progressBar.pauseAnimation();
        progressBar.setVisibility(View.GONE);*//*
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (signUpCardFlag) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container1)).commit();
            signUpCardFlag = false;
        } else {
//        if(AppConstants.SIGNUP_FLAG){
//            if(AppConstants.REGISTERING_FLAG){}else{
//            getSupportFragmentManager().popBackStackImmediate();
//            Intent inten = new Intent(this, ClientDealsListActivity.class);
//            startActivity(inten);
//            finish();
//            AppConstants.SIGNUP_FLAG=false;}
//
//        }else {
            Log.i(TAG, "onback client deal " + General.getSharedPreferences(this, AppConstants.ROLE_OF_USER));
            Intent intent;
            if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                intent = new Intent(this, BrokerMainActivity.class);
            } else {
                intent = new Intent(this, ClientMainActivity.class);
            }
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

//        }
    }


    private void deleteDealingroom(String deleteOyeId, String deleteOKId, final String specCode) {
        if (General.isNetworkAvailable(this)) {
            General.slowInternet(this);
            deleteDroomDb(deleteOKId);
            Log.i(TAG, "wadala default deals 3 " + deleteOKId);

            deleteHDroom deleteHDroom = new deleteHDroom();
            deleteHDroom.setOkId(deleteOKId);
            deleteHDroom.setDeleteOyeId(deleteOyeId);
            if (!General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase(""))
                deleteHDroom.setUserId(General.getSharedPreferences(this, AppConstants.USER_ID));
            else
                deleteHDroom.setUserId(General.getSharedPreferences(this, AppConstants.TIME_STAMP_IN_MILLI));
            deleteHDroom.setPage("1");
            deleteHDroom.setGcmId(General.getSharedPreferences(this, AppConstants.GCM_ID));

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL)
                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


            try {
                oyeokApiService.deleteHDroom(deleteHDroom, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.i("deleteDR CALLED", "delete hdroom success");

                        SnackbarManager.show(
                                Snackbar.with(ClientDealsListActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .text(specCode + " deleted")
                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));


                        JsonObject k = jsonElement.getAsJsonObject();
                        try {
                            JSONObject ne = new JSONObject(k.toString());
                            Log.i("deleteDR CALLED", "sdelete hdroom succes " + ne);
                            String success = ne.getString("success");


                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            Log.i("deleteDR CALLED", "delete hdroom Failed " + e.getMessage());
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        } else {

            General.internetConnectivityMsg(this);
        }

    }

    private void loadDefaultDealsNew() {
        Log.i(TAG, "load default deals called ");

        if (defaultOkIds != null)
            defaultOkIds.clear();
        else
            defaultOkIds = new ArrayList<String>();

        if (default_deals == null) {
            default_deals = new ArrayList<BrokerDeals>();
        } else {
            default_deals.clear();
        }
        if (default_dealsLL == null) {
            default_dealsLL = new ArrayList<BrokerDeals>();
        } else {
            default_dealsLL.clear();
        }
        if (default_dealsOR == null) {
            default_dealsOR = new ArrayList<BrokerDeals>();
        } else {
            default_dealsOR.clear();
        }
        Realm myRealm = General.realmconfig(this);

        try {


            // listAdapter = new BrokerDealsListAdapter(cachedDeals, getApplicationContext());
            Log.i(TAG, "until defaultDeals called 2");
            // listViewDeals.setAdapter(listAdapter);
            Log.i(TAG, "until defaultDeals called 3");
            RealmResults<DefaultDeals> results1 =
                    myRealm.where(DefaultDeals.class).findAll();

            Log.i(TAG, "until defaultDeals called 4 " + results1);

            for (DefaultDeals c : results1) {
                Log.i(TAG, "until defaultDeals ro 1 " + c.getSpec_code());
                Log.i(TAG, "until defaultDeals ro 2 " + c.getOk_id());
                Log.i(TAG, "until defaultDeals ro 3 " + c.getLocality());
                defaultOkIds.add(c.getOk_id());
                Log.i(TAG, "locality is the r " + c.getLocality());

                BrokerDeals dealsa = new BrokerDeals(General.getSharedPreferences(this, AppConstants.NAME), c.getOk_id(), c.getSpec_code(), c.getLocality(), c.getOk_id(), c.getLastSeen(), true);
                Log.i(TAG, "happy " + c.getSpec_code().toLowerCase().contains("-ll"));
                if (c.getSpec_code().toLowerCase().contains("-ll")) {


                    default_dealsLL.add(dealsa);


                } else {
                    default_dealsOR.add(dealsa);
                }
                Log.i(TAG, "default deals are ro" + default_dealsLL);
                Log.i(TAG, "default deals are ro" + default_deals);


            }
            default_deals.addAll(default_dealsLL);

            total_deals.addAll(default_deals);
            showBgText();
            Collections.sort(total_deals);
            listAdapter.notifyDataSetChanged();




           /* if (default_deals != null) {


                Log.i("inside adapter ", "object " + listAdapter);

                listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        Log.i("TRACE", "default deals adapter clicked" + position);


                        BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);
                        AppConstants.CLIENT_DEAL_FLAG = true;
                        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                        intent.putExtra("userRole", "client");
                        intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                        intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                        Log.i("TRACE", "ment" + AppConstants.OK_ID);

                        startActivity(intent);
                    }
                });


            }*/


        } catch (Exception e) {
            Log.i(TAG, "Caught in the exception reading defaultdeals from realm " + e);
        } finally {

            Log.i(TAG, "finally loaddefaultDeals ");
        }
    }


    private void loadBrokerDeals() {
        if (General.isNetworkAvailable(this)) {
            General.slowInternet(this);
            Log.i("TRACE", "in Load broker deals================= " + General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));

            // String defaultOK = "{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"Searching for brokers\"}],\"for_oks\":[]}";
            // Log.i("TRACE","DefailtOK" +defaultOK);
            // JSONObject jsonObj = new JSONObject("{\"for_oyes\":[{\"loc\":[72.8312300000001,19.1630000000001],\"ok_id\":\"szimjqcufrd784371\",\"time\":[\"2016\",\"4\",\"10\",\"8\",\"24\",\"28\"],\"oye_id\":\"3xd6amo1245617\",\"ok_user_id\":\"krve2cnz03rc1hfi06upjpnoh9hrrtsy\",\"name\":\"Shlok M\",\"mobile_no\":\"9769036234\",\"spec_code\":\"LL-200+-15000\"}],\"for_oks\":[]}");


            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            RestAdapter restAdapter = new RestAdapter
                    .Builder()
                    .setEndpoint(AppConstants.SERVER_BASE_URL_102)
                    .setConverter(new GsonConverter(gson))

                    .build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            String deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            //params

            HdRooms hdRooms = new HdRooms();
            if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equals("broker"))
                hdRooms.setUserRole("broker");
            else
                hdRooms.setUserRole("client");
            if (!General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equals(""))
                hdRooms.setUserId(General.getSharedPreferences(getApplicationContext(), AppConstants.USER_ID));
            else
                hdRooms.setUserId(General.getSharedPreferences(getApplicationContext(), AppConstants.TIME_STAMP_IN_MILLI));
            hdRooms.setGcmId(SharedPrefs.getString(getApplicationContext(), SharedPrefs.MY_GCM_ID));
            hdRooms.setLat("123456789");
            hdRooms.setLon("123456789");
            hdRooms.setDeviceId(deviceId);
            hdRooms.setPage("1");


            Log.i("TRACE", "in Load broker deals ");
            OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
            oyeokApiService.seeHdRooms(hdRooms, new Callback<PublishLetsOye>() {
                @Override
                protected Object clone() throws CloneNotSupportedException {
                    return super.clone();
                }

                @Override
                public void success(PublishLetsOye letsOye, Response response) {
                    General.slowInternetFlag = false;
                    General.t.interrupt();

                    Log.i("TRACE", "in successs " + General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.TIME_STAMP_IN_MILLI));
                    String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                    Log.i(TAG, "tidin tidin tindin 1 " + strResponse);
                    try {
                        JSONObject jsonObjectServer = new JSONObject(strResponse);
                        Log.i(TAG, "tidin tidin tindin 2" + jsonObjectServer);
                        if (jsonObjectServer.getBoolean("success")) {
                            JSONObject jsonObjectResponseData = new JSONObject(jsonObjectServer.getString("responseData"));
                            Log.i(TAG, "tidin tidin tindin 3 " + jsonObjectResponseData);
                            Log.i("TRACE", "jsonObjectResponseData" + jsonObjectResponseData);
                            Log.d("CHATTRACE", "default drooms" + jsonObjectResponseData);


//                        JSONObject jsonObjectResponseData1 = new JSONObject(jsonObjectResponseData.getString("for_oyes"));
//
//                        for (int i = 0; i < jsonObjectResponseData1.length(); i++) {
//                            jsonObjectResponseData1.getJSONObject()
//                        }


                            Gson gsonForOks = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                            ArrayList<BrokerDeals> listBrokerDeals;
                            if (General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                                listBrokerDeals = (ArrayList<BrokerDeals>)
                                        gsonForOks.fromJson(jsonObjectResponseData.getString("for_oks"),
                                                new TypeToken<ArrayList<BrokerDeals>>() {
                                                }.getType());
                            } else {
                                listBrokerDeals = (ArrayList<BrokerDeals>)
                                        gsonForOks.fromJson(jsonObjectResponseData.getString("for_oyes"),
                                                new TypeToken<ArrayList<BrokerDeals>>() {
                                                }.getType());
                            }


                            Log.i("TRACE", "list broker deals" + listBrokerDeals.isEmpty());
                            if (!listBrokerDeals.isEmpty()) {
                                myRealm = General.realmconfig(ClientDealsListActivity.this);
                                myRealm.beginTransaction();
                                RealmResults<HalfDeals> h = myRealm.where(HalfDeals.class).findAll();
                                 h.clear();
                                Iterator<BrokerDeals> it = listBrokerDeals.iterator();




                                while (it.hasNext()) {
                                    BrokerDeals deals = it.next();

                                    if (deals.getOkId() != null) {
                                        Log.i("TRACE", "dhishoom timestamp 1 " + deals.getLastSeen());
                                        if (deals.getLastSeen().equalsIgnoreCase("default"))
                                            deals.setLastSeen("1464922983000");

                                        Log.i("TRACE", "dhishoom hdroomstatus 22 " + deals.getOkId());
                                        Log.i("TRACE", "dhishoom hdroomstatus 22 " + deals.getHDroomStatus().getSelfStatus());
                                        Log.i("TRACE", "dhishoom hdroomstatus other " + deals.getHDroomStatus().getOtherStatus());
                                        Log.i("TRACE", "dhishoom hdroomstatus other ok user id " + deals.getOkUserId());
                                        Log.i("TRACE", "dhishoom hdroomstatus other ok user id 1 " + deals.getOyeUserId());

                                        if (General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                                            if (defaultOkIds.contains(deals.getOkId())) {
                                                if (matchedOkIds == null)
                                                    matchedOkIds = new ArrayList<String>();
                                                matchedOkIds.add(deals.getOkId());

                                            }
                                        }


                                        halfDeals = new HalfDeals();
                                        halfDeals.setOyeId(deals.getOyeId());
                                        halfDeals.setOk_id(deals.getOkId());
                                        halfDeals.setName(deals.getName());
                                        halfDeals.setLocality(deals.getLocality());
                                        halfDeals.setSpec_code(deals.getSpecCode());
                                        halfDeals.setSelfStatus(deals.getHDroomStatus().getSelfStatus());
                                        halfDeals.setOtherStatus(deals.getHDroomStatus().getOtherStatus());
                                        if (General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("client"))
                                            halfDeals.setOkUserId(deals.getOkUserId());
                                        else
                                            halfDeals.setOkUserId(deals.getOyeUserId());
                                        halfDeals.setLastSeen(deals.getLastSeen());
                                        myRealm.copyToRealmOrUpdate(halfDeals);

                                        Log.i("kabali","unverified_user"+General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.ROLE_OF_USER));
                                        if (deals.getOyeId().contains("unverified_user") ) {

                                            if (deals.getSpecCode().toLowerCase().contains("-ll"))
                                                unverifiedLL.add(deals);
                                            else
                                                unverifiedOR.add(deals);
                                        } else {

                                            if (deals.getSpecCode().toLowerCase().contains("-ll"))
                                                listBrokerDealsLL.add(deals);
                                            else
                                                listBrokerDealsOR.add(deals);
                                        }
                                    }


                                }
                                myRealm.commitTransaction();

                                Log.i("TRACE", "dhishoom unverifiedOR " + unverifiedOR);
                                if (General.getSharedPreferences(ClientDealsListActivity.this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
                                    if (matchedOkIds != null)
                                        deleteDefaultDeals();
                                }
                                Log.i("TRACE", "dhishoom unverifiedLL " + unverifiedLL);
                                Log.i("TRACE", "dhishoom listBrokerDealsLL " + listBrokerDealsLL);
                                Log.i("TRACE", "dhishoom total_deals " + total_deals);
                                total_deals.removeAll(cachedDealsLL);

                                listBrokerDeals_new.addAll(unverifiedLL);
                                listBrokerDeals_new.addAll(listBrokerDealsLL);
                                total_deals.addAll(listBrokerDeals_new);

                                Log.i(TAG, "listbrokerdeals loaded are " + listBrokerDeals_new);
                                showBgText();
                                Collections.sort(total_deals);
                                listAdapter.notifyDataSetChanged();





                                /*if(RefreshDrooms) {

                                    Log.i("Shine", "Drooms refreshed");
                                    listViewDeals.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                    //listViewDeals.setAdapter(null);
                                    //listViewDeals.setAdapter(listAdapter);
                                }else {

                                    Log.i("Shine", "Drooms not refreshed");*/


                                   /* listViewDeals.setAdapter(listAdapter);*/

                                    /*listViewDeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                                            Log.i("TRACE", "main deals adapter clicked" + position);


                                            final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                                            String json = gson.toJson(AppConstants.letsOye);

                                            Log.d(TAG, "AppConstants.letsOye " + json);
                                            Log.i("TRACE", "AppConstants.letsOye " + json);


                                            BrokerDeals brokerDeals = (BrokerDeals) adapterView.getAdapter().getItem(position);
                                            Log.i("getSpecCode","getSpecCode yo 2 "+brokerDeals.getSpecCode());
                                            AppConstants.CLIENT_DEAL_FLAG = true;
                                            Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
                                            intent.putExtra("userRole", "client");
                                            intent.putExtra(AppConstants.OK_ID, brokerDeals.getOkId());
                                            intent.putExtra(AppConstants.OYE_ID, brokerDeals.getOyeId());
                                            intent.putExtra(AppConstants.SPEC_CODE, brokerDeals.getSpecCode());
                                            intent.putExtra(AppConstants.LOCALITY, brokerDeals.getLocality());
                                            intent.putExtra("isDefaultDeal",brokerDeals.getdefaultDeal());
                                            Log.i("TRACE DEALS FLAG 2", "FLAG " + brokerDeals.getOyeId());
                                            startActivity(intent);
                                        }
                                    });
*/

                                // }


                            }
                        }
                    } catch (Exception e) {
                        Log.i("TRACE", "Caught in exception in loadbrokerdeals " + e);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    General.slowInternetFlag = false;
                    General.t.interrupt();

                    Log.i("TRACE", "in failure " + error);
                }
            });

        } else {

            General.internetConnectivityMsg(this);
        }
    }

    private void deleteDefaultDeals() {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        deals = General.getDefaultDeals(this);

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, String> deals1 = gson.fromJson(deals, type);

        Iterator<Map.Entry<String, String>> iter = deals1.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            Log.d(TAG, "entry.getKey" + entry.getKey());
            if (matchedOkIds.contains(entry.getKey())) {
                iter.remove();

            }
        }

        Log.i(TAG, "after deal " + deals1);

        Gson g = new Gson();
        String hashMapString = g.toJson(deals1);
        General.saveDefaultDeals(this, hashMapString);


        try {
            for (String okId : matchedOkIds) {
                Realm myRealm = General.realmconfig(this);
                myRealm.beginTransaction();
                RealmResults<DefaultDeals> result = myRealm.where(DefaultDeals.class).equalTo(AppConstants.OK_ID, okId).findAll();


                result.clear();
                RefreshDrooms = true;
                myRealm.commitTransaction();

                Iterator<BrokerDeals> it = default_deals.iterator();

                while (it.hasNext()) {
                    BrokerDeals deals = it.next();

                    Log.i("TRACE==", "deals.are" + deals);
                    Log.i("TRACE==", "deals.ok_id" + deals.getOkId());
                    Log.i(TAG, "default deals before delete " + default_deals);
                    Log.i(TAG, "default deals before delete 3 " + okId);
                    Log.i(TAG, "default deals before delete 4 " + deals.getOkId());
                    if (deals.getOkId().equalsIgnoreCase(okId)) {
                        Log.i(TAG, "default deals before delete 5 ");
                        default_deals.remove(deals);
                        if (default_dealsLL.contains(deals))
                            default_dealsLL.remove(deals);
                        else
                            default_dealsOR.remove(deals);
                        total_deals.remove(deals);
                        listAdapter.notifyDataSetChanged();

                        Log.i(TAG, "default deals before delete 2 " + default_deals);
                    }
                }

            }
        } catch (Exception e) {
            Log.i(TAG, "caught in exception deleting default droom " + e);
        }

    }


    /*private void displayTextMessage(String message) {
        if (message == null)
            message = getString(R.string.no_active_deal);
        txtNoActiveDeal.setText(message);
        txtNoActiveDeal.setVisibility(View.VISIBLE);
        listViewDeals.setVisibility(View.GONE);
    }
    private void displayListView() {
        txtNoActiveDeal.setVisibility(View.GONE);
        listViewDeals.setVisibility(View.VISIBLE);
    }*/
//
//    @OnClick(R.id.dealItemRoot)
//    public void onClickDealItemRoot(View v) {
//        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
//        intent.putExtra("userRole", "client");
//        intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
//        startActivity(intent);
//    }

    @OnClick(R.id.signUp)
    public void onClickSignUp(View v) {


        SignUpFragment d = new SignUpFragment();
        Bundle bundle = new Bundle();
        if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
            bundle.putString("lastFragment", "brokerDeal");
        else//consider as direct signup so keep last fragment as clientDrawer
            bundle.putString("lastFragment", "clientDeal");
        d.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);

        fragmentTransaction.addToBackStack("cardSignUp");
        fragment_container1.setVisibility(View.VISIBLE);
        fragmentTransaction.replace(R.id.fragment_container1, d);
        signUpCardFlag = true;
        fragmentTransaction.commitAllowingStateLoss();


    }


    @OnClick(R.id.dealItemRoot)
    public void onClickDealItemRoot(View v) {

        

        Log.i("USER_ID", " " + General.getSharedPreferences(this, AppConstants.USER_ID).isEmpty());


        Intent intent = new Intent(getApplicationContext(), DealConversationActivity.class);
        if(General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("client")) {
            intent.putExtra("userRole", "client");
            AppConstants.CLIENT_DEAL_FLAG = true;
        }else{
            intent.putExtra("userRole", "broker");
            AppConstants.BROKER_DEAL_FLAG = true;
        }

        intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
        startActivity(intent);

    }


    private final BroadcastReceiver handlePushNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            // Update list here and refresh listview using adapter.notifyDataSetChanged();

            RefreshDrooms = intent.getExtras().getBoolean("RefreshDrooms");

            Log.i("TRACE", "refreshdrooms is set" + RefreshDrooms);
            Log.i("Shine", "RefreshDrooms is " + RefreshDrooms);
            Log.i("Shine", "1");


            //loadDefaultDeals();
            //RefreshDrooms = false;

            //loadBrokerDeals();

            // ************  // default deal is created so remove it from totaldeals and notify adapter

            Toast.makeText(context, "We have just assigned a broker to your request.", Toast.LENGTH_LONG).show();

        }
    };


    @Override
    protected void onDestroy() {

        Log.i("SHINE3", "dystroyed");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(handlePushNewMessage);
        super.onDestroy();

    }


    /// phase seekbar
    @Override
    public void onPositionSelected(int position, int count) {
Log.i("this","this is role "+General.getSharedPreferences(this,AppConstants.ROLE_OF_USER));
        if (position == 0) {

            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RENTAL);
            TT = "LL";
            if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker") && General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
                Log.i("this","this is role 234 "+General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase(""));
            }else {
                total_deals.clear();
                default_deals.clear();
                default_deals.addAll(default_dealsLL);
                total_deals.addAll(default_deals);

                if (listBrokerDeals_new.isEmpty() && unverifiedLL.isEmpty()) {
                    cachedDeals.clear();
                    cachedDeals.addAll(cachedDealsLL);
                    total_deals.addAll(cachedDeals);
                } else {
                    listBrokerDeals_new.clear();
                    listBrokerDeals_new.addAll(unverifiedLL);
                    listBrokerDeals_new.addAll(listBrokerDealsLL);
                    total_deals.addAll(listBrokerDeals_new);
                }
                Collections.sort(total_deals);
                listAdapter.notifyDataSetChanged();
                if (searchQuery != null)
                    search(searchQuery);
            }


            showBgText();



            getSupportActionBar().setTitle(Html.fromHtml(String.format("DEALING ROOMs <font color=\"#%s\">(Rental)</font>", сolorString)));
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text("Rental Deals Type set")
                            .position(Snackbar.SnackbarPosition.TOP)
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), this);

            Log.i("kabali","kabali role bata : "+General.getSharedPreferences(getBaseContext(),AppConstants.ROLE_OF_USER));

            if (General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") && (General.getBadgeCount(this, AppConstants.HDROOMS_COUNT_UV) > 0)) {
                if(General.getSharedPreferences(this,AppConstants.Card_TT).equalsIgnoreCase("LL")) {
                    General.setBadgeCount(this, AppConstants.HDROOMS_COUNT_UV, 0);
                    rentalCount.setVisibility(View.GONE);
                }}

        } else {


            General.setSharedPreferences(this, AppConstants.TT, AppConstants.RESALE);
            TT = "OR";
            if (General.getSharedPreferences(this, AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker") && General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")){
                Log.i("this","this is role 234 "+General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase(""));


            }else {
                total_deals.clear();
                default_deals.clear();
                default_deals.addAll(default_dealsOR);
                total_deals.addAll(default_deals);

                if (listBrokerDeals_new.isEmpty() && unverifiedOR.isEmpty()) { //
                    cachedDeals.clear();
                    cachedDeals.addAll(cachedDealsOR);
                    total_deals.addAll(cachedDeals);
                } else {
                    listBrokerDeals_new.clear();
                    listBrokerDeals_new.addAll(unverifiedOR);
                    listBrokerDeals_new.addAll(listBrokerDealsOR);
                    total_deals.addAll(listBrokerDeals_new);
                }
                Collections.sort(total_deals);
                listAdapter.notifyDataSetChanged();
                if (searchQuery != null)
                    search(searchQuery);
            }


                showBgText();


            showBgText();
            SnackbarManager.show(
                    Snackbar.with(this)
                            .text("Buy/Sell Deal Type set")
                            .position(Snackbar.SnackbarPosition.TOP)
                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), this);
            getSupportActionBar().setTitle(Html.fromHtml(String.format("DEALING ROOMs <font color=\"#%s\">(Buy/Sell)</font>", сolorString)));

            if (General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("client") && (General.getBadgeCount(this, AppConstants.HDROOMS_COUNT_UV) > 0)) {
                if(General.getSharedPreferences(this,AppConstants.Card_TT).equalsIgnoreCase("OR")) {
                    General.setBadgeCount(this, AppConstants.HDROOMS_COUNT_UV, 0);
                    resaleCount.setVisibility(View.GONE);
                }}
        }


        //General.setSharedPreferences(this, AppConstants.TT, TT);

        Log.i(TAG, "PHASED seekbar current onPositionSelected" + position + " " + " count " + count + " " + General.getSharedPreferences(this, "TT"));


    }

    private void networkConnectivity() {
        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text("INTERNET CONNECTIVITY NOT AVAILABLE")
                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
    }


    private void loadCachedDeals() {

        if (cachedDeals != null) {
            cachedDeals.clear();
        } else
            cachedDeals = new ArrayList<BrokerDeals>();
        if (cachedDealsLL != null) {
            cachedDealsLL.clear();
        } else
            cachedDealsLL = new ArrayList<BrokerDeals>();
        if (cachedDealsOR != null) {
            cachedDealsOR.clear();
        } else
            cachedDealsOR = new ArrayList<BrokerDeals>();

        Realm myRealm = General.realmconfig(this);

        try {


            // listAdapter = new BrokerDealsListAdapter(cachedDeals, getApplicationContext());
            Log.i(TAG, "until loadCachedDeals called 2");
            // listViewDeals.setAdapter(listAdapter);
            Log.i(TAG, "until loadCachedDeals called 3");
            RealmResults<HalfDeals> results1 =
                    myRealm.where(HalfDeals.class).findAll();

            Log.i(TAG, "until loadCachedDeals called 4 " + results1);

            for (HalfDeals c : results1) {
                Log.i(TAG, "until loadCachedDeals ");
                Log.i(TAG, "until loadCachedDeals " + c.getOk_id());
                Log.i(TAG, "until loadCachedDeals " + c.getName());
                Log.i(TAG, "until loadCachedDeals " + c.getLocality());
                Log.i(TAG, "until loadCachedDeals " + c.getSelfStatus());
                Log.i(TAG, "until loadCachedDeals " + c.getOtherStatus());
                Log.i(TAG, "until loadCachedDeals " + c.getOkUserId());
                Log.i(TAG, "until loadCachedDeals " + c.getLastSeen());


                BrokerDeals dealsa = new BrokerDeals(c.getName(), c.getOk_id(), c.getSpec_code(), c.getLocality(), c.getOyeId(), c.getSelfStatus(), c.getOtherStatus(), c.getOkUserId(), c.getLastSeen(), true);


                Log.i(TAG, "robosasa 1 " + dealsa);
                if (c.getSpec_code().toLowerCase().contains("ll-") || c.getSpec_code().toLowerCase().contains("-ll")) {
                    Log.i(TAG, "robosasa " + dealsa.getSpecCode());
                    cachedDealsLL.add(dealsa);
                } else if (c.getSpec_code().toLowerCase().contains("or-") || c.getSpec_code().toLowerCase().contains("-or")) {
                    Log.i(TAG, "robosasa " + dealsa.getSpecCode());
                    cachedDealsOR.add(dealsa);
                }


            }

            cachedDeals.addAll(cachedDealsLL);

            total_deals.addAll(cachedDeals);
            showBgText();
            Collections.sort(total_deals);
            listAdapter.notifyDataSetChanged();
            //setCachedDeals();

        } catch (Exception e) {
            Log.i(TAG, "Caught in the exception reading cache from realm " + e);
        } finally {

            Log.i(TAG, "finally loadCachedDeals ");
        }
    }


    private void deleteDroomDb(String okId) {

        try {
            Realm myRealm = General.realmconfig(this);

            //clear cache
            Log.i(TAG, "until 3 ");
            myRealm.beginTransaction();
            Log.i(TAG, "until 4 ");
            RealmResults<HalfDeals> result = myRealm.where(HalfDeals.class).equalTo(AppConstants.OK_ID, okId).findAll();
            Log.i(TAG, "until result to del is 6 " + result);
            result.clear();

        } catch (Exception e) {
            Log.i(TAG, "Caught in the exception clearing cache " + e);
        } finally {
            myRealm.commitTransaction();
        }


    }

    private void showBgText() {
        Log.i(TAG, "inside show bg text ");
        if (total_deals.size() < 3 && showbgtext == true && !General.getSharedPreferences(this, AppConstants.IS_LOGGED_IN_USER).equalsIgnoreCase("")) {
            Log.i(TAG, "inside show bg text total_deals 5" + total_deals.size());

            bgtxt.setVisibility(View.VISIBLE);
            if(General.getSharedPreferences(this,AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
            bgtxt.setText("Go Back & Ok more leads to create New DEALs with more Clients");
            else
                bgtxt.setText("Go Back & Broadcast yours needs to create New DEALs with more Brokers");
        } else {
            bgtxt.setVisibility(View.GONE);
            Log.i(TAG, "inside show bg text total_deals else" + total_deals.size());
        }
    }

    private void search(String searchQuery) {
        Log.i(TAG, "deals deals searchQuery " + searchQuery);

        total_deals.clear();
        if (TT.equalsIgnoreCase("LL")) {
            total_deals.addAll(default_dealsLL);
            total_deals.addAll(unverifiedLL);
            total_deals.addAll(listBrokerDealsLL);
        } else {
            total_deals.addAll(default_dealsOR);
            total_deals.addAll(unverifiedOR);
            total_deals.addAll(listBrokerDealsOR);
        }


        if (searchQuery != null) {

            copy = new ArrayList<BrokerDeals>();
            copy.addAll(total_deals);
            Log.i(TAG, "deals deals searchQuery copy " + copy);
            Iterator<BrokerDeals> it = copy.iterator();
            while (it.hasNext()) {
                BrokerDeals deals = it.next();

                if (deals.getOkId() != null) {


                    String searchString = "";
                    if (deals.getSpecCode() != "") {
                        searchString = searchString + " " + deals.getSpecCode();
                    }
                    if (deals.getName() != "") {
                        searchString = searchString + " " + deals.getName();
                    }
                    if (deals.getLocality() != "") {
                        searchString = searchString + " " + deals.getLocality();
                    }


                    if (!searchString.toLowerCase().contains(searchQuery.toLowerCase())) {
                        total_deals.remove(deals);

                    } else {
                        Log.i(TAG, "deals deals " + deals.getSpecCode());
                    }

                }
            }
            listAdapter.notifyDataSetChanged();

        } else {
            listAdapter.notifyDataSetChanged();
        }
    }


    private void updateNewLastseen(String okId) {

        if (!total_deals.isEmpty()) {
            Iterator<BrokerDeals> it = total_deals.iterator();


            myRealm = General.realmconfig(ClientDealsListActivity.this);
            myRealm.beginTransaction();

            while (it.hasNext()) {
                BrokerDeals deals = it.next();

                if (deals.getOkId().equalsIgnoreCase(okId)) {
                    deals.setLastSeen(String.valueOf(System.currentTimeMillis()));
                }


            }
        }

        try{
            if(myRealm.isInTransaction())
                myRealm.cancelTransaction();
            Realm myRealm = General.realmconfig(this);
            HalfDeals deal = myRealm.where(HalfDeals.class).equalTo(AppConstants.OK_ID, okId).findFirst();
            if(deal != null){

                myRealm.beginTransaction();
                deal.setLastSeen(String.valueOf(System.currentTimeMillis()));
                myRealm.commitTransaction();

            }

        }
        catch(Exception e){
Log.i(TAG,"Caught in exception narcos "+e);
        }
    }

}