package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 23/05/16.
 */
public class BrokerBuildings {

    private String success;

    public ResponseData responseData = new ResponseData();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @SerializedName("gcm_id")
    private String gcmId;

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    @SerializedName("lat")
    private String lat;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @SerializedName("long")
    private String lng;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @SerializedName("page")
    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }


    @SerializedName("device_id")
    private String device_id;

    public String getDeviceId() {
        return device_id;
    }

    public void setDeviceId(String device_id) {
        this.device_id = device_id;
    }

    @SerializedName("property_type")
    private String property_type;

    public String getPropertyType() {
        return property_type;
    }

    public void setPropertyType(String property_type) {
        this.property_type = property_type;
    }



    public class ResponseData {

        @SerializedName("buildings")
        public buildings buildings[] = new buildings[10];
    }

    public class buildings {
        @SerializedName("name")
        public String name;

        @SerializedName("or_psf")
        public String or_psf;

        @SerializedName("ll_pm")
        public Integer ll_pm;

        @SerializedName("location")
        public location location[] = new location[2];

        public void setName(String name){this.name=name;}

        public String getName(){return name;}

        public void setPsf(String or_psf){this.or_psf=or_psf;}

        public String getPsf(){return or_psf;}

        public void setPm(Integer ll_pm){this.ll_pm=ll_pm;}

        public Integer getPm(){return ll_pm;}
    }

    public class location {
        @SerializedName("lat")
        public String lat;

        @SerializedName("long")
        public String lng;

        public void setLng(String lng){this.lng=lng;}

        public String getLng(){return lng;}

        public void setLat(String lat){this.lat=lat;}

        public String getLat(){return lat;}

    }

}
