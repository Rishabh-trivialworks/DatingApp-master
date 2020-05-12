package com.quintus.labs.datingapp.BoostPaidPlans;


import java.io.Serializable;

public class PlanModel implements Serializable {
    private int id;
    private String name;
    private String price;
    private String planTag;
    private long expiry;
    private String createdAt;
    private String updatedAt;

    public PlanModel(int id,String name, String price, String planTag) {
        this.name = name;
        this.price = price;
        this.planTag = planTag;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlanTag() {
        return planTag;
    }

    public void setPlanTag(String planTag) {
        this.planTag = planTag;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
