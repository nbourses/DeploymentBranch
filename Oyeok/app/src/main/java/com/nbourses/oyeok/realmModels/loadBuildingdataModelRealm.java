package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;

import static android.R.id.primary;

/**
 * Created by sushil on 07/02/17.
 */

public class loadBuildingdataModelRealm extends RealmObject {



    @PrimaryKey
    private String id;

    public loadBuildingdataModelRealm() {
    }

    public loadBuildingdataModelRealm(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
