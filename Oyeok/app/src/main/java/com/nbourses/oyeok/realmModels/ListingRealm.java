package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sushil on 23/02/17.
 */

public class ListingRealm extends RealmObject{

    @PrimaryKey
    private String listing_id;
    private String name;
    private String config;
    private int listed_ll_pm;
    private int listed_or_psf;
    private String lat;
    private String lng;
    private int real_ll_pm;
    private int real_or_psf;
    private String possession_date;
    private String req_avl;
    private String locality;
    private String city;
    private String rate_growth;
    private String portals;
    private String listings;
    private String transactions;


    public ListingRealm() {
    }

    public ListingRealm(String listing_id, String name, String config, int listed_ll_pm, int listed_or_psf, String lat, String lng, int real_ll_pm, int real_or_psf, String possession_date, String req_avl, String locality, String city, String rate_growth, String portals, String listings, String transactions) {
        this.listing_id = listing_id;
        this.name = name;
        this.config = config;
        this.listed_ll_pm = listed_ll_pm;
        this.listed_or_psf = listed_or_psf;
        this.lat = lat;
        this.lng = lng;
        this.real_ll_pm = real_ll_pm;
        this.real_or_psf = real_or_psf;
        this.possession_date = possession_date;
        this.req_avl = req_avl;
        this.locality = locality;
        this.city = city;
        this.rate_growth = rate_growth;
        this.portals = portals;
        this.listings = listings;
        this.transactions = transactions;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
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

    public int getListed_ll_pm() {
        return listed_ll_pm;
    }

    public void setListed_ll_pm(int listed_ll_pm) {
        this.listed_ll_pm = listed_ll_pm;
    }

    public int getListed_or_psf() {
        return listed_or_psf;
    }

    public void setListed_or_psf(int listed_or_psf) {
        this.listed_or_psf = listed_or_psf;
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

    public int getReal_ll_pm() {
        return real_ll_pm;
    }

    public void setReal_ll_pm(int real_ll_pm) {
        this.real_ll_pm = real_ll_pm;
    }

    public int getReal_or_psf() {
        return real_or_psf;
    }

    public void setReal_or_psf(int real_or_psf) {
        this.real_or_psf = real_or_psf;
    }

    public String getPossession_date() {
        return possession_date;
    }

    public void setPossession_date(String possession_date) {
        this.possession_date = possession_date;
    }

    public String getReq_avl() {
        return req_avl;
    }

    public void setReq_avl(String req_avl) {
        this.req_avl = req_avl;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRate_growth() {
        return rate_growth;
    }

    public void setRate_growth(String rate_growth) {
        this.rate_growth = rate_growth;
    }


    public String getPortals() {
        return portals;
    }

    public void setPortals(String portals) {
        this.portals = portals;
    }

    public String getListings() {
        return listings;
    }

    public void setListings(String listings) {
        this.listings = listings;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }
}
