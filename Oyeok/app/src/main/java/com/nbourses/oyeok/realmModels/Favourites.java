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
}
