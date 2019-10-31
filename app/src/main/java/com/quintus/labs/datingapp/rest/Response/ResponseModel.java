package com.quintus.labs.datingapp.rest.Response;

/**
 * Created by MyU10 on 1/21/2017.
 */

public class ResponseModel<T> {

    public boolean dataFromWiderWindow;
    public String success;
    public String errorMessage;
    public String updateInfoText;
    public int totalCount;
    public T data;

}
