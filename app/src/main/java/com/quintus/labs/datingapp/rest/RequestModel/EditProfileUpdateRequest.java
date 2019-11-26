package com.quintus.labs.datingapp.rest.RequestModel;

import okhttp3.MultipartBody;

public class EditProfileUpdateRequest {

    String email,fullName,userType,gender,dob,interested,exercise,education,drinking,smoking,lookingFor,pets,kids,politicalLeanings,religion,zodiac;
    int minRange,maxRange,distance;
    double height;

    MultipartBody.Part image;

    public EditProfileUpdateRequest(String email, String fullName, String userType, String gender, String dob, String interested, int minRange, int maxRange, int distance) {
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
                                    String userType, String gender, String dob, String interested, int minRange, int maxRange, int distance,
    String exercise,String education,String drinking,String smoking,String lookingFor,String pets,String kids,String politicalLeanings,String religion,String zodiac,double height
    ) {
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.interested = interested;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.distance = distance;

        this.exercise = exercise;
        this.education = education;
        this.drinking = drinking;
        this.smoking = smoking;
        this.lookingFor = lookingFor;
        this.pets = pets;
        this.kids = kids;
        this.politicalLeanings = politicalLeanings;
        this.religion = religion;
        this.zodiac = zodiac;
        this.height = height;

    }


}
