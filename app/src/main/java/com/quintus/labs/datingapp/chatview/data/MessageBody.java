package com.quintus.labs.datingapp.chatview.data;


import java.util.List;

public class MessageBody {
    String message;
    List<String> mediaUrl;
    List<String> localUrls;
    String messageTime;
    int messageType;


    public MessageBody(String message, List<String> mediaUrl,List<String> localUrls, String messageTime, int messageType) {
        this.message = message;
        this.mediaUrl = mediaUrl;
        this.messageTime = messageTime;
        this.messageType = messageType;
        this.localUrls = localUrls;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(List<String> mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public List<String> getLocalUrls() {
        return localUrls;
    }

    public void setLocalUrls(List<String> localUrls) {
        this.localUrls = localUrls;
    }
}
