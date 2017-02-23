package com.nbourses.oyeok.models;

/**
 * Created by Ritesh Warke on 18/02/17.
 */

public class MatchListing {
    private String title;
    private String desc;
    private String growth_rate;
    private String locality;
    private String broker_name;
    private String date;
    private String price;
    private String config;
    private String oye_id;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MatchListing(String oye_id,String config, String broker_name, String locality, String date, String price, String growth_rate) {
        this.config = config;
        this.broker_name = broker_name;
        this.locality = locality;
        this.date = date;
        this.price = price;
        this.growth_rate = growth_rate;
        this.oye_id = oye_id;
    }

    public String getOye_id() {
        return oye_id;
    }

    public void setOye_id(String oye_id) {
        this.oye_id = oye_id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getBroker_name() {
        return broker_name;
    }

    public void setBroker_name(String broker_name) {
        this.broker_name = broker_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getGrowth_rate() {
        return growth_rate;
    }

    public void setGrowth_rate(String growth_rate) {
        this.growth_rate = growth_rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
