package com.quintus.labs.datingapp.rest.RequestModel;

public class RegisterRequest {
    String email,password,userName;

    public RegisterRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.userName = name;
    }
}
