package com.nbourses.oyeok.helpers;

import com.nbourses.oyeok.models.PublishLetsOye;

/**
 * Created by rohit on 09/02/16.
 */
public class AppConstants {
    public static  int minRent = 35000;
    public static  int maxRent = 60000;

    public static  int minSale = 21500;
    public static  int maxSale = 27000;

    public static final int slowInternet = 5;


    public static String MY_LAT = "myLat";
    public static String MY_LNG = "myLong";
    public static String LOCALITY = "locality";
    public static final String AUTOCOMPLETEFLAG1 = "autoCompleteFlag1";
    public static final String AUTOCOMPLETEFLAG = "autoCompleteFlag";
    public static final String SIGNUPSUCCESSFLAG = "signupSuccessFlag";
    public static final String SLIDEDOWNBUILDINGS = "slideDownBuildings";
    public static final String BUILDINGSLIDERFLAG = "buildingSliderFlag";
    public static final String NETWORK_CONNECTIVITY = "networkConnectivity";
    public static final String CLOSE_OYE_SCREEN_SLIDE = "closeOyeScreenSlide";
    public static final String OYE_BUTTON_DATA = "oyeButtonData";

    public static final String BADGE_COUNT_BROADCAST = "badgeCountBroadcast";
    public static final String CHECK_BEACON = "checkBeacon";
    public static final String CHECK_WALKTHROUGH = "checkWalkthrough";

    public static final String LOCALITY_BROADCAST = "localityBroadcast";

    public static final String ON_FILTER_VALUE_UPDATE = "onFilterValueUpdate";
    public static final String BROADCAST_MIN_MAX_VAL = "broadcastMinMaxValue";

    public static final String IS_LOGGED_IN_USER = "isLoggedInUser";

    public static final String USER_ID = "userId";
    public static final String NAME = "name";

    public static final String EMAIL = "email";
    public static final String CLIENT_HEADING = "client_heading";

    public static final String MARKERSELECTED = "markerSelected";



    public static PublishLetsOye letsOye = new PublishLetsOye();

    public static final String SERVER_BASE_URL = "https://ssl.hailyo.com/1";

    public static final String SERVER_BASE_URL_101 = "https://ssl.hailyo.com/1.01";


    public static final String OK_ID = "ok_id";

    public static final String ROLE_OF_USER = "role_of_user";

    public static final String CLIENT_OK_ID = "client_ok_id";

    public static final String DEFAULT_SNACKBAR_COLOR = "#ff9f1c";

    public static final String GOOGLE_PLAY_STORE_APP_URL = "https://play.google.com/store/apps/details?id=com.nbourses.oyeok";

    public static final String SUPPORT_CHANNEL_NAME = "my_channel";

    /*public static final String PUBNUB_PUBLISH_KEY = "pub-c-9e6f8c01-05f2-4de3-85fd-4cc860681760";

    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-f662fb7c-b5ff-11e5-a916-0619f8945a4f";*/

    public static final String PUBNUB_PUBLISH_KEY = "pub-c-d7dde449-a76a-465c-8545-1d1ee3f7da9c";

    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-ea3f9f6e-0625-11e6-a6dc-02ee2ddab7fe";

    public static final String PUBNUB_SECRET_KEY = "sec-c-NDUxZWZlNWItMDMxZi00ZjM4LWIyODYtMjE5ODVmNmZhZjBj";
    public static final String BADGE_COUNT = "badgeCount";
    public static final String SUPPORT_COUNT = "supportCount";
    public static final String HDROOMS_COUNT = "hdRoomsCount";
    public static final String TENANTS_COUNT = "tenantsCount";
    public static final String OWNERS_COUNT = "ownersCount";
    public static final String BUYER_COUNT = "buyerCount";
    public static final String SELLER_COUNT = "sellerCount";
    public static final String RENTAL_COUNT = "rentalCount";
    public static final String RESALE_COUNT = "resaleCount";

    public static final String GCM_ID = "gcm_id";
    public static final String TT = "TT";
    public static final String RENTAL ="RENTAL";
    public static final String RESALE = "RESALE";
    public static final String MUTED_OKIDS ="mutedOKIds";
    public static final String NO_OF_SAVED_MESSAGES = "20";
    public static final String TIME_STAMP_IN_MILLI = "20";
    public static final String SPEC_CODE = "specCode";
    public static final String CACHE = "_cache";   // dynamic channel_name_CACHE

// REALM dB model app constants by Ritesh
//USERINFO MODEL
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String PTYPE = "ptype";
    public static final String PSTYPE = "pstype";
    public static final String PRICE = "price";
    public static Boolean SIGNUP_FLAG = false;
    public static final String UPLOADED_IMAGE_PATH = "uploadedImagePath";

}
