package com.nbourses.oyeok.fragments;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nbourses.oyeok.Database.DBHelper;
import com.nbourses.oyeok.Database.DatabaseConstants;
import com.nbourses.oyeok.Database.SharedPrefs;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.services.AcceptOkCall;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OnAcceptOkSuccess;
import com.nbourses.oyeok.RPOT.ApiSupport.services.OyeokApiService;
import com.nbourses.oyeok.RPOT.OkBroker.CircularSeekBar.CircularSeekBarNew;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedListener;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.CustomPhasedSeekBar;
import com.nbourses.oyeok.RPOT.PriceDiscovery.UI.PhasedSeekBarCustom.SimpleCustomPhasedAdapter;
import com.nbourses.oyeok.SignUp.SignUpFragment;
import com.nbourses.oyeok.activities.BrokerDealsListActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.nbourses.oyeok.helpers.General;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrokerPreokFragment extends Fragment implements CustomPhasedListener, CircularSeekBarNew.imageAction, OnAcceptOkSuccess {

    @Bind(R.id.phasedSeekBar)
    CustomPhasedSeekBar mCustomPhasedSeekbar;

    @Bind(R.id.txtOption1)
    TextView txtOption1;

    @Bind(R.id.txtOption2)
    TextView txtOption2;

    @Bind(R.id.displayOkText)
    TextView displayOkText;

    @Bind(R.id.rentText)
    TextView rentText;

    @Bind(R.id.contactText)
    TextView contactText;

    @Bind(R.id.pickContact)
    Button pickContact;

    @Bind(R.id.okButton)
    Button okButton;

    @Bind(R.id.deal)
    Button deal;

    @Bind(R.id.notClicked)
    LinearLayout notClicked;

    @Bind(R.id.circularSeekbar)
    CircularSeekBarNew circularSeekbar;

    private static final String TAG = "BrokerPreokFragment";
    private static final int REQUEST_CODE_TO_SELECT_CLIENT = 302;

    private JSONArray jsonArrayReqLl;
    private JSONArray jsonArrayReqOr;
    private JSONArray jsonArrayAvlLl;
    private JSONArray jsonArrayAvlOr;
    private JSONArray jsonArrayPreokRecent;

    private String strTenants = "Tenants";
    private String strOwners = "Owners";
    private String strSeekers = "Buyer";
    private String strSeller = "Seller";

    private final int currentCount = 2; //TODO: need to discuss with team
    private int currentSeekbarPosition = 0; //default is rental
    private String currentOptionSelectedString = strTenants; //default is Tenants
    private TextView txtPreviouslySelectedOption;
    private JSONArray jsonObjectArray;
    private int selectedItemPosition;
    private DBHelper dbHelper;

    public BrokerPreokFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_broker_preok, container, false);
        ButterKnife.bind(this, v);

        init();

        return v;
    }

    private void init() {
        mCustomPhasedSeekbar.setAdapter(new SimpleCustomPhasedAdapter(getActivity().getResources(),
                new int[]{R.drawable.real_estate_selector, R.drawable.broker_type2_selector},
                new String[]{"30", "15"},
                new String[]{"Rental", "Sale"
                }));
        mCustomPhasedSeekbar.setListener(this);

        txtPreviouslySelectedOption = txtOption1;
        txtPreviouslySelectedOption.setBackgroundResource(R.color.greenish_blue);

        txtOption1.setText(strTenants);
        txtOption2.setText(strOwners);

        circularSeekbar.setmImageAction(this);

        dbHelper = new DBHelper(getContext());

        //get preok data
        preok();
    }

    /**
     * load preok data by making server API call
     */
    public void preok() {
        //preok params
        Oyeok preok = new Oyeok();
        preok.setDeviceId("Hardware");
        preok.setUserRole("broker");
        preok.setGcmId(SharedPrefs.getString(getActivity(), SharedPrefs.MY_GCM_ID));
        preok.setLong(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LNG));
        preok.setLat(SharedPrefs.getString(getActivity(), SharedPrefs.MY_LAT));
        preok.setPlatform("android");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConstants.SERVER_BASE_URL)
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        OyeokApiService oyeokApiService = restAdapter.create(OyeokApiService.class);
        try {
            oyeokApiService.preOk(preok, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    JsonObject k = jsonElement.getAsJsonObject();
                    try {
                        JSONObject ne = new JSONObject(k.toString());
                        JSONObject neighbours = ne.getJSONObject("responseData").getJSONObject("neighbours");

                        jsonArrayReqLl = neighbours.getJSONArray("req_ll");
                        jsonArrayAvlLl = neighbours.getJSONArray("avl_ll");

                        jsonArrayReqOr = neighbours.getJSONArray("req_or");
                        jsonArrayAvlOr = neighbours.getJSONArray("avl_or");

                        jsonArrayPreokRecent = neighbours.getJSONArray("recent");
                        //if all values are empty then show from resent
                        if (jsonArrayReqLl.length() == 0 && jsonArrayAvlLl.length() == 0 &&
                                jsonArrayReqOr.length() == 0 && jsonArrayAvlOr.length() == 0) {
                            jsonArrayReqLl = jsonArrayPreokRecent;
                            jsonArrayAvlLl = jsonArrayPreokRecent;
                            jsonArrayReqOr = jsonArrayPreokRecent;
                            jsonArrayAvlOr = jsonArrayPreokRecent;
                        }

                        onPositionSelected(currentSeekbarPosition, currentCount);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    @OnClick({R.id.txtOption1, R.id.txtOption2})
    public void onOptionClick(View v) {

        if (txtPreviouslySelectedOption != null)
            txtPreviouslySelectedOption.setBackgroundResource(R.color.colorPrimaryDark);

        txtPreviouslySelectedOption = (TextView) v;

        if (v.getId() == txtOption1.getId()) {
            txtOption1.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption1.getText().toString();
        }
        else if (v.getId() == txtOption2.getId()) {
            txtOption2.setBackgroundResource(R.color.greenish_blue);
            currentOptionSelectedString = txtOption2.getText().toString();
        }

        onPositionSelected(currentSeekbarPosition, currentCount);
    }

    @Override
    public void onPositionSelected(int position, int count) {
        currentSeekbarPosition = position;

        if (position == 0) {
            //rental
            //Tenants, Owners
            txtOption1.setText(strTenants);
            txtOption2.setText(strOwners);

            if (currentOptionSelectedString.equalsIgnoreCase(strSeekers))
                currentOptionSelectedString = strTenants;

            if (jsonArrayReqLl != null && currentOptionSelectedString.equalsIgnoreCase(strTenants))
                circularSeekbar.setValues(jsonArrayReqLl.toString());
            else if (jsonArrayAvlLl != null && currentOptionSelectedString.equalsIgnoreCase(strOwners))
                circularSeekbar.setValues(jsonArrayAvlLl.toString());
        }
        else if (position == 1) {
            //sale
            //Buyer, Seller
            txtOption1.setText(strSeekers);
            txtOption2.setText(strSeller);

            if (currentOptionSelectedString.equalsIgnoreCase(strTenants))
                currentOptionSelectedString = strSeekers;

            if (jsonArrayReqOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeekers))
                circularSeekbar.setValues(jsonArrayReqOr.toString());
            else if (jsonArrayAvlOr != null && currentOptionSelectedString.equalsIgnoreCase(strSeller))
                circularSeekbar.setValues(jsonArrayAvlOr.toString());
        }

        synchronized (circularSeekbar) {
            circularSeekbar.post(new Runnable() {
                @Override
                public void run() {
                    circularSeekbar.requestLayout();
                    circularSeekbar.invalidate();
                }
            });
        }
    }

    @Override
    public void onclick(int position, JSONArray m, String show, int x_c, int y_c) {
        try {
            jsonObjectArray = m;
            selectedItemPosition = position;
            rentText.setText("Rs "+jsonObjectArray.getJSONObject(position).getString("price")+" /per m");
            displayOkText.setText(jsonObjectArray.getJSONObject(position).getString("ok_price")+" Oks will be used");

            if(show.equals("show")) {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                displayOkText.setVisibility(View.VISIBLE);
                pickContact.setVisibility(View.GONE);
                contactText.setVisibility(View.GONE);
            }
            else if(show.equals("hide")) {
                notClicked.setVisibility(View.VISIBLE);
                rentText.setVisibility(View.GONE);
                displayOkText.setVisibility(View.GONE);
                pickContact.setVisibility(View.GONE);
                contactText.setVisibility(View.GONE);
            }
            else {
                notClicked.setVisibility(View.GONE);
                rentText.setVisibility(View.VISIBLE);
                displayOkText.setVisibility(View.VISIBLE);
                pickContact.setVisibility(View.VISIBLE);
                contactText.setVisibility(View.VISIBLE);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.okButton, R.id.deal, R.id.pickContact})
    public void onButtonsClick(View v) {
        if (okButton.getId() == v.getId()) {
            if (jsonObjectArray == null) {
                SnackbarManager.show(
                        com.nispok.snackbar.Snackbar.with(getActivity())
                                .position(Snackbar.SnackbarPosition.BOTTOM)
                                .text("Please select a deal")
                                .color(Color.parseColor(AppConstants.DEFAULT_SNACKBAR_COLOR)));
            }
            else {
                if (!General.getSharedPreferences(getActivity(), AppConstants.ROLE_OF_USER).equalsIgnoreCase("broker")) {
                    dbHelper.save(DatabaseConstants.userRole, "Broker");
                    //show sign up screen if broker is not registered
                    Bundle bundle = new Bundle();
                    bundle.putString("lastFragment", "BrokerPreokFragment");
                    bundle.putString("JsonArray", jsonObjectArray.toString());
                    bundle.putInt("Position", selectedItemPosition);
                    Fragment fragment = null;
                    fragment = new SignUpFragment();
                    fragment.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_map, fragment);
                    fragmentTransaction.commit();
                } else {
                    //here broker is registered
                    AcceptOkCall a = new AcceptOkCall();
                    a.setmCallBack(BrokerPreokFragment.this);
                    a.acceptOk(jsonObjectArray, selectedItemPosition, dbHelper, getActivity());
                }
            }
        }
        else if (deal.getId() == v.getId()) {
            //open deals listing
            Intent openDealsListing = new Intent(getActivity(), BrokerDealsListActivity.class);
            startActivity(openDealsListing);
        }
        else if (pickContact.getId() == v.getId()) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            getActivity().startActivityFromFragment(this, intent, REQUEST_CODE_TO_SELECT_CLIENT);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (REQUEST_CODE_TO_SELECT_CLIENT == reqCode) {
                Uri contactData = data.getData();
                Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // TODO Fetch other Contact details as you want to use
                    contactText.setText(name);
                    contactText.setPadding(8, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void replaceFragment(Bundle args) {
        //open deals listing
        Intent openDealsListing = new Intent(getActivity(), BrokerDealsListActivity.class);
        startActivity(openDealsListing);
    }
}
