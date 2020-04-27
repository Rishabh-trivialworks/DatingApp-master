package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.room.Embedded;
import androidx.room.Ignore;

import java.io.Serializable;

/**
 * Created by Varun John on 15,July,2019
 */
public class MessageData implements Serializable {

    @Embedded
    public ChatMessage chatMessage;
    @Embedded
    public UserInfo userInfo;
    @Embedded
    public GroupChatInfo groupChatInfo;

    public MessageData() {
    }

    @Ignore
    public MessageData(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof MessageData) {
            MessageData chatMessageModel = (MessageData) obj;
            if (chatMessageModel.chatMessage.getMessageId().equals(chatMessage.getMessageId())) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }

}
