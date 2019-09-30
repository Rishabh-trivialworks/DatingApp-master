package com.quintus.labs.datingapp.rest.RequestModel;

public class RegisterRequest {
    String email,password,fullName,userType,gender,dob,interested;

    public RegisterRequest(String email, String password, String fullName,String userType,String gender,String dob,String interested) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userType = userType;
        this.gender = gender;
        this.dob = dob;
        this.interested = interested;

    }
}
