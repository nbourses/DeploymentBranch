package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseData {

    @SerializedName("price")
    @Expose
    public Price price;
    @SerializedName("buildings")
    @Expose
    public List<Building> buildings = new ArrayList<Building>();

    /**
     *
     * @return
     * The price
     */
    public Price getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The buildings
     */
    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     *
     * @param buildings
     * The buildings
     */
    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

}
