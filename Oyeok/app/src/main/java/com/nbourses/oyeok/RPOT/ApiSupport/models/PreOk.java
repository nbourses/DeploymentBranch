package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DADDU_DON on 12/28/2015.
 */
public class PreOk {
    private String success;

    public ResponseData responseData = new ResponseData();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class ResponseData{

        @SerializedName("neighbours")
        public Neighbours neighbours= new Neighbours();

        @SerializedName("user_id")
        public String userId;

        @SerializedName("published_at")
        public String publishedAt;

        public void setPublishedAt(String publishedAt){
            this.publishedAt=publishedAt;
        }

        public String getPublishedAt(){
            return publishedAt;
        }

        public void setUserId(String userId){
            this.userId=userId;
        }

        public String getUserId(){
            return userId;
        }



        public class Neighbours{
            @SerializedName("req_ll")
            public ReqLl reqLl[] = new ReqLl[6];
            @SerializedName("req_or")
            public ReqOr reqOr[] = new ReqOr[6];
            @SerializedName("avl_ll")
            public AvlLl avlLl[] = new AvlLl[6];
            @SerializedName("avl_or")
            public AvlOr avlOr[] = new AvlOr[6];

            public class ReqLl {
                @SerializedName("oye_id")
                public String oyeId;

                @SerializedName("req_avl")
                public String reqAvl;

                @SerializedName("user_role")
                public String userRole;

                @SerializedName("user_id")
                public String userId;

                @SerializedName("oye_status")
                public String oyeStatus;

                @SerializedName("size")
                public String size;

                public String price;

                public String tt;

                public String getTt() {
                    return tt;
                }

                public void setTt(String tt) {
                    this.tt = tt;
                }

                public String getReqAvl() {
                    return reqAvl;
                }

                public void setReqAvl(String reqAvl) {
                    this.reqAvl = reqAvl;
                }

                public void setOyeId(String publishedAt) {
                    this.oyeId = oyeId;
                }

                public String getOyeId() {
                    return oyeId;
                }

                public String getUserRole() {
                    return userRole;
                }

                public void setUserRole(String userRole) {
                    this.userRole = userRole;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getOyeStatus() {
                    return oyeStatus;
                }

                public void setOyeStatus(String oyeStatus) {
                    this.oyeStatus = oyeStatus;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }


            public class ReqOr{
                @SerializedName("oye_id")
                public String oyeId;

                @SerializedName("req_avl")
                public String reqAvl;

                @SerializedName("user_role")
                public String userRole;

                @SerializedName("user_id")
                public String userId;

                @SerializedName("oye_status")
                public String oyeStatus;

                @SerializedName("size")
                public String size;

                public String price;

                public String tt;

                public String getTt() {
                    return tt;
                }

                public void setTt(String tt) {
                    this.tt = tt;
                }

                public String getReqAvl() {
                    return reqAvl;
                }

                public void setReqAvl(String reqAvl) {
                    this.reqAvl = reqAvl;
                }

                public void setOyeId(String publishedAt) {
                    this.oyeId = oyeId;
                }

                public String getOyeId() {
                    return oyeId;
                }

                public String getUserRole() {
                    return userRole;
                }

                public void setUserRole(String userRole) {
                    this.userRole = userRole;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getOyeStatus() {
                    return oyeStatus;
                }

                public void setOyeStatus(String oyeStatus) {
                    this.oyeStatus = oyeStatus;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }

            public class AvlLl{
                @SerializedName("oye_id")
                public String oyeId;

                @SerializedName("req_avl")
                public String reqAvl;

                @SerializedName("user_role")
                public String userRole;

                @SerializedName("user_id")
                public String userId;

                @SerializedName("oye_status")
                public String oyeStatus;

                @SerializedName("size")
                public String size;

                public String price;

                public String tt;

                public String getTt() {
                    return tt;
                }

                public void setTt(String tt) {
                    this.tt = tt;
                }

                public String getReqAvl() {
                    return reqAvl;
                }

                public void setReqAvl(String reqAvl) {
                    this.reqAvl = reqAvl;
                }

                public void setOyeId(String publishedAt) {
                    this.oyeId = oyeId;
                }

                public String getOyeId() {
                    return oyeId;
                }

                public String getUserRole() {
                    return userRole;
                }

                public void setUserRole(String userRole) {
                    this.userRole = userRole;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getOyeStatus() {
                    return oyeStatus;
                }

                public void setOyeStatus(String oyeStatus) {
                    this.oyeStatus = oyeStatus;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }

            public class AvlOr{
                @SerializedName("oye_id")
                public String oyeId;

                @SerializedName("req_avl")
                public String reqAvl;

                @SerializedName("user_role")
                public String userRole;

                @SerializedName("user_id")
                public String userId;

                @SerializedName("oye_status")
                public String oyeStatus;

                @SerializedName("size")
                public String size;

                public String price;

                public String tt;

                public String getTt() {
                    return tt;
                }

                public void setTt(String tt) {
                    this.tt = tt;
                }

                public String getReqAvl() {
                    return reqAvl;
                }

                public void setReqAvl(String reqAvl) {
                    this.reqAvl = reqAvl;
                }

                public void setOyeId(String publishedAt) {
                    this.oyeId = oyeId;
                }

                public String getOyeId() {
                    return oyeId;
                }

                public String getUserRole() {
                    return userRole;
                }

                public void setUserRole(String userRole) {
                    this.userRole = userRole;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getOyeStatus() {
                    return oyeStatus;
                }

                public void setOyeStatus(String oyeStatus) {
                    this.oyeStatus = oyeStatus;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }

        }
    }
}
