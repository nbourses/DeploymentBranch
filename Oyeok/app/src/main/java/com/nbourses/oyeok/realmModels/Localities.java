package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;

/**
 * Created by Ritesh Warke on 02/01/17.
 */

public class Localities extends RealmObject{

    private String locality;
    private String orMin;
    private String llMin;
    private String llMax;
    private String orMax;
    private String lat;
    private String lng;
    private String type;
    private String timestamp;
    private String growthRate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(String growthRate) {
        this.growthRate = growthRate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrMin() {
        return orMin;
    }



    public void setOrMin(String orMin) {
        this.orMin = orMin;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLlMin() {
        return llMin;
    }

    public void setLlMin(String llMin) {
        this.llMin = llMin;
    }

    public String getLlMax() {
        return llMax;
    }

    public void setLlMax(String llMax) {
        this.llMax = llMax;
    }

    public String getOrMax() {
        return orMax;
    }

    public void setOrMax(String orMax) {
        this.orMax = orMax;
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
