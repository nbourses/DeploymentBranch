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



