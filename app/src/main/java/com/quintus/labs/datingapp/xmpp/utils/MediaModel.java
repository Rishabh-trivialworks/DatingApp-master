package com.quintus.labs.datingapp.xmpp.utils;

import java.io.Serializable;

public class MediaModel implements Serializable {
    private String thumbnailPath;
    private String mediaType;
    private int id;
    private String mediaPath;
    private String mediaName;
    private String originalMediaName;
    private String mediaSize;
    private long createdAt;
    private long mediaSizeInByte;
    private long consumedSpace;
    private long totalAllocatedSpace;
    private int height, width;
    public int downloadedPercentage;
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof MediaModel) {
            MediaModel model = (MediaModel) obj;
            if (model.getId() == getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return 928;
    }

    public long getConsumedSpace() {
        return consumedSpace;
    }

    public void setConsumedSpace(long consumedSpace) {
        this.consumedSpace = consumedSpace;
    }

    public long getTotalAllocatedSpace() {
        return totalAllocatedSpace;
    }

    public void setTotalAllocatedSpace(long totalAllocatedSpace) {
        this.totalAllocatedSpace = totalAllocatedSpace;
    }

    public long getMediaSizeInByte() {
        return mediaSizeInByte;
    }

    public void setMediaSizeInByte(long mediaSizeInByte) {
        this.mediaSizeInByte = mediaSizeInByte;
    }

    public String getOriginalMediaName() {
        return originalMediaName;
    }

    public void setOriginalMediaName(String originalMediaName) {
        this.originalMediaName = originalMediaName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getMediaSize() {
        return mediaSize;
    }

    public void setMediaSize(String mediaSize) {
        this.mediaSize = mediaSize;
    }

    public String getThumbnailPath() {
        return this.thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaPath() {
        return this.mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaName() {
        return this.mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
