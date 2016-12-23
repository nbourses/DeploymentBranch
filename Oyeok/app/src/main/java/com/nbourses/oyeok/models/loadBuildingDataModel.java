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
    String city;

    public loadBuildingDataModel(String name, double lat, double lng, String id,String locality ,String city) {
        this.name=name;
        this.lat=lat;
        this.lng=lng;
        this.id=id;
        this.locality=locality;
        this.city=city;

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
