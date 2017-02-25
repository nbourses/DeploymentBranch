package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sushil on 23/02/17.
 */

public class Listingidsrealm extends RealmObject {

    @PrimaryKey
    private  String listing_id;

    public Listingidsrealm() {
    }

    public Listingidsrealm(String listing_id) {
        this.listing_id = listing_id;
    }

    public String getListing_id() {
        return listing_id;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }
}
