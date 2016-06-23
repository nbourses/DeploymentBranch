package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DADDU_DON on 12/9/2015.
 */
public class SignUp {
    private String success;

    public Integer errors;
    public ResponseData responseData = new ResponseData();

    public Integer getError() {
        return errors;
    }

    public void setError(Integer error) {
        this.errors = errors;
    }


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class ResponseData{


        @SerializedName("user_id")
        public String userId;

        @SerializedName("name")
        public String name;
        @SerializedName("email")
        public String email;

        @SerializedName("user_role")
        public String user_role;

        @SerializedName("message")
        public String message;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUser_role() {
            return user_role;
        }

        public void setUser_role(String user_role) {
            this.user_role = email;
        }



        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }
}
