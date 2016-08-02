package com.nbourses.oyeok.RPOT.ApiSupport.services;

import com.google.gson.JsonElement;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AcceptOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.BrokerBuildings;
import com.nbourses.oyeok.RPOT.ApiSupport.models.LetsOye;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.models.ShareOwnersNoM;
import com.nbourses.oyeok.RPOT.ApiSupport.models.deleteHDroom;
import com.nbourses.oyeok.models.HdRooms;
import com.nbourses.oyeok.models.PublishLetsOye;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Abhinandan on 11/25/2015.
 */
public interface OyeokApiService {



    @POST("/1/oyeok/brokers")
    void bboxneighbours(@Body Oyeok oyeok , Callback<Oyeok> callBack);

    @POST("/1/hailyo/giverating")
    void giveUserRoleRating(@Body Oyeok oyeok, Callback<Oyeok> callback);

    @POST("/pre/ok")
    void preOk(@Body Oyeok oyeok, Callback<JsonElement> callback);

    @POST("/1/lets/oye")
    void letsOye(@Body Oyeok oyeok, Callback<LetsOye> callback);

    @POST("/1/ok/accept")
    void acceptOk(@Body Oyeok oyeok, Callback<AcceptOk> callback);

    @POST("/lets/oye")
    void publishOye(@Body PublishLetsOye publishLetsOye, Callback<PublishLetsOye> callback);

    @POST("/see/hdrooms")
    void seeHdRooms(@Body HdRooms hdRooms, Callback<PublishLetsOye> callback);

    @POST("/broker/buildings")
    void brokerBuildings(@Body BrokerBuildings brokerBuildings, Callback<JsonElement> callback);

    @POST("/generate/coupon")
    void generateCoupon(@Body ShareOwnersNoM shareOwnersNoM, Callback<JsonElement> callback);

    @POST("/delete/hdroom")
    void deleteHDroom(@Body deleteHDroom deleteHDroom, Callback<JsonElement> callback);
}


