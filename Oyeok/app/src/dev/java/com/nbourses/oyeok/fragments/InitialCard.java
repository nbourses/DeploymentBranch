package com.nbourses.oyeok.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        toggleBtn.toggle();
        tenant.setChecked(true);
        bounce = AnimationUtils.loadAnimation(getContext(),
                R.anim.bounce);

        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //resale
                    rent = false;
                    seller.setChecked(false);
                    buyer.setChecked(true);
                    tagline.setText("I own a Property, it earns rent");
                    buySell.setTextColor(getResources().getColor(R.color.greenish_blue));
                    rental.setTextColor(Color.parseColor("#a8a8a8"));
                    rentalPanel.clearAnimation();
                    resalePanel.clearAnimation();
                    rentalPanel.setVisibility(View.GONE);
                    resalePanel.setVisibility(View.VISIBLE);
                    resalePanel.startAnimation(bounce);tenantQ.setVisibility(View.VISIBLE);
                    tenantQ.setVisibility(View.GONE);
                    ownerQ.setVisibility(View.GONE);
                    buyerQ.setVisibility(View.VISIBLE);
                    sellerQ.setVisibility(View.GONE);

                }
                else{
                    rent = true;
                    tagline.setText("Searching Buildings, I now");
                    tenant.setChecked(true);
                    owner.setChecked(false);
                    rental.setTextColor(getResources().getColor(R.color.greenish_blue));
                    buySell.setTextColor(Color.parseColor("#a8a8a8"));
                    rentalPanel.clearAnimation();
                    resalePanel.clearAnimation();
                    resalePanel.setVisibility(View.GONE);
                    rentalPanel.setVisibility(View.VISIBLE);
                    rentalPanel.startAnimation(bounce);


                    ownerQ.setVisibility(View.GONE);
                    buyerQ.setVisibility(View.GONE);
                    sellerQ.setVisibility(View.GONE);
                    tenantQ.setVisibility(View.VISIBLE);
                }
            }
        });


        tenant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){
                                                      tagline.setText("Searching Buildings, I now");
                                                      owner.setChecked(false);
                                                      tenantQ.setVisibility(View.VISIBLE);
                                                      ownerQ.setVisibility(View.GONE);
                                                      buyerQ.setVisibility(View.GONE);
                                                      sellerQ.setVisibility(View.GONE);
                                                      //* rental.setChecked(true);

                                                  }


                                              }
                                          }
        );

        owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                 if(isChecked){
                                                     tagline.setText("I am looking to purchase");
                                                     tenant.setChecked(false);
                                                     //*  rental.setChecked(true);
                                                     tenantQ.setVisibility(View.GONE);
                                                     ownerQ.setVisibility(View.VISIBLE);
                                                     buyerQ.setVisibility(View.GONE);
                                                     sellerQ.setVisibility(View.GONE);
                                                 }

                                             }
                                         }
        );

        seller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                  if(isChecked){
                                                      tagline.setText("Market rate of my property today");
                                                      buyer.setChecked(false);
                                                      //*  buysell.setChecked(true);
                                                      tenantQ.setVisibility(View.GONE);
                                                      ownerQ.setVisibility(View.GONE);
                                                      buyerQ.setVisibility(View.GONE);
                                                      sellerQ.setVisibility(View.VISIBLE);

                                                  }

                                              }
                                          }
        );
        buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                 if(isChecked){

                                                     seller.setChecked(false);
                                                     //*   buysell.setChecked(true);
                                                     tenantQ.setVisibility(View.GONE);
                                                     ownerQ.setVisibility(View.GONE);
                                                     buyerQ.setVisibility(View.VISIBLE);
                                                     sellerQ.setVisibility(View.GONE);

                                                 }

                                             }
                                         }
        );


    }


}

