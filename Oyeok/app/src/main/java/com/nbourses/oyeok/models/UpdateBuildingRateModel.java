package com.nbourses.oyeok.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sushil on 21/11/16.
 */

public class UpdateBuildingRateModel {
    @Expose
    @SerializedName("building")
    private String building;
    @Expose
    @SerializedName("lat")
    private String lat;
    @Expose
    @SerializedName("long")
    private String longiute;
    @Expose
    @SerializedName("locality")
    private String locality;

    @Expose
    @SerializedName("user_id")
    private String user_id;

    @Expose
    @SerializedName("user_role")
    private String user_role;

    @Expose
    @SerializedName("building_id")
    private String building_id;

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

    public String getLongiute() {
        return longiute;
    }

    public void setLongiute(String longiute) {
        this.longiute = longiute;
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

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }
}
