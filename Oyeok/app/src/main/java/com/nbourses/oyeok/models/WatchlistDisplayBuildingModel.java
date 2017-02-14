package com.nbourses.oyeok.models;

/**
 * Created by sushil on 07/02/17.
 */

public class WatchlistDisplayBuildingModel {
    String id;
    String name;
    String config;
    int or_psf;
    int ll_pm;
    String lat;
    String lng;
    String rate_growth;
    String listing;
    String portals;
    String transactions;
    String  locality;
    String  req_avl;
    String status;
    String city;
    String possession_date;
    boolean checkbox;


    public WatchlistDisplayBuildingModel() {
    }

    public WatchlistDisplayBuildingModel(String id, String name, String config, int or_psf, int ll_pm, String lat, String lng, String rate_growth, String listing, String portals, String transactions, String locality, String req_avl, String status, String city, String possession_date, boolean checkbox) {
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
        this.transactions = transactions;
        this.locality = locality;
        this.req_avl = req_avl;
        this.status = status;
        this.city = city;
        this.possession_date = possession_date;
        this.checkbox = checkbox;
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

    public String getReq_avl() {
        return req_avl;
    }

    public void setReq_avl(String req_avl) {
        this.req_avl = req_avl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPossession_date() {
        return possession_date;
    }

    public void setPossession_date(String possession_date) {
        this.possession_date = possession_date;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
