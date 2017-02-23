package com.nbourses.oyeok.models;

/**
 * Created by RiteshWarke on 30/01/17.
 */

public class ActivityLog {
    private String name;
    private String user_id;
    private String action;
    public ActivityLog() {
    }

    public ActivityLog(String name, String action) {
        this.name = name;
        this.action = action;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
