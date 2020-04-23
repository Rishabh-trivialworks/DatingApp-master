package com.quintus.labs.datingapp.rest.RequestModel;

public  class AcceptRejectModel {


    private int friendId;
    private String requestStatus;

    public AcceptRejectModel(int friendId, String requestStatus) {
        this.friendId = friendId;
        this.requestStatus = requestStatus;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
