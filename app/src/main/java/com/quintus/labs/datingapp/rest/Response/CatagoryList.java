
package com.quintus.labs.datingapp.rest.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatagoryList {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("hourPrice")
    @Expose
    private Object hourPrice;
    @SerializedName("squareFeetPrice")
    @Expose
    private Object squareFeetPrice;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("serviceCategoryHourId")
    @Expose
    private Object serviceCategoryHourId;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Object hourPrice) {
        this.hourPrice = hourPrice;
    }

    public Object getSquareFeetPrice() {
        return squareFeetPrice;
    }

    public void setSquareFeetPrice(Object squareFeetPrice) {
        this.squareFeetPrice = squareFeetPrice;
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

    public Object getServiceCategoryHourId() {
        return serviceCategoryHourId;
    }

    public void setServiceCategoryHourId(Object serviceCategoryHourId) {
        this.serviceCategoryHourId = serviceCategoryHourId;
    }

}
