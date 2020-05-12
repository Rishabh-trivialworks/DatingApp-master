package com.quintus.labs.datingapp.rest.RequestModel;

public class PaymentResultRequest {
    String intentId;

    public PaymentResultRequest(String intentId) {
        this.intentId = intentId;
    }

    public String getIntentId() {
        return intentId;
    }

    public void setIntentId(String intentId) {
        this.intentId = intentId;
    }
}
