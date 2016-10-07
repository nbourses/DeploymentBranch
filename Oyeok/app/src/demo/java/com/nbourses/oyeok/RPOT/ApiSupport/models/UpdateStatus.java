package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ritesh on 19/08/16.
 */
public class UpdateStatus {
    private String success;
    @SerializedName("ok_id")
    private String okId;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getOkId() {
        return okId;
    }

    public void setOkId(String okId) {
        this.okId = okId;
    }
    @SerializedName("last_seen")
    private String last_seen;

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("blocked_by")
    private String blocked_by;

    public String getBlocked_by() {
        return blocked_by;
    }

    public void setBlocked_by(String blocked_by) {
        this.blocked_by = blocked_by;
    }
}
