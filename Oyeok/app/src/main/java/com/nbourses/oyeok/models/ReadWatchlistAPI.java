package com.nbourses.oyeok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nbourses.oyeok.realmModels.loadBuildingdataModelRealm;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by sushil on 07/02/17.
 */

public class ReadWatchlistAPI {
    @Expose
    @SerializedName("user_id")
    private String user_id;

    @Expose
    @SerializedName("watchlist_id")
    private String watchlist_id;

    @Expose
    @SerializedName("action")
    private String action;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("tt")
    private String tt;

    @Expose
    @SerializedName("build_list")
    private ArrayList<String> build_list;



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(String watchlist_id) {
        this.watchlist_id = watchlist_id;
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

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public ArrayList<String> getBuild_list() {
        return build_list;
    }

    public void setBuild_list(ArrayList<String> build_list) {
        this.build_list = build_list;
    }
}
