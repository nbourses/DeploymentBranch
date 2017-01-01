package com.nbourses.oyeok.models;

import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rohit on 12/02/16.
 */

//@Table(name = "PublishLetsOye")
public class PublishLetsOye //extends Model {
{
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

    @Expose
    @SerializedName("possession_date")
    @Column(name = "possession_date")
    public String possession_date;



    @Expose
    @SerializedName("furnishing")
    @Column(name = "furnishing")

    public String furnishing;



    @Expose
    @SerializedName("no_call")
    @Column(name = "no_call")
    public String no_call;


    @Expose
    @SerializedName("building_id")
    @Column(name = "building_id")
    public String building_id;

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getNo_call() {
        return no_call;
    }

    public void setNo_call(String no_call) {
        this.no_call = no_call;
    }


    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public String getPossession_date() {
        return possession_date;
    }

    public void setPossession_date(String possession_date) {
        this.possession_date = possession_date;
    }

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

//    public static List<PublishLetsOye> getAll() {
//        return new Select()
//                .from(PublishLetsOye.class)
//                .execute();
//    }
}
