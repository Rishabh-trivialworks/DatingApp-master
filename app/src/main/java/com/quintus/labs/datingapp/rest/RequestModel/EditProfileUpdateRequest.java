package com.quintus.labs.datingapp.rest.RequestModel;

public class EditProfileUpdateRequest {

    String name,email,mobile;

    public EditProfileUpdateRequest(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }
}
