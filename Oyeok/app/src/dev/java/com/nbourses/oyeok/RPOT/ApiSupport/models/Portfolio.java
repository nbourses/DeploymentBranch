package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushil on 29/09/16.
 */

public class Portfolio {
    @SerializedName("rate_growth")
    @Expose
    private String rate_growth;

    @SerializedName("config")
    @Expose
    private String config;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("or_psf")
    @Expose
    private String orPsf;

    @SerializedName("ll_pm")
    @Expose
    private String llPm;

    @SerializedName("loc")
    @Expose
    private List<String> loc = new ArrayList<String>();

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("listings")
    @Expose
    private String listings;

    @SerializedName("portals")
    @Expose
    private String portals;

    @SerializedName("transactions")
    @Expose
    private String transactions;

    @SerializedName("distance")
    @Expose
    private String distance;

    public String getRate_growth() {
        return rate_growth;
    }

    public void setRate_growth(String rate_growth) {
        this.rate_growth = rate_growth;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrPsf() {
        return orPsf;
    }

    public void setOrPsf(String orPsf) {
        this.orPsf = orPsf;
    }

    public String getLlPm() {
        return llPm;
    }

    public void setLlPm(String llPm) {
        this.llPm = llPm;
    }

    public List<String> getLoc() {
        return loc;
    }

    public void setLoc(List<String> loc) {
        this.loc = loc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getListings() {
        return listings;
    }

    public void setListings(String listings) {
        this.listings = listings;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
