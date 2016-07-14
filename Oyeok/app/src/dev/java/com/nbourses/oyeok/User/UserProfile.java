package com.nbourses.oyeok.User;

/**
 * Created by Abhinandan on 11/26/2015.
 */
public class UserProfile {
    public String getMobileNumber() {

        return MobileNumber;
    }

    public String getName() {
        return Name;
    }

    public String getEmailId() {
        return EmailId;
    }

    public String getProfilePhotoUrl() {
        return ProfilePhotoUrl;
    }

    public String getLat() {
        return Lat;
    }

    public String getLng() {
        return Lng;
    }

    public String getGcmId() {
        return GcmId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public static String MobileNumber="";
    public static String Name="";
    public static String EmailId="";
    public static String ProfilePhotoUrl="";
    public static String Lat="";
    public static String Lng="";
    public static String GcmId="";
    public static String deviceId="";

    public UserProfile(){}


}
