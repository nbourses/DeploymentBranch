package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 24/05/16.
 */
public class deleteHDroom {

    @SerializedName("success")
    public String success;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("gcm_id")
    public String gcm_id;

    @SerializedName("ok_id")
    public String ok_id;

    @SerializedName("page")
    public String page;

    @SerializedName("delete_oye_id")
    public String delete_oye_id;

    public String getSuccess(){
        return success;
    }

    public void setSuccess(String success){
        this.success=success;
    }

    public String getUserId(){
        return user_id;
    }

    public void setUserId(String user_id){
        this.user_id=user_id;
    }


    public String getGcmId(){
        return gcm_id;
    }

    public void setGcmId(String gcm_id){
        this.gcm_id=gcm_id;
    }


    public String getOkId(){
        return ok_id;
    }

    public void setOkId(String ok_id){
        this.ok_id=ok_id;
    }


    public String getPage(){
        return page;
    }

    public void setPage(String page){
        this.page=page;
    }

    public String getDeleteOyeid(){
        return delete_oye_id;
    }

    public void setDeleteOyeId(String delete_oye_id){
        this.delete_oye_id=delete_oye_id;
    }

}
