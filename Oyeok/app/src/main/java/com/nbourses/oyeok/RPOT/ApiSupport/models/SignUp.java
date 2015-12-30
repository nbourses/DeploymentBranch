package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DADDU_DON on 12/9/2015.
 */
public class SignUp {
    private String success;

    public ResponseData responseData = new ResponseData();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class ResponseData{


        @SerializedName("user_id")
        public String userId;


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
