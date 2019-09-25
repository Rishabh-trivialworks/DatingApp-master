package com.quintus.labs.datingapp.rest.RequestModel;

public class SaveBookingModel {

    int serviceCategoryId,addressId;

    public SaveBookingModel(int serviceCategoryId, int addressId) {
        this.serviceCategoryId = serviceCategoryId;
        this.addressId = addressId;
    }
}
