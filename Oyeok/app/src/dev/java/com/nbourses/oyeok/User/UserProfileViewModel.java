package com.nbourses.oyeok.User;

/**
 * Created by Abhinandan on 11/25/2015.
 */
public class UserProfileViewModel extends UserProfile {

    private  String MobileNumber="";
    private  String Name="";
    private  String EmailId="";
    private  String ProfilePhotoUrl="";
    private  String Lat="";
    private  String Lng="";
    private  String GcmId="";
    private  String DeviceId="";
    UserProfile userProfile=new UserProfile();

    public  String getDeviceId() {
        return this.DeviceId;
    }

    public void setDeviceId(String deviceId) {
        this.userProfile.deviceId=deviceId;
        DeviceId = deviceId;
    }

    public  String getGcmId() {
        return this.GcmId;
    }

    public void setGcmId(String GCMId) {
        this.userProfile.GcmId=GCMId;
        this.GcmId = GCMId;
    }

    public  String getLng() {
        return this.Lng;
    }

    public void setLng(String lng) {
        this.userProfile.Lng=lng;
        Lng = lng;
    }

    public  String getLat() {
        return this.Lat;
    }

    public void setLat(String lat) {
        this.userProfile.Lat=lat;
        Lat = lat;

    }



    public String getMobileNumber() {
        return this.MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.userProfile.MobileNumber=mobileNumber;
        MobileNumber = mobileNumber;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.userProfile.Name=name;
        Name = name;
    }

    public String getEmailId() {
        return this.EmailId;
    }

    public void setEmailId(String emailId) {
        this.userProfile.EmailId=emailId;
        EmailId = emailId;
    }

    public String getProfilePhotoUrl() {
        return this.ProfilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        ProfilePhotoUrl = profilePhotoUrl;
    }

    public UserProfile getUserProfile(){
        return this.userProfile;
    }

}
