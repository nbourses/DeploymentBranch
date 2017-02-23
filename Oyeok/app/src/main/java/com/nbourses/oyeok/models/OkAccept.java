package com.nbourses.oyeok.models;

/**
 * Created by Ritesh Warke on 22/02/17.
 */

public class OkAccept {
    String user_id;
    String lng;
    String lat;
    String user_role;
    String gcm_id;
    String oye_id;
    String listings;

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

    public String getListings() {
        return listings;
    }

    public void setListings(String listings) {
        this.listings = listings;
    }
}
