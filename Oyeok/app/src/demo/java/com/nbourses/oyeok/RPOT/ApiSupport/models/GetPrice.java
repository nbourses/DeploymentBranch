package com.nbourses.oyeok.RPOT.ApiSupport.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class GetPrice {

    @SerializedName("errors")
    @Expose
    private List<Object> errors = new ArrayList<Object>();
    @SerializedName("exceptions")
    @Expose
    private List<Object> exceptions = new ArrayList<Object>();
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("responseData")
    @Expose
    public ResponseData responseData;

    /**
     *
     * @return
     * The errors
     */
    public List<Object> getErrors() {
        return errors;
    }

    /**
     *
     * @param errors
     * The errors
     */
    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    /**
     *
     * @return
     * The exceptions
     */
    public List<Object> getExceptions() {
        return exceptions;
    }

    /**
     *
     * @param exceptions
     * The exceptions
     */
    public void setExceptions(List<Object> exceptions) {
        this.exceptions = exceptions;
    }

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The responseData
     */
    public ResponseData getResponseData() {
        return responseData;
    }

    /**
     *
     * @param responseData
     * The responseData
     */
    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

}


