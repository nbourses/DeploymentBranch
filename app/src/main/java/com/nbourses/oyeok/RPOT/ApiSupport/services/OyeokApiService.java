package com.nbourses.oyeok.RPOT.ApiSupport.services;

import com.google.gson.JsonElement;
import com.nbourses.oyeok.RPOT.ApiSupport.models.LetsOye;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.models.PreOk;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Abhinandan on 11/25/2015.
 */
public interface OyeokApiService {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter



    // API POST : bboxneighbours
        /*
            REQUEST
        {
        "user_id" : "7.216944372169443e+21",
        "Longitude": "9.96233",
        "Latitude": "49.80404",
        "user_role" : "client"
        "search_for" : "broker"
        }

            RESPONSE
        {
        "published_at": "2014-11-11T08:40:51.620Z",
        "user_id" : "7.216944372169443e+21",
        "user_role" : "client",
        "search_for" : "broker",
        "neighbours": [
                {
                "user_id": 1,
                "user_role":"broker",
                "user_intention":"pa",
                "loc":[9.96233,49.80404]
                },
                {
                "user_id": 2,
                "user_role":"broker",
                "user_intention":"pa",
                "loc": [9.96233,49.80404]
                }
                 ]
            }
        }*/

    @POST("/1/oyeok/brokers")
    void bboxneighbours(@Body Oyeok oyeok , Callback<Oyeok> callBack);

    @POST("/1/hailyo/giverating")
    void giveUserRoleRating(@Body Oyeok oyeok, Callback<Oyeok> callback);

    @POST("/1/pre/ok")
    void preOk(@Body Oyeok oyeok, Callback<JsonElement> callback);

    @POST("/1/lets/oye")
    void letsOye(@Body Oyeok oyeok, Callback<LetsOye> callback);

    @POST("/1/ok/acceptOk")
    void acceptOk(@Body Oyeok oyeok, Callback<Oyeok> callback);
}


