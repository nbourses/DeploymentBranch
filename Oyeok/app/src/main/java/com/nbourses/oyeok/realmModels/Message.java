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
    public Message() { }

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

}
