package com.quintus.labs.datingapp.rest;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Narendra on 10/26/2016.
 */

public class RestResponse {

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("errorMessage")
    private String errorMessage;

    public RestResponse(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage =errorMessage;
    }

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String code)
    {
        this.statusCode =code;
    }

}
