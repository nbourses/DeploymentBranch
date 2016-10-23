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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.Firebase.ChatList;
import com.nbourses.oyeok.Firebase.DroomChatFirebase;
import com.nbourses.oyeok.GooglePlacesApiServices.GooglePlacesReadTask;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AutoOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateStatus;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.ApiSupport.services.UserApiService;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.AutoCompletePlaces;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.CustomMapFragment;
import com.nbourses.oyeok.RPOT.PriceDiscovery.GoogleMaps.GetCurrentLocation;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.activities.ClientMainActivity;
import com.nbourses.oyeok.activities.Game;
import com.nbourses.oyeok.activities.ProfileActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nbourses.oyeok.interfaces.OnOyeClick;
import com.nbourses.oyeok.models.AddBuildingModel;
import com.nbourses.oyeok.realmModels.Favourites;
import com.nbourses.oyeok.realmModels.LatiLongi;
import com.nbourses.oyeok.realmModels.MyPortfolioModel;
import com.nbourses.oyeok.realmModels.addBuilding;
import com.nbourses.oyeok.widgets.HorizontalPicker.HorizontalPicker;
import com.nbourses.oyeok.widgets.NavDrawer.FragmentDrawer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.sdsmdg.tastytoast.TastyToast;
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
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static com.nbourses.oyeok.R.id.parent;
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
    String Property_type="",oyetext="2BHK";
    private static final int INITIAL_REQUEST = 133;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private static final int MAP_ZOOM = 14;
    private Point point;
   private  Intent callIntent;
    DBHelper dbHelper;
    //    private TextView mDrooms;
    private TextView mVisits,txt_info;
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
    private BitmapDescriptor iconHome;
    private BitmapDescriptor iconOffice;
    private BitmapDescriptor iconOther;
    private Drawable sort_down_black,sort_down_red,sort_up_black,sort_up_green,comman_icon;


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
    private String  text;
    private String[] config=new String[5],rate_growth =new String[5],name = new String[5],listing =new String[5],portal= new String[5],transaction=new String[5],id=new String[5],distance=new String[5];
    Marker[] mCustomerMarker = new Marker[5];
    private int[] or_psf = new int[5], ll_pm = new int[5];
    private LatLng []loc=new LatLng[5];
    private static int count = 0;
    private static final String ischeck = "true";
    private String filterValue;
    private String bhk;
    private int filterValueMultiplier = 950;
    TextView rental,resale;
    RelativeLayout property_type_layout;
    LinearLayout dispProperty;
    private int countertut,p;

    private ImageView myLoc,ic_search;
    LinearLayout recordWorkout;
    boolean clicked = true;
    private String address;
    private LinearLayout seekbar_linearlayout;
    private RelativeLayout hPicker;
    private FrameLayout hideOnSearch;
    private Boolean autoc = false;
    private Boolean autocomplete = false,MarkerClickEnable=true;
    private  static  View  rootView;
    private Boolean buildingSelected = true;
    private View v1;
    private Boolean spanning = false,autoIsClicked=false,pro_click=false;
    RelativeLayout parenttop,parentbottom;
    Animation zoomout_right, slide_up, zoomout_left, ani, zoomin_zoomout,slide_up1;
//    Intent intent ;

    private RelativeLayout topView;

    private String Walkthrough, permission, beacon;
    AutoCompleteTextView inputSearch;
    private int INDEX;
    Animation bounce;
    Animation slideUp;
    Animation slideDown;
    private String favTitle = "My Home",B_name="";
    private BitmapDescriptor favIcon;
    boolean savebuilding=false;
private Button CallButton,addbuilding;
    private Point centerPoint;

    private AutoCompletePlaces.GooglePlacesAutocompleteAdapter dataAdapter;
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

    @Bind(R.id.fav)
    ImageView fav;

    @Bind(R.id.favImg)
    ImageView favImg;

    @Bind(R.id.favBoard)
    LinearLayout favboard;

    @Bind(R.id.favSave)
    Button favSave;

    @Bind(R.id.favCancel)
    Button favCancel;

    @Bind(R.id.favAdrs)
    TextView favAdrs;

    @Bind(R.id.favOText)
    EditText favOText;

    @Bind(R.id.favHome)
    RadioButton favHome;

    @Bind(R.id.favWork)
    RadioButton favWork;

    @Bind(R.id.favOther)
    RadioButton favOther;

    @Bind(R.id.favRG)
    RadioGroup favRG;


    @Bind(R.id.addressBar)
    TextView addressBar;

    @Bind(R.id.addressPanel)
    LinearLayout addressPanel;

    @Bind(R.id.buildingIcon)
    ImageView buildingIcon;




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
                    getPrice();
                    new LocationUpdater().execute();
                    hideOnSearch.setVisibility(View.GONE);
                    seekbar_linearlayout.setVisibility(View.VISIBLE);
                    dispProperty.setVisibility(View.VISIBLE);
                    seekbar_linearlayout.setBackgroundColor(Color.WHITE);
                    seekbar_linearlayout.setAlpha(1);
                }
            } catch (Exception e) {}
        }

    };

    private BroadcastReceiver phasedSeekBarClicked = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!AppConstants.SETLOCATION && !savebuilding){
            if (intent.getExtras().getString("phaseseek") != null) {
                if ((intent.getExtras().getString("phaseseek").equalsIgnoreCase("clicked"))) {
                    try {
                        if(p!=1) {
                            Log.i( "indexxx", "index of layoutsusussjcdnck : " );
                            int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                            Log.i( "indexxx", "index of layoutsusussjcdnck : " + index );
                            if (index == 2) {
                                property_type_layout.clearAnimation();
                                parenttop.removeView( property_type_layout );
                                parentbottom.addView( property_type_layout, 5 );
                            }

                            PropertyButtonSlideAnimation();
                        }
                    } catch (Exception e) {

                    }

                }
            }
            }
        }
    };

    private BroadcastReceiver resetMap = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //if(AppConstants.SEARCHFLAG) {
                Log.i(TAG,"aalo re ");
                AppConstants.SEARCHFLAG = false;
                LatLng currentLocation = new LatLng(AppConstants.MY_LATITUDE,AppConstants.MY_LONGITUDE);
                SharedPrefs.save(getContext(),SharedPrefs.MY_LAT,AppConstants.MY_LATITUDE+"");
                SharedPrefs.save(getContext(),SharedPrefs.MY_LNG,AppConstants.MY_LONGITUDE+"");

                // map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,MAP_ZOOM));
                getRegion();
                 getPrice();
                new LocationUpdater().execute();
            if(!AppConstants.SETLOCATION &&!savebuilding) {
                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
            }

            //}


        }
    };

    private BroadcastReceiver setLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((ClientMainActivity)getActivity()).disEnDealsbtn(false);
            showHidepanel(false);
            AppConstants.SETLOCATION = true;
            map.clear();
            txtFilterValue.setText("OYE");

        }
    };



    private BroadcastReceiver onFilterValueUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {

                if ((intent.getExtras().getString("filterValue") != null)) {
                    Log.i("filtervalue", "filtervalue " + intent.getExtras().getString("filterValue"));
                    txtFilterValue.setText(intent.getExtras().getString("filterValue"));

                }
                if ((intent.getExtras().getString("filterValue") != null)) {
                    bhk = intent.getExtras().getString("bhk");
                    Log.i("bhk", "=================  " + bhk);
                    if (bhk.equalsIgnoreCase("1bhk") || bhk.equalsIgnoreCase("<600")) {
                        filterValueMultiplier = 600;
                        updateHorizontalPicker();

                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            if (bhk.equalsIgnoreCase("1bhk"))
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/month");
                            else
                                onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");

                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");

                    } else if (bhk.equalsIgnoreCase("2bhk") || bhk.equalsIgnoreCase("<950")|| bhk.equalsIgnoreCase("default")) {

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
                    } else if (bhk.equalsIgnoreCase("<300") ) {
                        filterValueMultiplier = 300;
                        updateHorizontalPicker();
                        BroadCastMinMaxValue(llMin * filterValueMultiplier, llMax * filterValueMultiplier, orMin * filterValueMultiplier, orMax * filterValueMultiplier);
                        if (brokerType.equals("rent"))
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, llMin * filterValueMultiplier, llMax * filterValueMultiplier, "/sq.ft");
                        else
                            onoyeclickRateChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier, orMin, orMax, "/sq.ft");
                    }

                }
            }
        }
    };



    public void setOyeButtonClickListener(OnOyeClick onOyeClick) {
        this.onOyeClick = onOyeClick;


    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rex_fragment_home, container, false);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        ButterKnife.bind(this, rootView);
        hideOnSearch = (FrameLayout) rootView.findViewById(R.id.hideOnSearch);
        seekbar_linearlayout = (LinearLayout) rootView.findViewById(R.id.seekbar_linearlayout);

        topView = (RelativeLayout) rootView.findViewById(R.id.top);

        parentbottom=(RelativeLayout) rootView.findViewById(R.id.top);
        parenttop=(RelativeLayout) rootView.findViewById(parent);
        txt_info=(TextView) rootView.findViewById(R.id.txt_info);

        if (General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI).equals("")) {
            General.setSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI, String.valueOf(System.currentTimeMillis()));
            Log.i("TIMESTAMP", "millis " + System.currentTimeMillis());
        }


       init();


        Log.i("notifications", "sendNotification ==========================" + SharedPrefs.getString(getContext(), SharedPrefs.MY_GCM_ID));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(closeOyeScreenSlide, new IntentFilter(AppConstants.CLOSE_OYE_SCREEN_SLIDE));


// Permission Request

       // General.showPermissionDialog(getContext(),getActivity());
        Log.i(TAG,"perma "+SharedPrefs.getString(getContext(), SharedPrefs.PERMISSION));


        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    AppConstants.LOCATION_PERMISSION_REQUEST_CODE);*/


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


        }


        droomChatFirebase = new DroomChatFirebase(DatabaseConstants.firebaseUrl, getActivity());
        mVisits = (TextView) rootView.findViewById(R.id.newVisits);
        mQrCode = (ImageView) rootView.findViewById(R.id.qrCode);
        mMarkerPanel = (LinearLayout) rootView.findViewById(R.id.ll_marker);

        mMarkerminmax = (RelativeLayout) rootView.findViewById(R.id.markerpanelminmax);
        ll_marker = (LinearLayout) rootView.findViewById(R.id.ll_marker);
        recordWorkout = (LinearLayout) rootView.findViewById(R.id.recordWorkout);
        ic_search = (ImageView) rootView.findViewById(R.id.ic_search);

        addbuilding=(Button)  rootView.findViewById( R.id.addbuilding );

//        if(!General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")){
        walkBeaconStatus();//}

        Mmarker = (ImageView) rootView.findViewById(R.id.Mmarker);


        try {//buildingiconbeforeclick

            icon1 = BitmapDescriptorFactory.fromResource(R.drawable.buildingiconbeforeclick);
            icon2 = BitmapDescriptorFactory.fromResource(R.drawable.buildingicononclick);
            sort_down_black = getContext().getResources().getDrawable(R.drawable.sort_down_black);
            sort_down_red = getContext().getResources().getDrawable(R.drawable.sort_down_red);
            sort_up_black = getContext().getResources().getDrawable(R.drawable.sort_up_black);
            sort_up_green = getContext().getResources().getDrawable(R.drawable.up);
            iconHome = BitmapDescriptorFactory.fromResource(R.drawable.favhome);
            iconOffice = BitmapDescriptorFactory.fromResource(R.drawable.favoffice);
            iconOther = BitmapDescriptorFactory.fromResource(R.drawable.favother);

            favIcon = iconHome;


        }
        catch (Exception e)
        {
            Log.i("BITMAP", "message " + e.getMessage());
        }

        recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
        search_building_icon = (ImageView) rootView.findViewById(R.id.selected_property);
//        if(!General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
            dashboardActivity = (ClientMainActivity) getActivity();

        wrapper = (RelativeLayout) dashboardActivity.findViewById(R.id.wrapper);
        dbHelper = new DBHelper(getContext());
        ll_map = (FrameLayout) rootView.findViewById(R.id.ll_map);

        permissionCheckForLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        rupeesymbol = (TextView) rootView.findViewById(R.id.rupeesymbol);
        tvRate = (TextView) rootView.findViewById(R.id.tvRate);
        tvFetchingrates = (TextView) rootView.findViewById(R.id.tvFetchingRates);
        tv_building = (TextView) rootView.findViewById(R.id.tv_building);
        CallButton=(Button) rootView.findViewById(R.id.CallButton);
        buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
        onPositionSelected(0, 3);
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

                Log.i(TAG,"callbutton status ");
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+912239659137"));//+912233836068
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String callPermission = Manifest.permission.CALL_PHONE;
                int hasPermission = ContextCompat.checkSelfPermission(getActivity(), callPermission);
                String[] permissions = new String[] { callPermission };
//                if(isTelephonyEnabled()) {
                    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(permissions, REQUEST_CALL_PHONE);
                        Log.i(TAG,"callbutton status 1");

                    } else {
                        Log.i(TAG,"callbutton status 2");
                        startActivity(callIntent);
                    }
//                }

            }
        });




        mPhasedSeekBar = (CustomPhasedSeekBar) rootView.findViewById(R.id.phasedSeekBar);

        if(!savebuilding) {
            if (!General.getSharedPreferences(getContext(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type2_selector}, new String[]{"30", "40", "15"}, new String[]{getContext().getResources().getString(R.string.Rental), "Game", getContext().getResources().getString(R.string.Resale)}));

            } else {
                mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(this.getResources(),
                        new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                        new String[]{"30", "15"},
                        new String[]{getContext().getResources().getString(R.string.Rental), getContext().getResources().getString(R.string.Resale)
                        }));
            }


        }
       /* if (dbHelper.getValue(DatabaseConstants.offmode).equalsIgnoreCase("null")) {
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector,R.drawable.broker_type2_selector, R.drawable.broker_type2_selector}, new String[]{"30", "40","15"}, new String[]{getContext().getResources().getString(R.string.Rental),"Game", getContext().getResources().getString(R.string.Resale)}));

        } else {
            mPhasedSeekBar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(), new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector, R.drawable.broker_type3_selector, R.drawable.real_estate_selector}, new String[]{"30", "15", "40", "20"}, new String[]{"Rental", "Sale", "Audit", "Auction"}));
        }*/
        mPhasedSeekBar.setListener(this);




        /*mPhasedSeekBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                Log.i("indexxx", "index of layoutsusussjcdnck : " + index);
                if(index==2){
                    property_type_layout.clearAnimation();
                    parenttop.removeView(property_type_layout);
                    parentbottom.addView(property_type_layout,5);}

                PropertyButtonSlideAnimation();
            }
        });*/

        /*ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(autoCompView.getWindowToken(),1);*//*
                *//*autoCompView.performClick();*//*


                searchFragment c = new searchFragment();
                AppConstants.SEARCHFLAG = true;

                loadFragmentAnimated(c, null, R.id.container_Signup, "Search");

            }
        });*/


        autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.inputSearch);
        autoCompView.setAdapter(new AutoCompletePlaces.GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item1));
        autoCompView.setOnItemClickListener(this);
        autoCompView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    ((ClientMainActivity)getActivity()).closeOyeConfirmation();
                    ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                    txtFilterValue.setTextSize(13);

                    txtFilterValue.setTextColor(Color.parseColor("white"));
                    txtFilterValue.setText(oyetext);
                    autoCompView.setCursorVisible(true);
                    autoCompView.clearListSelection();
                    autoCompView.setText("");
                    autoIsClicked=true;
                    autoCompView.showDropDown();
                    // new LocationUpdater().execute();
                    property_type_layout.clearAnimation(); /////
                    property_type_layout.setVisibility(View.GONE);
                    dispProperty.setVisibility(View.GONE);
                    //hideOnSearch.clearAnimation();/////
                    hideOnSearch.setVisibility(View.VISIBLE);
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

                } catch (Exception e) {
                    Log.i(TAG,"Caught in exception autocompleteview click "+e);
                }


            }
        });


        addbuilding.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClientMainActivity)getActivity()).openAddListing();
            }
        } );

        mVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!savebuilding) {
                OnOyeClick();
                }
                if(txtFilterValue.getText().toString().equalsIgnoreCase("save")){
                    map.addMarker(new MarkerOptions().icon(icon1).position(new LatLng(lat,lng)));
                    txtFilterValue.setText("done");
                    txt_info.setText("Is this Location Correct ? press Done");
                }else if(txtFilterValue.getText().toString().equalsIgnoreCase("done")){
                    Addbuilding();
                    ((ClientMainActivity)getActivity()).Reset();
                }
            }
        });




        mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
        txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));


        try {
            //if (isNetworkAvailable())

            customMapFragment = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

            map= customMapFragment.getMap();
            customMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    map = googleMap;

                    final LocationManager Loc_manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    if (!isNetworkAvailable() || !(Loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

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
            final View mMapView = getChildFragmentManager().findFragmentById(R.id.map).getView();

            map.getUiSettings().setRotateGesturesEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setScrollGesturesEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

/*
            VisibleRegion visibleRegion = map.getProjection()
                    .getVisibleRegion();

            Point x1 = map.getProjection().toScreenLocation(visibleRegion.farRight);

            Point y1 = map.getProjection().toScreenLocation(visibleRegion.nearLeft);


            centerPoint = new Point(x1.x / 2, y1.y / 2);*/
            centerPoint = new Point(362, 488);  //actual center point Point(360, 484)
            Log.i("centerPoint","centerPoint : "+centerPoint);


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

                        }

                         else if (scaleGestureDetector.onTouchEvent(motionEvent)) { // Pinch zoom
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

                       /* public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            int dx = (int) (e2.getX() - e1.getX());

                            return true;
                        }*/

                    });


                    private GestureDetector simpleGestureDetector1 = new GestureDetector(
                            getContext(), new GestureDetector.SimpleOnGestureListener() {

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            Log.i("sushil","in fling:  ");
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

/*
                customMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        map = googleMap;

                        final LocationManager Loc_manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                        if (!isNetworkAvailable() || !(Loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {

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
                });*/

            }



            map.getUiSettings().setZoomGesturesEnabled(true);




            //}

            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Log.i(TAG, "my Loc clicked ");


                        new CountDownTimer(200, 50) {

                            public void onTick(long millisUntilFinished) {
                                if (!AppConstants.SETLOCATION && !savebuilding) {
                                    Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);
                                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
//                            ( (ClientMainActivity)getActivity()).closeOyeScreen();
                                    ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
                                    ((ClientMainActivity) getActivity()).closeOyeConfirmation();
                                    txtFilterValue.setTextSize(13);
                                    txtFilterValue.setTextColor(Color.parseColor("white"));
                                    txtFilterValue.setText(oyetext);
                                    buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                    map.getUiSettings().setAllGesturesEnabled(true);
                                    mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                                    txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                                    UpdateRatePanel();
                                    search_building_icon.setVisibility(View.GONE);
                                    buildingIcon.setVisibility(View.GONE);
                                    fav.setVisibility(View.VISIBLE);
                                }
                            }

                            public void onFinish() {

                                getLocationActivity = new GetCurrentLocation(getActivity(), mcallback);


                            }
                        }.start();

                        return false;


                }
            });


            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    Log.i("inside getinfo","==========");
                    LatLng latLng = arg0.getPosition();
//                    v1 = inflater.inflate(R.layout.info_window_layout, null);
                    v1 = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);
                    ImageView rate_change_img = (ImageView) v1.findViewById(R.id.rate_change_img);
                    TextView rate_change_value = (TextView) v1.findViewById(R.id.rate_change_value);
                    String rate="0";
                    for(int i=0;i<5;i++) {
                        if (arg0.getId().equals(mCustomerMarker[i].getId())) {
                            if (flag[i] == false)
                                rate = rate_growth[i];
                        }
                    }

                    if (Integer.parseInt(rate) < 0){
                        comman_icon=sort_down_red;
                        rate_change_value.setTextColor(Color.parseColor("#ffb91422"));// FFA64139 red
                        rate_change_img.setBackground(comman_icon);
                        rate_change_value.setText(rate.subSequence(1, rate.length())+" %");
                    }
                    else if(Integer.parseInt(rate) > 0){
                        comman_icon = sort_up_green;
                        rate_change_value.setTextColor(Color.parseColor("#2dc4b6"));// FF377C39 green FF2CA621   FFB91422
                        rate_change_img.setBackground(comman_icon);
                        rate_change_value.setText(Integer.parseInt(rate)+" %");
                    }
                    else{
                        rate_change_img.setBackground(null);
                        rate_change_value.setTextColor(Color.parseColor("black"));
                        rate_change_value.setText(Integer.parseInt(rate)+" %");
                    }
                    return v1;
                }
                @Override
                public View getInfoContents(Marker arg0) {

                    Log.i("inside getinfo","==========");
                    /*LatLng latLng = arg0.getPosition();
//                    v1 = inflater.inflate(R.layout.info_window_layout, null);
                    v1 = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);
                    ImageView rate_change_img = (ImageView) v1.findViewById(R.id.rate_change_img);
                    TextView rate_change_value = (TextView) v1.findViewById(R.id.rate_change_value);
                    String rate="0";
                    for(int i=0;i<5;i++) {
                        if (arg0.getId().equals(mCustomerMarker[i].getId())) {
                            if (flag[i] == false)
                                rate = rate_growth[i];
                        }
                    }

                    if (Integer.parseInt(rate) < 0){
                        comman_icon=sort_down_red;
                        rate_change_value.setTextColor(Color.parseColor("red"));
                        rate_change_img.setBackground(comman_icon);
                        rate_change_value.setText(rate.subSequence(1, rate.length())+" %");
                    }
                    else if(Integer.parseInt(rate) > 0){
                        comman_icon = sort_up_green;
                        rate_change_value.setTextColor(Color.parseColor("green"));
                        rate_change_img.setBackground(comman_icon);
                        rate_change_value.setText(Integer.parseInt(rate)+" %");
                    }
                    else{
                        rate_change_img.setBackground(null);
                        rate_change_value.setTextColor(Color.parseColor("black"));
                        rate_change_value.setText(Integer.parseInt(rate)+" %");
                    }*/

                    return null;

                }
            });

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Log.i("MA999999 ", "MAP CLICK=========");
if(!AppConstants.SETLOCATION && !savebuilding) {

        tvRate.setVisibility(View.VISIBLE);
        rupeesymbol.setVisibility(View.VISIBLE);


    onMapclicked();
}


                }


            });




            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Marker m;
                    //intent =new Intent(getContext(), ClientMainActivity.class);
                    int i;
                    Log.i(" sushil flag[i] ==  ", "===========================  Entry to marker listner :  "+marker.getId().toString()+" "+ horizontalPicker.getValues());
                    if(MarkerClickEnable) {
                        for (i = 0; i < 5; i++) {
                            if (marker.getId().equals(mCustomerMarker[i].getId())) {
                                INDEX = i;
                                Log.i("1sushil11", "===========================   :  " + marker.getId().toString() + " " + mCustomerMarker[i].getId().toString() + " " + flag[i]);
                                if (flag[i] == false) {
                                    Log.i("1sushil11", "===========================");


                                    ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
                                    ((ClientMainActivity) getActivity()).OpenBuildingOyeConfirmation(listing[i],transaction[i],portal[i]);
                                    mCustomerMarker[i].setIcon(icon2);
                                    SaveBuildingDataToRealm();
//                                    sendDataToOyeConfirmation(i);
                                /*m=mCustomerMarker[i];



                                mCustomerMarker[i].remove();
                                mCustomerMarker[i]=  map.addMarker(new MarkerOptions().position(m.getPosition()).title(m.getTitle()).snippet(m.getSnippet()).icon(icon2));
                                search_building_icon.setVisibility(View.VISIBLE);*/
                                    buildingIcon.setVisibility(View.VISIBLE);
                                    fav.setVisibility(View.GONE);
                                    mCustomerMarker[i].showInfoWindow();
                                    horizontalPicker.setVisibility(View.GONE);
                                    tvFetchingrates.setVisibility(View.VISIBLE);
                                   tvRate.setVisibility(View.GONE);
                                    rupeesymbol.setVisibility(View.GONE);
                                    recordWorkout.setBackgroundColor(Color.parseColor("#ff9f1c"));

                                    mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.oyebutton_bg_color_yellow));
                                    txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_bg_color_white));
                                    String text1;//="<font color=#ffffff size=20> "+rate_growth[i] + " %</font>";

                                    text1 = "<font color=#ffffff>Observed </font><font color=#ff9f1c> 30 </font> <font color=#ffffff>online listing in last 1 WEEK</font>";
                                    tv_building.setText(Html.fromHtml(text1));
                                    txtFilterValue.setText(rate_growth[i] + " %");
                                    txtFilterValue.setTextSize(16);
                                    txtFilterValue.setTypeface(Typeface.DEFAULT_BOLD);
                                    if (Integer.parseInt(rate_growth[i]) < 0){
                                        txtFilterValue.setTextColor(Color.parseColor("#ffb91422"));// FFA64139 red
                                        if (brokerType.equalsIgnoreCase("rent")) {
                                            String text = "<font color=#ffffff >" + name[i] + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 " + General.currencyFormat(String.valueOf(ll_pm[i])).substring(2, General.currencyFormat(String.valueOf(ll_pm[i])).length()) + "</font><b><font color=#b91422><sub>/m</sub></font></br>";
                                            tvFetchingrates.setText(Html.fromHtml(text));
                                        } else {
                                            String text = "<font color=#ffffff >" + name[i] + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#b91422>\u20B9 " + General.currencyFormat(String.valueOf(or_psf[i])).substring(2, General.currencyFormat(String.valueOf(or_psf[i])).length()) + "</font><b><font color=#b91422><sub>/sq.ft</sub></font></br>";
                                            tvFetchingrates.setText(Html.fromHtml(text));
                                        }
                                    }
                                    else if(Integer.parseInt(rate_growth[i]) > 0){
                                        txtFilterValue.setTextColor(Color.parseColor("#2dc4b6"));// FF377C39 green FF2CA621   FFB91422
                                        if (brokerType.equalsIgnoreCase("rent")) {

                                            String text = "<font color=#ffffff >" + name[i] + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#2dc4b6>\u20B9 " + General.currencyFormat(String.valueOf(ll_pm[i])).substring(2, General.currencyFormat(String.valueOf(ll_pm[i])).length()) + "</font><b><font color=#2dc4b6><sub>/m</sub></font></br>";
                                            tvFetchingrates.setText(Html.fromHtml(text));
                                        } else {

                                            String text = "<font color=#ffffff >" + name[i] + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#2dc4b6>\u20B9 " + General.currencyFormat(String.valueOf(or_psf[i])).substring(2, General.currencyFormat(String.valueOf(or_psf[i])).length()) + "</font><b><font color=#2dc4b6><sub>/sq.ft</sub></font></br>";

                                            tvFetchingrates.setText(Html.fromHtml(text));
                                        }
                                    }
                                    else{
                                        if (brokerType.equalsIgnoreCase("rent")) {
                                            String text = "<font color=#ffffff >" + name[i] + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#ff9f1c>\u20B9 " + General.currencyFormat(String.valueOf(ll_pm[i])).substring(2, General.currencyFormat(String.valueOf(ll_pm[i])).length()) + "</font><b><font color=#ff9f1c><sub>/m</sub></font></br>";
                                            tvFetchingrates.setText(Html.fromHtml(text));
                                        } else {
                                            String text = "<font color=#ffffff >" + name[i] + "</b></font> <font color=#ffffff> @</font>&nbsp<font color=#ff9f1c>\u20B9 " + General.currencyFormat(String.valueOf(or_psf[i])).substring(2, General.currencyFormat(String.valueOf(or_psf[i])).length()) + "</font><b><font color=#ff9f1c><sub>/sq.ft</sub></font></br>";

                                            tvFetchingrates.setText(Html.fromHtml(text));
                                        }
                                        txtFilterValue.setTextColor(Color.parseColor("black"));

                                    }
//                                    txtFilterValue.setTextColor(Color.parseColor("black"));
                                    ll_marker.setEnabled(false);
                                    mVisits.setEnabled(false);
                                    txtFilterValue.setEnabled(false);
//                                txtFilterValue.setTextColor(Color.parseColor("green"));
                                    CancelAnimation();
                                    buildingSelected = false;
                                    Intent in = new Intent(AppConstants.MARKERSELECTED);
                                    in.putExtra("markerClicked", "true");
                                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
//                                  Log.i("coming soon", "coming soon :" + marker.getTitle().toString());
                                    tv_building.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setTypeface(null, Typeface.BOLD);
                                    lng = mCustomerMarker[i].getPosition().longitude;
                                    lat = mCustomerMarker[i].getPosition().latitude;
                                    Log.i("marker lat", "==============marker position :" + mCustomerMarker[i].getPosition() + " " + lat + " " + lng);
                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                                    SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                                    General.setSharedPreferences(getContext(), AppConstants.MY_LAT, lat + "");
                                    General.setSharedPreferences(getContext(), AppConstants.MY_LNG, lng + "");//*/
//                                  mCustomerMarker[i].showInfoWindow();
                                    new LocationUpdater().execute();
                                    flag[i] = true;

                                } else {
                                    ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
                                    mCustomerMarker[i].setIcon(icon1);


                               /* m=mCustomerMarker[i];
                                mCustomerMarker[i].remove();

                                mCustomerMarker[i]=  map.addMarker(new MarkerOptions().position(m.getPosition()).title(m.getTitle()).snippet(m.getSnippet()).icon(icon1));
                                mCustomerMarker[i].hideInfoWindow();*/
                                    updateHorizontalPicker();
                                    Log.i("mm_mithai", "marker draw");

//                                mCustomerMarker[i].setIcon(icon1);
                                    search_building_icon.setVisibility(View.GONE);
                                    buildingIcon.setVisibility(View.GONE);
                                    fav.setVisibility(View.VISIBLE);
                                    flag[i] = false;
                                    horizontalPicker.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setVisibility(View.GONE);
                                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));

                                    mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                                    txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                                    txtFilterValue.setTextSize(13);
                                    txtFilterValue.setTextColor(Color.parseColor("white"));
                                    txtFilterValue.setText(oyetext);

                                    mVisits.setEnabled(true);
                                    txtFilterValue.setEnabled(true);
                                    ll_marker.setEnabled(true);
//                                StartAnimation();
                                    //intent.putExtra("client_heading", "Live Region Rates");
                                    Intent in = new Intent(AppConstants.MARKERSELECTED);
                                    in.putExtra("markerClicked", "false");
                                    buildingSelected = true;
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
                            if (!AppConstants.SETLOCATION && !savebuilding) {
                                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                            }
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

        setDealStatus2(getContext());
        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        bounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                favboard.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
        slide_up1 = (AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_and_down));
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
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);

                parentbottom.removeView(property_type_layout);
                Log.i("indexxx", "index of layout : " + index);
                if(index!=2) {
                    parenttop.addView(property_type_layout, 2);
                }
                PropertyButtonAnimation();
                property_type_layout.setVisibility(View.GONE);

            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_click=true;
                property_type_layout.clearAnimation();
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);

                parentbottom.removeView(property_type_layout);
                Log.i("indexxx", "index of layout : " + index);
                if(index!=2) {
                    parenttop.addView(property_type_layout, 2);
                }
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
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);
                parentbottom.removeView(property_type_layout);

                Log.i("indexxx", "index of layout : " + index);
                if(index!=2) {
                    parenttop.addView(property_type_layout, 2);
                }
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
                int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild(property_type_layout);

                parentbottom.removeView(property_type_layout);
                Log.i("indexxx", "index of layout : " + index);
                if(index!=2) {
                    parenttop.addView(property_type_layout, 2);
                }
                Log.i("home", "you are in office ");
                Property_type = "Office";
                AppConstants.PROPERTY = "Office";
                oyetext = "OFFIC";
                PropertyButtonAnimation();
                property_type_layout.setVisibility(View.GONE);
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(!buildingSelected)
                    buildingSelected = true;
                favboard.clearAnimation();
                Boolean t = false;
                for(int i =0; i>5; i++){
                    if(flag[i])
                        t = true;
                }
                Intent intent = new Intent(AppConstants.CLOSE_OYE_SCREEN_SLIDE);

                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                if(!t) {

//                            ( (ClientMainActivity)getActivity()).closeOyeScreen();
                    ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
                    ((ClientMainActivity) getActivity()).closeOyeConfirmation();
                    txtFilterValue.setTextSize(13);
                    txtFilterValue.setTextColor(Color.parseColor("white"));
                    txtFilterValue.setText(oyetext);
                }
                favboard.setVisibility(View.VISIBLE);
                favboard.setAnimation(bounce);
                /*favboard.animate()
                        .translationYBy(0)
                        .translationY(favboard.getHeight())
                        .setDuration(500);*/

            }
        });

        favRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.favHome) {
                    favImg.setImageResource((R.drawable.favhome));
                    favOText.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(favboard.getWindowToken(), 0);
                    favTitle = "My Home";
                    favIcon = iconHome;

                } else if(checkedId == R.id.favWork) {
                    favImg.setImageResource((R.drawable.favoffice));
                    favOText.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(favboard.getWindowToken(), 0);
                    favTitle = "My Office";
                    favIcon = iconOffice;

                } else {
                    favImg.setImageResource((R.drawable.favother));
                    favOText.setVisibility(View.VISIBLE);
                    favOText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(favOText, InputMethodManager.SHOW_IMPLICIT);
                    favTitle = "My Other";
                    favIcon = iconOther;
                }
            }
        });


        /*favSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favboard.clearAnimation();
                favboard.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(favboard.getWindowToken(), 0);
                *//*favboard.animate()
                        .translationYBy(0)
                        .translationY(favboard.getHeight())
                        .setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));*//*
            }
        });

        favCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favboard.clearAnimation();
                favboard.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(favboard.getWindowToken(), 0);
               *//* favboard.animate()
                        .translationYBy(0)
                        .translationY(favboard.getHeight())
                        .setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));*//*
            }
        });*/

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

       /* if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"persy 12345");
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }*/
        Log.i(TAG,"persy 12346");

        Uri uri = Uri.fromFile(imageFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg/text/html");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        //intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Hey, please check out these property rates I found out on this super amazing app Oyeok.</p><p><a href=\"https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/\">Download Oyeok for android</a></p>"));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, please check out these property rates I found out on this super amazing app Oyeok. \n \n  https://play.google.com/store/apps/details?id=com.nbourses.oyeok&hl=en/");
        startActivity(Intent.createChooser(intent, "Share Image"));

//        Spanned spanned = Html.fromHtml(code, this, null);
    }

/*@Override
    public Drawable getDrawable(String arg0) {
        // TODO Auto-generated method stub
        int id = 0;

        if(arg0.equals("addbutton.png")){
            id = R.drawable.sort_down_red;
        }

        if(arg0.equals("tu1.png")){
            id = R.drawable.sort_up_green;
        }
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(id);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        return d;
    }*/

    private BroadcastReceiver closeOyeScreenSlide = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
      Log.i("closeOyeScreenSlide","=======");
            UpdateRatePanel();
            RatePanel = true;
//            OnOyeClick();
            ((ClientMainActivity)getActivity()).closeOyeScreen();

 try {  // crash on card
    ll_map.setAlpha(1f);
}
catch(Exception e){

}
//            StartAnimation();
            if(clicked==false){
                oyebuttonBackgrountColorGreenishblue();
                map.getUiSettings().setAllGesturesEnabled(true);
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

   /* @OnClick({R.id.ll_marker})
    public void onButtonsClick(View v) {
//        if (ll_marker.getId() == v.getId()) {

           // txtFilterValue.performClick();
            OnOyeClick();

//        }
    }*/

    @OnClick(R.id.txtFilterValue)
    public void onTxtFilterValueClick(View v) {


        if(txtFilterValue.getText().toString().equalsIgnoreCase("save")){
            map.addMarker(new MarkerOptions().icon(icon1).position(new LatLng(lat,lng)));
            txtFilterValue.setText("done");
            txt_info.setText("Is this Location Correct ? press Done");
        }else if(txtFilterValue.getText().toString().equalsIgnoreCase("done")){
            Addbuilding();
            ((ClientMainActivity)getActivity()).Reset();

        }else {
            OnOyeClick();
        }

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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(phasedSeekBarClicked, new IntentFilter(AppConstants.PHASED_SEEKBAR_CLICKED));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(resetMap, new IntentFilter(AppConstants.RESETMAP));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(setLocation, new IntentFilter(AppConstants.SETLOCN));


    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onFilterValueUpdate);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(closeOyeScreenSlide);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(autoComplete);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(phasedSeekBarClicked);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(resetMap);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(setLocation);

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
            if (!AppConstants.SETLOCATION && !savebuilding) {
            General.slowInternet(getContext());

            MarkerClickEnable = true;
            mVisits.setEnabled(false);
            disablepanel(false);
            txtFilterValue.setEnabled(false);
            CancelAnimation();
            User user = new User();
            Log.i(TAG, "get price prepaaration locality " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));

            user.setDeviceId(General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
            Log.i("PREOK", "getcontext " + General.getSharedPreferences(getContext(), AppConstants.TIME_STAMP_IN_MILLI));
            user.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
            user.setUserRole("client");
            user.setLongitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
            user.setProperty_type("home");
            user.setLatitude(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
            Log.i("t1", "My_lng" + "  " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
            if (SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY) == "")
                user.setLocality("mumbai");
            else
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
            tvFetchingrates.setText("Fetching Rates....");
            tvFetchingrates.setTextSize(15);
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_102).build();
            restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

            UserApiService userApiService = restAdapter.create(UserApiService.class);


            userApiService.getPrice(user, new retrofit.Callback<JsonElement>() {

                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        General.slowInternetFlag = false;
                        General.t.interrupt();
                        String strResponse = new String(((TypedByteArray) response.getBody()).getBytes());
//                        Log.e(TAG, "RETROFIT SUCCESS " + getPrice.getResponseData().getPrice().getLlMin().toString());
                        JSONObject jsonResponse = new JSONObject(strResponse);
                        String errors = jsonResponse.getString("errors");
                        String msg = "";
                        try {
                            msg = jsonResponse.getJSONObject("responseData").getJSONObject("price").getString("message");
                        } catch (Exception e) {

                        }
                        try {
                            msg = jsonResponse.getJSONObject("responseData").getString("message");
                        } catch (Exception e) {

                        }

                        Log.i("Chaman", "ho ghe " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY) + " " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG) + " " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT) + " " + jsonResponse);

                        if (errors.equals("8")) {
                            Log.i(TAG, "error code is 2 ");
                            Log.i(TAG, "error code is 1 " + jsonResponse.toString());
                            Log.i(TAG, "error code is " + errors);
                            Log.i(TAG, "error code is 3 ");
                            SnackbarManager.show(
                                    Snackbar.with(getActivity())
                                            .text("You must update profile to proceed.")
                                            .position(Snackbar.SnackbarPosition.TOP)
                                            .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());
                            Intent openProfileActivity = new Intent(getContext(), ProfileActivity.class);
                            openProfileActivity.putExtra("msg", "compulsary");
                            startActivity(openProfileActivity);
                        } else if (msg.equalsIgnoreCase("We dont cater here yet") || msg.equalsIgnoreCase("missing Fields in get price")) {
                            tvFetchingrates.setText("We only cater in Mumbai");
                        } else {
                            JSONObject jsonResponseData = new JSONObject(jsonResponse.getString("responseData"));
                            // horizontalPicker.stopScrolling();
                            Log.i("TRACE", "Response getprice buildings jsonResponseData" + jsonResponseData);
                            JSONObject price = new JSONObject(jsonResponseData.getString("price"));
                            Log.i("TRACE", "Response getprice buildings pricer ");
                            Log.i("TRACE", "Response getprice buildings price " + price);
                            JSONArray buildings = new JSONArray(jsonResponseData.getString("buildings"));
                            Log.i("TRACE", "Response getprice buildings" + buildings);
                            JSONObject k = new JSONObject(buildings.get(1).toString());
                            Log.i("TRACE", "Response getprice buildings yo" + price.getString("ll_min"));
                            if (!price.getString("ll_min").equalsIgnoreCase("")) {
                                if (!price.getString("ll_min").equalsIgnoreCase("0")) {
                                    Log.i("tt", "I am here" + 2);
                                    Log.i("TRACE", "RESPONSEDATAr" + response);
                                    llMin = Integer.parseInt(price.getString("ll_min"));
                                    llMax = Integer.parseInt(price.getString("ll_max"));
                                    Log.i("TRACE", "RESPONSEDATArr" + llMin);
                                    Log.i("TRACE", "RESPONSEDATArr" + llMax);
                                    llMin = 5 * (Math.round(llMin / 5));
                                    llMax = 5 * (Math.round(llMax / 5));
                                    Log.i("TRACE", "RESPONSEDATAr" + llMin);
                                    Log.i("TRACE", "RESPONSEDATAr" + llMax);
                                    orMin = Integer.parseInt(price.getString("or_min"));
                                    orMax = Integer.parseInt(price.getString("or_max"));
                                    Log.i("TRACE", "RESPONSEDATArr" + orMin);
                                    Log.i("TRACE", "RESPONSEDATArr" + orMax);
                                    orMin = 500 * (Math.round(orMin / 500));
                                    orMax = 500 * (Math.round(orMax / 500));
                                    Log.i("TRACE", "RESPONSEDATAr" + orMin);
                                    Log.i("TRACE", "RESPONSEDATAr" + orMax);
                                    BroadCastMinMaxValue(llMin, llMax, orMin, orMax);
                                    updateHorizontalPicker();
                                    marquee(500, 100);
                                    map.clear();
                                    buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                    mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
                                    txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
                                    search_building_icon.setVisibility(View.GONE);
                                    buildingIcon.setVisibility(View.GONE);
                                    fav.setVisibility(View.VISIBLE);
                                    StartOyeButtonAnimation();

                                    try {
                                        for (int i = 0; i < 5; i++) {
                                            JSONObject j = new JSONObject(buildings.get(i).toString());
                                            config[i] = j.getString("config");
                                            Log.i("Buildingdata", "config" + config[i]);
                                            name[i] = j.getString("name");
                                            Log.i("Buildingdata", "name" + name[i]);
                                            rate_growth[i] = j.getString("rate_growth");
                                            Log.i("Buildingdata", "rate_growth" + rate_growth[i]);
                                            or_psf[i] = Integer.parseInt(j.getString("or_psf"));
                                            Log.i("Buildingdata", "or_psf" + or_psf);
                                            ll_pm[i] = Integer.parseInt(j.getString("ll_pm"));
                                            id[i] = j.getString("id");
                                            Log.i("Buildingdata", "ll_pm" + ll_pm);
                                            double lat = Double.parseDouble(j.getJSONArray("loc").get(1).toString());
                                            Log.i("Buildingdata", "lat " + lat);
                                            double longi = Double.parseDouble(j.getJSONArray("loc").get(0).toString());
                                            Log.i("Buildingdata", "longi" + longi);
                                            loc[i] = new LatLng(lat, longi);
                                            Log.i("Buildingdata", "loc " + loc);
                                            Log.i("Buildingdata", "mCustomerMarker " + mCustomerMarker[i]);
                                            listing[i] = j.getString("listings");
                                            transaction[i] = j.getString("transactions");
                                            portal[i]=j.getString( "portals" );
                                            Log.i("Buildingdata", "listing transaction portal" +listing[i]+" "+transaction[i]+" "+ portal[i]);
                                            String customSnippet = rate_growth[i];
                                            mCustomerMarker[i] = map.addMarker(new MarkerOptions().position(loc[i]).title(name[i]).snippet(customSnippet).icon(icon1).flat(true));
                                            Log.i("TRACE", "mCustomerMarker after :" + mCustomerMarker[i]);
                                            flag[i] = false;
                                            dropPinEffect(mCustomerMarker[i]);

                                        }
                                        String building_count = jsonResponseData.getString("building_count");
                                        Log.i("Buildingdata", "loc " + building_count);
                                        SnackbarManager.show(
                                                Snackbar.with(getActivity())
                                                        .text("Displaying 5 buildings out of "+building_count)
                                                        .position(Snackbar.SnackbarPosition.TOP)
                                                        .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)), getActivity());
                                    } catch (Exception e) {

                                    }
                                    showFavourites();
                                    mVisits.setEnabled(true);
                                    txtFilterValue.setEnabled(true);
                                    disablepanel(true);
                                    horizontalPicker.setVisibility(View.VISIBLE);
                                    tv_building.setVisibility(View.VISIBLE);
                                    tvRate.setVisibility(View.VISIBLE);
                                    rupeesymbol.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setVisibility(View.GONE);
                                    missingArea.setVisibility(View.GONE);

                                } else {
                                    Log.i("tt", "I am here" + 3);

                                    map.clear();
                                    tv_building.setVisibility(View.INVISIBLE);
                                    horizontalPicker.setVisibility(View.GONE);
                                    tvRate.setVisibility(View.INVISIBLE);
                                    rupeesymbol.setVisibility(View.INVISIBLE);
                                    tvFetchingrates.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setText("Coming Soon...");
                                    missingArea.setVisibility(View.VISIBLE);
                                    mVisits.setEnabled(false);
                                    txtFilterValue.setEnabled(false);
                                    CancelAnimation();
                                }
                            } else {

                                map.clear();
                                tv_building.setVisibility(View.INVISIBLE);
                                horizontalPicker.setVisibility(View.GONE);
                                tvRate.setVisibility(View.INVISIBLE);
                                rupeesymbol.setVisibility(View.INVISIBLE);
                                tvFetchingrates.setVisibility(View.VISIBLE);
                                tvFetchingrates.setText("Coming Soon...");
                                missingArea.setVisibility(View.VISIBLE);
                                mVisits.setEnabled(false);
                                txtFilterValue.setEnabled(false);
                                CancelAnimation();
                                Log.i("GETPRICE", "Else mode ====== ");


                            }

                        }
                    } catch (Exception e) {
                        General.slowInternetFlag = false;
                        General.t.interrupt();

                        Log.i("Price Error", "Caught in exception getprice success" + e.getMessage());
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    General.slowInternetFlag = false;
                    map.clear();
                    tv_building.setVisibility(View.INVISIBLE);
                    horizontalPicker.setVisibility(View.GONE);
                    tvRate.setVisibility(View.INVISIBLE);
                    rupeesymbol.setVisibility(View.INVISIBLE);
                    tvFetchingrates.setVisibility(View.VISIBLE);
                    tvFetchingrates.setText("Coming Soon...");
                    missingArea.setVisibility(View.VISIBLE);
                    mVisits.setEnabled(false);
                    txtFilterValue.setEnabled(false);
                    CancelAnimation();
                    General.t.interrupt();
                    Log.i("getPrice", "retrofit failure getprice " + error.getMessage());

                }
            });

        } }
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

                case REQUEST_CALL_PHONE:
                     startActivity(callIntent);
                     break;


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

                    }


                    else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        lat=Double.parseDouble(SharedPrefs.getString(getContext(),SharedPrefs.MY_LAT));
                        lng=Double.parseDouble(SharedPrefs.getString(getContext(),SharedPrefs.MY_LNG));
                        LatLng latlng=new LatLng(lat,lng);
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13));
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
        p=position;
        if(!AppConstants.SETLOCATION && !savebuilding) {

            switch (position) {
                case 0:
                    int index = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                    Log.i( "indexxx", "index of layout : " + index );
                    if (index == 2) {
                        property_type_layout.clearAnimation();
                        parenttop.removeView( property_type_layout );
                        parentbottom.addView( property_type_layout, 5 );
                    }

                    PropertyButtonSlideAnimation();


                    marquee( 500, 100 );

                    SnackbarManager.show(
                            Snackbar.with( getContext() )
                                    .text( "Rental Property Type set" )
                                    .position( Snackbar.SnackbarPosition.TOP )
                                    .color( Color.parseColor( AppConstants.DEFAULT_SNACKBAR_COLOR ) ) );

                    tvRate.setText( "/ month" );
                    brokerType = "rent";
                    AppConstants.CURRENT_DEAL_TYPE = "rent";
                    dbHelper.save( DatabaseConstants.brokerType, "LL" );
                    dbHelper.save( "brokerType", "On Rent" );
                    recordWorkout.setBackgroundColor( Color.parseColor( "#2dc4b6" ) );
                    if (Property_type.equalsIgnoreCase( "" )) {
                        rental.setText( "Home" );
                        rental.setVisibility( View.VISIBLE );
                        resale.setVisibility( View.INVISIBLE );
                        property_type_layout.setVisibility( View.VISIBLE );

                    } else {
                        rental.setVisibility( View.VISIBLE );
                        resale.setVisibility( View.INVISIBLE );
                        rental.setText( Property_type );
                        property_type_layout.setVisibility( View.VISIBLE );
                    }


                    if (flag[INDEX] == true) {

                        tv_building.setVisibility( View.VISIBLE );
                        tv_building.setText( "Average Rate in last 1 WEEK" );
                        String text = "<font color=#ffffff>" + name[INDEX] + "</b></b></font> <font color=#ffffff>@</font>&nbsp&nbsp<font color=#ff9f1c>\u20B9" + General.currencyFormat( String.valueOf( ll_pm[INDEX] ) ).substring( 2, General.currencyFormat( String.valueOf( ll_pm[INDEX] ) ).length() ) + "</font><b><font color=#ff9f1c><sub>/m</sub></font>";
                        tvFetchingrates.setText( Html.fromHtml( text ) );

                    }
                    break;

                case 1:
                    if(!General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                        try {
                            new CountDownTimer(1000, 500) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    if (p == 1) {
                                        Intent intent = new Intent(getContext(), Game.class);
                                        startActivity(intent);
                                    }
                                }
                            }.start();

                        } catch (Exception e) {
                        }
                    }else{
                        marquee( 500, 100 );

                        int index1 = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                        Log.i( "indexxx", "index of layout : " + index1 );
                        if (index1 == 2) {
                            Log.i( "indexx", "inside if stmt" );
                            property_type_layout.clearAnimation();
                            parenttop.removeView( property_type_layout );
                            parentbottom.addView( property_type_layout, 5 );
                        }

                        PropertyButtonSlideAnimation();
                        SnackbarManager.show(
                                Snackbar.with( getContext() )
                                        .text( "Buy/Sell Property Type set" )
                                        .position( Snackbar.SnackbarPosition.TOP )
                                        .color( Color.parseColor( AppConstants.DEFAULT_SNACKBAR_COLOR ) ) );

                        updateHorizontalPicker();
                        tvRate.setText( "/ sq.ft" );
                        brokerType = "resale";
                        AppConstants.CURRENT_DEAL_TYPE = "resale";
                        dbHelper.save( DatabaseConstants.brokerType, "OR" );
                        dbHelper.save( "brokerType", "For Sale" );

                        if (Property_type.equalsIgnoreCase( "" )) {
                            rental.setText( "Home" );
                            resale.setVisibility( View.VISIBLE );
                            rental.setVisibility( View.INVISIBLE );
                            property_type_layout.setVisibility( View.VISIBLE );

                        } else {
                            resale.setText( Property_type );
                            resale.setVisibility( View.VISIBLE );
                            rental.setVisibility( View.INVISIBLE );
                            property_type_layout.setVisibility( View.VISIBLE );

                        }

                        if (flag[INDEX] == true) {
                            tv_building.setVisibility( View.VISIBLE );
                            tv_building.setText( "Average Rate in last 1 WEEK" );
                            String text = "<font color=#ffffff>" + name[INDEX] + "</b></b></font> <font color=#ffffff> @ </font>&nbsp<font color=#ff9f1c>\u20B9" + General.currencyFormat( String.valueOf( or_psf[INDEX] ) ).substring( 2, General.currencyFormat( String.valueOf( or_psf[INDEX] ) ).length() ) + "</font><b><font color=#ff9f1c><sub>/sq.ft</sub></font>";
                            tvFetchingrates.setText( Html.fromHtml( text ) );
                        }


                        break;
                    }

                    break;

                case 2:
                    marquee( 500, 100 );

                    int index1 = ((ViewGroup) property_type_layout.getParent()).indexOfChild( property_type_layout );
                    Log.i( "indexxx", "index of layout : " + index1 );
                    if (index1 == 2) {
                        Log.i( "indexx", "inside if stmt" );
                        property_type_layout.clearAnimation();
                        parenttop.removeView( property_type_layout );
                        parentbottom.addView( property_type_layout, 5 );
                    }

                    PropertyButtonSlideAnimation();
                    SnackbarManager.show(
                            Snackbar.with( getContext() )
                                    .text( "Buy/Sell Property Type set" )
                                    .position( Snackbar.SnackbarPosition.TOP )
                                    .color( Color.parseColor( AppConstants.DEFAULT_SNACKBAR_COLOR ) ) );

                    updateHorizontalPicker();
                    tvRate.setText( "/ sq.ft" );
                    brokerType = "resale";
                    AppConstants.CURRENT_DEAL_TYPE = "resale";
                    dbHelper.save( DatabaseConstants.brokerType, "OR" );
                    dbHelper.save( "brokerType", "For Sale" );

                    if (Property_type.equalsIgnoreCase( "" )) {
                        rental.setText( "Home" );
                        resale.setVisibility( View.VISIBLE );
                        rental.setVisibility( View.INVISIBLE );
                        property_type_layout.setVisibility( View.VISIBLE );

                    } else {
                        resale.setText( Property_type );
                        resale.setVisibility( View.VISIBLE );
                        rental.setVisibility( View.INVISIBLE );
                        property_type_layout.setVisibility( View.VISIBLE );

                    }

                    if (flag[INDEX] == true) {
                        tv_building.setVisibility( View.VISIBLE );
                        tv_building.setText( "Average Rate in last 1 WEEK" );
                        String text = "<font color=#ffffff>" + name[INDEX] + "</b></b></font> <font color=#ffffff> @ </font>&nbsp<font color=#ff9f1c>\u20B9" + General.currencyFormat( String.valueOf( or_psf[INDEX] ) ).substring( 2, General.currencyFormat( String.valueOf( or_psf[INDEX] ) ).length() ) + "</font><b><font color=#ff9f1c><sub>/sq.ft</sub></font>";
                        tvFetchingrates.setText( Html.fromHtml( text ) );
                    }


                    break;
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
        Log.i("localityBroadcast","addresses latlng "+lat1+" "+lng1);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat1), Double.parseDouble(lng1), 1);
        } catch (IOException e) {
            Log.i(TAG,"Caught in exception in getRegion 1"+e);
        }
        try {
            Log.i("localityBroadcast","addresses "+addresses);
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
            Log.i(TAG,"Caught in exception in getRegion"+e);
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
        Log.i("sushil","onposition    ====  ");
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

            Log.i("savebuilding","savebuilding: "+fullAddress);
          return fullAddress;
          // return address;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            autoCompView.setText(s);
            addressBar.setText(s);
            favAdrs.setText(s);
            Log.i("", "");
            autoCompView.dismissDropDown();
            autoCompView.setCursorVisible(false);
            // new LocationUpdater().execute();
            Log.i(TAG,"locality automata ");
            try {

                if(savebuilding)
                tv_building.setText(fullAddress);
                getRegion();
                if(!savebuilding) {
                    if (autoIsClicked == true) {
                        Log.i(TAG, "locality automata " + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY));

                        getPrice();
                        autoIsClicked = false;
                        buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                    }
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
        llmin=roundoff1(llmin);
        orMin=roundoff1(orMin);
        llmax=roundoff1(llmax);
        orMax=roundoff1(orMax);

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
                          .text("Check rate of selected locality")
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
                                                    .text("Press oye button to post your requirement")
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
            tv_building.setText(Html.fromHtml("<font color='#2dc4b6'>Avg Rate</font> @ " + locality + " | Area " + area + "sqft."));
        }else{
            Log.i(TAG,"buildingTextChange else called");
            tv_building.setText(Html.fromHtml("<font color='#2dc4b6'>Avg Rate</font> @ Andheri | Area " + area + "sqft."));

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







   /* public void Wlak_Beacon() throws InterruptedException {


        *//*if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true") && SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON).equalsIgnoreCase("true")) {

            beaconAlert(rootView);
            tutorialAlert(rootView);


        }else if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_WALKTHROUGH).equalsIgnoreCase("true")){
            tutorialAlert(rootView);
        }else{
            beaconAlert(rootView);
        }*//*
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
*/

    private void onMapDrag(final MotionEvent motionEvent) {



            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if(!AppConstants.SETLOCATION &&!savebuilding) {
                    tvRate.setVisibility(View.GONE);
                    rupeesymbol.setVisibility(View.GONE);

                    horizontalPicker.keepScrolling();
                    horizontalPicker.stopScrolling();
                }
                Log.i("MotionEvent.ACTION_MOVE", "=========================");
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    horizontalPicker.stopScrolling();
//                    marquee(500,100);

                updateHorizontalPicker();
                if (!spanning) {
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



                                if (!AppConstants.SETLOCATION && !savebuilding) {
                                    txtFilterValue.setTextSize(13);
                                    txtFilterValue.setTextColor(Color.parseColor("white"));
                                    txtFilterValue.setText(oyetext);
                                    ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
                                    Intent in = new Intent(AppConstants.MARKERSELECTED);
                                    in.putExtra("markerClicked", "false");
                                    buildingSelected = true;

                                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
//                            txtFilterValue.setTextColor(Color.parseColor("white"));
                                    txtFilterValue.setText(oyetext);

                                    tvFetchingrates.setVisibility(View.VISIBLE);
                                    mMarkerminmax.setVisibility(View.VISIBLE);
                                    tvRate.setVisibility(View.VISIBLE);
                                    rupeesymbol.setVisibility(View.VISIBLE);
                                    tvFetchingrates.setVisibility(View.VISIBLE);
                                    buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);

                                    tv_building.setVisibility(View.VISIBLE);
                                    recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                                }

                                LatLng currentLocation1; //= new LatLng(location.getLatitude(), location.getLongitude());
                                Log.i("map", "============ map:" + " " + map);
//                            currentLocation1 = map.getProjection().fromScreenLocation(point);
                            /*VisibleRegion visibleRegion = map.getProjection()
                                    .getVisibleRegion();

                            Point x1 = map.getProjection().toScreenLocation(visibleRegion.farRight);

                            Point y1 = map.getProjection().toScreenLocation(visibleRegion.nearLeft);


                            centerPoint = new Point(x1.x / 2, y1.y / 2);
                            Log.i("centerPoint","centerPoint1 : "+centerPoint);*/


                                LatLng centerFromPoint = map.getProjection().fromScreenLocation(
                                        centerPoint);
                                currentLocation1 = centerFromPoint;
                                lat = currentLocation1.latitude;
                                Log.i("t1", "lat" + " " + lat);
                                lng = currentLocation1.longitude;
                                Log.i("t1", "lng" + " " + lng);
//                                map.addMarker(new MarkerOptions().title("hey").position(currentLocation1));
                                Log.i("MARKER-- ", "====================================");
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LAT, lat + "");
                                SharedPrefs.save(getActivity(), SharedPrefs.MY_LNG, lng + "");
                                General.setSharedPreferences(getContext(), AppConstants.MY_LAT, lat + "");
                                General.setSharedPreferences(getContext(), AppConstants.MY_LNG, lng + "");
                                Log.i("t1", "Sharedpref_lat" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
                                Log.i("t1", "Sharedpref_lng" + SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
                                getRegion();

                                if (!AppConstants.SETLOCATION && !savebuilding) {
                                    search_building_icon.setVisibility(View.GONE);
                                    buildingIcon.setVisibility(View.GONE);
                                    fav.setVisibility(View.VISIBLE);
                                    horizontalPicker.stopScrolling();
                                    missingArea.setVisibility(View.GONE);
                                    getPrice();
                                }

                            new LocationUpdater().execute();
                            if(txtFilterValue.getText().toString().equalsIgnoreCase("done")) {
                                txtFilterValue.setText("SAVE");
                                txt_info.setText("Find Building on Map & Save");
                                map.clear();
                            }
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
                spanning = false;

            } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if(!AppConstants.SETLOCATION && !savebuilding) {
                    lastTouched = SystemClock.uptimeMillis();
                    map.getUiSettings().setScrollGesturesEnabled(true);
                    //LatLng currentLocation11;
                    Log.i("MotionEvent.ACTION_DOWN", "=========================");
                }
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
//            property_type_layout.setEnabled(false);
            ani = zoomout_left;
            property_type_layout.startAnimation(ani);

        } else {
//            property_type_layout.setEnabled(false);
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
//                property_type_layout.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });

    }



    protected void PropertyButtonSlideAnimation() {



        property_type_layout.startAnimation(slide_up1);
        pro_click=false;
        slide_up1.setAnimationListener(new Animation.AnimationListener() {
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

        MarkerClickEnable=false;
        tvRate.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.GONE);
        txtFilterValue.setEnabled(false);
        mVisits.setEnabled(false);
        disablepanel(false);

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
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=19.122848,72.8347977&radius=1000&types=ATM&sensor=true&key=AIzaSyD9u7py1PGKcnlrO77NuY_40jxgIOhX34I
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

        Log.i("onMapclicked","Inside onMapclicked   ");

        for (int i = 0; i < 5; i++) {
            if (flag[i] == true) {

                mVisits.setEnabled(true);
                txtFilterValue.setEnabled(true);
                tvRate.setVisibility(View.VISIBLE);
                rupeesymbol.setVisibility(View.VISIBLE);
                mCustomerMarker[i].setIcon(icon1);
                mCustomerMarker[i].hideInfoWindow();
                ((ClientMainActivity)getActivity()).CloseBuildingOyeComfirmation();
                Intent in = new Intent(AppConstants.MARKERSELECTED);
                in.putExtra("markerClicked", "false");
                buildingSelected = true;
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
                search_building_icon.setVisibility(View.GONE);
                buildingIcon.setVisibility(View.GONE);
                fav.setVisibility(View.VISIBLE);
                flag[i] = false;
                horizontalPicker.setVisibility(View.VISIBLE);
                tvFetchingrates.setVisibility(View.GONE);
                recordWorkout.setBackgroundColor(Color.parseColor("#2dc4b6"));
                tvRate.setVisibility(View.VISIBLE);
                buildingTextChange(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LOCALITY), filterValueMultiplier);
                mVisits.setBackground(getContext().getResources().getDrawable(R.drawable.bg_animation));
//                            mVisits.startAnimation(zoomin_zoomout);
                StartOyeButtonAnimation();
                updateHorizontalPicker();
                txtFilterValue.setTextSize(13);

                txtFilterValue.setTextColor(Color.parseColor("white"));
                txtFilterValue.setText(oyetext);
                txtFilterValue.setBackground(getContext().getResources().getDrawable(R.drawable.oye_button_border));
//                txtFilterValue.setTextColor(Color.parseColor("white"));
                txtFilterValue.setText(oyetext);
//                txtFilterValue.setText("sushil");
                Log.i("onMapclicked","Inside onMapclicked 909099099099  "+oyetext);
                ll_marker.setEnabled(true);
                tv_building.setVisibility(View.VISIBLE);
            }
        }

    }

    public void OnOyeClick(){
        if(AppConstants.SETLOCATION)
        {
            autoOk();
        }else if(!savebuilding){



        openOyeScreen();
        CancelAnimation();

        AppConstants.GOOGLE_MAP = map;
        if (clicked == true) {
            MarkerClickEnable=false;
            oyebuttonBackgrountColorOrange();
            clicked = false;
            map.getUiSettings().setAllGesturesEnabled(false);
            mHelperView.setEnabled(false);
        } else {
            oyebuttonBackgrountColorGreenishblue();
            map.getUiSettings().setAllGesturesEnabled(true);
            mHelperView.setEnabled(true);
            MarkerClickEnable=true;
            clicked = true;

        }
        if (RatePanel == true) {
            UpdateRatePanel();
            RatePanel = false;
        } else {
            /*horizontalPicker.setVisibility(View.GONE);
            tvRate.setVisibility(View.GONE);
            rupeesymbol.setVisibility(View.GONE);
            tvFetchingrates.setVisibility(View.VISIBLE);*/
            RatePanel = true;
//            tvFetchingrates.setVisibility(View.VISIBLE);
        }
    }
    }

    public void walkBeaconStatus(){
    if (SharedPrefs.getString(getContext(), SharedPrefs.CHECK_BEACON).equalsIgnoreCase("")) {
       // beacon = "true";
        beacon = "false";  // beacon disabled
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
//         if(!General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker"))
         ((ClientMainActivity)getActivity()).showCard();
     }



}


    public void OnOyeClick1(){
        openOyeScreen();
        CancelAnimation();
        AppConstants.GOOGLE_MAP = map;
//        if (clicked == true) {
        map.clear();
        oyebuttonBackgrountColorOrange();
        clicked = false;
        txtFilterValue.setEnabled(true);
        mVisits.setEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(false);
        mHelperView.setEnabled(false);
        horizontalPicker.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.GONE);
        tvFetchingrates.setVisibility(View.VISIBLE);
//        } else {
//            oyebuttonBackgrountColorGreenishblue();
//            customMapFragment.getMap().getUiSettings().setAllGesturesEnabled(true);
//            mHelperView.setEnabled(true);
//            clicked = true;
//
//        }
//        if (RatePanel == true) {
//            UpdateRatePanel();
//            RatePanel = false;
//        } else {
//            RatePanel = true;
////            tvFetchingrates.setVisibility(View.VISIBLE);
//        }

    }


    private void StartOyeButtonAnimation() {
//        Log.i("starting timer", " " + timer);
        if (timer == null) {
            timer = new Timer();
//            Log.i("starting timer2", " " + timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Log.i("starting timer1", " " + timer);
                                mVisits.startAnimation(zoomin_zoomout);
                            }
                        });
                    }
                }
            }, 1000, 1000);

        }
    }



    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    Log.i(TAG,"building drop ");
                   // marker.showInfoWindow();

                }
            }
        });
    }

    @OnClick({R.id.ll_marker, R.id.markerpanelminmax, R.id.picker, R.id.tv_building, R.id.tvRate, R.id.rupeesymbol, R.id.tvFetchingRates})
    public void onOptionClickM(View v) {
        Log.i(TAG,"I am clicked "+v +" "+buildingSelected);
if(buildingSelected)
     OnOyeClick();

    }

    public void disablepanel(Boolean x){
        ll_marker.setEnabled(x);
        mMarkerminmax.setEnabled(x);
        horizontalPicker.setEnabled(x);
        tv_building.setEnabled(x);
        tvRate.setEnabled(x);
        rupeesymbol.setEnabled(x);
        tvFetchingrates.setEnabled(x);

    }

    public void showHidepanel(Boolean x){
        if(x){
            /*ll_marker.setVisibility(View.VISIBLE);
            mMarkerminmax.setVisibility(View.VISIBLE);*/
            horizontalPicker.setVisibility(View.VISIBLE);
            tv_building.setVisibility(View.GONE);
            tvRate.setVisibility(View.VISIBLE);
            rupeesymbol.setVisibility(View.VISIBLE);
            tvFetchingrates.setVisibility(View.GONE);
            CallButton.setClickable(true);
            fav.setClickable(true);
        }else{
            /*ll_marker.setVisibility(View.GONE);
            mMarkerminmax.setVisibility(View.GONE);*/
            horizontalPicker.setVisibility(View.GONE);
            tv_building.setVisibility(View.VISIBLE);
            tvRate.setVisibility(View.GONE);
            rupeesymbol.setVisibility(View.GONE);
            tvFetchingrates.setVisibility(View.VISIBLE);
            tv_building.setText("search or drag on map to set location");
            tvFetchingrates.setText("Click OYE to reach brokers");
            CallButton.setClickable(false);
            fav.setClickable(false);
            CallButton.setVisibility(View.GONE);

        }

    }

    @OnClick({R.id.addressPanel,R.id.ic_search})
    public void onOptionClickS(View v){
    searchFragment c = new searchFragment();
    AppConstants.SEARCHFLAG = true;
    loadFragmentAnimated(c, null, R.id.container_Signup, "Search");
    if(!AppConstants.SETLOCATION && !savebuilding) {
        ((ClientMainActivity) getActivity()).closeOyeConfirmation();
        ((ClientMainActivity) getActivity()).closeOyeScreen();
        Intent in = new Intent(AppConstants.MARKERSELECTED);
        in.putExtra("markerClicked", "false");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(in);
        ((ClientMainActivity) getActivity()).CloseBuildingOyeComfirmation();
        onMapclicked();
    }
}


    @OnClick({R.id.favSave, R.id.favCancel})
    public void onOptionClick(View v) {

        Log.i(TAG,"fav2 yo");
        if (v.getId() == favSave.getId()) {

            Log.i(TAG,"fav2 save");
            favboard.clearAnimation();
            favboard.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(favboard.getWindowToken(), 0);

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(SharedPrefs.getString(getContext(),SharedPrefs.MY_LAT)), Double.parseDouble(SharedPrefs.getString(getContext(),SharedPrefs.MY_LNG))))
                    .title(favTitle)
                    .icon(favIcon));

            try {
                Realm myRealm = General.realmconfig(getContext());
                Favourites favourites = new Favourites();
                favourites.setTitle(favTitle);
                favourites.setAddress(favAdrs.getText().toString());
                LatiLongi latlon = new LatiLongi();
                latlon.setLat(Double.parseDouble(SharedPrefs.getString(getContext(), SharedPrefs.MY_LAT)));
                latlon.setLng(Double.parseDouble(SharedPrefs.getString(getContext(), SharedPrefs.MY_LNG)));
                favourites.setLatiLongi(latlon);

                Log.i(TAG,"fav2 3 latlng "+Double.parseDouble(SharedPrefs.getString(getContext(), SharedPrefs.MY_LAT))+" "+Double.parseDouble(SharedPrefs.getString(getContext(), SharedPrefs.MY_LNG)));
                Log.i(TAG,"fav2 latlng "+Double.parseDouble(SharedPrefs.getString(getContext(), SharedPrefs.MY_LAT))+" "+Double.parseDouble(SharedPrefs.getString(getContext(), SharedPrefs.MY_LNG)));
              if(myRealm.isInTransaction())
                   myRealm.cancelTransaction();
                myRealm.beginTransaction();
                myRealm.copyToRealmOrUpdate(favourites);
                myRealm.commitTransaction();

               RealmResults<Favourites> results1 =
                        myRealm.where(Favourites.class).findAll();

                for (Favourites c : results1) {
                    Log.i(TAG, "insiderr2 ");
                    Log.i(TAG, "insiderr3 " + c.getTitle());
                    Log.i(TAG, "insiderr4 " + c.getLatiLongi().getLat());
                    Log.i(TAG, "insiderr4 " + c.getLatiLongi().getLng());
                }

            }
            catch(Exception e){
                Log.i(TAG,"Caught in exception Favourites Realm "+e );
            }


        }
        else if (v.getId() == favCancel.getId()) {

            Log.i(TAG,"fav2 cancel");
            favboard.clearAnimation();
            favboard.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(favboard.getWindowToken(), 0);

        }


    }



   private void setDealStatus2(Context c){
       Log.i("updateStatus CALLED","updateStatus success called ");
       UpdateStatus updateStatus = new UpdateStatus();



       updateStatus.setOkId("okId");
       updateStatus.setStatus("dealStatus");
       updateStatus.setLast_seen("lastseen");
       updateStatus.setBlocked_by("blockBy");


          RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json;versions=1");
                        if ( General.getSharedPreferences(getContext(),AppConstants.token) != null) {
                            String token = General.getSharedPreferences(getContext(),AppConstants.token);
                            request.addHeader("Token", token);
                        }
                    }
                })
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.updateStatus1(updateStatus, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("updateStatus CALLED","updateStatus success ");



                    JsonObject k = jsonElement.getAsJsonObject();
                    try {

                        Log.i("updateStatus","updateStatus success response "+response);


                        JSONObject ne = new JSONObject(k.toString());
                        General.setSharedPreferences(getContext(),AppConstants.token,ne.getString("token"));
                        setDealStatus3(getContext());
                        Log.i("updateStatus","updateStatus success ne "+ne);


                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("updateStatus CALLED","updateStatus Failed "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("BROKER BUILDINGS CALLED","update status failed "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the the"+ e.getMessage());
        }

    }

    private void setDealStatus3(Context c){
        Log.i("updateStatus CALLED","updateStatus3 success called ");
        UpdateStatus updateStatus = new UpdateStatus();



        updateStatus.setOkId("okId");
        updateStatus.setStatus("dealStatus");
        updateStatus.setLast_seen("lastseen");
        updateStatus.setBlocked_by("blockBy");


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json;versions=1");
                        if ( General.getSharedPreferences(getContext(),AppConstants.token) != null) {
                            String token = General.getSharedPreferences(getContext(),AppConstants.token);
                            request.addHeader("Token", token);
                        }
                    }
                })
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.updateStatus2(updateStatus, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("updateStatus CALLED","updateStatus3 success ");



                    JsonObject k = jsonElement.getAsJsonObject();
                    try {

                        Log.i("updateStatus","updateStatus3 success response "+response);


                        JSONObject ne = new JSONObject(k.toString());
//                        General.setSharedPreferences(getContext(),AppConstants.token,ne.getString("token"));
                        Log.i("updateStatus","updateStatus3 success ne "+ne);


                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("updateStatus CALLED","updateStatus3 Failed "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("BROKER BUILDINGS CALLED","update status failed "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the the"+ e.getMessage());
        }

    }

    private void autoOk(){
        Log.i("AUTO OK CALLED","autook 1 is "+General.getSharedPreferences(getContext(),AppConstants.MY_LAT)+" "+General.getSharedPreferences(getContext(),AppConstants.MY_LNG)+" "+General.getSharedPreferences(getContext(), AppConstants.LOCALITY));

        AutoOk autoOk = new AutoOk();
        autoOk.setGcm_id(General.getSharedPreferences(getContext(),AppConstants.GCM_ID));
        autoOk.setLat(General.getSharedPreferences(getContext(),AppConstants.MY_LAT));
        autoOk.setLng(General.getSharedPreferences(getContext(),AppConstants.MY_LNG));
        autoOk.setUser_id(General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));
        autoOk.setPlatform("android");
        autoOk.setLocality(General.getSharedPreferences(getContext(), AppConstants.LOCALITY));
        autoOk.setEmail("ritesh@nexchanges1.com");
        autoOk.setReq_avl(AppConstants.Card_REQ_AVL);
        autoOk.setTt(AppConstants.Card_TT);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.autoOk(autoOk, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("AUTO OK CALLED","autook success "+General.getSharedPreferences(getContext(),AppConstants.TIME_STAMP_IN_MILLI));



                    JsonObject k = jsonElement.getAsJsonObject();
                    try {

                        Log.i("AUTOOK CALLED","autook success response 23 "+response);

                        JSONObject ne = new JSONObject(k.toString());
//                        JSONObject neo = ne.getJSONObject("responseData");
//                        Log.i("AUTOOK CALLED","autook response "+neo);
                        Log.i("AUTOOK CALLED","autook response "+ne);


                        if(ne.getString("success").equalsIgnoreCase("true")){
                            JSONArray ne1 = ne.getJSONObject("responseData").getJSONArray("ok_ids");
                            Log.i("AUTOOK CALLED","autook response 24 "+ne1);

                            General.setBadgeCount(getContext(), AppConstants.HDROOMS_COUNT,ne1.length());
                            //Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getString("message"));
                            //  Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getJSONArray("ok_ids"));
                            // Log.i("AUTOOK CALLED","autook responser "+ne.getJSONObject("responseData").getJSONArray("ok_ids").toJSONArray());

                            TastyToast.makeText(getContext(), "We have connected you with "+ne1.length()+" brokers in your area.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            TastyToast.makeText(getContext(), "Sign up to connect with "+(10 - ne1.length())+" more brokers waiting for you.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);


                        /*Log.i("BROKER BUILDINGS CALLED","success ne "+ne);
                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData"));



                        Log.i("BROKER BUILDINGS CALLED","success ne "+ne.getJSONObject("responseData").getString("coupon"));
                        coupon.setText(ne.getJSONObject("responseData").getString("coupon"));*/
                            // getFragmentManager().popBackStack();



                            General.setSharedPreferences(getContext(),AppConstants.STOP_CARD,"yes");

                            Intent inten = new Intent(getContext(), ClientMainActivity.class);
                            inten.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppConstants.SETLOCATION = false;
                            startActivity(inten);

                        }


                    }
                    catch (JSONException e) {
                        Log.e("TAG","Something went wrong "+e.getMessage());
                        TastyToast.makeText(getContext(), "Something went wrong.", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
//                        TastyToast.makeText(getContext(), "Please signup to get connected with 10 brokers waiting for you", TastyToast.LENGTH_LONG, TastyToast.INFO);
                        Intent inten = new Intent(getContext(), ClientMainActivity.class);
                        inten.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppConstants.SETLOCATION = false;
                        startActivity(inten);
                        Log.i("BROKER AUTOOK CALLED ","autook Failed "+e.getMessage());
                    }




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("AUTOOK CALLED","coupon fail "+error);

                }
            });


        }
        catch (Exception e){
            Log.e("AUTO OK CALLED", e.getMessage());
        }




    }


    private void showFavourites(){

        BitmapDescriptor favIcon;

        try {
           Realm myRealm = General.realmconfig(getContext());
            RealmResults<Favourites> results1 =
                    myRealm.where(Favourites.class).findAll();

            for (Favourites c : results1) {
                Log.i(TAG, "insiderr2 ");
                Log.i(TAG, "insiderr3 " + c.getTitle());
                Log.i(TAG, "insiderr4 " + c.getLatiLongi().getLat());
                Log.i(TAG, "insiderr4 " + c.getLatiLongi().getLng());
                if(c.getTitle().equalsIgnoreCase("My home"))
                    favIcon = iconHome;
                else if(c.getTitle().equalsIgnoreCase("My office"))
                    favIcon = iconOffice;
                else
                    favIcon = iconOther;
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(c.getLatiLongi().getLat(), c.getLatiLongi().getLng()))
                        .title(c.getTitle())
                        .snippet(c.getTitle())
                        .icon(favIcon));
                dropPinEffect(marker);
            }

        }
        catch(Exception e){
            Log.i(TAG,"Caught in exception Favourites Realm "+e );
        }


    }


    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void SaveBuildingDataToRealm(){




            Realm myRealm = General.realmconfig( getContext() );
            MyPortfolioModel myPortfolioModel = new MyPortfolioModel();

            myPortfolioModel.setName( name[INDEX] );
            myPortfolioModel.setConfig( config[INDEX] );
            myPortfolioModel.setLat( loc[INDEX].latitude + "" );
            myPortfolioModel.setLng( loc[INDEX].longitude + "" );


        if(brokerType=="rent") {
            myPortfolioModel.setId( id[INDEX] );
            myPortfolioModel.setLl_pm( ll_pm[INDEX] );

        }else{
            myPortfolioModel.setId( id[INDEX]+"1" );
            myPortfolioModel.setOr_psf( or_psf[INDEX] );
        }
        /*if(brokerType=="rent" && or_psf[INDEX]!=0) {
            myPortfolioModel.setLl_pm( ll_pm[INDEX] );
            myPortfolioModel.setOr_psf( or_psf[INDEX] );
            //myPortfolioModel.setOr_psf( 0 );
        }else if(brokerType=="rent" && or_psf[INDEX]==0){
            myPortfolioModel.setLl_pm( ll_pm[INDEX] );
            myPortfolioModel.setOr_psf(0);
        }else if(brokerType=="resale" && ll_pm[INDEX]!=0) {
            myPortfolioModel.setLl_pm( ll_pm[INDEX] );
            myPortfolioModel.setOr_psf( or_psf[INDEX] );
        }else if(brokerType=="resale" && ll_pm[INDEX]==0){
            myPortfolioModel.setOr_psf( or_psf[INDEX] );
            myPortfolioModel.setLl_pm(0);
        }*/
            myPortfolioModel.setPortals( portal[INDEX] );
            myPortfolioModel.setListing( listing[INDEX] );
            myPortfolioModel.setRate_growth( rate_growth[INDEX] );
            myPortfolioModel.setTransactions( transaction[INDEX] );
            myPortfolioModel.setLocality( SharedPrefs.getString( getContext(), SharedPrefs.MY_LOCALITY ) );
            myRealm.beginTransaction();
            myRealm.copyToRealmOrUpdate( myPortfolioModel );
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
            myRealm.commitTransaction();



    }



    public void saveBuiding(String b_name){

        B_name=b_name;
        savebuilding=true;
        map.clear();
        new LocationUpdater().execute();
        horizontalPicker.setVisibility(View.GONE);
        rupeesymbol.setVisibility(View.GONE);
        tvRate.setVisibility(View.GONE);
        txtFilterValue.setText("SAVE");
        txt_info.setText("Find Building on Map & Save");
        tv_building.setText(fullAddress);
        tvFetchingrates.setVisibility(View.VISIBLE);
        txt_info.setVisibility(View.VISIBLE);
        CallButton.setVisibility(View.GONE);
        addbuilding.setVisibility(View.GONE);
        String txt;
        txt="<font color=#2dc4b6><big>Drag & Save Building Location</big></font>";
        tvFetchingrates.setText(Html.fromHtml(txt));
    }

    public void  ResetChanges(){
      savebuilding=false;
      txt_info.setVisibility(View.GONE);
      tvFetchingrates.setVisibility(View.GONE);
      horizontalPicker.setVisibility(View.VISIBLE);
      rupeesymbol.setVisibility(View.VISIBLE);
      tvRate.setVisibility(View.VISIBLE);
      CallButton.setVisibility(View.VISIBLE);
      addbuilding.setVisibility(View.VISIBLE);
      txtFilterValue.setText("2BHK");
      AppConstants.PROPERTY="Home";
      /*txtFilterValue.setText("SAVE");
      tv_building.setText("hey sushil");*/

    }


    public void AddBuildingDataToRealm(){

        Realm myRealm = General.realmconfig( getContext());
        addBuilding add_Building = new addBuilding();
        add_Building.setTimestamp(String.valueOf(SystemClock.currentThreadTimeMillis()));
        add_Building.setBuilding_name(B_name);
        add_Building.setConfig(General.getSharedPreferences(getContext(),AppConstants.PROPERTY_CONFIG));
        add_Building.setProperty_type(AppConstants.PROPERTY);
        add_Building.setLat( lat + "" );
        add_Building.setLng( lng + "" );
        add_Building.setLocality( SharedPrefs.getString( getContext(), SharedPrefs.MY_LOCALITY ) );
        myRealm.beginTransaction();
        myRealm.copyToRealmOrUpdate(add_Building);
//        myRealm.copyToRealmOrUpdate((Iterable<RealmObject>) myPortfolioModel);
        myRealm.commitTransaction();
    }


    public void Addbuilding()
    {
        Log.i("updateStatus CALLED","updateStatus success called ");
        AddBuildingModel addBuildingModel = new AddBuildingModel();
        addBuildingModel.setBuilding(B_name);
        addBuildingModel.setLat(lat+"");
        addBuildingModel.setLng(lng+"");
        addBuildingModel.setLocality(SharedPrefs.getString( getContext(), SharedPrefs.MY_LOCALITY ));
        addBuildingModel.setUser_role(General.getSharedPreferences(getContext(),AppConstants.ROLE_OF_USER));
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AppConstants.SERVER_BASE_URL_103).build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

//        UserApiService userApiService = restAdapter.create(UserApiService.class);


        /*userApiService.addBuilding(AddBuildingModel, new retrofit.Callback<JsonElement>() {*/




        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);


        try {
            oyeokApiService.addBuilding(addBuildingModel, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    Log.i("magic","addBuilding success ");



                    JsonObject k = jsonElement.getAsJsonObject();
                    AddBuildingDataToRealm();
                   /* try {

                        Log.i("magic","addBuilding success response "+response);


                        JSONObject ne = new JSONObject(k.toString());
                        General.setSharedPreferences(getContext(),AppConstants.token,ne.getString("token"));
                        setDealStatus3(getContext());
                        Log.i("magic","addBuilding success ne "+ne);


                    }
                    catch (JSONException e) {
                        Log.e("TAG", e.getMessage());
                        Log.i("magic","addBuilding Failed "+e.getMessage());
                    }*/




                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("magic","addBuilding failed "+error);
                }
            });


        }
        catch (Exception e){
            Log.e("TAG", "Caught in the the"+ e.getMessage());
        }

    }







}


















