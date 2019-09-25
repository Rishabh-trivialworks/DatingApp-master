package com.quintus.labs.datingapp.rest.RequestModel;

public class PaymentRSARequest {

    String payment_id, signature,order_id;
    boolean status;


    public PaymentRSARequest(String payment_id, String signature, String order_id) {
        this.payment_id = payment_id;
        this.signature = signature;
        this.order_id = order_id;
    }

    public PaymentRSARequest(String signature, String order_id) {
        this.signature = signature;
        this.order_id = order_id;
    }
}
