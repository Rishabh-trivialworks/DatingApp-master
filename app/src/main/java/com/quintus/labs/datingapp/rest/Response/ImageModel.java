package com.quintus.labs.datingapp.rest.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("url")
    @Expose
    String url;
    @SerializedName("userId")
    @Expose
    int userId;
    @SerializedName("updatedAt")
    @Expose
    String updatedAt;
    @SerializedName("createdAt")
    @Expose
    String createdAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
