package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Building {




    @SerializedName("rate_growth")
    @Expose
    private String rate_growth;



    @SerializedName("config")
    @Expose
    private String config;
    @SerializedName("name")
    @Expose
    private String name;



    @SerializedName("or_psf")
    @Expose
    private String orPsf;
    @SerializedName("ll_pm")
    @Expose
    private String llPm;
    @SerializedName("loc")
    @Expose
    private List<String> loc = new ArrayList<String>();


    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }


    public String getRate_growth() {
        return rate_growth;
    }

    public void setRate_growth(String rate_growth) {
        this.rate_growth = rate_growth;
    }

    /**
     *
     * @return
     * The name
     */




    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The orPsf
     */
    public String getOrPsf() {
        return orPsf;
    }

    /**
     *
     * @param orPsf
     * The or_psf
     */
    public void setOrPsf(String orPsf) {
        this.orPsf = orPsf;
    }

    /**
     *
     * @return
     * The llPm
     */
    public String getLlPm() {
        return llPm;
    }

    /**
     *
     * @param llPm
     * The ll_pm
     */
    public void setLlPm(String llPm) {
        this.llPm = llPm;
    }

    /**
     *
     * @return
     * The loc
     */
    public List<String> getLoc() {
        return loc;
    }

    /**
     *
     * @param loc
     * The loc
     */
    public void setLoc(List<String> loc) {
        this.loc = loc;
    }

}
