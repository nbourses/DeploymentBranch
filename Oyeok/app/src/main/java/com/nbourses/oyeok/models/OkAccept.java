package com.nbourses.oyeok.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ritesh Warke on 22/02/17.
 */

public class OkAccept {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("long")
    private String lng;
    @SerializedName("lat")
    private String lat;
    @SerializedName("user_role")
    private String user_role;
    @SerializedName("gcm_id")
    private String gcm_id;
    @SerializedName("oye_id")
    private String oye_id;
    @SerializedName("listings")
    private ArrayList listings;


    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getOye_id() {
        return oye_id;
    }

    public void setOye_id(String oye_id) {
        this.oye_id = oye_id;
    }

    public ArrayList getListings() {
        return listings;
    }

    public void setListings(ArrayList listings) {
        this.listings = listings;
    }
}
