package com.nbourses.oyeok.models;

/**
 * Created by sushil on 22/11/16.
 */

public class buildingCacheModel {
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
    private Boolean flag;

    public buildingCacheModel(String id, String name, String config, int or_psf, int ll_pm, String lat, String lng, String rate_growth, String listing, String portals, long timestamp, String transactions, String locality, Boolean flag) {
        this.id = id;
        this.name = name;
        this.config = config;
        this.or_psf = or_psf;
        this.ll_pm = ll_pm;
        this.lat = lat;
        this.lng = lng;
        this.rate_growth = rate_growth;
        this.listing = listing;
        this.portals = portals;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.locality = locality;
        this.flag = flag;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
