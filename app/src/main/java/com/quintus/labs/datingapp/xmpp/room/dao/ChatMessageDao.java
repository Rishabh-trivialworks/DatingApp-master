package com.quintus.labs.datingapp.xmpp.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessageEmptyId;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;

import java.util.List;


@Dao
public interface ChatMessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ChatMessage chatMessage);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ChatMessage> chatMessages);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<ChatMessage> chatMessages);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ChatMessage chatMessages);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message ")
    List<ChatMessage> getAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE chat_message SET msg_time_stamp = :timeStamp WHERE msg_message_id = :messageId")
    long updateTimeStamp(String messageId, long timeStamp);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_status = " + AppConstants.Chat.STATUS_SEEN + " " +
            "AND msg_type = 'chat' " +
            "ORDER BY msg_time_stamp DESC LIMIT 1")
    MessageData getLastSeenMessageSingleChat(int conversationId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_time_stamp >= :timestamp " +
            "AND msg_type = 'chat' " +
            "ORDER BY msg_time_stamp ASC LIMIT 1")
    MessageData getLastSeenMessageSingleChatTimeStamp(int conversationId, long timestamp);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_status = " + AppConstants.Chat.STATUS_SEEN + " " +
            "AND msg_type != 'chat' " +
            "ORDER BY msg_time_stamp ")
    MessageData getLastSeenMessageGroupChat(int conversationId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type = 'chat' " +
            "ORDER BY msg_time_stamp ")
    List<MessageData> getMessagesSingle(int conversationId);

//    @Query("SELECT * FROM chat_message " +
//            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
//            "WHERE msg_body LIKE :keywords " +
//            "AND msg_type = 'chat' " +
//            "AND msg_subject = " + AppConstants.Chat.TYPE_CHAT_TEXT + " " +
//            "ORDER BY msg_time_stamp ")
//    List<MessageData> searchSingleMessages(String keywords);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "LEFT JOIN group_chat_info ON chat_message.msg_conversation_id = group_chat_info.grp_id " +
            "WHERE msg_body LIKE :keywords " +
            "AND msg_subject = '" + AppConstants.Chat.TYPE_CHAT_TEXT + "' " +
            "ORDER BY msg_time_stamp DESC")
    List<MessageData> searchGroupMessages(String keywords);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type = 'chat' " +
            "AND msg_time_stamp < :timestamp " +
            "ORDER BY msg_time_stamp DESC LIMIT :limit ")
    List<MessageData> getMessagesSingleWithLimit(int conversationId, long timestamp, int limit);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type != 'chat' " +
            "ORDER BY msg_time_stamp ")
    List<MessageData> getMessagesGroup(int conversationId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type != 'chat' " +
            "AND msg_time_stamp < :timestamp " +
            "ORDER BY msg_time_stamp DESC LIMIT :limit ")
    List<MessageData> getMessagesGroupWithLimit(int conversationId, long timestamp, int limit);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_message_id = :messageId " +
            "ORDER BY msg_time_stamp LIMIT 1")
    MessageData getMessage(String messageId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "WHERE msg_message_id = :messageId " +
            "ORDER BY msg_time_stamp LIMIT 1")
    ChatMessage getChatMessage(String messageId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_receipts NOT LIKE :searchId " +
            "AND msg_subject LIKE 'chat%' " +
            "ORDER BY msg_time_stamp")
    List<MessageData> getGroupMessagesWithPendingReadReceipts(int conversationId, String searchId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_time_stamp > :timestamp " +
            "AND msg_message_type = '" + AppConstants.Chat.MESSAGE_TYPE_RECEIVED + "' " +
            "AND msg_subject LIKE 'chat%' " +
            "ORDER BY msg_time_stamp")
    List<MessageData> getGroupMessagesWithPendingReadReceiptsTimestamp(int conversationId, long timestamp);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type = 'chat' " +
            "AND msg_subject LIKE 'chat%' " +
            "ORDER BY msg_time_stamp DESC LIMIT 1")
    MessageData getLastPendingReadReceiptsForSingleConversation(int conversationId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "LEFT JOIN user_info ON chat_message.msg_from_to_user_id = user_info.user_id " +
            "WHERE msg_status = " + AppConstants.Chat.STATUS_FAILED + " " +
            "OR msg_status = " + AppConstants.Chat.STATUS_PENDING + " " +
            "ORDER BY msg_time_stamp ")
    List<MessageData> getPendingMessages();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("DELETE FROM chat_message")
    void deleteAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("DELETE FROM chat_message " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type != 'chat' ")
    void deleteGroupMessages(int conversationId);

    @Query("DELETE FROM chat_message " +
            "WHERE msg_conversation_id = :conversationId " +
            "AND msg_type = 'chat'  ")
    void deleteSingleMessages(int conversationId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE chat_message SET msg_status = :status WHERE msg_conversation_id = :conservationId AND msg_time_stamp <= :timestamp")
    void setStatusTimestampBefore(int conservationId, long timestamp, int status);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE chat_message SET msg_status = :status ")
    void setStatusALL(int status);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE chat_message SET msg_status = :status WHERE msg_conversation_id = :conservationId AND msg_time_stamp > :timestamp")
    void setStatusTimestampAfter(int conservationId, long timestamp, int status);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("UPDATE chat_message SET msg_status = :status WHERE msg_message_id = :messageId")
    void setStatus(String messageId, int status);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT msg_message_id FROM chat_message " +
            "WHERE msg_conversation_id = :userId AND msg_message_type = '" + AppConstants.Chat.MESSAGE_TYPE_SENT + "' " +
            "LIMIT 1")
    ChatMessageEmptyId myInteractionWithUser(int userId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT msg_message_id FROM chat_message " +
            "WHERE msg_conversation_id = :userId AND msg_message_type = '" + AppConstants.Chat.MESSAGE_TYPE_RECEIVED + "' " +
            "LIMIT 1")
    ChatMessageEmptyId haveMessageFrom(int userId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_message " +
            "ORDER BY msg_time_stamp DESC LIMIT 1")
    ChatMessage getLastMessage();
}
