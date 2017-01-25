package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sushil on 20/10/16.
 */

public class addBuildingRealm extends RealmObject{
    @PrimaryKey
    private String id;
    private String timestamp;
    private String property_type;
    private String config;
    private String Building_name;
    private String locality;
    private String lat;
    private String lng;
    private String sublocality;
    private String listing_date;
    private String tt;
    private int ll_pm;
    private int or_psf;
    private String possession_date;
    private String type;
    private String address;
    private String growth_rate;
    private String display_type;
    private String req_avl;
    private String furnishing;


    public String getReq_avl() {
        return req_avl;
    }

    public void setReq_avl(String req_avl) {
        this.req_avl = req_avl;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public String getGrowth_rate() {
        return growth_rate;
    }

    public void setGrowth_rate(String growth_rate) {
        this.growth_rate = growth_rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getListing_date() {
        return listing_date;
    }

    public void setListing_date(String listing_date) {
        this.listing_date = listing_date;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }


    public int getLl_pm() {
        return ll_pm;
    }

    public void setLl_pm(int ll_pm) {
        this.ll_pm = ll_pm;
    }

    public int getOr_psf() {
        return or_psf;
    }

    public void setOr_psf(int or_psf) {
        this.or_psf = or_psf;
    }

    public String getPossession_date() {
        return possession_date;
    }

    public void setPossession_date(String possession_date) {
        this.possession_date = possession_date;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getBuilding_name() {
        return Building_name;
    }

    public void setBuilding_name(String building_name) {
        Building_name = building_name;
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
