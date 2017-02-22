package com.nbourses.oyeok.realmModels;

import android.net.Uri;

import com.nbourses.oyeok.models.loadBuildingDataModel;

import java.net.URI;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;

/**
 * Created by sushil on 07/02/17.
 */

public class WatchListRealmModel extends RealmObject  {

    @PrimaryKey
    private  String watchlist_id;

    private  String watchlist_name;

    private  String user_role;

    private  String user_id;

    private  String user_name;

    private RealmList<loadBuildingdataModelRealm> buildingids;

    private RealmList<WatchlistBuildingRealm> DisplayBuildinglist;

    private  String city;

    private  String tt;

    private String Imageuri;


    public String getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(String watchlist_id) {
        this.watchlist_id = watchlist_id;
    }

    public String getWatchlist_name() {
        return watchlist_name;
    }

    public void setWatchlist_name(String watchlist_name) {
        this.watchlist_name = watchlist_name;
    }

    public RealmList<loadBuildingdataModelRealm> getBuildingids() {
        return buildingids;
    }

    public void setBuildingids(RealmList<loadBuildingdataModelRealm> buildingids) {
        this.buildingids = buildingids;
    }

    public RealmList<WatchlistBuildingRealm> getDisplayBuildinglist() {
        return DisplayBuildinglist;
    }

    public void setDisplayBuildinglist(RealmList<WatchlistBuildingRealm> displayBuildinglist) {
        DisplayBuildinglist = displayBuildinglist;
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

    public String getImageuri() {
        return Imageuri;
    }

    public void setImageuri(String imageuri) {
        Imageuri = imageuri;
    }


    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
