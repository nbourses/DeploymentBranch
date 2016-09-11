package com.nbourses.oyeok.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rohit on 16/02/16.
 */

public class BrokerDeals {


    @Expose
    @SerializedName("ok_id")
    private String okId;

    @Expose
    @SerializedName("oye_id")
    private String oyeId;

    @Expose
    @SerializedName("oye_user_id")
    private String oyeUserId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("mobile_no")
    private String mobileNo;

    @Expose
    @SerializedName("spec_code")
    private String specCode;

    @Expose
    @SerializedName("default_deal")
    private Boolean defaultDeal;

    @Expose
    @SerializedName("locality")
    private String locality;



    public BrokerDeals(String name,String ok_id, String specs,String locality,String oyeId, Boolean default_deal)
    { // Constructor for default deal
        //this.okId = "default_id";


        Log.i("IN BROKERDEALS ","FLAG "+default_deal);
        this.okId = ok_id;
        this.specCode = specs;
        this.name = name;
        this.locality = locality;

        this.defaultDeal = default_deal;
        this.oyeId = oyeId;

    }
    public BrokerDeals(String name,String ok_id, String specs, Boolean default_deal)
    { // Constructor for default deal
        //this.okId = "default_id";


        Log.i("IN BROKERDEALS ","FLAG "+default_deal);
        this.okId = ok_id;
        this.specCode = specs;
        this.name = name;
        this.oyeId = ok_id;

    }

    public Boolean getdefaultDeal() {
        return defaultDeal;
    }

    public String getOkId() {
        return okId;
    }

    public void setOkId(String okId) {
        this.okId = okId;
    }

    public String getOyeId() {
        return oyeId;
    }

    public void setOyeId(String oyeId) {
        this.oyeId = oyeId;
    }

    public String getOyeUserId() {
        return oyeUserId;
    }

    public void setOyeUserId(String oyeUserId) {
        this.oyeUserId = oyeUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }


}
