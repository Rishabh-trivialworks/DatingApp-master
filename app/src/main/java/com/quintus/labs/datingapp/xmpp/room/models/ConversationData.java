package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.room.Embedded;

import java.io.Serializable;

/**
 * Created by Varun John on 15,July,2019
 */
public class ConversationData implements Serializable {

    @Embedded
    public ChatMessage chatMessage;
    @Embedded
    public GroupChatInfo groupChatInfo;
    @Embedded
    public UserInfo userInfo;
    @Embedded
    public ChatConversation chatConversation;

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ConversationData) {
            ConversationData chatMessageModel = (ConversationData) obj;
            if (chatMessageModel.chatConversation.getConversationId() == chatConversation.getConversationId() &&
                    chatMessageModel.chatConversation.getConversationType() == chatConversation.getConversationType()) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }
}
