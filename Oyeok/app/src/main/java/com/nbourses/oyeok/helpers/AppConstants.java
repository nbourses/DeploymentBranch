package com.nbourses.oyeok.helpers;

import com.nbourses.oyeok.models.PublishLetsOye;

/**
 * Created by rohit on 09/02/16.
 */
public class AppConstants {
    public static final int minRent = 20000;
    public static final int maxRent = 1200000;

    public static final int minSale = 9000000;
    public static final int maxSale = 100000000;

    public static final String CLOSE_OYE_SCREEN_SLIDE = "closeOyeScreenSlide";

    public static final String ON_FILTER_VALUE_UPDATE = "onFilterValueUpdate";

    public static final String IS_LOGGED_IN_USER = "isLoggedInUser";

    public static final String USER_ID = "userId";

    public static PublishLetsOye letsOye = new PublishLetsOye();

    public static final String SERVER_BASE_URL = "https://ssl.hailyo.com/1";

    public static final String OK_ID = "ok_id";

    public static final String ROLE_OF_USER = "role_of_user";

    public static final String CLIENT_OK_ID = "client_ok_id";

    public static final String DEFAULT_SNACKBAR_COLOR = "#ff9f1c";

    public static final String GOOGLE_PLAY_STORE_APP_URL = "https://play.google.com/store/apps/details?id=com.nbourses.oyeok";

    public static final String SUPPORT_CHANNEL_NAME = "my_channel";

    /*public static final String PUBNUB_PUBLISH_KEY = "pub-c-9e6f8c01-05f2-4de3-85fd-4cc860681760";

    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-f662fb7c-b5ff-11e5-a916-0619f8945a4f";*/

    public static final String PUBNUB_PUBLISH_KEY = "pub-c-9e6f8c01-05f2-4de3-85fd-4cc860681760";

    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-f662fb7c-b5ff-11e5-a916-0619f8945a4f";

    public static final String PUBNUB_SECRET_KEY = "sec-c-NDUxZWZlNWItMDMxZi00ZjM4LWIyODYtMjE5ODVmNmZhZjBj";
}
