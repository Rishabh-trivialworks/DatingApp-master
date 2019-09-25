package com.quintus.labs.datingapp.rest;

import android.util.Log;

import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public abstract class RestFulCallback<T> implements Callback<T> {

    public abstract void onFailure(String message);

    public abstract void onSuccess(Response<T> response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (call.isCanceled())
            return;
        Log.e("ERROR", t.toString());
        onFailure("Something went wrong! please try again.");
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response);
        } else {
            onFailure(parseError(response).getErrorMessage());
        }
    }

    private RestResponse parseError(Response<?> response) {
        Converter<ResponseBody, RestResponse> converter = RestServiceFactory.retrofit()
                .responseBodyConverter(RestResponse.class, new Annotation[0]);

      RestResponse error;
        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            return new RestResponse("Something went wrong! please try again.");
        }
        return error;
    }
}
