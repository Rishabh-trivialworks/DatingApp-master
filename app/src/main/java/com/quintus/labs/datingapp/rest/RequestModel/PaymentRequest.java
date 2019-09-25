package com.quintus.labs.datingapp.rest.RequestModel;

public class PaymentRequest {

    String payment_id, signature;
    boolean status;


    public PaymentRequest(String payment_id, String signature, boolean status) {
        this.payment_id = payment_id;
        this.signature = signature;
        this.status = status;
    }

    public PaymentRequest(String signature, boolean status) {
        this.signature = signature;
        this.status = status;
    }
}
