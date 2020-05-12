
package com.quintus.labs.datingapp.rest.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public  class CardList implements Serializable {
    @SerializedName("media")
    @Expose
    private List<ImageModel> media;

    @SerializedName("interests")
    @Expose
    private List<Interest> interests;
    @SerializedName("howFar")
    @Expose
    private double howFar;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("zodiac")
    @Expose
    private String zodiac;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("politicalLeanings")
    @Expose
    private String politicalLeanings;
    @SerializedName("kids")
    @Expose
    private String kids;
    @SerializedName("pets")
    @Expose
    private String pets;
    @SerializedName("lookingFor")
    @Expose
    private String lookingFor;
    @SerializedName("smoking")
    @Expose
    private String smoking;
    @SerializedName("drinking")
    @Expose
    private String drinking;
    @SerializedName("education")
    @Expose
    private String education;
    @SerializedName("exercise")
    @Expose
    private String exercise;
    @SerializedName("height")
    @Expose
    private int height;
    @SerializedName("userToken")
    @Expose
    private String userToken;
    @SerializedName("distance")
    @Expose
    private int distance;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("maxRange")
    @Expose
    private int maxRange;
    @SerializedName("minRange")
    @Expose
    private int minRange;
    @SerializedName("interested")
    @Expose
    private String interested;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("id")
    @Expose
    private int id;


    public List<ImageModel> getMedia() {
        return media;
    }

    public void setMedia(List<ImageModel> media) {
        this.media = media;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public double getHowFar() {
        return howFar;
    }

    public void setHowFar(double howFar) {
        this.howFar = howFar;
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

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getPoliticalLeanings() {
        return politicalLeanings;
    }

    public void setPoliticalLeanings(String politicalLeanings) {
        this.politicalLeanings = politicalLeanings;
    }

    public String getKids() {
        return kids;
    }

    public void setKids(String kids) {
        this.kids = kids;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
