package com.nbourses.oyeok.RPOT.ApiSupport.services;

import com.google.gson.JsonElement;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AcceptOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.AutoOk;
import com.nbourses.oyeok.RPOT.ApiSupport.models.BrokerBuildings;
import com.nbourses.oyeok.RPOT.ApiSupport.models.LetsOye;
import com.nbourses.oyeok.RPOT.ApiSupport.models.Oyeok;
import com.nbourses.oyeok.RPOT.ApiSupport.models.ShareOwnersNoM;
import com.nbourses.oyeok.RPOT.ApiSupport.models.UpdateStatus;
import com.nbourses.oyeok.RPOT.ApiSupport.models.User;
import com.nbourses.oyeok.RPOT.ApiSupport.models.deleteHDroom;
import com.nbourses.oyeok.models.AddBuildingModel;
import com.nbourses.oyeok.models.AddListingBorker;
import com.nbourses.oyeok.models.CreateWatchlistAPI;
import com.nbourses.oyeok.models.GetLocality;
import com.nbourses.oyeok.models.HdRooms;
import com.nbourses.oyeok.models.MatchingOK;
import com.nbourses.oyeok.models.PreOk;
import com.nbourses.oyeok.models.PublishLetsOye;
import com.nbourses.oyeok.models.ReadWatchlistAPI;
import com.nbourses.oyeok.models.SearchBuildingModel;
import com.nbourses.oyeok.models.UpdateBuildingRateModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Abhinandan on 11/25/2015.
 */
public interface OyeokApiService {


    @POST("/matching/ok")
    void matchingOk(@Body MatchingOK m, Callback<JsonElement> callback);

    @POST("/1/oyeok/brokers")
    void bboxneighbours(@Body Oyeok oyeok , Callback<Oyeok> callBack);

    @POST("/1/hailyo/giverating")
    void giveUserRoleRating(@Body Oyeok oyeok, Callback<Oyeok> callback);

    @POST("/pre/ok")
    void preOk(@Body PreOk preok, Callback<JsonElement> callback);

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

    @POST("/unregistered/ok")
    void autoOk(@Body AutoOk autoOk, Callback<JsonElement> callback);

    @POST("/update/hdroom_status")
    void updateStatus(@Body UpdateStatus updateStatus, Callback<JsonElement> callback);

    @POST("/get/hdroom_status")
    void getStatus(@Body UpdateStatus updateStatus, Callback<JsonElement> callback);

    @POST("/delete/hdroom")
    void deleteHDroom(@Body deleteHDroom deleteHDroom, Callback<JsonElement> callback);

    @POST("/user/signup")
    void signUp(@Body User user, Callback<JsonElement> callback);

    @POST("/generate/token")
    void updateStatus1(@Body UpdateStatus updateStatus, Callback<JsonElement> callback);

    @POST("/check/token")
    void updateStatus2(@Body UpdateStatus updateStatus, Callback<JsonElement> callback);


    @POST("/add/building")
    void addBuilding(@Body AddBuildingModel addBuildingModel, Callback<JsonElement> callback);

    @POST("/shortlist/building")
    void SearchBuilding(@Body SearchBuildingModel searchBuildingModel, Callback<JsonElement> callback);

    @POST("/add/listings")
    void addListing(@Body AddListingBorker addListingBorker, Callback<JsonElement> callback);

    @POST("/add/building")
    void updateBuildingData(@Body UpdateBuildingRateModel updateBuildingRateModel, Callback<JsonElement> callback);

    @POST("/get/locality")
    void getLocality(@Body GetLocality getLocality, Callback<JsonElement> callback);

    @POST("/watch/list")
    void CreateWatchlist(@Body CreateWatchlistAPI createWatchlistAPI, Callback<JsonElement> callback);

    @POST("/watch/list")
    void ReadWatchlist(@Body ReadWatchlistAPI readWatchlistAPI, Callback<JsonElement> callback);

}


