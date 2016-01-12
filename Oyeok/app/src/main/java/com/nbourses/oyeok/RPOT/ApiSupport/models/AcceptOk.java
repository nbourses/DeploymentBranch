package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DADDU_DON on 12/30/2015.
 */
public class AcceptOk {

    public ResponseData responseData = new ResponseData();

    public class ResponseData{


        @SerializedName("time")

        ArrayList<String> time = new ArrayList<String>();


        @SerializedName("message")
        String message;

        @SerializedName("lat")
        String latitude;

        @SerializedName("long")
        String longitude;

        @SerializedName("oye_status")
        String oyeStatus;

        @SerializedName("oye_id")
        String oyeId;

        @SerializedName("ok_id")
        String okId;

        @SerializedName("ok_user_id")
        String okUserId;

        @SerializedName("oye_user_id")
        String oyeUserId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getOyeId() {
            return oyeId;
        }

        public void setOyeId(String oyeId) {
            this.oyeId = oyeId;
        }

        public String getOkId() {
            return okId;
        }

        public void setOkId(String okId) {
            this.okId = okId;
        }
        public String getOkUserId() {
            return okUserId;
        }

        public void setOkUserId(String okUserId) {
            this.okUserId = okUserId;
        }

        public String getOyeUserId() {
            return oyeUserId;
        }

        public void setOyeUserId(String oyeUserId) {
            this.oyeUserId = oyeUserId;
        }

        public String getOyeStatus() {
            return oyeStatus;
        }

        public void setOyeStatus(String oyeStatus) {
            this.oyeStatus = oyeStatus;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public ArrayList<String> getTime() {
            return time;
        }



        public void setTime(ArrayList<String> time) {
            this.time = time;
        }
    }

}
