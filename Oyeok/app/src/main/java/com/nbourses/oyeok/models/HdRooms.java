package com.nbourses.oyeok.models;

import com.activeandroid.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rohit on 16/02/16.
 */
public class HdRooms extends Model {
    @Expose
    @SerializedName("user_id")
    private String userId;

    @Expose
    @SerializedName("user_role")
    private String userRole;

    @Expose
    @SerializedName("gcm_id")
    private String gcmId;

    @Expose
    @SerializedName("device_id")
    private String deviceId;

    @Expose
    @SerializedName("lat")
    private String lat;

    @Expose
    @SerializedName("lon")
    private String lon;

    @Expose
    @SerializedName("page")
    private String page;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
