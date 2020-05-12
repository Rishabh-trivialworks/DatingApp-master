package com.quintus.labs.datingapp.xmpp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMedia implements java.io.Serializable {
    private static final long serialVersionUID = -1895096268926742297L;
    private String s3MediaUrl;
    private String s3MediaThumbnailUrl;

    private Integer width;
    private Integer height;
    private Integer duration;

    private String storagePath;
    private Boolean isUploading = false;
    private Boolean isDownloading = false;
    private Boolean isSuccessfullyUploaded = false;



    public Boolean isUploading() {
        return isUploading;
    }

    public void setUploading(Boolean uploading) {
        isUploading = uploading;
    }

    public Boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(Boolean downloading) {
        isDownloading = downloading;
    }

    public Boolean isSuccessfullyUploaded() {
        return isSuccessfullyUploaded;
    }

    public void setSuccessfullyUploaded(Boolean successfullyUploaded) {
        isSuccessfullyUploaded = successfullyUploaded;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getS3MediaUrl() {
        return this.s3MediaUrl;
    }

    public void setS3MediaUrl(String s3MediaUrl) {
        this.s3MediaUrl = s3MediaUrl;
    }

    public String getS3MediaThumbnailUrl() {
        return this.s3MediaThumbnailUrl;
    }

    public void setS3MediaThumbnailUrl(String s3MediaThumbnailUrl) {
        this.s3MediaThumbnailUrl = s3MediaThumbnailUrl;
    }

    public Integer getWidth() {
        return width == null ? 0 : width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height == null ? 0 : height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDuration() {
        return duration == null ? 0 : duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}