package com.nbourses.oyeok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sushil on 17/10/16.
 */

public class AddBuildingModel {

    @Expose
    @SerializedName("building")
    private String building;

    @Expose
    @SerializedName("lat")
    private String lat;

    @Expose
    @SerializedName("long")
    private String lng;

    @Expose
    @SerializedName("locality")
    private String locality;

    @Expose
    @SerializedName("user_id")
    private String user_id;

    @Expose
    @SerializedName("user_role")
    private String user_role;


    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}
