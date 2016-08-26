package com.nbourses.oyeok.realmModels;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ritesh on 17/08/16.
 */



public class NotifCount extends RealmObject {
    @PrimaryKey
    private String ok_id;
    private Integer notif_count;

    public NotifCount() { }

    public String getOk_id() {
        return ok_id;
    }

    public void setOk_id(String ok_id) {
        this.ok_id = ok_id;
    }

    public Integer getNotif_count() {
        return notif_count;
    }

    public void setNotif_count(Integer notif_count) {
        this.notif_count = notif_count;
    }
}