package com.quintus.labs.datingapp.rest.model;

import java.util.List;

public  class PaymentResultModel {


    private String status;
    private List<String> payment_method_types;
    private Payment_method_options payment_method_options;
    private Metadata metadata;
    private boolean livemode;
    private String description;
    private String currency;
    private int created;
    private String confirmation_method;
    private String client_secret;
    private Charges charges;
    private String capture_method;
    private int amount_received;
    private int amount_capturable;
    private int amount;
    private String object;
    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPayment_method_types() {
        return payment_method_types;
    }

    public void setPayment_method_types(List<String> payment_method_types) {
        this.payment_method_types = payment_method_types;
    }

    public Payment_method_options getPayment_method_options() {
        return payment_method_options;
    }

    public void setPayment_method_options(Payment_method_options payment_method_options) {
        this.payment_method_options = payment_method_options;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getConfirmation_method() {
        return confirmation_method;
    }

    public void setConfirmation_method(String confirmation_method) {
        this.confirmation_method = confirmation_method;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public Charges getCharges() {
        return charges;
    }

    public void setCharges(Charges charges) {
        this.charges = charges;
    }

    public String getCapture_method() {
        return capture_method;
    }

    public void setCapture_method(String capture_method) {
        this.capture_method = capture_method;
    }

    public int getAmount_received() {
        return amount_received;
    }

    public void setAmount_received(int amount_received) {
        this.amount_received = amount_received;
    }

    public int getAmount_capturable() {
        return amount_capturable;
    }

    public void setAmount_capturable(int amount_capturable) {
        this.amount_capturable = amount_capturable;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Payment_method_options {
        private Card card;

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }
    }

    public static class Card {
        private String request_three_d_secure;

        public String getRequest_three_d_secure() {
            return request_three_d_secure;
        }

        public void setRequest_three_d_secure(String request_three_d_secure) {
            this.request_three_d_secure = request_three_d_secure;
        }
    }

    public static class Metadata {
    }

    public static class Charges {
        private String url;
        private int total_count;
        private boolean has_more;
        private List<String> data;
        private String object;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public boolean getHas_more() {
            return has_more;
        }

        public void setHas_more(boolean has_more) {
            this.has_more = has_more;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }
    }
}
