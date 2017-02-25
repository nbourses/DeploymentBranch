package com.nbourses.oyeok.models;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sushil on 07/11/16.
 */
//implements Comparable<portListingModel>
public class portListingModel {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("locality")
    private String locality;

    @Expose
    @SerializedName("growth_rate")
    private String growth_rate;

    @Expose
    @SerializedName("ll_pm")
    private int ll_pm;


    @Expose
    @SerializedName("or_psf")
    private int or_psf;

    @Expose
    @SerializedName("timpstamp")
    private String timpstamp;

    @Expose
    @SerializedName("transaction")
    private String transaction;

    @Expose
    @SerializedName("config")
    private String config;

    @Expose
    @SerializedName("display_type")
    private String display_type;

    @Expose
    @SerializedName("tt")
    private String tt;

    @Expose
    @SerializedName("req_avl")
    private String req_avl;

    @Expose
    @SerializedName("furnishing")
    private String furnishing;

    @Expose
    @SerializedName("market_rate")
    private int market_rate;
    @Expose
    @SerializedName("possession_date")
    private String possession_date;

    @Expose
    @SerializedName("watchlist_id")
    private String watchlist_id;

    @Expose
    @SerializedName("imageUri")
    private String imageUri;

    @Expose
    @SerializedName("city")
    private String city;

    @Expose
    @SerializedName("checkbox")
    private boolean checkbox;


    @Expose
    @SerializedName("catalog_id")
    private String catalog_id;



    /*@Expose
    @SerializedName("display_type")
    private String display_type;   watchlist_name

    @Expose
    @SerializedName("display_type")
    private String display_type;*/

    public String getPossession_date() {
        return possession_date;
    }

    public void setPossession_date(String possession_date) {
        this.possession_date = possession_date;
    }

    public int getMarket_rate() {
        return market_rate;
    }

    public void setMarket_rate(int market_rate) {
        this.market_rate = market_rate;
    }

    public void setReq_avl(String req_avl) {
        this.req_avl = req_avl;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }



    public void setDisplay_type(String display_type) {
        this.display_type = display_type;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getDisplay_type() {
        return display_type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config, String display_type, String tt,boolean checkbox) {
        this.id = id;
        this.name = name;
        this.locality = locality;
        this.growth_rate = growth_rate;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.timpstamp = timpstamp;
        this.transaction = transaction;
        this.config = config;
        this.display_type = display_type;
        this.tt = tt;
        this.checkbox=checkbox;
    }

    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config, String display_type, String tt) {
        this.id = id;
        this.name = name;
        this.locality = locality;
        this.growth_rate = growth_rate;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.timpstamp = timpstamp;
        this.transaction = transaction;
        this.config = config;
        this.display_type = display_type;
        this.tt = tt;
    }

    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config, String display_type, String tt, String req_avl, String furnishing, int market_rate, String possession_date,boolean checkbox) {
        this.id = id;
        this.name = name;
        this.locality = locality;
        this.growth_rate = growth_rate;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.timpstamp = timpstamp;
        this.transaction = transaction;
        this.config = config;
        this.display_type = display_type;
        this.tt = tt;
        this.req_avl = req_avl;
        this.furnishing = furnishing;
        this.market_rate = market_rate;
        this.possession_date = possession_date;
        this.checkbox=checkbox;
    }

    public portListingModel(String id, String name, String locality, String growth_rate, int ll_pm, int or_psf, String timpstamp, String transaction, String config, String display_type, String tt, String req_avl, String furnishing) {
        this.id = id;
        this.name = name;
        this.locality = locality;
        this.growth_rate = growth_rate;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.timpstamp = timpstamp;
        this.transaction = transaction;
        this.config = config;
        this.display_type = display_type;
        this.tt = tt;
        this.req_avl = req_avl;
        this.furnishing = furnishing;
    }

    public portListingModel(String name, String locality, int ll_pm, int or_psf, String timpstamp, String growth_rate, String display_type) {

        this.name = name;
        this.locality = locality;
        this.growth_rate = growth_rate;
        this.ll_pm = ll_pm;
        this.or_psf = or_psf;
        this.timpstamp = timpstamp;

        this.display_type=display_type;
    }

    public portListingModel(String watchlist_id, String name,String imageUri, String display_type) {
        this.watchlist_id = watchlist_id;
        this.name = name;
        this.imageUri = imageUri;
        this.display_type = display_type;
    }

    public portListingModel(String catalog_id, String name,String imageUri, String display_type,String tt) {
        this.catalog_id = catalog_id;
        this.name = name;
        this.imageUri = imageUri;
        this.display_type = display_type;
        this.tt = tt;
    }

    public String getReq_avl() {
        return req_avl;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getGrowth_rate() {
        return growth_rate;
    }

    public void setGrowth_rate(String growth_rate) {
        this.growth_rate = growth_rate;
    }

    public int getLl_pm() {
        return ll_pm;
    }

    public void setLl_pm(int ll_pm) {
        this.ll_pm = ll_pm;
    }

    public int getOr_psf() {
        return or_psf;
    }

    public void setOr_psf(int or_psf) {
        this.or_psf = or_psf;
    }

    public String getTimpstamp() {
        return timpstamp;
    }

    public void setTimpstamp(String timpstamp) {
        this.timpstamp = timpstamp;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(String watchlist_id) {
        this.watchlist_id = watchlist_id;
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

  /*
    @Override
    public int compareTo(portListingModel another) {
        return 0;
    }*/
}
