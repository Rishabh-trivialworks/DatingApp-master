package com.quintus.labs.datingapp.rest.RequestModel;

import java.util.ArrayList;

public class RegisterRequest {
    String email,password,fullName,userType,gender,dob,interested;
    double longitude,latitude;
    ArrayList<String> interests;

    public RegisterRequest(String email, String password, String fullName,String userType,String gender,String dob,String interested,double longitude,double latitude,ArrayList<String> interests) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.interested = interested;
        this.latitude =latitude;
        this.longitude =longitude;
        this.interests = interests;


    }
}
