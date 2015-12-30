package com.nbourses.oyeok.RPOT.ApiSupport.services;

import android.util.Log;

import com.nbourses.oyeok.RPOT.ApiSupport.models.GetPrice;
import com.nbourses.oyeok.RPOT.ApiSupport.models.MobileVerify;
import com.nbourses.oyeok.RPOT.ApiSupport.models.SignUp;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


/*
 * Created by sagun on 07/09/15.
 * Reference Tutorials
 * http://themakeinfo.com/2015/04/retrofit-android-tutorial/
 * http://www.node.mu/2014/07/02/using-retrofit-and-rxjava-to-interact-with-web-services-on-android/
 * http://stackoverflow.com/questions/24279245/how-to-handle-dynamic-json-in-retrofit
 * http://stackoverflow.com/questions/27648291/post-request-using-retrofit-custom-parser
 * Generate POJO Class: http://www.jsonschema2pojo.org/

 */


/*public class UserApiService {



    public static final String TAG = OyeokApiService.class.getSimpleName();
    //String API = "http://ec2-52-27-37-225.us-west-2.compute.amazonaws.com:9000";
    String API = "http://ec2-52-25-136-179.us-west-2.compute.amazonaws.com:9000";

    private final UserApiInterface service ;

    public UserApiService(User user, Callback<User> callBack) {

        Log.i(TAG, "INSIDE Update Location Service");

        //Retrofit section start from here...
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).build();

        //Create API service
        service = restAdapter.create(UserApiInterface.class);

        //Invoke method. Parameters must be defined earlier of course
        //service.sendLocation(user, callBack);
        //service.getUserProfile(user, callBack);
        //service.userSignUp(user, callBack);
        //service.getUserRoleRating(user, callBack);
        service.testGcm(user, callBack);

    }*/

    public interface UserApiService {

        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        // API POST Request
      /*  {
        "user_id" : "7.216944372169443e+21",
        "Longitude": 9.96233,
        "Latitude": 49.80404,
        "user_role" : "broker"
        }*/


        //https://futurestud.io/blog/retrofit-send-objects-in-request-body/
        @POST("/1/user/gps")
        void sendLocation(@Body User user ,Callback<User> callBack);

        // API POST Request
      /*  {
        "user_id" : "7.216944372169443e+21",
        "Longitude": 9.96233,
        "Latitude": 49.80404,
        "user_role" : "broker"
        }

        if user_id is "anonymous" then the user signing up is new user if user_id is "a value" then it's an existing user
        */

        @POST("/1/get/price")
        void getPrice(@Body User user, Callback<GetPrice> callback);

        @POST("/1/user/existing")
        void existingUser(@Body User user, Callback<User> callback);

        @POST("/1/user/switchrole")
        void switchroleUser(@Body User user, Callback<User> callback);

        @POST("/1/user/deactivate")
        void deactivateUser(@Body User user, Callback<User> callback);

        @POST("/1/give/rating")
        void getUserRoleRating (@Body User user, Callback<User> callback);

        //Pratik's playground
        //SignUp removed from the user flow
        @POST("/1/user/signup")
        void userSignUp(@Body User user, Callback<SignUp> callback);



        @POST("/1/user/updatemydetails")
        void updateUserDetails(@Body User user, Callback<User> callback);

        @POST("/1/user/profile")
        void getUserProfile(@Body User user, Callback<User> callback);

       /* @POST("1/user/rating")
        void setUserRating(@Body User user, Callback<User>);*/

        @POST("/1/test/gcm")
        void testGcm(@Body User user, Callback<User> callback);

        @POST("/1/verify/mobile")
        void verifyMobile(@Body User user, Callback<MobileVerify> callback);


    }

//}
