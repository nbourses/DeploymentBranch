package com.nbourses.oyeok.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ritesh Warke on 13/02/17.
 */

public class PreOk {
    @SerializedName("lat")
    private String lat;
    @SerializedName("long")
    private String lng;
    @SerializedName("user_role")
    private String user_role;
    @SerializedName("gcm_id")
    private String gcm_id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("platform")
    private String platform;
    @SerializedName("email")
    private String email;
    @SerializedName("locality")
    private String locality;
    @SerializedName("page")
    private String page;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
