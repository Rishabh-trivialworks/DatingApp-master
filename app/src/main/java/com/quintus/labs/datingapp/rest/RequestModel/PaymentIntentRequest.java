package com.quintus.labs.datingapp.rest.RequestModel;

public class PaymentIntentRequest {
    private int packageId;

    public PaymentIntentRequest(int packageId) {
        this.packageId = packageId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }
}
