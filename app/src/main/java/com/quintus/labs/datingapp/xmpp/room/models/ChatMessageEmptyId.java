
package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.room.ColumnInfo;

import java.io.Serializable;

public class ChatMessageEmptyId implements Serializable {

    @ColumnInfo(name = "msg_message_id")
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isEmpty() {
        return messageId == null ? true : false;
    }
}
