package com.nbourses.oyeok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sushil on 07/11/16.
 */
//implements Comparable<portListingModel>
public class portListingModel {



    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("locality")
    private String locality;

    @Expose
    @SerializedName("growth_rate")
    private String growth_rate;

    @Expose
    @SerializedName("ll_pm")
    private int ll_pm;


    @Expose
    @SerializedName("or_psf")
    private int or_psf;

    @Expose
    @SerializedName("timpstamp")
    private String timpstamp;

    @Expose
    @SerializedName("transaction")
    private String transaction;

    @Expose
    @SerializedName("config")
    private String config;

    @Expose
    @SerializedName("display_type")
    private String display_type;

    public String getDisplay_type() {
        return display_type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config,String display_type) {
        this.id = id;
        this.name = name;
        this.locality = locality;
        this.growth_rate = growth_rate;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.timpstamp = timpstamp;
        this.transaction = transaction;
        this.config=config;
        this.display_type=display_type;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getGrowth_rate() {
        return growth_rate;
    }

    public void setGrowth_rate(String growth_rate) {
        this.growth_rate = growth_rate;
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

    public String getTimpstamp() {
        return timpstamp;
    }

    public void setTimpstamp(String timpstamp) {
        this.timpstamp = timpstamp;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
/*
    @Override
    public int compareTo(portListingModel another) {
        return 0;
    }*/
}
