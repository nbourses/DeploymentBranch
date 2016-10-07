package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ritesh on 23/09/16.
 */
public class Favourites extends RealmObject{
    @PrimaryKey
    private String title;
    private LatiLongi latiLongi;
    private String address;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatiLongi getLatiLongi() {
        return latiLongi;
    }

    public void setLatiLongi(LatiLongi latiLongi) {
        this.latiLongi = latiLongi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
