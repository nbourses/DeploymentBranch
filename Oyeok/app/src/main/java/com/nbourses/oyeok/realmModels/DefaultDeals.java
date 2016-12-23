package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ritesh on 23/06/16.
 */
public class DefaultDeals extends RealmObject {
    @PrimaryKey
    private String ok_id;
    private String spec_code;
    private String locality;
    private String lastSeen;
    private String p_type;
    private String ps_type;
    private String furnishing;
    private String possation_date;
    private String budget;
    public DefaultDeals() { }

    public String getP_type() {
        return p_type;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getPs_type() {
        return ps_type;
    }

    public void setPs_type(String ps_type) {
        this.ps_type = ps_type;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public String getPossation_date() {
        return possation_date;
    }

    public void setPossation_date(String possation_date) {
        this.possation_date = possation_date;
    }

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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}
