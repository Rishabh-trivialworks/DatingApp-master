package com.quintus.labs.datingapp.rest.Response;

import java.io.Serializable;

public class SuperLikeModel implements Serializable {

    int id;
    String createdAt;
    String updatedAt;
    int userId;
    int likedById;
    UserData likedBy;

    public SuperLikeModel(int id, String createdAt, String updatedAt, int userId, int likedById, UserData likedBy) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.likedById = likedById;
        this.likedBy = likedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLikedById() {
        return likedById;
    }

    public void setLikedById(int likedById) {
        this.likedById = likedById;
    }

    public UserData getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(UserData likedBy) {
        this.likedBy = likedBy;
    }
}
