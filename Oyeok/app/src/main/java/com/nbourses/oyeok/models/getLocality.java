package com.nbourses.oyeok.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shlokmalik on 29/12/16.
 */

public class GetLocality {

    @SerializedName("tt")
    private String tt;

    @SerializedName("price")
    private String price;

    @SerializedName("city")
    private String city;

    @SerializedName("gcm_id")
    private String gcm_id;

    @SerializedName("property_type")
    private String property_type;

    @SerializedName("config_area")
    private String config_area;

    public String getConfig_area() {
        return config_area;
    }

    public void setConfig_area(String config_area) {
        this.config_area = config_area;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }
}
