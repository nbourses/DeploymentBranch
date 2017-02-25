package com.nbourses.oyeok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sushil on 23/02/17.
 */

public class CreateCatalogListing {

    @Expose
    @SerializedName("user_id")
    private String user_id;

    @Expose
    @SerializedName("action")
    private String action;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("tt")
    private String tt;

    @Expose
    @SerializedName("listing_ids")
    private ArrayList<String> listing_ids;

    @Expose
    @SerializedName("catalog_id")
    private String catalog_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public ArrayList<String> getListing_ids() {
        return listing_ids;
    }

    public void setListing_ids(ArrayList<String> listing_ids) {
        this.listing_ids = listing_ids;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }
}


