package com.nbourses.oyeok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sushil on 17/10/16.
 */

public class AddListingBorker {

    @Expose
    @SerializedName("building")
    private String name;

    @Expose
    @SerializedName("lat")
    private String lat;

    @Expose
    @SerializedName("long")
    private String lng;

    @Expose
    @SerializedName("locality")
    private String locality;


    @Expose
    @SerializedName("sublocality")
    private String sublocality;

    @Expose
    @SerializedName("config")
    private String config;

    @Expose
    @SerializedName("listing_date")
    private String listing_date;

    @Expose
    @SerializedName("listing_by")
    private String listing_by;

    @Expose
    @SerializedName("req_avl")
    private String req_avl;

    @Expose
    @SerializedName("tt")
    private String tt;

    @Expose
    @SerializedName("ll_pm")
    private String ll_pm;

    @Expose
    @SerializedName("or_psf")
    private String or_psf;

    @Expose
    @SerializedName("possession_date")
    private String possession_date;

    @Expose
    @SerializedName("google_place_id")
    private String google_place_id;

    @Expose
    @SerializedName("user_name")
    private String user_name;

    @Expose
    @SerializedName("mobile")
    private String mobile;
    @Expose

    @SerializedName("price_per_sqft")
    private String price_per_sqft;

    @Expose
    @SerializedName("price_per_req")
    private String price_per_req;

    @Expose
    @SerializedName("property_type")
    private String property_type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getListing_date() {
        return listing_date;
    }

    public void setListing_date(String listing_date) {
        this.listing_date = listing_date;
    }

    public String getListing_by() {
        return listing_by;
    }

    public void setListing_by(String listing_by) {
        this.listing_by = listing_by;
    }

    public String getReq_avl() {
        return req_avl;
    }

    public void setReq_avl(String req_avl) {
        this.req_avl = req_avl;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getLl_pm() {
        return ll_pm;
    }

    public void setLl_pm(String ll_pm) {
        this.ll_pm = ll_pm;
    }

    public String getOr_psf() {
        return or_psf;
    }

    public void setOr_psf(String or_psf) {
        this.or_psf = or_psf;
    }

    public String getPossession_date() {
        return possession_date;
    }

    public void setPossession_date(String possession_date) {
        this.possession_date = possession_date;
    }

    public String getGoogle_place_id() {
        return google_place_id;
    }

    public void setGoogle_place_id(String google_place_id) {
        this.google_place_id = google_place_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPrice_per_sqft() {
        return price_per_sqft;
    }

    public void setPrice_per_sqft(String price_per_sqft) {
        this.price_per_sqft = price_per_sqft;
    }

    public String getPrice_per_req() {
        return price_per_req;
    }

    public void setPrice_per_req(String price_per_req) {
        this.price_per_req = price_per_req;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }
}

/*

        1.      Building
        2.      Lat
        3.      Long
        4.      Locality
        5.      sublocality,
        6.      config,
        7.      listing_date
        8.     listing_by
        9.     req_avl
        10.  tt
        11.  ll_pm
        12.  or_psf
        13.    possession_date
        14.  google_place_id
        15.  user_name
        16.  mobile
        17.  price_per_sqft
        18.  price_per_req
        19.  property_type*/
