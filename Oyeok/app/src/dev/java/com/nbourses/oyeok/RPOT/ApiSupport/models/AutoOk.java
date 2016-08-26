package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 12/08/16.
 */
public class AutoOk {
    private String success;
// {"user_id":"","long":,"lat":,"tt":"","gcm_id" :"","platform" : ""}
    @SerializedName("long")
    private String lng;

    @SerializedName("locality")
    private String locality;

    @SerializedName("email")
    private String email;

    @SerializedName("lat")
    private String lat;

    @SerializedName("tt")
    private String tt;

    @SerializedName("gcm_id")
    private String gcm_id;

    @SerializedName("platform")
    private String platform;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("req_avl")
    private String req_avl;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReq_avl() {
        return req_avl;
    }

    public void setReq_avl(String req_avl) {
        this.req_avl = req_avl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }
}
