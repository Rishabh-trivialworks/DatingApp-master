package com.quintus.labs.datingapp.xmpp.utils;


import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;

import java.util.List;

public class ChatNotification implements java.io.Serializable {

    private static final long serialVersionUID = -283621658340385394L;

    private String body;
    private String type;
    private String title;

    private boolean isPremiumUser;
    private boolean receivePrivateMsg;
    private boolean hideReadReceipt;
    private int objectDataId;
    private String profileImageURL;
    private Object url;
    private String messageId;
    private List<UserDeviceInfoModel> deviceInfo;
    private String username;
    private int userTypeId;

    private ChatMessage chatMessage;
    private UserInfo whoFiredUserBasicDetail;

    private String onWhoseSide;

    public String getOnWhoseSide() {
        return onWhoseSide;
    }

    public void setOnWhoseSide(String onWhoseSide) {
        this.onWhoseSide = onWhoseSide;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public UserInfo getWhoFiredUserBasicDetail() {
        return whoFiredUserBasicDetail;
    }

    public void setWhoFiredUserBasicDetail(UserInfo whoFiredUserBasicDetail) {
        this.whoFiredUserBasicDetail = whoFiredUserBasicDetail;
    }

    public List<UserDeviceInfoModel> getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(List<UserDeviceInfoModel> deviceInfo) {
        this.deviceInfo = deviceInfo;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getObjectDataId() {
        return this.objectDataId;
    }

    public void setObjectDataId(int objectDataId) {
        this.objectDataId = objectDataId;
    }

    public String getProfileImageURL() {
        return this.profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Object getUrl() {
        return this.url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
