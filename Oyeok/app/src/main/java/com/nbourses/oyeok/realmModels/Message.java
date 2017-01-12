package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;

/**
 * Created by ritesh on 24/06/16.
 */
public class Message extends RealmObject {


    private String ok_id;
    private String message;
    private String timestamp;
    private String to;
    private String from;
    private String imageUrl;
    private String user_id;
    private String status;
    private String r_by;
    public Message() { }


    public String getR_by() {
        return r_by;
    }

    public void setR_by(String r_by) {
        this.r_by = r_by;
    }

    public String getOk_id() {
        return ok_id;
    }

    public void setOk_id(String ok_id) {
        this.ok_id = ok_id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
