package com.nbourses.oyeok.realmModels;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sushil on 23/02/17.
 */

public class ListingCatalogRealm  extends RealmObject {

    @PrimaryKey
    private  String catalog_id;

    private  String catalog_name;

    private  String user_id;

    private  String user_name;

    private RealmList<Listingidsrealm> Listingids;

    private RealmList<ListingRealm> DisplayListings;

    private  String city;

    private  String tt;

    private String Imageuri;


    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getCatalog_name() {
        return catalog_name;
    }

    public void setCatalog_name(String catalog_name) {
        this.catalog_name = catalog_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public RealmList<Listingidsrealm> getListingids() {
        return Listingids;
    }

    public void setListingids(RealmList<Listingidsrealm> listingids) {
        Listingids = listingids;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getImageuri() {
        return Imageuri;
    }

    public void setImageuri(String imageuri) {
        Imageuri = imageuri;
    }

    public RealmList<ListingRealm> getDisplayListings() {
        return DisplayListings;
    }

    public void setDisplayListings(RealmList<ListingRealm> displayListings) {
        DisplayListings = displayListings;
    }
}
