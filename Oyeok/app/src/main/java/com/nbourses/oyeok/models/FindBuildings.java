package com.nbourses.oyeok.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ritesh Warke on 10/03/17.
 */

public class FindBuildings {
    @SerializedName("config")
    private String config;
    @SerializedName("property_type")
    private String property_type;
    @SerializedName("lat")
    private String lat;
    @SerializedName("long")
    private String lng;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
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
}
