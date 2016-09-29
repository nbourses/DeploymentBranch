package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;

/**
 * Created by ritesh on 23/09/16.
 */
public class LatiLongi extends RealmObject{
    private Double lat;
    private Double lng;


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
