package com.quintus.labs.datingapp.rest.model;

public class PaymentIntentModel {
    private String client_secret;

    public PaymentIntentModel(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
