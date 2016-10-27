package com.nbourses.oyeok.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.nbourses.oyeok.R;

/**
 * Created by ritesh on 10/08/16.
 */
public class InitialCard  extends Fragment{

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
    private ImageView icon;
    private TextView userType;

private SwitchButton toggleBtn;
    Animation bounce;
    private Boolean rent = true;
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
        icon = (ImageView) rootView.findViewById(R.id.icon);
        userType = (TextView) rootView.findViewById(R.id.userType);

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
        tagline1.setText(Html.fromHtml("<b>Tenant</b> you are 3 clicks away"));
        setTenant();

        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {R.color.greenish_blue, R.color.dark_white};
        CompoundButtonCompat.setButtonTintList(tenant, new ColorStateList(states, colors));

         tenant.setChecked(true);
         bounce = AnimationUtils.loadAnimation(getContext(),
                R.anim.bounce);

        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //resale
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
                    setOwner();

                }
                else{
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
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){
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
                                             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                 if(isChecked){
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
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){
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
*/                                                    setSeller();
                                                  }

                                              }
                                          }
        );
        buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                 if(isChecked){
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
*/                                                     setBuyer();
                                                 }

                                             }
                                         }
        );


    }

    private void setTenant(){
        String s =null;
        SpannableString ss1;

        //q1.setText(Html.fromHtml(getString(R.string.tenant_card_question1)));
        /*q2.setText(Html.fromHtml(getString(R.string.tenant_card_question2)));
        q3.setText(Html.fromHtml(getString(R.string.tenant_card_question3)));*/
        s= getString(R.string.tenant_card_question1);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 11,17, 0); // set size
        q1.setText(ss1);

        s= getString(R.string.tenant_card_question2);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 6,12, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 21,29, 0);
        q2.setText(ss1);

        s= getString(R.string.tenant_card_question3);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 8,19, 0); // set size
        q3.setText(ss1);

    }

    private void setOwner(){
        String s =null;
        SpannableString ss1;
        s= getString(R.string.owner_card_question1);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 25,s.length(), 0); // set size
        q1.setText(ss1);

        s= getString(R.string.owner_card_question2);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 7,16, 0); // set size
        q2.setText(ss1);

        s= getString(R.string.owner_card_question3);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 11,18, 0); // set size
        q3.setText(ss1);

    }

    private void setBuyer(){
        String s =null;
        SpannableString ss1;
        s= getString(R.string.buyer_card_question1);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0,5, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 20,29, 0);
        q1.setText(ss1);

        s= getString(R.string.buyer_card_question2);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 4,12, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 16,s.length(), 0);
        q2.setText(ss1);

        s= getString(R.string.buyer_card_question3);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 5,s.length(), 0); // set size
        q3.setText(ss1);

    }

    private void setSeller(){
        String s =null;
        SpannableString ss1;
        s= getString(R.string.seller_card_question1);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 13,s.length(), 0); // set size

        q1.setText(ss1);

        s= getString(R.string.seller_card_question2);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 0,5, 0); // set size
        ss1.setSpan(new RelativeSizeSpan(1.5f), 23,s.length(), 0);
        q2.setText(ss1);

        s= getString(R.string.seller_card_question3);
        ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 18,s.length(), 0); // set size
        q3.setText(ss1);

    }


}

