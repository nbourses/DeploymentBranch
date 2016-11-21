package com.nbourses.oyeok.models;

/**
 * Created by sushil on 04/11/16.
 */

public class loadBuildingDataModel {

    String name;
    double lat;
    double lng;
    String id;
    String locality;

    public loadBuildingDataModel(String name, double lat, double lng, String id,String locality ) {
        this.name=name;
        this.lat=lat;
        this.lng=lng;
        this.id=id;
        this.locality=locality;

    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }

    public String getLocality() {
        return locality;
    }


}
