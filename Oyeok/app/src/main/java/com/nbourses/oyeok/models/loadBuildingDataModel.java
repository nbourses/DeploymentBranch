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
    String ll_pm;
    String or_psf;
    boolean checkbox;

    public loadBuildingDataModel(String name, double lat, double lng, String id, String locality, String city, String ll_pm, String or_psf) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.locality = locality;
        this.city = city;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;

    }

    public loadBuildingDataModel(String name, double lat, double lng, String id, String locality, String city, String ll_pm, String or_psf, boolean checkbox) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.locality = locality;
        this.city = city;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.checkbox = checkbox;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLl_pm() {
        return ll_pm;
    }

    public void setLl_pm(String ll_pm) {
        this.ll_pm = ll_pm;
    }

    public String getOr_psf() {
        return or_psf;
    }

    public void setOr_psf(String or_psf) {
        this.or_psf = or_psf;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

}
