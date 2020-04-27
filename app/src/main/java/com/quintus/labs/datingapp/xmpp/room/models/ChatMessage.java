
package com.quintus.labs.datingapp.xmpp.room.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "chat_message")
public class ChatMessage implements Serializable, Comparable {

    @Override
    public int compareTo(@NonNull Object o) {
        long comp = ((ChatMessage) o).getTimestamp();
        /* For Ascending order*/
        return (int) (this.timestamp - comp);
    }

    public int getChatRoomType() {
        int type = AppConstants.Chat.TYPE_GROUP_CHAT;
        if (getType().equals(AppConstants.Chat.TYPE_CHAT)) {
            type = AppConstants.Chat.TYPE_SINGLE_CHAT;
        }
        return type;
    }

    public static class MessageReceipts implements Serializable {
        private int id;
        private long timestamp;


        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MessageReceipts) {
                MessageReceipts model = (MessageReceipts) obj;
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
            return 938132;
        }

        @Ignore
        public UserInfo userInfo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    @ColumnInfo(name = "msg_body")
    private String body;
    @ColumnInfo(name = "msg_conversation_id")
    private int conversationId;
    @ColumnInfo(name = "msg_from_to_user_id")
    private int fromToUserId;
    @ColumnInfo(name = "msg_is_tagged")
    private boolean isHashTagOrMentioned;

    @PrimaryKey
    @ColumnInfo(name = "msg_message_id")
    @NonNull
    private String messageId;

    @ColumnInfo(name = "msg_message_type")
    private String messageType;
    @ColumnInfo(name = "msg_receipts")
    private List<MessageReceipts> receipts;
    @ColumnInfo(name = "msg_subject")
    private String subject;
    @ColumnInfo(name = "msg_time_stamp")
    private long timestamp;
    @ColumnInfo(name = "msg_type")
    private String type;
    @ColumnInfo(name = "msg_user_id")
    private int userId;
    @ColumnInfo(name = "msg_status")
    private int status = 0;

    @Ignore
    public UserInfo subscriber;
    @Ignore
    public UserInfo operator;
    @Ignore
    private String date;

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ChatMessage) {
            ChatMessage chatMessageModel = (ChatMessage) obj;
            if (chatMessageModel.getMessageId().equals(messageId)) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.equals(obj);
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getFromToUserId() {
        return fromToUserId;
    }

    public void setFromToUserId(int fromToUserId) {
        this.fromToUserId = fromToUserId;
    }

    public boolean getIsHashTagOrMentioned() {
        return isHashTagOrMentioned;
    }

    public void setIsHashTagOrMentioned(boolean isHashTagOrMentioned) {
        this.isHashTagOrMentioned = isHashTagOrMentioned;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType == null ? "" : messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<MessageReceipts> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<MessageReceipts> receipts) {
        this.receipts = receipts;
    }

    public String getSubject() {
        return subject == null || subject.isEmpty() ? type : subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getTimestamp() {
        if (String.valueOf(timestamp).length() > 14) {
            return timestamp / 1000;
        }
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate(Context context) {
        if (date == null) {
            date = TimeUtils.getChatHeaderTimeFromMillis(getTimestamp(), context);
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
