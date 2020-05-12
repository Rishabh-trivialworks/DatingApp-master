package com.quintus.labs.datingapp.rest.Response;

public class MatchedFriend {

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("sender")
    private UserData sender;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("receiver")
    private UserData receiver;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("updatedAt")
    private String updatedAt;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("createdAt")
    private String createdAt;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("receiverStatus")
    private String receiverStatus;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("senderStatus")
    private String senderStatus;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("receiverId")
    private int receiverId;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("senderId")
    private int senderId;
    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("id")
    private int id;

    public UserData getSender() {
        return sender;
    }

    public void setSender(UserData sender) {
        this.sender = sender;
    }

    public UserData getReceiver() {
        return receiver;
    }

    public void setReceiver(UserData receiver) {
        this.receiver = receiver;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReceiverStatus() {
        return receiverStatus;
    }

    public void setReceiverStatus(String receiverStatus) {
        this.receiverStatus = receiverStatus;
    }

    public String getSenderStatus() {
        return senderStatus;
    }

    public void setSenderStatus(String senderStatus) {
        this.senderStatus = senderStatus;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
