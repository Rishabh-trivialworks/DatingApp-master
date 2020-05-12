
package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.quintus.labs.datingapp.Utils.AppConstants;

import java.io.Serializable;

@Entity(tableName = "chat_conversation")
public class ChatConversation implements Serializable {

    @ColumnInfo(name = "chat_conversation_conversation_id")
    private int conversationId;
    @ColumnInfo(name = "chat_conversation_conversation_type")
    private int conversationType;
    @ColumnInfo(name = "chat_conversation_group_role")
    private String groupRole = AppConstants.Chat.SERVER_ROLE_NONE;

    @ColumnInfo(name = "chat_conversation_id")
    private String id;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "chat_conversation_primary_id")
    private int primaryId;

    @ColumnInfo(name = "chat_conversation_last_message_id")
    private String lastMessageId;
    @ColumnInfo(name = "chat_conversation_last_read_timestamp")
    private long lastReadTimestamp;
    @ColumnInfo(name = "chat_conversation_user_id")
    private int userId;
    @ColumnInfo(name = "chat_conversation_unread_count")
    private int unreadCount;


    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public String getGroupRole() {
        return groupRole == null || groupRole.isEmpty() ? AppConstants.Chat.SERVER_ROLE_NONE : groupRole.toLowerCase();
    }

    public void setGroupRole(String groupRole) {
        this.groupRole = groupRole;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public long getLastReadTimestamp() {
        if (String.valueOf(lastReadTimestamp).length() > 14) {
            return lastReadTimestamp / 1000;
        }
        return lastReadTimestamp;
    }

    public void setLastReadTimestamp(Long lastReadTimestamp) {
        this.lastReadTimestamp = lastReadTimestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
