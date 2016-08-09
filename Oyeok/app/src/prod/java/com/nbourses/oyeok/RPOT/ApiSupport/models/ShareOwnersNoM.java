package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 30/07/16.
 */
public class ShareOwnersNoM {
    private String success;
    @SerializedName("mobile_no")
    private String mobile;

    @SerializedName("name")
    private String name;
    @SerializedName("user_id")
    private String user_id;




    public ResponseData responseData = new ResponseData();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public class ResponseData {

        @SerializedName("mobile")
        public String mobile;
        @SerializedName("coupon")
        public String coupon;
    }
}
