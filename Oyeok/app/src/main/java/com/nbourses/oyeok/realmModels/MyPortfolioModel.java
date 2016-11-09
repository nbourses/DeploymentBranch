package com.nbourses.oyeok.realmModels;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sushil on 29/09/16.
 */


@RealmClass
public class MyPortfolioModel extends RealmObject {
    @PrimaryKey
    private  String id;
    private  String name;
    private  String config;
    private  int or_psf;
    private  int ll_pm;
    private  String lat;
    private  String lng;
    private  String rate_growth;
    private  String listing;
    private  String portals;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private  String timestamp;
    private  String transactions;
    private String  locality;

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public int getOr_psf() {
        return or_psf;
    }

    public void setOr_psf(int or_psf) {
        this.or_psf = or_psf;
    }

    public int getLl_pm() {
        return ll_pm;
    }

    public void setLl_pm(int ll_pm) {
        this.ll_pm = ll_pm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public String getRate_growth() {
        return rate_growth;
    }

    public void setRate_growth(String rate_growth) {
        this.rate_growth = rate_growth;
    }

    public String getListing() {
        return listing;
    }
    public void setListing(String listing) {
        this.listing = listing;
    }

    public String getPortals() {
        return portals;
    }

    public void setPortals(String portals) {
        this.portals = portals;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }
}





   /* private String Timestamp;
    private String property_type;
    private String config;
    private String Building_name;
    private String locality;
    private String lat;
    private String lng;

    private String sublocality;
    private String listing_date;
    private String tt;
    private String ll_pm;
    private String or_psf;
    private String possession_date;*/


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