package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhinandan on 11/25/2015.
 */
public class Oyeok {
    /**
     * A unique identifier for the user
     *
     */
    @SerializedName("oye_id")
    private String oyeId;

    @SerializedName("time_to_meet")
    private String timeToMeet;

    @SerializedName("responseData")
    public ResponseData responseData = new ResponseData();

    @SerializedName("user_id")
    private String userId;

    @SerializedName("mobile_no")
    private String mobileNo;

    @SerializedName("oye_user_id")
    private String oyeUserId;

    @SerializedName("success")
    private String success;
    /**
     * The hash geonum value is a numeric
     *
     */
    @SerializedName("geonum")
    private Double geonum;
    /**
     * 2BHK-95000-Rs (required,string)- Updated spec code is sent
     *
     */
    @SerializedName("spec_code")
    private String specCode;
    /**
     * true boolean value when a Broker manually accepts a Yo and false boolean value when time elapses to accept a oye which means no 'Yo' is made
     *
     */
    @SerializedName("oked")
    private Boolean oked;
    /**
     * Time in milliseconds(ms) collected to understand urgency of broker | liquidity of system to serve oyes
     *
     */
    @SerializedName("timetook")
    private Double timetook;
    /**
     * Shelf Life Timeout in milliseconds(ms) to expire the oye on the broker app screen
     *
     */
    @SerializedName("timeout")
    private Double timeout;
    /**
     * oyeok id helps in creating history book for previous oyeoks
     *
     */
    @SerializedName("oyeok_id")
    private Double oyeokId;
    /**
     * Helps in setting the status of oye whether it is on or cancelled
     *
     */
    @SerializedName("oye_status")
    private Boolean oyeStatus;
    /**
     * End hail command giver
     *
     */
    @SerializedName("userid_giver")
    private String useridGiver;

    @SerializedName("long")
    private String lng;

    @SerializedName("lat")
    private String lat;

    @SerializedName("push_token")
    private String pushToken;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    @SerializedName("gcm_id")
    private String gcmId;


    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @SerializedName("platform")
    public String platform;

    /**
     * End hail command taker
     *
     */
    @SerializedName("userid_taker")
    private String useridTaker;

    @SerializedName("user_role")
    private String userRole;
    /**
     * Binary rating "Wow" = true and "Not so Wow" = False
     *
     */
    @SerializedName("rating")
    private Boolean rating;

    @SerializedName("req_avl")
    private String reqAvl;

    @SerializedName("mobile_code")
    private String mobileCode;


    @SerializedName("device_id")
    private String deviceId;



    @SerializedName("region")
    private String region;



    @SerializedName("pincode")
    private String pincode;

    @SerializedName("tt")
    private String tt;

    @SerializedName("size")
    private String size;

    @SerializedName("price")
    private String price;



    @SerializedName("property_type")
    private String propertyType;


    @SerializedName("property_subtype")
    private String propertySubtype;

    public String getPropertySubtype() {
        return propertySubtype;
    }

    public void setPropertySubtype(String propertySubtype) {
        this.propertySubtype = propertySubtype;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    //@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * A unique identifier for the user
     *
     * @return
     * The userId
     */


    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
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

    public void setOyeId(String oyeId){
        this.oyeId=oyeId;
    }

    public String getOyeId(){
        return oyeId;
    }



    public void setTimeToMeet(String timeToMeet){
        this.timeToMeet=timeToMeet;
    }

    public String getTimeToMeet(){
        return timeToMeet;
    }

    public ResponseData getResponseData(){
        return responseData;
    }

    public void setResponseData(ResponseData responseData){
        this.responseData= responseData;
    }
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMobileCode(){
        return mobileCode;
    }

    public void setMobileCode(String mobileCode){
        this.mobileCode=mobileCode;
    }

    public String getPushToken(){
        return pushToken;
    }

    public void setPushToken(String pushToken){
        this.pushToken=pushToken;
    }

    public String getDeviceIdd(){
        return deviceId;
    }

    public void setDeviceId(String deviceId){
        this.deviceId=deviceId;
    }

    public String getReqAvl(){
        return reqAvl;
    }
    public String getUserRoleole(){
        return userRole;
    }
    public void setUserRole(String user_role){
        this.userRole=user_role;
    }

    public void setMobileNo(String mobileNo){
        this.mobileNo=mobileNo;
    }

    public String getOyeUserId() {
        return oyeUserId;
    }

    public void setOyeUserId(String oyeUserId) {
        this.oyeUserId = oyeUserId;
    }

    public String getMobileNo(){
        return mobileNo;
    }

    public String getLong(){
        return lng;
    }
    public void setLong(String lng){
        this.lng=lng;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getemail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }

    public String getLat(){
        return lat;
    }
    public void setLat(String lat){
        this.lat=lat;
    }

    public void setReqAvl(String reqAvl){
        this.reqAvl=reqAvl;
    }
    public String getUserId() {
        return userId;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
    /**
     * A unique identifier for the user
     *
     * @param userId
     * The user_id
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * The hash geonum value is a numeric
     *
     * @return
     * The geonum
     */

    public Double getGeonum() {
        return geonum;
    }

    /**
     * The hash geonum value is a numeric
     *
     * @param geonum
     * The geonum
     */

    public void setGeonum(Double geonum) {
        this.geonum = geonum;
    }

    /**
     * 2BHK-95000-Rs (required,string)- Updated spec code is sent
     *
     * @return
     * The specCode
     */

    public String getSpecCode() {
        return specCode;
    }

    /**
     * 2BHK-95000-Rs (required,string)- Updated spec code is sent
     *
     * @param specCode
     * The spec_code
     */

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    /**
     * true boolean value when a Broker manually accepts a ok and false boolean value when time elapses to accept a hail which means no 'ok' is made
     *
     * @return
     * The oked
     */

    public Boolean getoked() {
        return oked;
    }

    /**
     * true boolean value when a Broker manually accepts a ok and false boolean value when time elapses to accept a hail which means no 'ok' is made
     *
     * @param oked
     * The oked
     */

    public void setoked(Boolean oked) {
        this.oked = oked;
    }

    /**
     * Time in milliseconds(ms) collected to understand urgency of broker | liquidity of system to serve hails
     *
     * @return
     * The timetook
     */

    public Double getTimetook() {
        return timetook;
    }

    /**
     * Time in milliseconds(ms) collected to understand urgency of broker | liquidity of system to serve hails
     *
     * @param timetook
     * The timetook
     */

    public void setTimetook(Double timetook) {
        this.timetook = timetook;
    }

    /**
     * Shelf Life Timeout in milliseconds(ms) to expire the hail on the broker app screen
     *
     * @return
     * The timeout
     */

    public Double getTimeout() {
        return timeout;
    }

    /**
     * Shelf Life Timeout in milliseconds(ms) to expire the hail on the broker app screen
     *
     * @param timeout
     * The timeout
     */

    public void setTimeout(Double timeout) {
        this.timeout = timeout;
    }

    /**
     * hailok id helps in creating history book for previous hailoks
     *
     * @return
     * The hailokId
     */

    public Double getoyeokId() {
        return oyeokId;
    }

    /**
     * hailok id helps in creating history book for previous hailoks
     *
     * @param oyeokId
     * The oyeok_id
     */

    public void setoyeokId(Double oyeokId) {
        this.oyeokId = oyeokId;
    }

    /**
     * Helps in setting the status of oye whether it is on or cancelled
     *
     * @return
     * The oyeStatus
     */

    public Boolean getoyeStatus() {
        return oyeStatus;
    }

    /**
     * Helps in setting the status of oye whether it is on or cancelled
     *
     * @param oyeStatus
     * The oye_status
     */

    public void setoyeStatus(Boolean oyeStatus) {
        this.oyeStatus = oyeStatus;
    }

    /**
     * End oye command giver
     *
     * @return
     * The useridGiver
     */

    public String getUseridGiver() {
        return useridGiver;
    }

    /**
     * End oye command giver
     *
     * @param useridGiver
     * The userid_giver
     */

    public void setUseridGiver(String useridGiver) {
        this.useridGiver = useridGiver;
    }

    /**
     * End oye command taker
     *
     * @return
     * The useridTaker
     */

    public String getUseridTaker() {
        return useridTaker;
    }

    /**
     * End oye command taker
     *
     * @param useridTaker
     * The userid_taker
     */



    public void setUseridTaker(String useridTaker) {
        this.useridTaker = useridTaker;
    }

    /**
     * Binary rating "Wow" = true and "Not so Wow" = False
     *
     * @return
     * The rating
     */

    public Boolean getRating() {
        return rating;
    }

    /**
     * Binary rating "Wow" = true and "Not so Wow" = False
     *
     * @param rating
     * The rating
     */

    public void setRating(Boolean rating) {
        this.rating = rating;
    }

    //@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    //@JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public class ResponseData{

        @SerializedName("neighbours")
        public Neighbours neighbours[] = new Neighbours[5];

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



        /*private String neighbours;

        public void setNeighbours(String neighbours){
            this.neighbours=neighbours;
        }

        public String getNeighbours(){
            return neighbours;
        }*/

        public class Neighbours{
            @SerializedName("oye_id")
            public String oyeId;

            public void setOyeId(String publishedAt){
                this.oyeId=oyeId;
            }

            public String getOyeId(){
                return oyeId;
            }


            @SerializedName("spec_code")
            public String specCode;

            public void setSpecCode(String spec_code){
                this.specCode=specCode;
            }

            public String getSpecCode(){
                return specCode;
            }


            public User user= new User();


        }
    }
}
