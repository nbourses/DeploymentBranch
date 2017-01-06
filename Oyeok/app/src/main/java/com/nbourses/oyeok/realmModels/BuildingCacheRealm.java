package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sushil on 22/11/16.
 */

public class BuildingCacheRealm extends RealmObject {
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
    private  long timestamp;
    private  String transactions;
    private String  locality;
    private String  city;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
