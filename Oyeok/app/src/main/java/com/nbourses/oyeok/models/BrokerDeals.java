package com.nbourses.oyeok.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rohit on 16/02/16.
 */

public class BrokerDeals implements Comparable<BrokerDeals>{


    @Expose
    @SerializedName("ok_id")
    private String okId;

    @Expose
    @SerializedName("oye_id")
    private String oyeId;

    @Expose
    @SerializedName("oye_user_id")
    private String oyeUserId;

    @Expose
    @SerializedName("ok_user_id")
    private String okUserId;


    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("mobile_no")
    private String mobileNo;

    @Expose
    @SerializedName("spec_code")
    private String specCode;

    @Expose
    @SerializedName("default_deal")
    private Boolean defaultDeal;

    @Expose
    @SerializedName("locality")
    private String locality;

    @Expose
    @SerializedName("hdroom_status")
    private HDroomStatus hdroomStatus;

    @Expose
    @SerializedName("last_seen")
    private String lastSeen;






    public BrokerDeals(String name,String ok_id, String specs,String locality,String oyeId,String lastSeen, Boolean default_deal)
    { // Constructor for default deal
        //this.okId = "default_id";


        Log.i("IN BROKERDEALS ","FLAG "+default_deal);
        this.okId = ok_id;
        this.specCode = specs;
        this.name = name;
        this.locality = locality;
        this.lastSeen = lastSeen;
        this.defaultDeal = default_deal;
        this.oyeId = oyeId;

    }

    public BrokerDeals(String name,String ok_id, String specs,String locality,String oyeId,String selfStatus,String otherStatus,String oyeUserId,String lastSeen, Boolean default_deal)
    { // Constructor for default deal
        //this.okId = "default_id";


        Log.i("IN BROKERDEALS ","FLAG "+selfStatus);
        this.okId = ok_id;
        this.specCode = specs;
        this.name = name;
        this.locality = locality;
        this.lastSeen = lastSeen;
        this.defaultDeal = default_deal;
        this.oyeId = oyeId;
        //this.getHDroomStatus().selfStatus = selfStatus;
        HDroomStatus hDroomStatus = new HDroomStatus(selfStatus, otherStatus);

        /*this.hdroomStatus.selfStatus = selfStatus;
        this.hdroomStatus.otherStatus = otherStatus;*/
        this.oyeUserId = oyeUserId;

    }


    public BrokerDeals(String name,String ok_id, String specs, Boolean default_deal)
    { // Constructor for default deal
        //this.okId = "default_id";


        Log.i("IN BROKERDEALS ","FLAG "+default_deal);
        this.okId = ok_id;
        this.specCode = specs;
        this.name = name;
        this.oyeId = ok_id;

    }


    public class HDroomStatus{
        @Expose
        @SerializedName("self_status")
        private String selfStatus;

        @Expose
        @SerializedName("other_status")


        private String otherStatus;

        public HDroomStatus(String selfStatus,String otherStatus){
            this.selfStatus = selfStatus;
            this.otherStatus = otherStatus;
        }
        public String getSelfStatus() {
            return selfStatus;
        }

        public void setSelfStatus(String selfStatus) {
            this.selfStatus = selfStatus;
        }
        public String getOtherStatus() {
            return otherStatus;
        }

        public void setOtherStatus(String otherStatus) {
            this.otherStatus = otherStatus;
        }


    }

    public HDroomStatus getHDroomStatus() {
        return hdroomStatus;
    }

    public void setHDroomStatus(HDroomStatus hdroomStatus) {
        this.hdroomStatus = hdroomStatus;
    }

    public Boolean getdefaultDeal() {
        return defaultDeal;
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

    public void setOkUserId(String oyeUserId) {
        this.okUserId = okUserId;
    }
    public String getOyeId() {
        return oyeId;
    }

    public void setOyeId(String oyeId) {
        this.oyeId = oyeId;
    }

    public String getOyeUserId() {
        return oyeUserId;
    }

    public void setOyeUserId(String oyeUserId) {
        this.oyeUserId = oyeUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int compareTo(BrokerDeals timeStamp) {



        long t1 = Long.parseLong(this.lastSeen);
        long t2 = Long.parseLong(timeStamp.getLastSeen());
        if(t2 > t1)
            return 1;
        else if(t1 > t2)
            return -1;
        else
            return 0;
        //Log.i("Adapter","spandan chukya"+compareFruit.getLastSeen()+" "+Integer.parseInt(compareFruit.getLastSeen()));

        //int compareQuantity = Integer.parseInt(compareFruit.getLastSeen());
       /* Long comparetimeStamp = Long.parseLong(timeStamp.getLastSeen());
        Log.i("Adapter","spandan chukya called 1 "+comparetimeStamp);*/
        //ascending order
        /*Log.i("Adapter","spandan chukya called 2 "+Long.parseLong(this.lastSeen));
        return (int) (comparetimeStamp - Long.parseLong(this.lastSeen));*/

        //descending order
       //return compareQuantity - this.quantity;

    }


}
