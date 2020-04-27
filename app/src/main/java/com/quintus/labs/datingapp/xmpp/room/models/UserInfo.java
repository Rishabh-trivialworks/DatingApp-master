
package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.UserDeviceInfoModel;
import com.quintus.labs.datingapp.xmpp.utils.UserModel;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "user_info")
public class UserInfo implements Serializable {

    @ColumnInfo(name = "user_email")
    private String email;
    @ColumnInfo(name = "user_id")
    @PrimaryKey
    @NonNull
    private int id;
    @ColumnInfo(name = "user_user_type_id")
    private int userTypeId;
    @ColumnInfo(name = "user_hide_read_receipt")
    private boolean hideReadReceipt;
    @ColumnInfo(name = "user_is_blocked")
    private boolean isBlocked;
    @ColumnInfo(name = "user_receive_private_msg")
    private boolean receivePrivateMsg;
    @ColumnInfo(name = "on_whose_side")
    private String onWhoseSide;
    @ColumnInfo(name = "user_is_premium_user")
    private boolean isPremiumUser;
    @ColumnInfo(name = "user_name")
    private String name;
    @ColumnInfo(name = "user_surname")
    private String surName;
    @ColumnInfo(name = "user_profile_image")
    private String profileImage;
    @ColumnInfo(name = "user_profile_image_thumbnail")
    private String profileImageThumbnail;
    @ColumnInfo(name = "user_username")
    private String username;
    @ColumnInfo(name = "user_device_info")
    private List<UserDeviceInfoModel> deviceInfo;

    @Ignore
    private String relation;
    @Ignore
    private String colorIndicator;

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof UserInfo) {
            UserInfo model = (UserInfo) obj;
            if (model.getId() == getId()) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof UserModel) {
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

    public UserInfo setUserModel(UserModel userModel) {

        setId(userModel.getId());
        setEmail(userModel.getEmail());
        setUserTypeId(userModel.getUserTypeId());
        setBlocked(userModel.isBlocked());
        setHideReadReceipt(userModel.isHideReadReceipt());
        setReceivePrivateMsg(userModel.receivePrivateMsg);
        setOnWhoseSide(userModel.getOnWhoseSide());
        setPremiumUser(userModel.isPremiumUser());
        setName(userModel.getName());
        setSurName(userModel.getSurName());
        setProfileImage(userModel.getProfileImage());
        setProfileImageThumbnail(userModel.getProfileImageThumbnail());
        setUsername(userModel.getUsername());
        setDeviceInfo(userModel.getDeviceInfo());

        return this;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFullName() {
        String fullname = name;
        return fullname;

    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public boolean isHideReadReceipt() {
        return hideReadReceipt;
    }

    public void setHideReadReceipt(boolean hideReadReceipt) {
        this.hideReadReceipt = hideReadReceipt;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isReceivePrivateMsg() {
        return receivePrivateMsg;
    }

    public void setReceivePrivateMsg(boolean receivePrivateMsg) {
        this.receivePrivateMsg = receivePrivateMsg;
    }

    public String getOnWhoseSide() {
        return onWhoseSide;
    }

    public void setOnWhoseSide(String onWhoseSide) {
        this.onWhoseSide = onWhoseSide;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }

    public String getName() {
        return name;
    }

    public String getOnlyName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageThumbnail() {
        return profileImageThumbnail == null ? "" : profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserDeviceInfoModel> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(List<UserDeviceInfoModel> deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getColorIndicator() {
        return AppConstants.userColorIndicator[id % 10];
    }

    public void setColorIndicator(String colorIndicator) {
        this.colorIndicator = colorIndicator;
    }
}
