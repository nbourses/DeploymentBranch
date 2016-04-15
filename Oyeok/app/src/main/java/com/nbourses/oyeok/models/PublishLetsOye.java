package com.nbourses.oyeok.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rohit on 12/02/16.
 */
@Table(name = "PublishLetsOye")
public class PublishLetsOye extends Model {
    @Expose
    @SerializedName("tt")
    @Column(name = "tt")
    private String tt;

    @Expose
    @SerializedName("property_type")
    @Column(name = "propertyType")
    public String propertyType;

    @Expose
    @SerializedName("property_subtype")
    @Column(name = "propertySubType")
    public String propertySubType;

    @Expose
    @SerializedName("size")
    @Column(name = "size")
    public String size;

    @Expose
    @SerializedName("price")
    @Column(name = "price")
    public String price;

    @Expose
    @SerializedName("req_avl")
    @Column(name = "reqAvl")
    public String reqAvl;

    @Expose
    @SerializedName("user_id")
    @Column(name = "userId")
    public String userId;

    @Expose
    @SerializedName("user_role")
    @Column(name = "userRole")
    public String userRole;

    @Expose
    @SerializedName("lat")
    @Column(name = "lat")
    public String lat;

    @Expose
    @SerializedName("long")
    @Column(name = "lon")
    public String lon;

    @Expose
    @SerializedName("time")
    @Column(name = "time")
    public String time;

    @Expose
    @SerializedName("gcm_id")
    @Column(name = "gcm_id")
    public String gcmId;

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertySubType() {
        return propertySubType;
    }

    public void setPropertySubType(String propertySubType) {
        this.propertySubType = propertySubType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReqAvl() {
        return reqAvl;
    }

    public void setReqAvl(String reqAvl) {
        this.reqAvl = reqAvl;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public static List<PublishLetsOye> getAll() {
        return new Select()
                .from(PublishLetsOye.class)
                .execute();
    }
}