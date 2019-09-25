package com.quintus.labs.datingapp.rest.RequestModel;

public class AddAddressRequest {
    String house,locality,city,state,pincode,name,number;
    double latitude,longitude;


    public AddAddressRequest(String house, String locality, String city, String state, String pincode, double latitude, double longitude, String name, String number) {
        this.house = house;
        this.locality = locality;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.number = number;
    }
}
