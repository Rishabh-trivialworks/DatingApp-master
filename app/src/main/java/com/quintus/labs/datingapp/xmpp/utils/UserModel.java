package com.quintus.labs.datingapp.xmpp.utils;



import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private String profileImageThumbnail;
    private String name;
    private int id;
    private String surName;
    private String profileImage;
    private String email;
    private boolean isPremiumUser;
    private String username;
    private String relation;
    private int userTypeId;
    private String studentId;
    private List<UserDeviceInfoModel> deviceInfo;

    public int isfollow;
    public boolean receivePrivateMsg;

    public boolean isHittingFollowingApi;

    public boolean isProgressItem;
    public boolean isProgressItemListEnd;

    private boolean isBlocked;
    private boolean isSelected;
    private boolean hideReadReceipt;
    private String colorIndicator;

    private String onWhoseSide;

    public String getOnWhoseSide() {
        return onWhoseSide;
    }

    public void setOnWhoseSide(String onWhoseSide) {
        this.onWhoseSide = onWhoseSide;
    }

    public String getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(String colorIndicator) {
        this.colorIndicator = colorIndicator;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<UserDeviceInfoModel> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(List<UserDeviceInfoModel> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }


    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(int isfollow) {
        this.isfollow = isfollow;
    }

    public UserModel(boolean isProgressItem, boolean isProgressItemListEnd) {
        this.isProgressItem = isProgressItem;
        this.isProgressItemListEnd = isProgressItemListEnd;
    }

    public boolean isReceivePrivateMsg() {
        return receivePrivateMsg;
    }

    public void setReceivePrivateMsg(boolean receivePrivateMsg) {
        this.receivePrivateMsg = receivePrivateMsg;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public UserModel(int id) {
        this.id = id;
    }

    public UserModel(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.email = userInfo.getEmail();
        this.userTypeId = userInfo.getUserTypeId();
        this.isBlocked = userInfo.isBlocked();
        this.hideReadReceipt = userInfo.isHideReadReceipt();
        this.receivePrivateMsg = userInfo.isReceivePrivateMsg();
        this.onWhoseSide = userInfo.getOnWhoseSide();
        this.isPremiumUser = userInfo.isPremiumUser();
        this.name = userInfo.getName();
        this.profileImage = userInfo.getProfileImage();
        this.profileImageThumbnail = userInfo.getProfileImageThumbnail();
        this.username = userInfo.getUsername();
        this.deviceInfo = userInfo.getDeviceInfo();
        this.surName = userInfo.getSurName();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof UserModel) {
            UserModel model = (UserModel) obj;
            if (model.getId() == getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return 248;
    }

    public UserModel() {
    }

    public String getProfileImageThumbnail() {
        return this.profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return this.relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFullName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isHideReadReceipt() {
        return hideReadReceipt;
    }

    public void setHideReadReceipt(boolean hideReadReceipt) {
        this.hideReadReceipt = hideReadReceipt;
    }
}