package com.nbourses.oyeok.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.nbourses.oyeok.R;
import com.nbourses.oyeok.activities.DealConversationActivity;
import com.nbourses.oyeok.helpers.AppConstants;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by ritesh on 10/08/16.
 */
public class InitialCard  extends Fragment {

    /*@Bind(R.id.toggleBtn)
    SwitchButton toggleBtn;*/

    /*@Bind(R.id.tenant)
    CheckBox tenant;

    @Bind(R.id.owner)
    CheckBox owner;

    @Bind(R.id.seller)
    CheckBox seller;

    @Bind(R.id.buyer)
    CheckBox buyer;

    @Bind(R.id.rentalPanel)
    LinearLayout rentalPanel;

    @Bind(R.id.resalePanel)
    LinearLayout resalePanel;

    @Bind(R.id.rental)
    TextView rental;

    @Bind(R.id.buySell)
    TextView buySell;*/
    private TextView rental;
    private TextView buySell;
    private CheckBox tenant;
    private CheckBox owner;
    private CheckBox buyer;
    private CheckBox seller;
    private LinearLayout rentalPanel;
    private LinearLayout resalePanel;
    private LinearLayout tenantQ;
    private LinearLayout ownerQ;
    private LinearLayout buyerQ;
    private LinearLayout sellerQ;
    private TextView tagline;
    private TextView tagline1;
    private TextView q1;
    private TextView q2;
    private TextView q3;
    private TextView q4;
    private ImageView icon;
    private TextView userType;
    private ImageButton call;
    private ImageButton chat;
    private  Intent callIntent;

    private SwitchButton toggleBtn;
    Animation bounce;
    private Boolean rent = true;
    private int selection = 0;
    private static final int REQUEST_CALL_PHONE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_initial_card, container,
                false);
        toggleBtn = (SwitchButton) rootView.findViewById(R.id.toggleBtn);
        rental = (TextView) rootView.findViewById(R.id.rental);
        buySell = (TextView) rootView.findViewById(R.id.buySell);
        tenant = (CheckBox) rootView.findViewById(R.id.tenant);
        owner = (CheckBox) rootView.findViewById(R.id.owner);
        buyer = (CheckBox) rootView.findViewById(R.id.buyer);
        seller = (CheckBox) rootView.findViewById(R.id.seller);
        rentalPanel = (LinearLayout) rootView.findViewById(R.id.rentalPanel);
        resalePanel = (LinearLayout) rootView.findViewById(R.id.resalePanel);
        tenantQ = (LinearLayout) rootView.findViewById(R.id.tenantQ);
        ownerQ = (LinearLayout) rootView.findViewById(R.id.ownerQ);
        buyerQ = (LinearLayout) rootView.findViewById(R.id.buyerQ);
        sellerQ = (LinearLayout) rootView.findViewById(R.id.sellerQ);
        tagline = (TextView) rootView.findViewById(R.id.tagline);
        tagline1 = (TextView) rootView.findViewById(R.id.tagline1);
        q1 = (TextView) rootView.findViewById(R.id.q1);
        q2 = (TextView) rootView.findViewById(R.id.q2);
        q3 = (TextView) rootView.findViewById(R.id.q3);
        q4 = (TextView) rootView.findViewById(R.id.q4);
        icon = (ImageView) rootView.findViewById(R.id.icon);
        userType = (TextView) rootView.findViewById(R.id.userType);
        chat = (ImageButton) rootView.findViewById(R.id.chat);
        call = (ImageButton) rootView.findViewById(R.id.call);
        toggleBtn.performClick();


        init();
        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();


    }

    private void init() {

        //q1.setText(Html.fromHtml(getString(R.string.tenant_card_question1)));
        /*q2.setText(Html.fromHtml(getString(R.string.tenant_card_question2)));
        q3.setText(Html.fromHtml(getString(R.string.tenant_card_question3)));*/
        tagline1.setText(Html.fromHtml("<i><b>Tenant</b> you are 3 clicks away</i>"));
        setTenant();

        /*int states[][] = {{android.R.attr.state_checked}, {}};
       int colors[] = {R.color.black, R.color.dark_white};
        CompoundButtonCompat.setButtonTintList(tenant, new ColorStateList(states, colors));*/

        tenant.setChecked(true);
        bounce = AnimationUtils.loadAnimation(getContext(),
                R.anim.bounce);

        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //resale
                    AppConstants.Card_TT = "OR";
                    userType.setText("BUY/SELL");
                    rent = false;
                    seller.setChecked(false);
                    buyer.setChecked(true);
                    tagline.setText("I am looking to buy, compare...");
                    tagline1.setText(Html.fromHtml("<b>Buyer</b> you are 3 clicks away"));
                    icon.setImageResource(R.drawable.asset_buysell_deal_type_icon_v1);
                    buySell.setTextColor(getResources().getColor(R.color.greenish_blue));
                    rental.setTextColor(Color.parseColor("#a8a8a8"));
                    rentalPanel.clearAnimation();
                    resalePanel.clearAnimation();
                    rentalPanel.setVisibility(View.GONE);
                    resalePanel.setVisibility(View.VISIBLE);
                    resalePanel.startAnimation(bounce);
                    /*tenantQ.setVisibility(View.GONE);
                    ownerQ.setVisibility(View.GONE);
                    buyerQ.setVisibility(View.VISIBLE);
                    sellerQ.setVisibility(View.GONE);*/
                    /*q1.setText(Html.fromHtml(getString(R.string.owner_card_question1)));
                    q2.setText(Html.fromHtml(getString(R.string.owner_card_question2)));
                    q3.setText(Html.fromHtml(getString(R.string.owner_card_question3)));*/
                    setBuyer();

                } else {
                    AppConstants.Card_TT = "LL";
                    userType.setText("RENT");
                    rent = true;
                    tagline.setText("I am Searching Property...");
                    tagline1.setText(Html.fromHtml("<b>Tenant</b> you are 3 clicks away"));
                    icon.setImageResource(R.drawable.asset_rent_dealtype_icon_v1);
                    tenant.setChecked(true);
                    owner.setChecked(false);
                    rental.setTextColor(getResources().getColor(R.color.greenish_blue));
                    buySell.setTextColor(Color.parseColor("#a8a8a8"));
                    rentalPanel.clearAnimation();
                    resalePanel.clearAnimation();
                    resalePanel.setVisibility(View.GONE);
                    rentalPanel.setVisibility(View.VISIBLE);
                    rentalPanel.startAnimation(bounce);


                    /*ownerQ.setVisibility(View.GONE);
                    buyerQ.setVisibility(View.GONE);
                    sellerQ.setVisibility(View.GONE);
                    tenantQ.setVisibility(View.VISIBLE);*/
                    /*q1.setText(Html.fromHtml(getString(R.string.tenant_card_question1)));
                    q2.setText(Html.fromHtml(getString(R.string.tenant_card_question2)));
                    q3.setText(Html.fromHtml(getString(R.string.tenant_card_question3)));*/
                    setTenant();
                }
            }
        });


        tenant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (isChecked) {
                                                      AppConstants.Card_REQ_AVL = "req";
                                                      tagline.setText("I am Searching Property...");
                                                      tagline1.setText(Html.fromHtml("<b>Tenant</b> you are 3 clicks away"));
                                                      owner.setChecked(false);
                                                      icon.setImageResource(R.drawable.asset_rent_dealtype_icon_v1);
                                                      /*tenantQ.setVisibility(View.VISIBLE);
                                                      ownerQ.setVisibility(View.GONE);
                                                      buyerQ.setVisibility(View.GONE);
                                                      sellerQ.setVisibility(View.GONE);*/
                                                     /* q1.setText(Html.fromHtml(getString(R.string.tenant_card_question1)));
                                                      q2.setText(Html.fromHtml(getString(R.string.tenant_card_question2)));
                                                      q3.setText(Html.fromHtml(getString(R.string.tenant_card_question3)));*/
                                                      //* rental.setChecked(true);
                                                      setTenant();

                                                  }


                                              }
                                          }
        );

        owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                 if (isChecked) {
                                                     AppConstants.Card_REQ_AVL = "avl";
                                                     tagline.setText("I own Property, it earns rent...");
                                                     tagline1.setText(Html.fromHtml("<b>Owner</b> you are 3 clicks away"));
                                                     tenant.setChecked(false);
                                                     icon.setImageResource(R.drawable.asset_rent_dealtype_icon_v1);
                                                     //*  rental.setChecked(true);
                                                     /*tenantQ.setVisibility(View.GONE);
                                                     ownerQ.setVisibility(View.VISIBLE);
                                                     buyerQ.setVisibility(View.GONE);
                                                     sellerQ.setVisibility(View.GONE);*/
                                                     /*q1.setText(Html.fromHtml(getString(R.string.owner_card_question1)));
                                                     q2.setText(Html.fromHtml(getString(R.string.owner_card_question2)));
                                                     q3.setText(Html.fromHtml(getString(R.string.owner_card_question3)));*/
                                                     setOwner();
                                                 }

                                             }
                                         }
        );

        seller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (isChecked) {
                                                      AppConstants.Card_REQ_AVL = "avl";
                                                      tagline.setText("I own property, want to sell...");
                                                      tagline1.setText(Html.fromHtml("<b>Seller</b> you are 3 clicks away"));
                                                      buyer.setChecked(false);
                                                      icon.setImageResource(R.drawable.asset_buysell_deal_type_icon_v1);
                                                      //*  buysell.setChecked(true);
                                                      /*tenantQ.setVisibility(View.GONE);
                                                      ownerQ.setVisibility(View.GONE);
                                                      buyerQ.setVisibility(View.GONE);
                                                      sellerQ.setVisibility(View.VISIBLE);*/
                                                      /*q1.setText(Html.fromHtml(getString(R.string.seller_card_question1)));
                                                      q2.setText(Html.fromHtml(getString(R.string.seller_card_question2)));
                                                      q3.setText(Html.fromHtml(getString(R.string.seller_card_question3)));
*/
                                                      setSeller();
                                                  }

                                              }
                                          }
        );
        buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                 if (isChecked) {
                                                     AppConstants.Card_REQ_AVL = "req";
                                                     tagline.setText("I am looking to buy, compare...");
                                                     tagline1.setText(Html.fromHtml("<b>Buyer</b> you are 3 clicks away"));
                                                     seller.setChecked(false);
                                                     icon.setImageResource(R.drawable.asset_buysell_deal_type_icon_v1);
                                                     //*   buysell.setChecked(true);
                                                     /*tenantQ.setVisibility(View.GONE);
                                                     ownerQ.setVisibility(View.GONE);
                                                     buyerQ.setVisibility(View.VISIBLE);
                                                     sellerQ.setVisibility(View.GONE);*/
                                                     /*q1.setText(Html.fromHtml(getString(R.string.buyer_card_question1)));
                                                     q2.setText(Html.fromHtml(getString(R.string.buyer_card_question2)));
                                                     q3.setText(Html.fromHtml(getString(R.string.buyer_card_question3)));
*/
                                                     setBuyer();
                                                 }

                                             }
                                         }
        );


        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TastyToast.makeText(getContext(),q1.getText().toString(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                if(selection == 0){

                    Bundle b = new Bundle();
                    if(rent)
                        b.putString(AppConstants.TT,"LL");
                    else
                        b.putString(AppConstants.TT,"OR");
AppConstants.SETLOCATIONBTOL = true;

                    BudgetToLocations budgetToLocations = new BudgetToLocations();
                    loadFragmentAnimated(budgetToLocations,b,R.id.card,"");
                }
                else if(selection == 1){
                    Bundle b = new Bundle();
                    b.putString(AppConstants.QUESTION,AppConstants.OWNERQ1);
                    MentalMapOwnerScreen mentalMapOwnerScreen = new MentalMapOwnerScreen();
                    loadFragmentAnimated(mentalMapOwnerScreen,b,R.id.card,"");

                }

                else{
                    underConstruction();
                }
            }
        });

        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selection == 0) {
                    //TastyToast.makeText(getContext(),q2.getText().toString(),TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.card)).commit();

                    Intent intent = new Intent(AppConstants.SETLOCN);
                    intent.putExtra("question", "LtoP");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }
                else if(selection == 1){
                    Bundle b = new Bundle();
                    b.putString(AppConstants.QUESTION,AppConstants.OWNERQ2);
                    MentalMapOwnerScreen mentalMapOwnerScreen = new MentalMapOwnerScreen();
                    loadFragmentAnimated(mentalMapOwnerScreen,b,R.id.card,"");

                }

                else{
                    underConstruction();
                }



            }
        });

        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selection == 0) {
                    getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_down).remove(getFragmentManager().findFragmentById(R.id.card)).commit();

                    Intent intent = new Intent(AppConstants.SETLOCN);
                    intent.putExtra("question", "TravelTime");
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                }else if(selection == 1){
                    Bundle b = new Bundle();
                    b.putString(AppConstants.QUESTION,AppConstants.OWNERQ3);
                    MentalMapOwnerScreen mentalMapOwnerScreen = new MentalMapOwnerScreen();
                    loadFragmentAnimated(mentalMapOwnerScreen,b,R.id.card,"");
                }
                    else{
                        underConstruction();

                }
            }
        });
        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                underConstruction();
                if(selection == 0){

                    /*Bundle b = new Bundle();
                    if(rent)
                        b.putString(AppConstants.TT,"LL");
                    else
                        b.putString(AppConstants.TT,"OR");


                    DepositeAsEmi depositeAsEmi = new DepositeAsEmi();
                    loadFragmentAnimated(depositeAsEmi,b,R.id.card,"");*/
                }
            }
        });



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+912239659137"));//+912233836068
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String callPermission = Manifest.permission.CALL_PHONE;
                int hasPermission = ContextCompat.checkSelfPermission(getActivity(), callPermission);
                String[] permissions = new String[] { callPermission };
//                if(isTelephonyEnabled()) {
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, REQUEST_CALL_PHONE);

                } else {

                    startActivity(callIntent);
                }
//                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DealConversationActivity.class);

                intent.putExtra("userRole", "client");
                AppConstants.CLIENT_DEAL_FLAG = true;


                intent.putExtra(AppConstants.OK_ID, AppConstants.SUPPORT_CHANNEL_NAME);
                startActivity(intent);
            }
        });
    }

    private void loadFragmentAnimated(Fragment fragment, Bundle args, int containerId, String title)
    {
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("cardbtol");
        fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setTenant() {
        String s = null;
        SpannableString ss1;
        selection = 0;

        //q1.setText(Html.fromHtml(getString(R.string.tenant_card_question1)));
        /*q2.setText(Html.fromHtml(getString(R.string.tenant_card_question2)));
        q3.setText(Html.fromHtml(getString(R.string.tenant_card_question3)));*/
        s = getString(R.string.tenant_card_question1);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 11, 17, 0); // set size
        q1.setText(ss1);
        q1.setPaintFlags( q1.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        q1.setTextColor(getResources().getColor(R.color.black));

        s = getString(R.string.tenant_card_question2);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 6, 15, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 29, 35, 0);
        q2.setText(ss1);
        q2.setPaintFlags( q2.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        q2.setTextColor(getResources().getColor(R.color.black));

        s = getString(R.string.tenant_card_question3);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 8, 19, 0); // set size
        q3.setText(ss1);
        q3.setPaintFlags( q3.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        q3.setTextColor(getResources().getColor(R.color.black));

        s = getString(R.string.tenant_card_question4);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 9, 24, 0); // set size
        q4.setText(ss1);
        q4.setPaintFlags(q4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q4.setTextColor(getResources().getColor(R.color.gray));


    }

    private void setOwner() {
        selection = 1;
        String s = null;
        SpannableString ss1;
        s = getString(R.string.owner_card_question1);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 25, s.length(), 0); // set size
        q1.setText(ss1);
        q1.setPaintFlags(q1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q1.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.owner_card_question2);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 7, 16, 0); // set size
        q2.setText(ss1);
        q2.setPaintFlags(q2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q2.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.owner_card_question3);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 11, 18, 0); // set size
        q3.setText(ss1);
        q3.setPaintFlags(q3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q3.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.tenant_card_question4);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 9, 24, 0); // set size
        q4.setText(ss1);
        q4.setPaintFlags(q4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q4.setTextColor(getResources().getColor(R.color.gray));


    }

    private void setBuyer() {
        selection = 2;
        String s = null;
        SpannableString ss1;
        s = getString(R.string.buyer_card_question1);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 20, 29, 0);
        q1.setText(ss1);
        q1.setPaintFlags(q1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q1.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.buyer_card_question2);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 4, 12, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 16, s.length(), 0);
        q2.setText(ss1);
        q2.setPaintFlags(q2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q2.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.buyer_card_question3);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0, 9, 0); // set size
        q3.setText(ss1);
        q3.setPaintFlags(q3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q3.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.buyer_card_question4);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 14, 20, 0); // set size
        q4.setText(ss1);
        q4.setPaintFlags(q4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q4.setTextColor(getResources().getColor(R.color.gray));
    }

    private void setSeller() {
        selection = 3;
        String s = null;
        SpannableString ss1;
        s = getString(R.string.seller_card_question1);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 13, s.length(), 0); // set size
        q1.setText(ss1);
        q1.setPaintFlags(q1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q1.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.seller_card_question2);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 23, s.length(), 0);
        q2.setText(ss1);
        q2.setPaintFlags(q2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q2.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.seller_card_question3);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 18, s.length(), 0); // set size
        q3.setText(ss1);
        q3.setPaintFlags(q3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q3.setTextColor(getResources().getColor(R.color.gray));

        s = getString(R.string.tenant_card_question4);
        ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 9, 24, 0); // set size
        q4.setText(ss1);
        q4.setPaintFlags(q4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        q4.setTextColor(getResources().getColor(R.color.gray));


    }



private void underConstruction(){
    try {

        TastyToast.makeText(getContext(), "We are coming with this feature very soon!", TastyToast.LENGTH_LONG, TastyToast.INFO);

    }
    catch(Exception e){}
}



}

