package com.quintus.labs.datingapp.rest.RequestModel;

public class LoginRequest {
    String email;
    String password;
    String userType;

    public LoginRequest(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
