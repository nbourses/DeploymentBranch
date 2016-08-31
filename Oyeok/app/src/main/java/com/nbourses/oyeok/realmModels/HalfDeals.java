package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ritesh on 12/07/16.
 */
public class HalfDeals extends RealmObject {
    @PrimaryKey
    private String ok_id;
    private String spec_code;
    private String name;
    private String locality;
    private String oyeId;
    public HalfDeals() { }

    public String getOk_id() {
        return ok_id;
    }

    public void setOk_id(String ok_id) {
        this.ok_id = ok_id;
    }

    public String getSpec_code() {
        return spec_code;
    }

    public void setSpec_code(String spec_code) {
        this.spec_code = spec_code;
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

    public String getOyeId() {
        return oyeId;
    }

    public void setOyeId(String oyeId) {
        this.oyeId = oyeId;
    }
}
