
package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Pratik on 11/24/2015.
 */

public class GetPrice {

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    String success;


    //String responseData;
    @SerializedName("responseData")
    public ResponseData responseData = new ResponseData();
    //ResponseData responseData[] = new ResponseData[5];

//ResponseData responseData= new ResponseData();
    /*public ResponseData getResponseData(){
        return responseData;
    }

    public void setResponseData(ResponseData responseData){
        this.responseData= responseData;
    }*/
    public class ResponseData{
        @SerializedName("ll_min")
        private String ll_min;
        private String ll_max;
        private String or_min;
        private String or_max;

        public String getLl_min() {
            return ll_min;
        }

        public void setLl_min(String ll_min) {
            this.ll_min = ll_min;
        }

        public String getLl_max() {
            return ll_max;
        }

        public void setLl_max(String ll_max) {
            this.ll_max = ll_max;
        }

        public String getOr_min() {
            return or_min;
        }

        public void setOr_min(String or_min) {
            this.or_min = or_min;
        }

        public String getOr_max() {
            return or_max;
        }

        public void setOr_max(String or_max) {
            this.or_max = or_max;
        }

    }


    //String responseData;
}


