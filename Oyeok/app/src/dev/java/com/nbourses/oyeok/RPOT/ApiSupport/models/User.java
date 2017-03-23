package com.nbourses.oyeok.RPOT.ApiSupport.models;
//import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
/**
 * Created by sagun on 08/09/15.
 */



/*{
        "user_id": "7216944372169443721694",
        "name": "Ajit Malhotra",
        "mobile_no": 9920050385,
        "mobile_code": "+91",
        "email": "ajitmalhotra@gmail.com",
        "user_role": "broker",
        "Longitude": "9.96233",
        "Latitude": "49.80404",
        "geonum": "43721694",
        "Gcm_Id": "992005038599200503859920050385992005038599200503859920050385",
        "Login_At": "2014-11-11T08:40:51.620Z",
        "photo_url": "<http:/api.hailyo.com/userphoto/>",
        "published_at": "2014-11-11T08:40:51.620Z",
        "user_state": "unavailable",
        "oyeok_id": "323232382328",
        "photo_url": "<http:/api.hailyo.com/userphoto/>",
        "user_intention": "pa",
        "rating_given":"Wow"
        }*/


import java.util.HashMap;
import java.util.Map;
/*import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")*/
public class User {

        // User Details
        @SerializedName("responseData")
        public ResponseData responseData= new ResponseData();
        @SerializedName("name")
        private String name;
        @SerializedName("demo_id")
        private String demoId;

        @SerializedName("mobile_no")
        private String mobileNo;
        @SerializedName("mobile_code")
        private String mobileCode;
        @SerializedName("email")
        private String email;
        @SerializedName("photo_url")
        private String photoUrl;
        @SerializedName("rating_given")
        private String ratingGiven;

        // User Role, State and Intention
        @SerializedName("user_intention")
        private String userIntention;
        @SerializedName("user_state")
        private String userState;
        @SerializedName("user_role")
        private String userRole;
        @SerializedName("oyeok_id")
        private String oyeokId;

        // Location Variables
        @SerializedName("long")
        private String Longitude;
        @SerializedName("lat")
        private String Latitude;
        @SerializedName("geo_num")
        private String geonum;

        // System Generated and System Specific Variables
        @SerializedName("user_id")
        private String userId;
        @SerializedName("push_token")
        private String pushToken;
        @SerializedName("login_at")
        private String LoginAt;
        @SerializedName("published_at")
        private String publishedAt;
        @SerializedName("device_id")
        private String deviceId;

        //relatd to gcm notifications
        @SerializedName("message")
        private String gcmMessage;
        @SerializedName("identifier")
        private String gcmMessageIdentifier;

        //realated to user profile
        @SerializedName("see_what")
        private String seeWhat;

        @SerializedName("success")
        public String success;

        @SerializedName("pincode")
        public String pincode;

    @SerializedName("organization_name")
    public String organization_name;

    @SerializedName("organization_id")
    public String organization_id;

    @SerializedName("pan")
    public String pan;

    @SerializedName("property_type")
    private String property_type;

    @SerializedName("distance")
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
    @SerializedName("gcm_id")
    public String gcmId;

        public String getPlatform() {
                return platform;
        }

        public void setPlatform(String platform) {
                this.platform = platform;
        }

        @SerializedName("platform")
        public String platform;

        public String getMyPhoto() {
                return myPhoto;
        }

        public void setMyPhoto(String myPhoto) {
                this.myPhoto = myPhoto;
        }

        @SerializedName("my_photo")
        public String myPhoto;

    public String getDemoId() {
        return demoId;
    }

    public void setDemoId(String demoId) {
        this.demoId = demoId;
    }

    public class ResponseData{
                @SerializedName("otp")
                private String otp;

                @SerializedName("user_id")
                public String userId;

                public void setOtp(String otp){
                        this.otp=otp;
                }

                public String getOtp(){
                        return otp;
                }

                public String getUserId() {
                        return userId;
                }

                public void setUserId(String userId) {
                        this.userId = userId;
                }
        }

        public String getSeeWhat(){
                return seeWhat;
        }

        public void setSeeWhat(String seeWhat){
                this.seeWhat=seeWhat;
        }

        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getGcmMessage(){ return gcmMessage;}

        public void setGcmMessage(String gcmMessage){
                this.gcmMessage=gcmMessage;
        }

        public String getGcmMessageIdentifier(){
                return gcmMessageIdentifier;
        }

        public void  setGcmMessageIdentifier(String gcmMessageIdentifier){
                this.gcmMessageIdentifier = gcmMessageIdentifier;
        }

        public String getLocality() {
                return locality;
        }

        public void setLocality(String locality) {
                this.locality = locality;
        }

    public String getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(String organization_id) {
        this.organization_id = organization_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @SerializedName("locality")
        private String locality;

        public String getDeviceId() {
                return deviceId;
        }
        //@JsonProperty("device_id")
        public void setSuccess(String publishedAt){
                this.success=success;
        }

        public String getSuccess(){
                return success;
        }
        public void setDeviceId(String deviceId){
                this.deviceId = deviceId;
        }
        /**
         *
         * @return
         * The userId
         */

                public String getUserId() {
                return userId;
        }

        /**
         *
         * @param userId
         * The user_id
         */
        //@JsonProperty("user_id")
        public void setUserId(String userId) {
                this.userId = userId;
                //System.out.println("aaaya"+userId);
        }

        /**
         *
         * @return
         * The name
         */
        //@JsonProperty("name")
        public String getName() {
                return name;
        }

        /**
         *
         * @param name
         * The name
         */
        //@JsonProperty("name")
        public void setName(String name) {
                this.name = name;
        }

        /**
         *
         * @return
         * The mobileNo
         */
        //@JsonProperty("mobile_no")
        public String getMobileNo() {
                return mobileNo;
        }

        /**
         *
         * @param mobileNo
         * The mobile_no
         */
        //@JsonProperty("mobile_no")
        public void setMobileNo(String mobileNo) {
                this.mobileNo = mobileNo;
        }

        /**
         *
         * @return
         * The mobileCode
         */
        //@JsonProperty("mobile_code")
        public String getMobileCode() {
                return mobileCode;
        }

        /**
         *
         * @param mobileCode
         * The mobile_code
         */
        //@JsonProperty("mobile_code")
        public void setMobileCode(String mobileCode) {
                this.mobileCode = mobileCode;
        }

        /**
         *
         * @return
         * The email
         */
        public String getEmail() {
                return email;
        }

        /**
         *
         * @param email
         * The email
         */
        public void setEmail(String email) {
                this.email = email;
        }

        /**
         *
         * @return
         * The userRole
         */
        //@JsonProperty("user_role")
        public String getUserRole() {
                return userRole;
        }

        /**
         *
         * @param userRole
         * The user_role
         */
        //@JsonProperty("user_role")
        public void setUserRole(String userRole) {
                this.userRole = userRole;
        }

        /**
         *
         * @return
         * The Longitude
         */
        //@JsonProperty("long")
        public String getLongitude() {
                return Longitude;
        }

        /**
         *
         * @param Longitude
         * The Longitude
         */
        //@JsonProperty("long")
        public void setLongitude(String Longitude) {
                this.Longitude = Longitude;
        }

        /**
         *
         * @return
         * The Latitude
         */
        //@JsonProperty("lat")
        public String getLatitude() {
                return Latitude;
        }

        /**
         *
         * @param Latitude
         * The Latitude
         */
        //@JsonProperty("lat")
        public void setLatitude(String Latitude) {
                this.Latitude = Latitude;
        }

        /**
         *
         * @return
         * The geonum
         */
        //@JsonProperty("geo_num")
        public String getGeonum() {
                return geonum;
        }

        /**
         *
         * @param geonum
         * The geonum
         */
        public void setGeonum(String geonum) {
                this.geonum = geonum;
        }

        /**
         *
         * @return
         * The GcmId
         */
        //@JsonProperty("gcm_id")
        public String getPushToken() {
                return pushToken;
        }

        /**
         *
         * @param pushToken
         * The Gcm_Id
         */
        //@JsonProperty("gcm_id")
        public void setPushToken(String pushToken) {
                this.pushToken = pushToken;
        }

        /**
         *
         * @return
         * The LoginAt
         */
        public String getLoginAt() {
                return LoginAt;
        }

        /**
         *
         * @param LoginAt
         * The Login_At
         */
        public void setLoginAt(String LoginAt) {
                this.LoginAt = LoginAt;
        }

        /**
         *
         * @return
         * The photoUrl
         */
        //@JsonProperty("photo_url")
        public String getPhotoUrl() {
                return photoUrl;
        }

        /**
         *
         * @param photoUrl
         * The photo_url
         */
        //@JsonProperty("photo_url")
        public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
        }

        /**
         *
         * @return
         * The publishedAt
         */
        public String getPublishedAt() {
                return publishedAt;
        }

        /**
         *
         * @param publishedAt
         * The published_at
         */
        public void setPublishedAt(String publishedAt) {
                this.publishedAt = publishedAt;
        }

        /**
         *
         * @return
         * The userState
         */
        //@JsonProperty("user_state")
        public String getUserState() {
                return userState;
        }

        /**
         *
         * @param userState
         * The user_state
         */
        //@JsonProperty("user_state")
        public void setUserState(String userState) {
                this.userState = userState;
        }

        /**
         *
         * @return
         * The oyeokId
         */
        public String getOyeokId() {
                return oyeokId;
        }

        /**
         *
         * @param oyeokId
         * The oyeok_id
         */
        public void setOyeokId(String oyeokId) {
                this.oyeokId = oyeokId;
        }

    /**
     *
     * @return
     * The property_type
     */


    public void setProperty_type(String property_type) {
            this.property_type = property_type;
        }
    public String getProperty_type() {
        return property_type;
    }

    /**
     *
     * @return
     * The userIntention
     */



    public String getUserIntention() {
                return userIntention;
        }

        /**
         *
         * @param userIntention
         * The user_intention
         */
        public void setUserIntention(String userIntention) {
                this.userIntention = userIntention;
        }

        /**
         *
         * @return
         * The ratingGiven
         */
        //@JsonProperty("rating_given")
        public String getRatingGiven() {
                return ratingGiven;
        }

        /**
         *
         * @param ratingGiven
         * The rating_given
         */
        //@JsonProperty("rating_given")
        public void setRatingGiven(String ratingGiven) {
                this.ratingGiven = ratingGiven;
        }
        public String getPincode() {
                return pincode;
        }

        public void setPincode(String pincode) {
                this.pincode = pincode;
        }

        public Map<String, Object> getAdditionalProperties() {
                return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
                this.additionalProperties.put(name, value);
        }


}

