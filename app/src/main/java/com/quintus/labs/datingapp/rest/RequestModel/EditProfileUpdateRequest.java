package com.quintus.labs.datingapp.rest.RequestModel;

import okhttp3.MultipartBody;

public class EditProfileUpdateRequest {

    String email,fullName,userType,gender,dob,interested;
    int minRange,maxRange,distance;

    MultipartBody.Part image;

    public EditProfileUpdateRequest(String email, String fullName, String userType, String gender, String dob, String interested, int minRange, int maxRange, int distance, MultipartBody.Part image) {
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.interested = interested;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.distance = distance;
        this.image = image;
    }

    public EditProfileUpdateRequest(String email, String fullName,
                                    String userType, String gender, String dob, String interested, int minRange, int maxRange, int distance) {
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.interested = interested;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.distance = distance;

    }


}
