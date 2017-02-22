package com.nbourses.oyeok.models;

/**
 * Created by Ritesh Warke on 22/02/17.
 */

public class MatchingOK {
private String user_id;
    private String oye_id;
    private String gcm_id;
    private String page;
    private String platform;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOye_id() {
        return oye_id;
    }

    public void setOye_id(String oye_id) {
        this.oye_id = oye_id;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
