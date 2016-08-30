package com.nbourses.oyeok.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.ChatList;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.GooglePlacesApiServices.GooglePlacesReadTask;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.GetPrice;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.interfaces.OnOyeClick;
import com.nbourses.oyeok.widgets.HorizontalPicker.HorizontalPicker;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.skyfishjy.library.RippleBackground;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static java.lang.Math.log10;


public class DashboardClientFragment extends Fragment implements CustomPhasedListener,AdapterView.OnItemClickListener, ChatList, HorizontalPicker.pickerPriceSelected, FragmentDrawer.MDrawerListener {


    //GoogleMap.OnCameraChangeListener,TouchableWrapper.UpdateMapAfterUserInterection
    private final String TAG = DashboardClientFragment.class.getSimpleName();
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final String[] CAMERA_PERMS = {
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS = {
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    static  int count1 =0;

    private static final int REQUEST_CALL_PHONE = 1;

    View mHelperView;
Button home,shop,industrial,office;
    String Property_type="",oyetext="";
    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private static final int MAP_ZOOM = 14;
    private Point point;
    DBHelper dbHelper;
    //    private TextView mDrooms;
    private TextView mVisits;
    private ImageView mQrCode;
    private LinearLayout mMarkerPanel;
    private Timer timer;
    private RelativeLayout mMarkerminmax;
    private GoogleMap map;
    private LinearLayout ll_marker;
    private ImageView Mmarker;
    private ImageView search_building_icon;
    private BitmapDescriptor icon1;
    private BitmapDescriptor icon2;

    long then;
    long now;
    private Thread r;
    private RelativeLayout wrapper;

//    private Drawable icon1;
//      private Drawable icon2;

    private boolean flag[] = new boolean[5], RatePanel = false;


    private long lastTouched = 0, start = 0;
    private static final long SCROLL_TIME = 200L;




    /*private GeoFence geoFence;*/

    private int permissionCheckForCamera, permissionCheckForLocation;
    private final int MY_PERMISSION_FOR_CAMERA = 11;
    private CustomPhasedSeekBar mPhasedSeekBar;
    Drawable marker = null;
    Marker[] mCustomerMarker = new Marker[5];
    String brokerType;
    private Geocoder geocoder;
    private GetCurrentLocation.CurrentLocationCallback mcallback;
    private FrameLayout ll_map;
    String pincode, region, fullAddress;
    Double lat, lng;
    ClientMainActivity dashboardActivity;
    DroomChatFirebase droomChatFirebase;
    private   MapView mapView;
    private GetCurrentLocation getLocationActivity;
    //View rootView;
    HashMap<String, HashMap<String, String>> chatListData;


    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "", fullAddres = "";
    AutoCompleteTextView autoCompView;
    private RelativeLayout errorView;
    private TextView errorText;
    private HorizontalPicker horizontalPicker;
    private TextView tvRate;
    private TextView rupeesymbol;
    private OnOyeClick onOyeClick;
    //private TextView tvCommingsoon;
    private TextView tv_building;
    private TextView tvFetchingrates;
    CustomMapFragment customMapFragment;
    GPSTracker gpsTracker;
    static int x, y;
    static int top, bottom, left, right, width, height, truncate_first;
    private int llMin=35, llMax=60, orMin=21000, orMax=27000;
    private String name, text;
    private String[] config=new String[5];

    private static int count = 0;
    private static final String ischeck = "true";
    private String filterValue;
    private String bhk;
    private int filterValueMultiplier = 950;
TextView rental,resale;
    RelativeLayout property_type_layout;
    LinearLayout dispProperty;
    private int countertut;
    private int[] or_psf = new int[5], ll_pm = new int[5];
    private LatLng loc;
    private ImageView myLoc,ic_search;
    LinearLayout recordWorkout;
    boolean clicked = true;
    private String address;
    private LinearLayout seekbar_linearlayout;
    private RelativeLayout hPicker;
    private FrameLayout hideOnSearch;
    private Boolean autoc = false;
    private Boolean autocomplete = false;
  private  static  View  rootView;
    private Boolean spanning = false,autoIsClicked=false,pro_click=false;
    RelativeLayout parenttop,parentbottom;
    Animation zoomout_right, slide_up, zoomout_left, ani, zoomin_zoomout;
//    Intent intent ;

    private RelativeLayout topView;

    private String Walkthrough, permission, beacon;
    AutoCompleteTextView inputSearch;
    private int INDEX;
//    @Bind(R.id.seekbar_linearlayout)
//    LinearLayout seekbarLinearLayout;

    @Bind(R.id.missingArea)
    RelativeLayout missingArea;

    @Bind(R.id.txtFilterValue)
    TextView txtFilterValue;

    @Bind(R.id.dateTime)
    TextView dateTime;

    @Bind(R.id.copyright)
    TextView copyright;

//    @Bind(R.id.hPicker)
//    LinearLayout hPicker;

    ValueAnimator mFlipAnimator;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Boolean buildingTouched = false;


    private BroadcastReceiver autoComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                if (intent.getExtras().getBoolean("autocomplete") == true) {
                    // autocomplete = true;
                    Log.i(TAG, "hohohoh 2");
                    hideOnSearch.setVisibility(View.GONE);
                    seekbar_linearlayout.setVisibility(View.VISIBLE);
                    seekbar_linearlayout.setAlpha(1f);
                    mPhasedSeekBar.setVisibility(View.VISIBLE);
//                    property_type_layout.setVisibility(View.VISIBLE);
                    dispProperty.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {}
        }

    };


//    private BroadcastReceiver oncheckWalkthrough=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("walkthrough","walkthrough1"+intent.getExtras().getString("checkWalkthrough"));
//            if(intent.getExtras().getBoolean("checkWalkthrough")==true){
//                Log.i("walkthrough","walkthrough2"+intent.getExtras().getBoolean("checkWalkthrough"));
//               // Walkthrough=intent.getExtras().getString("checkWalkthrough");
//
//                Log.i("walkthrough","walkthrough3"+Walkthrough);
//
//            }
//
//        }
//    };

    private BroadcastReceiver onFilterValueUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {

                if ((intent.getExtras().getString("filterValue") != null)) {
                    // txtFilterValue.setText(Html.fromHtml(intent.getExtras().getString("filterValue")));
                    Log.i("filtervalue", "filtervalue " + intent.getExtras().getString("filterValue"));
                    txtFilterValue.setText(intent.getExtras().getString("filterValue"));

                }
                if ((intent.getExtras().getString("filterValue") != null)) {
                    // txtFilterValue.setText(intent.getExtras().getString("filterValue"));
                    bhk = intent.getExtras().getString("bhk");
                    Log.i("bhk", "=================  " + bhk);
                    //filterValue =  intent.getExtras().getString("filterValue").toString();

                    if (bhk.equalsIgnoreCase("1bhk") || bhk.equalsIgnoreCase("<600")) {
                        filterValueMultiplier = 600;
                        updateHorizontalPicker();

                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            if (bhk.equalsIgnoreCase("1bhk"))
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/month");
                            else
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");

//                        BroadCastMinMaxValue(llMin*filterValueMultiplier,llMax*filterValueMultiplier,orMin,orMax);
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");

                    } else if (bhk.equalsIgnoreCase("2bhk") || bhk.equalsIgnoreCase("<950")) {

                        filterValueMultiplier = 950;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            if (bhk.equalsIgnoreCase("2bhk"))
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/month");
                            else
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");
                    } else if (bhk.equalsIgnoreCase("3bhk") || bhk.equalsIgnoreCase("<1600")) {
                        filterValueMultiplier = 1600;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            if (bhk.equalsIgnoreCase("3bhk"))
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/month");
                            else
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");
                    } else if (bhk.equalsIgnoreCase("4bhk") || bhk.equalsIgnoreCase("<2100")) {
                        filterValueMultiplier = 2100;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            if (bhk.equalsIgnoreCase("4bhk"))
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/month");
                            else
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");
                    } else if (bhk.equalsIgnoreCase("4+bhk") || bhk.equalsIgnoreCase("<3000")) {
                        filterValueMultiplier = 3000;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            if (bhk.equalsIgnoreCase("4+bhk"))
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/month");
                            else
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");

                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");
                    } else if (bhk.equalsIgnoreCase("<300") || bhk.equalsIgnoreCase("default")) {
                        filterValueMultiplier = 300;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");
                    }

///// check if required
//                 try{
//                // txtFilterValue.setText(Html.fromHtml(intent.getExtras().getString("filterValue")));
//             }catch (Exception e){
//               ///  Log.i("txtFilterValue","txtFilterValue: "+ intent.getExtras().getString("filterValue"));
//
                }
            }
        }
    };



    public void setOyeButtonClickListener(OnOyeClick onOyeClick) {
        this.onOyeClick = onOyeClick;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);

        ButterKnife.bind(this, rootView);


        //  gpsTracker = new GPSTracker(getContext());
//        myLoc = (ImageView) rootView.findViewById(R.id.myLoc);
        hideOnSearch = (FrameLayout) rootView.findViewById(R.id.hideOnSearch);
        seekbar_linearlayout = (LinearLayout) rootView.findViewById(R.id.seekbar_linearlayout);
        topView = (RelativeLayout) rootView.findViewById(R.id.top);
        parentbottom=(RelativeLayout) rootView.findViewById(R.id.top);
        parenttop=(RelativeLayout) rootView.findViewById(R.id.parent);
//        hPicker = (RelativeLayout) rootView.findViewById(R.id.hPicker);
        // View locationButton = suppormanagerObj.getView().findViewById(2);
        if (General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI).equals("")) {
            General.setSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI, String.valueOf(System.currentTimeMillis()));
            Log.i("TIMESTAMP", "millis " + System.currentTimeMillis());
        }


       init();


        Log.i("notifications", "sendNotification ==========================" + SharedPrefs.getString(getContext(), SharedPrefs.MY_GCM_ID));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));


// Permission Request

        if (SharedPrefs.getString(getContext(), SharedPrefs.PERMISSION).equalsIgnoreCase("")) {

            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            permission = "true";

            SharedPrefs.save(getContext(), SharedPrefs.PERMISSION, "false");
            Log.i("checked", "permission" + permission);

        } else {
            permission = SharedPrefs.getString(getContext(), SharedPrefs.PERMISSION);

            Log.i("checked", "permission" + permission);
        }

        if (permission.equalsIgnoreCase("true")) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
            SharedPrefs.save(getContext(), SharedPrefs.PERMISSION, "false");
        }


        droomChatFirebase = new DroomChatFirebase(DatabaseConstants.firebaseUrl, getActivity());
//        mDrooms = (TextView) rootView.findViewById(R.id.linearlayout_drooms);
        mVisits = (TextView) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

        mMarkerminmax = (RelativeLayout) rootView.findViewById(R.id.markerpanelminmax);
        ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        recordWorkout = (LinearLayout) rootView.findViewById(R.id.recordWorkout);
        ic_search = (ImageView) rootView.findViewById(R.id.ic_search);



        walkBeaconStatus();
        /*if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON).equalsIgnoreCase("")) {
            beacon = "true";
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
        } else {
            beacon = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON);
            // SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
            Log.i("ischecked", "walkthrough3dashboard" + beacon);
        }

        if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("")) {
            Walkthrough = "true";
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_WALKTHROUGH, "false");
        } else {
            Walkthrough = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH);
            Log.i("ischecked", "walkthrough3dashboard" + Walkthrough);
        }*/

        Mmarker = (ImageView) rootView.findViewById(R.id.Mmarker);


        try {//buildingiconbeforeclick

            icon1 = BitmapDescriptorFactory.fromResource(R.drawable.buildingiconbeforeclick);
            icon2 = BitmapDescriptorFactory.fromResource(R.drawable.buildingicononclick);

        } catch (Exception e) {
            Log.i("BITMAP", "message " + e.getMessage());
        }
        recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
        //selected_property = BitmapDescriptorFactory.fromResource(R.drawable.search_building_icon);
        search_building_icon = (ImageView) rootView.findViewById(R.id.selected_property);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Mmarker.getViewTreeObserver().removeGlobalOnLayoutListener(this);


                int[] locations = new int[2];
                Mmarker.getLocationOnScreen(locations);
                x = locations[0] + 37;
                y = locations[1] -170;
//                x = left - (right - left) / 2;
//                y = bottom;
//                bottom = Mmarker.getBottom();
//                top = Mmarker.getTop();
//                left = Mmarker.getLeft();
//                right = Mmarker.getRight();
//                width = Mmarker.getMeasuredWidth();
//                height = Mmarker.getMeasuredHeight();
                Log.i("t1", "Bottom" + Mmarker.getBottom() + "top" + top + "left" + left + "right" + right);
                Log.i("t1", "width" + width + "height " + height);
                point = new Point(x, y);

                Log.i("t1", "co-ordinate" + x + " " + y);
            }
        });

        dashboardActivity = (ClientMainActivity) getActivity();

        wrapper = (RelativeLayout) dashboardActivity.findViewById(R.id.wrapper);

        dbHelper = new DBHelper(getContext());
        ll_map = (FrameLayout) rootView.findViewById(R.id.ll_map);


        permissionCheckForLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
//        errorView = (RelativeLayout) rootView.findViewById(R.id.alertLayout);
//        errorText = (TextView) rootView.findViewById(R.id.errorText);

        rupeesymbol = (TextView) rootView.findViewById(R.id.rupeesymbol);
        // tvCommingsoon = (TextView) rootView.findViewById(R.id.tvCommingsoon);
        tvRate = (TextView) rootView.findViewById(R.id.tvRate);
        tvFetchingrates = (TextView) rootView.findViewById(R.id.tvFetchingRates);
        tv_building = (TextView) rootView.findViewById(R.id.tv_building);
        Button CallButton=(Button) rootView.findViewById(R.id.CallButton);

        //search_building_icon.setVisibility(View.INVISIBLE);
        buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
        onPositionSelected(0, 2);

        horizontalPicker = (HorizontalPicker) rootView.findViewById(R.id.picker);
        horizontalPicker.setMpicker(this);
        horizontalPicker.setTvRate(tvRate, rupeesymbol);
        horizontalPicker.setSelectedItem(2);
        horizontalPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        horizontalPicker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            @Override
            public void onItemSelected(int index) {
                horizontalPicker.addValues(index);
            }
        });


        // Call Button
        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+912233836068"));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);

                String callPermission = Manifest.permission.CALL_PHONE;
                int hasPermission = ContextCompat.checkSelfPermission(getActivity(), callPermission);
                String[] permissions = new String[] { callPermission };
                if(isTelephonyEnabled()) {
                    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, REQUEST_CALL_PHONE);
                        startActivity(callIntent);
                    } else {
                        startActivity(callIntent);
                    }
                }

            }
        });



        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);
        if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null"))
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector}, new String[]{"30", "15"}, new String[]{getContext().getResources().getString(R.string.Rental), getContext().getResources().getString(R.string.Resale)}));
        else
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        mPhasedSeekBar.setListener(this);


        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompView.performClick();
            }
        });


        autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch);
        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));
        autoCompView.setOnItemClickListener(this);
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(autoCompView.getWindowToken(), 0);
        //autoCompView.setOnItemClickListener(this);
        autoCompView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    autoIsClicked=true;
                    autoCompView.setCursorVisible(true);
                    autoCompView.clearListSelection();
                    autoCompView.setText("");
                    autoIsClicked=true;
                    autoCompView.showDropDown();
                    // new LocationUpdater().execute();
                    property_type_layout.setVisibility(View.GONE);
                    dispProperty.setVisibility(View.GONE);

                    hideOnSearch.setVisibility(View.VISIBLE);
                    //seekbar_linearlayout.setVisibility(View.GONE);
                    mPhasedSeekBar.setVisibility(View.VISIBLE);
                    mPhasedSeekBar.setClickable(false);
                    seekbar_linearlayout.setVisibility(View.INVISIBLE);
                    seekbar_linearlayout.setBackgroundColor(Color.TRANSPARENT);

                    //seekbar_linearlayout.setBackgroundColor(getResources().getColor(R.color.gray));
                    seekbar_linearlayout.setAlpha(0.8f);
                    Intent intent = new Intent(AppConstants.AUTOCOMPLETEFLAG);
                    intent.putExtra("autocomplete", true);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    autoc = true;
                    Intent intent11 = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent11);

                    //  ll_map.setAlpha(0.5f);
                    //hideOnSearch.setAlpha(0.5f);
                } catch (Exception e) {
                }


            }
        });




        /*mDrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open deals listing
                Intent openDealsListing = new Intent(getActivity(), ClientDealsListActivity.class);
                startActivity(openDealsListing);
            }
        });*/

        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnOyeClick();
              /*  openOyeScreen();
                CancelAnimation();

                if (clicked == true) {
                    oyebuttonBackgrountColorOrange();
                    clicked = false;
                    customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
                    mHelperView.setEnabled(false);
                } else {
                    oyebuttonBackgrountColorGreenishblue();
                    customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
                    mHelperView.setEnabled(true);
                    clicked = true;

                }
                if (RatePanel == true) {
                    UpdateRatePanel();
                    RatePanel = false;
                } else {
                    RatePanel = true;
                    // tvFetchingrates.setVisibility(View.VISIBLE);
                }*/


//                openOyeScreen();
//                CancelAnimation();


            }
        });

//        mMarkerPanel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });


        mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
        txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));


        try {
            //if (isNetworkAvailable())

            customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
//            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
            final View mMapView = getChildFragmentManager().findFragmentById(R.id.map).getView();
            // mapView =(MapView) rootView.findViewById(R.id.map);
            map = customMapFragment.getMap();
            map.getUiSettings().setRotateGesturesEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setScrollGesturesEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

//            resetMyPositionButton();
            // geoFence = new GeoFence();
            //if (isNetworkAvailable()) {
            mHelperView = rootView.findViewById(R.id.helperView);


                mHelperView.setOnTouchListener(new View.OnTouchListener() {
                    private float scaleFactor = 1f;

                    @Override
                    public boolean onTouch(final View view, final MotionEvent motionEvent) {

                        if (simpleGestureDetector.onTouchEvent(motionEvent)) { // Double tap
                            map.animateCamera(CameraUpdateFactory.zoomIn()); // Fixed zoom in
                        } else if (motionEvent.getPointerCount() == 1) { // Single tap
                            // horizontalPicker.keepScrolling();
                            onMapDrag(motionEvent);
                            mMapView.dispatchTouchEvent(motionEvent); // Propagate the event to the map (Pan)
//                        onMapDrag(motionEvent);
                        } else if (scaleGestureDetector.onTouchEvent(motionEvent)) { // Pinch zoom
                            spanning = true;
                            map.moveCamera(CameraUpdateFactory.zoomBy( // Zoom the map without panning it
                                    (map.getCameraPosition().zoom * scaleFactor
                                            - map.getCameraPosition().zoom) / 5));
                        }

                        return true; // Consume all the gestures
                    }

                    // Gesture detector to manage double tap gestures
                    private GestureDetector simpleGestureDetector = new GestureDetector(
                            getContext(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            return true;
                        }
                    });

                    // Gesture detector to manage scale gestures
                    private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
                            getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                        @Override
                        public boolean onScale(ScaleGestureDetector detector) {
                            scaleFactor = detector.getScaleFactor();
                            return true;
                        }
                    });



                });



            if (map != null) {
                //if ((int) Build.VERSION.SDK_INT <= 23) {


                customMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        map = googleMap;

                        // map = googleMap;
                        final LocationManager Loc_manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                        if (!isNetworkAvailable() || !(Loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                            map = googleMap;
                            double lat11 = 19.1269299;
                            double lng11 = 72.8376545999999;
                            Log.i("slsl", "location====================:1 ");
                            LatLng currLatLong = new LatLng(lat11, lng11);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLong, 14));
                        }

                        enableMyLocation();
                        Log.i("slsl", "location====================: ");
                        getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);
                        // map.setPadding(left, top, right, bottom);
                        map.setPadding(0, -10, 0, 0);


                    }
                });

            }

            // }


            //}

            map.getUiSettings().setZoomGesturesEnabled(true);


            //}

            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Log.i(TAG, "my Loc clicked ");


                    new CountDownTimer(200, 50) {

                        public void onTick(long millisUntilFinished) {
                            ( (ClientMainActivity)getActivity()).closeOyeScreen();
                            buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                            recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
                            mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                            txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                            UpdateRatePanel();
                            search_building_icon.setVisibility(View.GONE);
                        }

                        public void onFinish() {

                            getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);


                        }
                    }.start();

                    return false;

                }
            });

//            myLoc.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    Log.i(TAG,"my Loc clicked "+event);
//                    // enableMyLocation();
//                     getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
////                    if (isNetworkAvailable()) {
////                        new LocationUpdater().execute();
//                    buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier);
////                    }
//
//                    return false;
//                }
//            });


            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    Log.i("MA999999 ", "MAP CLICK=========");

                    onMapclicked();
                   /* spanning = false;
                    mVisits.setEnabled(true);
                    txtFilterValue.setEnabled(true);

                    for(int i=0;i<5;i++){
                        if(flag[i]==true){
                            mCustomerMarker[i].setIcon(icon1);
                            search_building_icon.setVisibility(View.GONE);
                            flag[i] = false;
                            horizontalPicker.setVisibility(View.VISIBLE);
                            tvFetchingrates.setVisibility(View.GONE);
                            recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                            tvRate.setVisibility(View.VISIBLE);
                            buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                            rupeesymbol.setVisibility(View.VISIBLE);
                            mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.asset_oye_symbol_icon));
                            txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.deal_circle));
                            Intent in = new Intent(AppConstants.MARKERSELECTED);
                            in.putExtra("markerClicked", "false");
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
                            ll_marker.setEnabled(true);
                            tv_building.setVisibility(View.VISIBLE);
                        }
                    }*/




//            UpdateRatePanel();
//            if(clicked==true){
//                oyebuttonBackgrountColorOrange();
//                clicked=false;
//            }else {
//            if(clicked==false){
//                oyebuttonBackgrountColorGreenishblue();
//                clicked = true;
//            }
//            Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
//            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

                }


            });
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Marker m;
                    //intent =new Intent(getContext(), ClientMainActivity.class);
                    int i;

                    for (i = 0; i < 5; i++) {
                        if (marker.getId().equals(mCustomerMarker[i].getId())) {
                            INDEX = i;
                            if (flag[i] == false) {
                                Log.i("flag[i] == false ", "===========================");
//                                mCustomerMarker[i].setIcon(icon2);
                                ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                                ((ClientMainActivity)getActivity()).OpenBuildingOyeConfirmation();
                                m=mCustomerMarker[i];
                                mCustomerMarker[i].remove();
                                mCustomerMarker[i]=  map.addMarker(new MarkerOptions().position(m.getPosition()).title(m.getTitle()).icon(icon2));
                                search_building_icon.setVisibility(View.VISIBLE);
                                horizontalPicker.setVisibility(View.GONE);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                tvRate.setVisibility(View.GONE);
                                rupeesymbol.setVisibility(View.GONE);
                                recordWorkout.setBackgroundColor(Color.parseColor("#ff9f1c"));
                                mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
                                txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
                                ll_marker.setEnabled(false);
                                mVisits.setEnabled(false);
                                txtFilterValue.setEnabled(false);
                                CancelAnimation();
                                Intent in = new Intent(AppConstants.MARKERSELECTED);
                                in.putExtra("markerClicked", "true");
                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
//                                Log.i("coming soon", "coming soon :" + marker.getTitle().toString());
                                tv_building.setVisibility(View.VISIBLE);
                                tv_building.setText("Average Rate in last 1 WEEK");

                                if(brokerType.equalsIgnoreCase("rent")) {
                                    String text = "<font color=#ffffff >" + marker.getTitle().toString() + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#ff9f1c>\u20B9 " + General.currencyFormat(String.valueOf(ll_pm[i])).substring(2, General.currencyFormat(String.valueOf(ll_pm[i])).length()) + "</font><b><font color=#ff9f1c><sub>/m</sub></font></br>";
                                    tvFetchingrates.setText(Html.fromHtml(text));
                                }else {
                                    String text = "<font color=#ffffff >" + marker.getTitle().toString() + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#ff9f1c>\u20B9 " + General.currencyFormat(String.valueOf(ll_pm[i])).substring(2, General.currencyFormat(String.valueOf(ll_pm[i])).length()) + "</font><b><font color=#ff9f1c><sub>/sq.ft</sub></font></br>";
                                    tvFetchingrates.setText(Html.fromHtml(text));
                                }
                                tvFetchingrates.setTypeface(null, Typeface.BOLD);

                                //intent.putExtra("client_heading", "Live Building Rates");

                                lng = marker.getPosition().longitude;
                                lat = marker.getPosition().latitude;
                                Log.i("marker lat", "==============marker position :" + marker.getPosition() + " " + lat + " " + lng);

                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                                General.setSharedPreferences(getContext(), AppConstants.MY_LAT, lat + "");
                                General.setSharedPreferences(getContext(), AppConstants.MY_LNG, lng + "");
                                new LocationUpdater().execute();
                                flag[i] = true;

                            } else {
                                ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                                m=mCustomerMarker[i];
                                mCustomerMarker[i].remove();
                                mCustomerMarker[i]=  map.addMarker(new MarkerOptions().position(m.getPosition()).title(m.getTitle()).icon(icon1));
                                Log.i("mm_mithai","marker draw");
//                                mCustomerMarker[i].setIcon(icon1);
                                search_building_icon.setVisibility(View.GONE);
                                flag[i] = false;
                                horizontalPicker.setVisibility(View.VISIBLE);
                                tvFetchingrates.setVisibility(View.GONE);
                                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));

                                mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                                txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                                mVisits.setEnabled(true);
                                txtFilterValue.setEnabled(true);
                                ll_marker.setEnabled(true);
//                                StartAnimation();
                                //intent.putExtra("client_heading", "Live Region Rates");
                                Intent in = new Intent(AppConstants.MARKERSELECTED);
                                in.putExtra("markerClicked", "false");
                                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
//                                Log.i("coming soon", "coming soon :" + marker.getTitle().toString() + recordWorkout);

                                tvRate.setVisibility(View.VISIBLE);
                                rupeesymbol.setVisibility(View.VISIBLE);
                                tv_building.setVisibility(View.VISIBLE);
//                                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);

                                //tv_building.setText("Average Rate @ this Locality");

                            }
                        } else {
                            mCustomerMarker[i].setIcon(icon1);
                        }
                    }

                    return true;
                }
            });


        } catch (Exception e) {
        }


        try {
            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {



                }
            });

        } catch (Exception e) {}






        mcallback = new GetCurrentLocation.CurrentLocationCallback() {

            @Override
            public void onComplete(Location location) {
                if (location != null) {
                    Log.i("Exception11", "inside mcallback");
                    lat = location.getLatitude();


                    lng = location.getLongitude();
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                    General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
                    General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");
                    if (isNetworkAvailable()) {
                        try {
                            getRegion();
                            buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),filterValueMultiplier);
                            new LocationUpdater().execute();

                        } catch (Exception e) {
                            Log.i("Exception", "caught in get region");
                        }
                    }

                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


                   // map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,MAP_ZOOM));


                    //make retrofit call to get Min Max price
                    if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null") && isNetworkAvailable()) {
                        try {

                            Log.i("Network available","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                            getPrice();
                        } catch (Exception e) {}
                    }
                }
            }
        };
        Log.i("t2", "mcallback" + mcallback);


       /* if (!dbHelper.getValue(DatabaseConstants.userId).equalsIgnoreCase("null"))
            droomChatFirebase.getDroomList(dbHelper.getValue(DatabaseConstants.userId), getActivity());

            */

        dbHelper.save(DatabaseConstants.userRole, "Client");

        rupeesymbol.bringToFront();
        tvRate.bringToFront();
//        ll_marker.bringToFront();
        // txtFilterValue.setText(Html.fromHtml(getResources().getString(R.string.default_2_bhk)));
        txtFilterValue.setText("2BHK");


        /**
         * animate views
         */
        mFlipAnimator = ValueAnimator.ofFloat(0f, 1f);
//        mFlipAnimator.addUpdateListener(new FlipListener(txtFilterValue,mVisits));

//        StartAnimation();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFlipped()) {
//                                Log.i("mFlipAnimator","is flipped");
//                                mFlipAnimator.reverse();
//                            } else {
//                                Log.i("mFlipAnimator","is flipped not");
//                                mFlipAnimator.start();
//                            }
//                        }
//                    });
//                }
//            }
//        }, 2000, 2000);





//for(int i=1;i<10;i++) {
//    ImageView image1 = (ImageView) rootView.findViewById(R.id.beacons1);
//    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade);
//    // animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);
//    image1.startAnimation(animation);
//

//}



        if(Walkthrough.equalsIgnoreCase("true")) {
            Log.i("ischecked","walkthrough3dashboard1111111"+Walkthrough);
            tutorialAlert(rootView);

            Walkthrough="false";

        }

        else if(beacon.equalsIgnoreCase("true") ) {
            Log.i("ischecked","walkthrough3dashboard1111111beacon"+beacon);
            try {
                beaconAlert(rootView);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            beacon="false";

        }





        return rootView;
    }






    private void init() {
        home = (Button) rootView.findViewById(R.id.home);
        shop = (Button) rootView.findViewById(R.id.shop);
        industrial = (Button) rootView.findViewById(R.id.industrial);
        office = (Button) rootView.findViewById(R.id.office);

        rental = (TextView) rootView.findViewById(R.id.rental);
        resale = (TextView) rootView.findViewById(R.id.sale);
        property_type_layout = (RelativeLayout) rootView.findViewById(R.id.property_type);
        dispProperty=(LinearLayout) rootView.findViewById(R.id.property_type_layout);
        zoomout_right = (AnimationUtils.loadAnimation(getContext(), R.anim.zoomout_slide));
        zoomout_left = (AnimationUtils.loadAnimation(getContext(), R.anim.zoomout_slide_left));
        slide_up = (AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
        zoomin_zoomout = (AnimationUtils.loadAnimation(getContext(), R.anim.zoomout_zoomin));
        PropertyButtonSlideAnimation();
        AppConstants.PROPERTY = "Home";

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                Property_type = "Home";
                oyetext = "2BHK";
                AppConstants.PROPERTY = "Home";
                Log.i("home", "you are in home ");
                parentbottom.removeView(property_type_layout);
                parenttop.addView(property_type_layout,2);
                PropertyButtonAnimation();
                property_type_layout.setVisibility(View.GONE);

            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                parentbottom.removeView(property_type_layout);
                parenttop.addView(property_type_layout,2);
                Property_type = "Shop";
                oyetext = "SHOP";
                AppConstants.PROPERTY = "Shop";
                Log.i("home", "you are in shop ");
                PropertyButtonAnimation();
                property_type_layout.setVisibility(View.GONE);

            }
        });
        industrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                parentbottom.removeView(property_type_layout);
                parenttop.addView(property_type_layout,2);
                Property_type = "Industrial";
                oyetext = "INDUS";
                AppConstants.PROPERTY = "Industrial";
                Log.i("home", "you are in industrial ");
                PropertyButtonAnimation();
                property_type_layout.setVisibility(View.GONE);

            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                parentbottom.removeView(property_type_layout);
                parenttop.addView(property_type_layout,2);
                Log.i("home", "you are in office ");
                Property_type = "Office";
                AppConstants.PROPERTY = "Office";
                oyetext = "OFFIC";
                PropertyButtonAnimation();
                property_type_layout.setVisibility(View.GONE);
            }
        });
    }





    public void screenShot()
    {
        dateTime.setText(DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
        dateTime.setVisibility(View.VISIBLE);
        copyright.setVisibility(View.VISIBLE);
        Log.i(TAG,"persy 123");
        GoogleMap.SnapshotReadyCallback callback= new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                try {

                    int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);


                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        Log.i(TAG,"persy 12345");
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE
                        );
                    }

                    topView.setDrawingCacheEnabled(true);
                    Bitmap backBitmap = topView.getDrawingCache();
                    Bitmap bmOverlay = Bitmap.createBitmap(
                            backBitmap.getWidth(), backBitmap.getHeight(),
                            backBitmap.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(bitmap, new Matrix(), null);
                    canvas.drawBitmap(backBitmap, 0, 0, null);

                    File imageFile = new File(
                            Environment.getExternalStorageDirectory()
                                    + "/MapScreenShot"
                                    + System.currentTimeMillis() + ".png");

                    FileOutputStream out = new FileOutputStream(imageFile);

                   bmOverlay.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                    openScreenshot(imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {

                    dateTime.setVisibility(View.GONE);
                    copyright.setVisibility(View.GONE);
                }
            }
        };

        map.snapshot(callback);
    }


    private void openScreenshot(File imageFile) {
        Log.i(TAG,"persy 1234");
        int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"persy 12345");
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        Log.i(TAG,"persy 12346");

        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg/text/html");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        //intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Hey, please check out these property rates I found out on this super amazing app Oyeok.</p><p><a href=\"https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/\">Download Oyeok for android</a></p>"));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, please check out these property rates I found out on this super amazing app Oyeok. \n \n  https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/");
        startActivity(Intent.createChooser(intent, "Share Image"));
    }



    private BroadcastReceiver closeOyeScreenSlide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.i("inside notification","=======");
            UpdateRatePanel();

            ll_map.setAlpha(1f);
//            StartAnimation();
            if(clicked==false){
                oyebuttonBackgrountColorGreenishblue();
                customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
                mHelperView.setEnabled(true);
                clicked=true;
            }

            if(RatePanel==true) {
                UpdateRatePanel();
                RatePanel = false;
            }
            else {
                RatePanel = true;
                // tvFetchingrates.setVisibility(View.VISIBLE);
            }
            // Change color of flip oye button

        }
    };
    private void StartAnimation()
    {
        Log.i("starting timer"," "+ timer);
        if(timer == null) {
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFlipped()) {
                                    mFlipAnimator.reverse();
                                } else {
                                    mFlipAnimator.start();
                                }
                            }
                        });
                    }
                }
            }, 2000, 2000);

        }
    }

    private  void CancelAnimation()
    {
        try {
            if (timer != null) {
                timer.cancel();

                timer = null;

//        if (isFlipped()) {
                mFlipAnimator.start();
//        }
            }
        }catch(Exception e){}
    }




    public void enableMyLocation() {
        Log.i("slsl","loc+++++++++++++++++++ : ");
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (map != null) {
            // Access to the location has been granted to the app.
//            LocationManager lm;
//            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getL astKnownLocation(LocationManager.GPS_PROVIDER);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
            map.setMyLocationEnabled(true);
        }
    }

    @OnClick({R.id.ll_marker})
    public void onButtonsClick(View v) {
        if (ll_marker.getId() == v.getId()) {

           // txtFilterValue.performClick();
            OnOyeClick();
           /* openOyeScreen();
            Log.i("txtFilterValue","txtFilterValue =========================== "+SystemClock.currentThreadTimeMillis());
            CancelAnimation();
            if(clicked==true){
                oyebuttonBackgrountColorOrange();
                customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
                clicked=false;
            }else {
                oyebuttonBackgrountColorGreenishblue();
                customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
                clicked = true;
            }
            Log.i("rate","rate penal1111  : "+RatePanel);
            if(RatePanel==true) {
                Log.i("rate","rate penal  : "+RatePanel);

                UpdateRatePanel();
                RatePanel = false;
            }
            else {
                RatePanel = true;
                // tvFetchingrates.setVisibility(View.VISIBLE);
            }*/
        }
    }

    @OnClick(R.id.txtFilterValue)
    public void onTxtFilterValueClick(View v) {


        OnOyeClick();
        /*openOyeScreen();
        Log.i("txtFilterValue","txtFilterValue =========================== "+SystemClock.currentThreadTimeMillis());
        CancelAnimation();
        if(clicked==true){
            oyebuttonBackgrountColorOrange();
            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
            mHelperView.setEnabled(false);
            clicked=false;
        }else {
            oyebuttonBackgrountColorGreenishblue();
            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
            mHelperView.setEnabled(true);
            clicked = true;
        }
        if(RatePanel==true) {
            UpdateRatePanel();
            RatePanel = false;
        }
        else {
            RatePanel = true;
            // tvFetchingrates.setVisibility(View.VISIBLE);
        }*/
    }

    private void openOyeScreen() {



//        if(android.os.Build.VERSION.SDK_INT >18) {
//            Log.i("FLipanimator paused", "fipanimator paused");
//            mFlipAnimator.end();
//        }

        Intent intent = new Intent(AppConstants.ON_FILTER_VALUE_UPDATE);
        intent.putExtra("tv_dealinfo","Home ");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        //mFlipAnimator.end();
//        mFlipAnimator.removeAllUpdateListeners();
//        //FlipListener f = new FlipListener(txtFilterValue);
//        mFlipAnimator = ValueAnimator.ofFloat(0f);
//        mFlipAnimator.addUpdateListener(new FlipListener(txtFilterValue));
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFlipped()) {
//                                Log.i("mFlipAnimator","is flipped");
//                                mFlipAnimator.reverse();
//                            } else {
//                                Log.i("mFlipAnimator","is flipped not");
//                                mFlipAnimator.start();
//                            }
//                        }
//                    });
//                }
//            }
//        }, 2000, 2000);


//        mFlipAnimator.end();
//        mFlipAnimator.start();
//        mFlipAnimator = ValueAnimator.ofFloat(0f);
//
//        mFlipAnimator.addUpdateListener(new FlipListener(txtFilterValue, txtFilterValue));
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isFlipped()) {
//                                mFlipAnimator.reverse();
//                            } else {
//                                mFlipAnimator.start();
//                            }
//                        }
//                    });
//                }
//            }
//        }, 2000, 2000);
//        //mFlipAnimator.end();
//        mFlipAnimator.removeAllUpdateListeners();

        Bundle args = new Bundle();
        args.putString("brokerType", brokerType);
        args.putString("Address", SharedPrefs.getString(getActivity(), SharedPrefs.MY_REGION));
        onOyeClick.onClickButton(args);

    }

    private boolean isFlipped() {
        return mFlipAnimator.getAnimatedFraction() == 1;
    }

    @Override
    public void onResume() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onFilterValueUpdate, new IntentFilter(AppConstants.ON_FILTER_VALUE_UPDATE));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(oncheckWalkthrough, new IntentFilter(AppConstants.CHECK_WALKTHROUGH));
        //LocalBroadcastManager.getInstance(getContext()).registerReceiver(oncheckbeacon, new IntentFilter(AppConstants.CHECK_BEACON));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(autoComplete, new IntentFilter(AppConstants.AUTOCOMPLETEFLAG1));

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onFilterValueUpdate);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(closeOyeScreenSlide);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(autoComplete);
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(oncheckWalkthrough);
        // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(oncheckbeacon);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        // getLocationActivity.setCallback(null);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

    private void displayToast(String toast) {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
        }
    }


    private Handler mHandler;
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            hideMap(0);
            errorView.setVisibility(View.GONE);
        }
    };

    private void hideMap(int i) {
        Animation m = null;
        if (isAdded())
            //Load animation
            if (i == 0)
                m = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
            else
                m = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);

        errorView.setAnimation(m);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String toast = null;
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }
            // At this point we may or may not have a reference to the activity
            displayToast(toast);
        }
    }

    public void getPrice() {

        //getRegion();

        if(General.isNetworkAvailable(getContext())) {
            General.slowInternet(getContext());


        mVisits.setEnabled(false);
        txtFilterValue.setEnabled(false);
        CancelAnimation();
        User user = new User();


//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE)
//                == PackageManager.PERMISSION_GRANTED) {
//            Log.i("PREOK", "getcontext " + General.getDeviceId(getContext()));
//            user.setDeviceId(General.getDeviceId(getContext()));
//
//
//        } else {
            // preok.setDeviceId(General.getSharedPreferences(this,AppConstants.));
            user.setDeviceId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
            Log.i("PREOK", "getcontext " + General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));

//        }

            user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
            user.setUserRole("client");
            user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
            user.setProperty_type("home");
            user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
            Log.i("t1", "My_lng" + "  " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
            user.setLocality(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
            Log.i("t1", "My_lat" + "  " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));

            user.setPlatform("android");
            Log.i("my_locality", SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));
            user.setPincode("400058");

            if (General.getSharedPreferences(getContext(), AppConstants.IS_LOGGED_IN_USER).equals("")) {
                user.setUserId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));

            } else {
                user.setUserId(General.getSharedPreferences(getContext(), AppConstants.USER_ID));
                Log.i(TAG, "user_id " + General.getSharedPreferences(getContext(), AppConstants.USER_ID));
            }

            tv_building.setVisibility(View.INVISIBLE);
            horizontalPicker.setVisibility(View.GONE);
            tvRate.setVisibility(View.GONE);
            rupeesymbol.setVisibility(View.GONE);
            tvFetchingrates.setVisibility(View.VISIBLE);
            // tvCommingsoon.setVisibility(View.GONE);
            tvFetchingrates.setText("Fetching Rates....");
            //tvFetchingrates.setm
            //tvCommingsoon.setHeight(18);

            // tvCommingsoon.setTypeface(null, Typeface.BOLD);
            //tvFetchingrates.setTypeface(null, Typeface.ITALIC);
            tvFetchingrates.setTextSize(15);
            //  missingArea.setVisibility(View.VISIBLE);


            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_101).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            UserApiService userApiService = restAdapter.create(UserApiService.class);

            userApiService.getPrice(user, new Callback<GetPrice>() {

                @Override
                public void success(GetPrice getPrice, Response response) {

                    try {
                        General.slowInternetFlag = false;
                        General.t.interrupt();


                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                        // horizontalPicker.stopScrolling();
                        Log.i("TRACE", "Response" + jsonResponseData);
                        if (getPrice.getResponseData().getPrice().getLlMin() != null &&
                                !getPrice.getResponseData().getPrice().getLlMin().equals("")) {

                            Log.i("tt", "I am here price" + getPrice.getResponseData());
                            Log.i("tt", "I am here price" + getPrice.getResponseData().getPrice());
//                       Log.i("tt", "I am here building" + getPrice.getResponseData().getBuildings());
                            if (Integer.parseInt(getPrice.getResponseData().getPrice().getLlMin()) != 0) {
                                Log.i("tt", "I am here" + 2);
                                Log.i("TRACE", "RESPONSEDATAr" + response);
                                llMin = Integer.parseInt(getPrice.getResponseData().getPrice().getLlMin());
                                llMax = Integer.parseInt(getPrice.getResponseData().getPrice().getLlMax());
                                Log.i("TRACE", "RESPONSEDATArr" + llMin);
                                Log.i("TRACE", "RESPONSEDATArr" + llMax);
                                llMin = 5 * (Math.round(llMin / 5));
                                llMax = 5 * (Math.round(llMax / 5));
                                Log.i("TRACE", "RESPONSEDATAr" + llMin);
                                Log.i("TRACE", "RESPONSEDATAr" + llMax);

                                orMin = Integer.parseInt(getPrice.getResponseData().getPrice().getOrMin());
                                orMax = Integer.parseInt(getPrice.getResponseData().getPrice().getOrMax());
                                Log.i("TRACE", "RESPONSEDATArr" + orMin);
                                Log.i("TRACE", "RESPONSEDATArr" + orMax);
                                orMin = 500 * (Math.round(orMin / 500));
                                orMax = 500 * (Math.round(orMax / 500));
                                Log.i("TRACE", "RESPONSEDATAr" + orMin);
                                Log.i("TRACE", "RESPONSEDATAr" + orMax);


                                BroadCastMinMaxValue(llMin, llMax, orMin, orMax);

                                updateHorizontalPicker();
                                marquee(500, 100);

                               /* for (int i = 0; i < 5; i++) {

                                    if (mCustomerMarker[i] != null)
                                        mCustomerMarker[i].remove();
                                }*/
                                map.clear();
                                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));

                                mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                                txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                                search_building_icon.setVisibility(View.GONE);
                                StartOyeButtonAnimation();
                                //if(mflag=false) {


                                for (int i = 0; i < 5; i++) {
                                    config[i] = getPrice.getResponseData().getBuildings().get(i).getConfig();
                                    Log.i("TRACE", "RESPONSEDATAr" + name);
                                    name = getPrice.getResponseData().getBuildings().get(i).getName();
                                    Log.i("TRACE", "RESPONSEDATAr" + name);

                                    or_psf[i] = Integer.parseInt(getPrice.getResponseData().getBuildings().get(i).getOrPsf());
                                    Log.i("TRACE", "RESPONSEDATAr" + or_psf);
                                    ll_pm[i] = Integer.parseInt(getPrice.getResponseData().getBuildings().get(i).getLlPm());

                                    Log.i("TRACE", "RESPONSEDATAr" + ll_pm);
                                    double lat = Double.parseDouble(getPrice.getResponseData().getBuildings().get(i).getLoc().get(1));
                                    Log.i("TRACE", "RESPONSEDATAr" + lat);
                                    double longi = Double.parseDouble(getPrice.getResponseData().getBuildings().get(i).getLoc().get(0));
                                    Log.i("TRACE", "RESPONSEDATAr" + longi);
                                    loc = new LatLng(lat, longi);
                                    Log.i("TRACE", "RESPONSEDATAr" + loc);
                                    Log.i("TRACE", "RESPONSEDATAr" + mCustomerMarker[i]);

                                    mCustomerMarker[i] = map.addMarker(new MarkerOptions().position(loc).title(name).snippet("Rent:" + ll_pm[i] + " " + "Sale" + or_psf[i]).icon(icon1).flat(true));

                                    Log.i("TRACE", "RESPONSEDATAr" + mCustomerMarker[i]);
                                    flag[i] = false;
                                }
                                //mflag=true;

                                // }
                                // updateHorizontalPicker();

                                mVisits.setEnabled(true);
                                txtFilterValue.setEnabled(true);
//                                StartAnimation();
                                horizontalPicker.setVisibility(View.VISIBLE);
                                tv_building.setVisibility(View.VISIBLE);

                                tvRate.setVisibility(View.VISIBLE);
                                rupeesymbol.setVisibility(View.VISIBLE);
                                //  tvCommingsoon.setVisibility(View.INVISIBLE);
                                tvFetchingrates.setVisibility(View.GONE);


                                missingArea.setVisibility(View.GONE);
                            } else {
                                Log.i("tt", "I am here" + 3);
                    /*SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text("We don't cater here yet")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());*/
                                //horizontalPicker.stopScrolling();
//                                for (int i = 0; i < 5; i++) {
//
//                                    if (mCustomerMarker[i] != null)
//                                        mCustomerMarker[i].remove();
//                                }
                                map.clear();
//                                ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                                tv_building.setVisibility(View.INVISIBLE);
                                horizontalPicker.setVisibility(View.GONE);
                                tvRate.setVisibility(View.INVISIBLE);
                                rupeesymbol.setVisibility(View.INVISIBLE);
                                // tvCommingsoon.setVisibility(View.GONE);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                tvFetchingrates.setText("Coming Soon...");
                                // tvCommingsoon.setTypeface(null, Typeface.BOLD);
                                // tvCommingsoon.setTextSize(18);
                                missingArea.setVisibility(View.VISIBLE);
                                mVisits.setEnabled(false);
                                txtFilterValue.setEnabled(false);
                                CancelAnimation();
                                //missingArea.setVisibility(View.VISIBLE);
                            }
                        } else {
                    /*SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text("We don't cater here yet")
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity()); */
                           /* for (int i = 0; i < 5; i++) {

                                if (mCustomerMarker[i] != null)
                                    mCustomerMarker[i].remove();
                            }*/
                            map.clear();
//                            ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                            tv_building.setVisibility(View.INVISIBLE);
                            horizontalPicker.setVisibility(View.GONE);
                            tvRate.setVisibility(View.INVISIBLE);
                            rupeesymbol.setVisibility(View.INVISIBLE);
                            // tvCommingsoon.setVisibility(View.GONE);
                            tvFetchingrates.setVisibility(View.VISIBLE);
                            tvFetchingrates.setText("Coming Soon...");
                            // tvCommingsoon.setTypeface(null, Typeface.BOLD);
                            // tvCommingsoon.setTextSize(18);
                            missingArea.setVisibility(View.VISIBLE);
                            mVisits.setEnabled(false);
                            txtFilterValue.setEnabled(false);
                            CancelAnimation();

                            Log.i("GETPRICE", "Else mode ====== ");



                    /*missingArea.setAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_up));*/
                        }
                    } catch (Exception e) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        Log.i("Price Error", " " + e.getMessage());
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    General.slowInternetFlag = false;

                    General.t.interrupt();
                    Log.i("getPrice", "error: " + error.getMessage());

                }
            });

        }
        else{
            General.internetConnectivityMsg(getContext());

        }
    }


    // map.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener));


    private void updateHorizontalPicker() {


    if (horizontalPicker != null) {
        if (brokerType.equals("rent")) {
            Log.i(TAG, "updateHorizontalPicker rental andro " + llMin + " " + llMax);
            //   horizontalPicker.setInterval((llMin*1000), (llMax*1000),10, HorizontalPicker.THOUSANDS);

            Log.i("HORRIZONTALPICKER", "filterValue " + filterValue + " filterValueMultiplier " + filterValueMultiplier + "  LLmin && LLmax" + llMin + " " + llMax);
            horizontalPicker.setInterval((roundoff1(llMin * filterValueMultiplier)), (roundoff1(llMax * filterValueMultiplier)), 10, HorizontalPicker.THOUSANDS);
        } else {
            Log.i(TAG, "updateHorizontalPicker resale andro " + orMin + " " + orMax);
            horizontalPicker.setInterval(roundoff1(orMin), roundoff1(orMax), 10, HorizontalPicker.THOUSANDS);
        }
    }
}








    public boolean canAccessLocation() {

        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(getContext(),perm));
    }


    //rem


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case MY_PERMISSION_FOR_CAMERA: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    IntentIntegrator.forSupportFragment(DashboardClientFragment.this).setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES).setCaptureActivity(CaptureActivityAnyOrientation.class).setOrientationLocked(false).initiateScan();
                        // permission was granted, y-------------ay! Do the
                        // contacts-related task you need to do.


                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
                case LOCATION_REQUEST:
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        customMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;


                                enableMyLocation();
//                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//

                                getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);
//                                return;
//                            }
//                            map.setMyLocationEnabled(true);
                                //setCameraListener();
                                Log.i("t1", "broker_map" + map);
                                //  geoFence.drawPloygon(map);
                            }

                        });
                        //getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);

                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    break;
                // other 'case' lines to check for other
                // permissions this app might request
            }
            if (canAccessLocation()) {
                new GetCurrentLocation(getActivity(), new GetCurrentLocation.CurrentLocationCallback() {
                    @Override
                    public void onComplete(Location location) {
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            Log.i("t1", "lat_long_getcurrentlocation" + " " + currentLocation);

                            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                            map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

                        }
                        getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
                    }
                });
                // getLocationActivity = new GetCurrentLocation(getActivity(),mcallback);
                //Log.i("t1","mcallback"+""+mcallback);
            }
            else {
                //Intent intent = new Intent(this, MainActivity.class);
                // startActivity(intent);
                Toast.makeText(getContext(), "Offline Mode", Toast.LENGTH_LONG);
                //((DashboardActivity) getActivity()).showToastMessage("Offline Mode");
            }

        }catch (Exception e){}


    }


    @Override
    public void onPositionSelected(int position, int count) {
        if (count == 2) {
            if (position == 0) {


                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                Log.i("indexxx", "index of layout : " + index);
                if(index==2){
                    property_type_layout.clearAnimation();
                    parenttop.removeView(property_type_layout);
                    parentbottom.addView(property_type_layout,5);}

                PropertyButtonSlideAnimation();






                marquee(500, 100);

                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .text("Rental Property Type set")
                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                tvRate.setText("/ month");
                brokerType = "rent";
                AppConstants.CURRENT_DEAL_TYPE="rent";
                dbHelper.save(DatabaseConstants.brokerType, "LL");
                dbHelper.save("brokerType", "On Rent");
                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                if (Property_type.equalsIgnoreCase("")) {
                    rental.setText("Home");
                    rental.setVisibility(View.VISIBLE);
                    resale.setVisibility(View.INVISIBLE);
                    property_type_layout.setVisibility(View.VISIBLE);

                } else {
                    rental.setVisibility(View.VISIBLE);
                    resale.setVisibility(View.INVISIBLE);
                    rental.setText(Property_type);
                    property_type_layout.setVisibility(View.VISIBLE);
                }


                if(flag[INDEX]==true) {

                    tv_building.setVisibility(View.VISIBLE);
                    tv_building.setText("Average Rate in last 1 WEEK");
                    String text = "<font color=#ffffff>"+mCustomerMarker[INDEX].getTitle().toString()+"</b></b></font> <font color=#ffffff>@</font>&nbsp&nbsp<font color=#ff9f1c>\u20B9"+General.currencyFormat(String.valueOf(ll_pm[INDEX])).substring(2,General.currencyFormat(String.valueOf(ll_pm[INDEX])).length())+"</font><b><font color=#ff9f1c><sub>/m</sub></font>";
                    tvFetchingrates.setText(Html.fromHtml(text));

                }


            } else if (position == 1) {


                marquee(500, 100);

                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                Log.i("indexxx", "index of layout : " + index);
                if(index==2){
                    Log.i("indexx","inside if stmt");
                    property_type_layout.clearAnimation();
                    parenttop.removeView(property_type_layout);
                    parentbottom.addView(property_type_layout,5);}

                PropertyButtonSlideAnimation();
                SnackbarManager.show(
                        Snackbar.with(getContext())
                                .text("Buy/Sell Property Type set")
                                .position(Snackbar.SnackbarPosition.TOP)
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));

                updateHorizontalPicker();
                tvRate.setText("/ sq.ft");
                brokerType = "resale";
                AppConstants.CURRENT_DEAL_TYPE = "resale";
                dbHelper.save(DatabaseConstants.brokerType, "OR");
                dbHelper.save("brokerType", "For Sale");

                if (Property_type.equalsIgnoreCase("")) {
                    rental.setText("Home");
                    resale.setVisibility(View.VISIBLE);
                    rental.setVisibility(View.INVISIBLE);
                    property_type_layout.setVisibility(View.VISIBLE);

                } else {
                    resale.setText(Property_type);
                    resale.setVisibility(View.VISIBLE);
                    rental.setVisibility(View.INVISIBLE);
                    property_type_layout.setVisibility(View.VISIBLE);

                }

                if(flag[INDEX]==true) {
                    tv_building.setVisibility(View.VISIBLE);
                    tv_building.setText("Average Rate in last 1 WEEK");
                    String text = "<font color=#ffffff>"+mCustomerMarker[INDEX].getTitle().toString()+"</b></b></font> <font color=#ffffff> @ </font>&nbsp<font color=#ff9f1c>\u20B9"+General.currencyFormat(String.valueOf(or_psf[INDEX])).substring(2,General.currencyFormat(String.valueOf(or_psf[INDEX])).length())+"</font><b><font color=#ff9f1c><sub>/sq.ft</sub></font>";
                    tvFetchingrates.setText(Html.fromHtml(text));
                }
                // onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY),950,orMin,orMax);
                // horizontalPicker.stopScrolling();

//                try {
//                    horizontalPicker.stopScrolling();
//                }catch (Exception e){}

            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // ll_map.setAlpha(1f);
        hideOnSearch.setVisibility(View.GONE);
        seekbar_linearlayout.setVisibility(View.VISIBLE);
//        property_type_layout.setVisibility(View.VISIBLE);
        dispProperty.setVisibility(View.VISIBLE);
        seekbar_linearlayout.setBackgroundColor(Color.WHITE);
        seekbar_linearlayout.setAlpha(1);
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(autoCompView.getWindowToken(), 0);



        mPhasedSeekBar.setVisibility(View.VISIBLE);
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
        autoCompView.clearListSelection();
        autoc = false;
        //rem
        getLocationFromAddress(autoCompView.getText().toString());
        if (isNetworkAvailable()) {
            new LocationUpdater().execute();

        }
    }

    public void getRegion() {
        String lat1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT);
        String lng1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat1), Double.parseDouble(lng1), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            region = addresses.get(0).getSubLocality();
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LOCALITY, region);
            General.setSharedPreferences(getContext(),AppConstants.LOCALITY,region);
            Log.i("localityBroadcast","localityBroadcast3 "+region);

            Intent intent = new Intent(AppConstants.LOCALITY_BROADCAST);
            intent.putExtra("locality",region);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

            pincode = addresses.get(0).getPostalCode();
            address = addresses.get(0).getAddressLine(0);
            Log.i(TAG,"address "+address);
            // fullAddress = "";
        } catch (Exception e) {
        }
//        for(int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
//            fullAddress += addresses.get(0).getAddressLine(i);

//        }

        // SharedPrefs.save(getActivity(), SharedPrefs.MY_REGION, fullAddress);
//        if (addresses.size() > 0) {
//            pincode = addresses.get(0).getPostalCode();
//
//            //if 1st provider does not have data, loop through other providers to find it.
//            int count = 0;
//            while (pincode == null && count < addresses.size()) {
//                pincode = addresses.get(count).getPostalCode();
//                count++;
//            }
//        }
        // return fullAddres;
    }

//    @Override
//    public void onCameraChange(CameraPosition cameraPosition) {
//        if (isNetworkAvailable()) {
//
//            //lat = cameraPosition.target.latitude;
//            // lng = cameraPosition.target.longitude;
//            // LatLng currentLocation1;
//            // Point p= new Point(x,y);
//            // currentLocation1= broker_map.getProjection().fromScreenLocation(p);
//            // lat=currentLocation1.latitude;
//            //Log.i("t1","lat"+" "+lat);
//            // lng=currentLocation1.longitude;
//            Log.i("t1", "lat_target" + lat);
//            Log.i("t1", "lng_target" + lng);
//            //LatLng  currentLocation1= new LatLng(lat, lng);
//            //  broker_map.addMarker(new MarkerOptions().position(currentLocation1).title("marker"));
//            //SharedPrefs.save(getActivity(),SharedPrefs.MY_LAT,lat+"");
//            //SharedPrefs.save(getActivity(),SharedPrefs.MY_LNG,lng+"");
//            // broker_map.addMarker(new MarkerOptions().position(currentLocation1).title("marker"));
//
//            // new LocationUpdater().execute();
//        }
//    }

    //@Override
    public void onPositionSelected(int position) {
        // Toast.makeText(getActivity(), "Selected position:" + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void sendData(HashMap<String, HashMap<String, String>> hashMap) {
        chatListData = hashMap;
        Log.i("chatdata in rex", chatListData.toString());
    }

    @Override
    public void priceSelected(String val) {
        try {
            map.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception Zoom map to 12 "+e);
        }

    }

    @Override
    public void drawerOpened() {
        horizontalPicker.stopScrolling();
    }




    protected class LocationUpdater extends AsyncTask<Double, Double, String> {
        public JSONObject getJSONfromURL(String url) {

            // initialize
            InputStream is = null;
            String result = "";
            JSONObject jObject = null;

            // http post
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

            } catch (Exception e) {
                Log.e(TAG, "Error in http connection " + e.toString());
            }

            // convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e(TAG, "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing data " + e.toString());
            }

            return jObject;
        }

        @Override
        protected String doInBackground(Double[] objects) {

            try {
                String lat1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT);
                String lng1 = SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG);
                JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat1 + ","
                        + lng1 + "&sensor=true");
                Log.i("chai","Response_chai1");
//                JSONObject jsonObj = getJSONfromURL("https://maps.googleapis.com/maps/api/place/queryautocomplete/json?input=Arun%20Ka&scope=APP&key=AIzaSyC7aqVbRyNsF1JNgtYbpPDsJAf981dPp5Q");
                String Status = jsonObj.getString("status");

//                Log.i("chai","Response_chai11"+jsonObj);
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
//                    Log.i("chai","Response1_chai11"+Results);
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    fullAddress = zero.getString("formatted_address");
                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);


                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {
                                Address1 += long_name;

                            } else if (Type.equalsIgnoreCase("route")) {
                                Address1 += " " + long_name;

                            } else if (Type.equalsIgnoreCase("sublocality_level_2")) {
                                Address2 = long_name;

                            } else if (Type.equalsIgnoreCase("sublocality_level_1")) {
                                Address2 += " " + long_name;

                                if (getActivity() != null) {
                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LOCALITY, long_name);
                                    General.setSharedPreferences(getContext(), AppConstants.LOCALITY, region);
                                }
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;

                                SharedPrefs.save(getActivity(), SharedPrefs.MY_CITY, City);
                            } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                                County = long_name;
                            } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                                State = long_name;
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                PIN = long_name;
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_PINCODE, PIN);
                            }
                        }
                        if (getActivity() != null)
                            SharedPrefs.save(getActivity(), SharedPrefs.MY_REGION, fullAddress);
                        // JSONArray mtypes = zero2.getJSONArray("types");
                        // String Type = mtypes.getString(0);
                        // Log.e(Type,long_name);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
          return fullAddress;
          // return address;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            autoCompView.setText(s);
            Log.i("", "");
            autoCompView.dismissDropDown();

            // new LocationUpdater().execute();
            Log.i(TAG,"locality automata ");
            try {



                getRegion();
                if(autoIsClicked==true) {
                    Log.i(TAG, "locality automata " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));

                    getPrice();
                    autoIsClicked=false;
                    buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                }



            }catch(Exception e){}

        }
    }

   public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i("Checking network","====================");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean setCameraListener() {
        //broker_map.setOnCameraChangeListener(this);
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        //GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                //return null;
            }
            Address location = address.get(0);
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.i("lat=" + location.getLatitude(), " long=" + location.getLongitude());
            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("t1", "lng" + " " + lng);
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
            SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
            General.setSharedPreferences(getContext(),AppConstants.MY_LAT,lat + "");
            General.setSharedPreferences(getContext(),AppConstants.MY_LNG,lng + "");


            //Marker marker = broker_map.addMarker(new MarkerOptions()
            //     .position(l)
            //  .title("Title")
            //.snippet("Description")
            //.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), Mmarker))));

            //Marker m = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("I am here!").icon(icon1).anchor(x,y));
            // broker_map.animateCamera(CameraUpdateFactory.newLatLng(l));
            map.moveCamera(CameraUpdateFactory.newLatLng(l));
            // broker_map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM));

            // getPrice();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getContext().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }





    private void BroadCastMinMaxValue(int llMin, int llMax,int orMin,int orMax) {

        int llmin=numToVals(llMin);

        int llmax=numToVals(llMax);
        llmin=roundoff(llmin);
        orMin=roundoff(orMin);
        llmax=roundoff(llmax);
        orMax=roundoff(orMax);

        Intent intent = new Intent(AppConstants.BROADCAST_MIN_MAX_VAL);
        intent.putExtra("llmin", llmin);
        intent.putExtra("llmax", llmax);
        intent.putExtra("ormin", orMin);
        intent.putExtra("ormax", orMax);
        //intent.putExtra("tv_dealinfo",oyeButtonData);

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);


    }



    private int   roundoff(int val){

        val=val/1000;
        val=val * 1000;
        return  val;
    }
    private int   roundoff1(int val){

        val=val/500;
        val=val * 500;
        return  val;
    }


    public void onoyeclickRateChange(String locality,int area,int llmin,int llmax,String psf){
        horizontalPicker.setVisibility(View.GONE);
        tv_building.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.INVISIBLE);
        tvFetchingrates.setVisibility(View.VISIBLE);

        String llmin1;
        String llmax1;
        llmin1=numToVal(llmin);
        llmax1=numToVal(llmax);

        Log.i("TRACE11","llmin"+llmin);
        Log.i("TRACE11","llmax "+llmax);
        Log.i("TRACE11","llmin "+llmin1);
        Log.i("TRACE11","llmax"+llmax1);
        tv_building.setVisibility(View.VISIBLE);
        tv_building.setText("Range @ "+locality+" | AREA = "+area +"sqft");
        Log.i("TRACE11","tv_building"+tv_building.getText());
        text = "<font color=#ff9f1c><b>\u20B9</b> "+llmin1+"<sub> "+psf+" </sub></b></b> <b> - </b> <b>\u20B9</b>"+llmax1+"<b><sub>"+psf+"</sub></font>";
        tvFetchingrates.setText(Html.fromHtml(text));


    }



    public void UpdateRatePanel(){
        try {
            Log.i("Update","UpdateRatePanel:   ");
            horizontalPicker.setVisibility(View.VISIBLE);
//       tv_building.setText("Average Rate @ This Locality");
            buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);

            tvRate.setVisibility(View.VISIBLE);
            rupeesymbol.setVisibility(View.VISIBLE);
            tvFetchingrates.setVisibility(View.GONE);
        }
        catch(Exception e){}

    }


    String numToVal(int no){
        String str = "",v = "";

        int twoWord = 0,val = 1;

        int c = (no == 0 ? 1 : (int)(log10(no)+1));

        if (c > 8) {

            c = 8;
        }
        if (c%2 == 1){

            c--;
        }

        c--;
        //   int q = Int(pow(Double(10),Double(c)))
        switch(c)
        {
            case 7:
//            if(propertyType)
                val = no/10000000;
//            else
//                val = no/100000;
                no = no%10000000;
                String formatted = String.format("%07d", no);
                formatted = formatted.substring(0,1);

                v = val+"."+formatted;
                str = v+" cr";


                twoWord++;
                break;

            case 5:

                val = no/100000;

                v = val+"";
                no = no%100000;
                String s2 = String.format("%05d", no);
                s2 = s2.substring(0,1);

                if (val != 0){
                    str = str+v+"."+s2+" lacs";
                    twoWord++;
                }

                break;

            case 3:
                val = no/1000;
                v = val+"";
                no = no%1000;
                String.format("%05d", no);
                String s3 = String.format("%03d", no);
                s3 = s3.substring(0,1);
                if (val != 0) {
                    str = str+v+"."+s3+" k";
                }
                break;
            default :
                // print("noToWord Default")
                break;
        }
        return str;
    }

    public int numToVals(int no){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int truncate_first;
        if(currentapiVersion>=23)
            truncate_first = 2;
        else
            truncate_first = 3;

        Log.i("TRACED","no is"+no);
        String str = "",v = "";

        int twoWord = 0,val = 1;

        int c = (no == 0 ? 1 : (int)(log10(no)+1));

        if (c > 8) {

            c = 8;
        }
        if (c%2 == 1){

            c--;
        }

        c--;
        //   int q = Int(pow(Double(10),Double(c)))
        switch(c)
        {
            case 7:

                val=no;



                twoWord++;
                break;

            case 5:
                val=no;



                twoWord++;

                break;

            case 3:

                val=no;

                //str = str+v+"."+s3+"K";


                break;
            default :
                // print("noToWord Default")
                break;
        }
//        Log.i("TRACE","budget string"+str);
        return val;
    }



    public void tutorialAlert(final View rootView) {
        //tutorial and alert beacon
        String text,text2;
        // final RippleBackground rippleBackground4 = (RippleBackground) rootView.findViewById(R.id.content4);
        countertut = 0;
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this.getActivity());
//text = "<font color=#ff9f1c>Tenant/ <br>Property Owner <br>Choose<br> 'Rental'</font>";
//    text2="<font color=#ff9f1c>Property <br>Buyer/Seller <br>Choose <br>'Resale'</font>";
        sequence.addSequenceItem(rootView.findViewById(R.id.phasedSeekBar),
                "     Property                       Property\n Tenant/Owner             Buyer/Seller\n\n    Choose                            Choose\n    'Rental'                             'Resale'" , "      GOT IT! (Go to next screen)");
//"     Tenant/                       Property\nProperty Owner             Buyer/Seller\n\n    Choose                            Choose\n    'Rental'                              'Resale'"
        sequence.addSequenceItem(rootView.findViewById(R.id.ic_search),
                "                   Type Locality\n        1.Close to your Workplace\n  2.Your current/new neighbourhood\n       3.Where you want to Invest\n\n                              OR\n\n                You own a Property ?,\n      you can type name and address\n                of your building.\n", "     GOT IT! (Go to next screen)");

//    sequence.addSequenceItem(rootView.findViewById(R.id.picker),"",
//            "Touch 'Outside' the\n'Rate Panel'\nDrag/Move the map,\nSet the Pointer\n\nYou can find\nAverage Rate @ Locality\nfor 2BHK[can be changed]", "GOT IT! (Go to next screen)");
        sequence.addSequenceItem(rootView.findViewById(R.id.tvFetchingRates),
                "            Check Your Budget Price", "       GOT IT! (Click to FINISH)");
        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView materialShowcaseView, int i) {
                countertut++;
                if (countertut == 3) {
                    Log.i("ischecked", "beacon_walk==========  :" + beacon);




                    try {
                        if (beacon.equalsIgnoreCase("true")) {
                            beaconAlert(rootView);
                            Log.i("ischecked", "beacon_walk1  ==========   :" + beacon);
                        }else
                        {
                            Log.i(TAG,"sasti masti 1 "+AppConstants.cardCounter);
                            ((ClientMainActivity)getActivity()).showCard();
                        }
                    } catch (InterruptedException e) {e.printStackTrace();}
                    // rippleBackground4.startRippleAnimation();
                }
            }
        });
        sequence.start();





    }



    public void beaconAlert( final View rootView) throws InterruptedException {

        final RippleBackground rippleBackground1 =
                (RippleBackground) rootView.findViewById(R.id.client_content);
        final RippleBackground rippleBackground2 = (RippleBackground) rootView.findViewById(R.id.client_content2);
        final RippleBackground rippleBackground3 = (RippleBackground) rootView.findViewById(R.id.client_content3);
        start = System.currentTimeMillis();
        boolean ripple = true;
        long now;

        try {
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                    rippleBackground2.startRippleAnimation();
                    try {

                    SnackbarManager.show(
                            Snackbar.with(getContext())
                                    .text("Set Location")
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                }catch(Exception e){}
                }

                public void onFinish() {

                    new CountDownTimer(3000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            rippleBackground2.stopRippleAnimation();
                            rippleBackground3.startRippleAnimation();
               try {
                   SnackbarManager.show(
                      Snackbar.with(getContext())
                          .text("Set your Budget")
                          .position(Snackbar.SnackbarPosition.TOP)
                          .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                   }catch(Exception e){}
                        }

                        public void onFinish() {

                            new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    rippleBackground3.stopRippleAnimation();

                                    rippleBackground1.startRippleAnimation();
                                    try {
                                    SnackbarManager.show(
                                            Snackbar.with(getContext())
                                                    .text("Press oye button to send your requirement")
                                                    .position(Snackbar.SnackbarPosition.TOP)
                                                    .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
                                    }catch(Exception e){}
                                }

                                public void onFinish() {

                                    rippleBackground1.stopRippleAnimation();
                                    Log.i(TAG,"sasti masti "+AppConstants.cardCounter);
                                    try {
                                        ((ClientMainActivity) getActivity()).showCard();
                                    }
                                    catch(Exception e){}


                                }
                            }.start();


                        }
                    }.start();
                }
            }.start();

        } catch (Exception e) {
        }



    }



public void oyebuttonBackgrountColorOrange(){
    mVisits.clearAnimation();
    mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
    txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
    recordWorkout.setBackgroundColor(Color.parseColor("#ff9f1c"));
}


    public void oyebuttonBackgrountColorGreenishblue(){
        mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
//        mVisits.startAnimation(zoomin_zoomout);
        StartOyeButtonAnimation();
        txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
        recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
    }






    private void  buildingTextChange(String locality,int area){
        if(isNetworkAvailable()) {
            Log.i(TAG,"buildingTextChange if called");
            tv_building.setText("Average Rate @ " + locality + " | Area " + area + "sqft.");
        }else{
            Log.i(TAG,"buildingTextChange else called");
            tv_building.setText("Average Rate @ Andheri | Area " + area + "sqft.");

//            llMin = 35;
//            llMax = 60;
//            orMin = 21500;
//            llMax = 27000;
//
//           // updateHorizontalPicker();
        }
    }

    private void marquee(int timeInMillis, int timeDivider){
        try {

            new CountDownTimer(timeInMillis, timeDivider) {

                public void onTick(long millisUntilFinished) {
                    horizontalPicker.keepScrolling();
//                    if(horizontalPicker.getVisibility()==View.VISIBLE)
                    rupeesymbol.setVisibility(View.GONE);
                }

                public void onFinish() {

                    horizontalPicker.stopScrolling();
                    updateHorizontalPicker();
                    if(horizontalPicker.getVisibility()==View.VISIBLE)
                    rupeesymbol.setVisibility(View.VISIBLE);

                }
            }.start();


        }catch (Exception e){}
    }







    public void Wlak_Beacon() throws InterruptedException {


        /*if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") && SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")) {

            beaconAlert(rootView);
            tutorialAlert(rootView);


        }else if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true")){
            tutorialAlert(rootView);
        }else{
            beaconAlert(rootView);
        }*/
        if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON).equalsIgnoreCase("")) {
            beacon = "true";
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
        } else {
            beacon = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON);
            // SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
            Log.i("ischecked", "walkthrough3dashboard" + beacon);
        }

        if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("")) {
            Walkthrough = "true";
            SharedPrefs.save(getContext(), SharedPrefs.CHECK_WALKTHROUGH, "false");
        } else {
            Walkthrough = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH);
            Log.i("ischecked", "walkthrough3dashboard" + Walkthrough);
        }



        //Tutorial and Beacon code
        if(Walkthrough.equalsIgnoreCase("true")) {
            Log.i("ischecked","walkthrough3dashboard1111111"+Walkthrough);
            tutorialAlert(rootView);

            Walkthrough="false";

        }
        else if(beacon.equalsIgnoreCase("true") ) {
            Log.i("ischecked","walkthrough3dashboard1111111beacon"+beacon);
            try {
                beaconAlert(rootView);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            beacon="false";

        }



    }


    private void onMapDrag(final MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    tvRate.setVisibility(View.INVISIBLE);
                    rupeesymbol.setVisibility(View.INVISIBLE);

                    horizontalPicker.keepScrolling();
                    horizontalPicker.stopScrolling();
                    Log.i("MotionEvent.ACTION_MOVE", "=========================");
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    horizontalPicker.stopScrolling();
//                    marquee(500,100);
                            if(!spanning) {
                    if (isNetworkAvailable()) {
                        Log.i("MotionEvent.ACTION_UP", "=========================");
                        final long now = SystemClock.uptimeMillis();
                        if (clicked == true) {
                            map.getUiSettings().setScrollGesturesEnabled(true);
                            Log.i("MotionEvent.ACTION_UP", "=========================" + clicked);
                        }
//
                        if (now - lastTouched > SCROLL_TIME && !(motionEvent.getPointerCount() > 1) && isNetworkAvailable()) {
                            Log.i("MotionEvent.ACTION_UP", "=========================22");
                            Log.i("setScroll", "=======================setScrollGesturesEnabled==");
                            ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                            Intent in = new Intent(AppConstants.MARKERSELECTED);
                            in.putExtra("markerClicked", "false");
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
                            tvFetchingrates.setVisibility(View.VISIBLE);
                            mMarkerminmax.setVisibility(View.VISIBLE);
                            tvRate.setVisibility(View.VISIBLE);
                            rupeesymbol.setVisibility(View.VISIBLE);
                            tvFetchingrates.setVisibility(View.VISIBLE);
                            buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);

                            tv_building.setVisibility(View.VISIBLE);
                            recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));

                            LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());
                            Log.i("map", "============ map:" + " " + map);
//                            currentLocation1 = map.getProjection().fromScreenLocation(point);

                            VisibleRegion visibleRegion = map.getProjection()
                                    .getVisibleRegion();

                            Point x1 = map.getProjection().toScreenLocation(visibleRegion.farRight);

                            Point y1 = map.getProjection().toScreenLocation(visibleRegion.nearLeft);


                            Point centerPoint = new Point(x1.x / 2, y1.y / 2);

                            LatLng centerFromPoint = map.getProjection().fromScreenLocation(
                                    centerPoint);
                            currentLocation1=centerFromPoint;
                            lat = currentLocation1.latitude;
                            Log.i("t1", "lat" + " " + lat);
                            lng = currentLocation1.longitude;
                            Log.i("t1", "lng" + " " + lng);
//                            map.addMarker(new MarkerOptions().title("hey").position(currentLocation1));
                            Log.i("MARKER-- ", "====================================");
                            SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                            SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                            General.setSharedPreferences(getContext(), AppConstants.MY_LAT, lat + "");
                            General.setSharedPreferences(getContext(), AppConstants.MY_LNG, lng + "");
                            Log.i("t1", "Sharedpref_lat" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
                            Log.i("t1", "Sharedpref_lng" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
                            getRegion();
                            search_building_icon.setVisibility(View.GONE);
                            horizontalPicker.stopScrolling();
                            missingArea.setVisibility(View.GONE);
                            getPrice();
                            new LocationUpdater().execute();
                        }

                    } else {
                        tvFetchingrates.setVisibility(View.VISIBLE);
                        tvRate.setVisibility(View.GONE);
                        rupeesymbol.setVisibility(View.GONE);
                        horizontalPicker.setVisibility(View.GONE);
                        tv_building.setVisibility(View.GONE);
                        tvFetchingrates.setText("No Internet Connection..");
                        General.internetConnectivityMsg(getContext());
//                        try {
//                            SnackbarManager.show(
//                                    Snackbar.with(getContext())
//                                            .text("Seems like you dont have Internet Connection,Check your internet connection and try again.. ")
//                                            .position(Snackbar.SnackbarPosition.TOP)
//                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
//                        } catch (Exception e) {
//                        }
                    }

                    }
                            spanning=false;

                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastTouched = SystemClock.uptimeMillis();
                    map.getUiSettings().setScrollGesturesEnabled(true);
                    //LatLng currentLocation11;
                    Log.i("MotionEvent.ACTION_DOWN", "=========================");


                }
        }


    private boolean isTelephonyEnabled(){
        TelephonyManager tm = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState()== TelephonyManager.SIM_STATE_READY;
    }

    /*private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        getActivity(),
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".png";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
Log.i(TAG,"imageFileimageFile "+imageFile);
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
            Log.i(TAG,"Caught in exception in take screenshot "+e);
        }
    }

    private void openScreenshot(File imageFile) {
        int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image*//*");
        startActivity(intent);
    }*/

    protected void PropertyButtonAnimation() {


        /*Intent intent = new Intent(AppConstants.PROPERTY_TYPE_BROADCAST);
        intent.putExtra("protype",Property_type );
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);*/
        if (brokerType.equalsIgnoreCase("rent")) {
            ani = zoomout_left;
            property_type_layout.startAnimation(ani);
        } else {
            ani = zoomout_right;
            property_type_layout.startAnimation(ani);
        }
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (brokerType.equalsIgnoreCase("rent")) {

                    property_type_layout.clearAnimation();
                    property_type_layout.setVisibility(View.GONE);
//                    if(!Property_type.equalsIgnoreCase("home"))
                    txtFilterValue.setText(oyetext);
                    rental.setText(Property_type);


                } else {

                    property_type_layout.clearAnimation();
                    property_type_layout.setVisibility(View.GONE);
//                    if(!Property_type.equalsIgnoreCase("home"))
                    txtFilterValue.setText(oyetext);
                    resale.setText(Property_type);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

    }



    protected void PropertyButtonSlideAnimation() {



        property_type_layout.startAnimation(slide_up);
        pro_click=false;
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(  pro_click==false) {
                    property_type_layout.clearAnimation();
                    property_type_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

    }



    public void getNearbyLatLong() {
        map.getUiSettings().setAllGesturesEnabled(false);

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + lat + "," + lng);
        googlePlacesUrl.append("&radius=1000");
        googlePlacesUrl.append("&types=" + "ATM");
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=AIzaSyD9u7py1PGKcnlrO77NuY_40jxgIOhX34I");
        Log.i("url", "Url for google Place" + googlePlacesUrl);
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = map;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);

    }

    /*public void clearGooglemap() {
        map.clear();
    }*/

    public void broadcastingConfirmationMsg(){
        String text;
        tv_building.setText("Inform brokers,get listing & schedule visits");
        tvFetchingrates.setTextSize(12);
        text="Brodcasting to <font color=#2dc4b6><big><b>"+AppConstants.NUMBER_OF_BROKER+"</b></big></font> brokers  <font color=#2dc4b6>@ <big><b>"+SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY)+"</b></big></font>";
        tvFetchingrates.setText(Html.fromHtml(text));

    }



    public void onMapclicked(){
        spanning = false;
        mVisits.setEnabled(true);
        txtFilterValue.setEnabled(true);

        for (int i = 0; i < 5; i++) {
            if (flag[i] == true) {
                mCustomerMarker[i].setIcon(icon1);
                ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                Intent in = new Intent(AppConstants.MARKERSELECTED);
                in.putExtra("markerClicked", "false");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
                search_building_icon.setVisibility(View.GONE);
                flag[i] = false;
                horizontalPicker.setVisibility(View.VISIBLE);
                tvFetchingrates.setVisibility(View.GONE);
                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                tvRate.setVisibility(View.VISIBLE);
                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                rupeesymbol.setVisibility(View.VISIBLE);
                mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
//                            mVisits.startAnimation(zoomin_zoomout);
                StartOyeButtonAnimation();
                txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                ll_marker.setEnabled(true);
                tv_building.setVisibility(View.VISIBLE);
            }
        }

    }

    public void OnOyeClick(){
        openOyeScreen();
        CancelAnimation();
        AppConstants.GOOGLE_MAP = map;
        if (clicked == true) {
            oyebuttonBackgrountColorOrange();
            clicked = false;
            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
            mHelperView.setEnabled(false);
        } else {
            oyebuttonBackgrountColorGreenishblue();
            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
            mHelperView.setEnabled(true);
            clicked = true;

        }
        if (RatePanel == true) {
            UpdateRatePanel();
            RatePanel = false;
        } else {
            RatePanel = true;
//            tvFetchingrates.setVisibility(View.VISIBLE);
        }
    }

 public void walkBeaconStatus(){
    if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON).equalsIgnoreCase("")) {
        beacon = "true";
        SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
    } else {
        beacon = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON);
        // SharedPrefs.save(getContext(), SharedPrefs.CHECK_BEACON, "false");
        Log.i("ischecked", "walkthrough3dashboard" + beacon);
    }

    if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("")) {
        Walkthrough = "true";
        SharedPrefs.save(getContext(), SharedPrefs.CHECK_WALKTHROUGH, "false");
    } else {
        Walkthrough = SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH);
        Log.i("ischecked", "walkthrough3dashboard" + Walkthrough);
    }


     if(Walkthrough.equalsIgnoreCase("false") && beacon.equalsIgnoreCase("false")){
         Log.i(TAG,"sasti masti 2 "+AppConstants.cardCounter);
         ((ClientMainActivity)getActivity()).showCard();
     }



}





    private void StartOyeButtonAnimation() {
        Log.i("starting timer", " " + timer);
        if (timer == null) {
            timer = new Timer();
            Log.i("starting timer2", " " + timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("starting timer1", " " + timer);
                                mVisits.startAnimation(zoomin_zoomout);
                            }
                        });
                    }
                }
            }, 1000, 1000);

        }
    }



}























