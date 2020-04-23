package com.quintus.labs.datingapp.rest.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Receiver {
    @Expose
    @SerializedName("imageId")
    private int imageId;
    @Expose
    @SerializedName("updatedAt")
    private String updatedAt;
    @Expose
    @SerializedName("createdAt")
    private String createdAt;
    @Expose
    @SerializedName("userToken")
    private String userToken;
    @Expose
    @SerializedName("distance")
    private int distance;
    @Expose
    @SerializedName("coordinates")
    private Coordinates coordinates;
    @Expose
    @SerializedName("dob")
    private String dob;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("about")
    private String about;
    @Expose
    @SerializedName("maxRange")
    private int maxRange;
    @Expose
    @SerializedName("minRange")
    private int minRange;
    @Expose
    @SerializedName("interested")
    private String interested;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("password")
    private String password;
    @Expose
    @SerializedName("userType")
    private String userType;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("fullName")
    private String fullName;
    @Expose
    @SerializedName("id")
    private int id;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public int getMinRange() {
        return minRange;
    }

    public void setMinRange(int minRange) {
        this.minRange = minRange;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
