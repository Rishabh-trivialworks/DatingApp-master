package com.quintus.labs.datingapp.xmpp.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.room.models.ChatConversation;
import com.quintus.labs.datingapp.xmpp.room.models.ConversationData;

import java.util.List;


/**
 * Created by Varun John on 02,July,2019
 */

@Dao
public interface ChatConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ChatConversation> chatConversations);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertIgnore(List<ChatConversation> chatConversations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChatConversation chatConversation);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ChatConversation chatConversation);

    @Query("SELECT * FROM chat_conversation WHERE chat_conversation_primary_id = :primaryId")
    ChatConversation get(int primaryId);

    @Query("SELECT * FROM chat_conversation")
    List<ChatConversation> getAllChatConversations();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM chat_conversation " +
            "LEFT JOIN user_info ON chat_conversation.chat_conversation_conversation_id = user_info.user_id " +
            "WHERE chat_conversation_conversation_id = :conservationId  " +
            "AND chat_conversation_conversation_type = " + AppConstants.Chat.TYPE_SINGLE_CHAT)
    ConversationData haveUser(int conservationId);

    @Query("SELECT * FROM chat_conversation " +
            "WHERE chat_conversation_conversation_id = :conservationId  " +
            "AND chat_conversation_conversation_type = :conversationType " +
            "AND chat_conversation_last_message_id != '' " +
            "AND chat_conversation_last_message_id IS NOT NULL ")
    ChatConversation getChatConversation(int conservationId, int conversationType);

    @Query("SELECT * FROM chat_conversation " +
            "INNER JOIN chat_message ON chat_conversation.chat_conversation_last_message_id = chat_message.msg_message_id " +
            "LEFT JOIN user_info ON msg_from_to_user_id = user_info.user_id " +
            "LEFT JOIN group_chat_info ON chat_conversation.chat_conversation_conversation_id = group_chat_info.grp_id " +
            "WHERE chat_conversation_last_message_id IS NOT NULL " +
            "AND chat_conversation_last_message_id != '' " +
            "ORDER BY chat_message.msg_time_stamp DESC")
    List<ConversationData> getChatConversationDialogs();

    @Query("SELECT * FROM chat_conversation " +
            "INNER JOIN chat_message ON chat_conversation.chat_conversation_last_message_id = chat_message.msg_message_id " +
            "LEFT JOIN user_info ON msg_from_to_user_id = user_info.user_id " +
            "LEFT JOIN group_chat_info ON chat_conversation.chat_conversation_conversation_id = group_chat_info.grp_id " +
            "WHERE user_name LIKE :keywords " +
            "OR grp_name LIKE :keywords " +
            "AND chat_conversation_last_message_id IS NOT NULL " +
            "AND chat_conversation_last_message_id != '' " +
            "ORDER BY chat_message.msg_time_stamp DESC")
    List<ConversationData> getChatConversationDialogsSearch(String keywords);

    @Query("SELECT * FROM chat_conversation " +
            "INNER JOIN chat_message ON chat_conversation.chat_conversation_last_message_id = chat_message.msg_message_id " +
            "INNER JOIN user_info ON msg_from_to_user_id = user_info.user_id " +
            "INNER JOIN group_chat_info ON chat_conversation.chat_conversation_conversation_id = group_chat_info.grp_id " +
            "WHERE msg_body LIKE :keywords " +
            "AND msg_subject = '" + AppConstants.Chat.TYPE_CHAT_TEXT + "' " +
            "ORDER BY chat_message.msg_time_stamp DESC")
    List<ConversationData> getChatConversationDialogsMessageSearch(String keywords);

    @Query("SELECT * FROM chat_conversation " +
            "INNER JOIN chat_message ON chat_conversation.chat_conversation_last_message_id = chat_message.msg_message_id " +
            "LEFT JOIN user_info ON msg_from_to_user_id = user_info.user_id " +
            "LEFT JOIN group_chat_info ON chat_conversation.chat_conversation_conversation_id = group_chat_info.grp_id " +
            "WHERE chat_conversation_conversation_id = :conversationId " +
            "AND chat_conversation_conversation_type = :conversationType " +
            "AND chat_conversation_last_message_id IS NOT NULL " +
            "AND chat_conversation_last_message_id != '' " +
            "ORDER BY chat_message.msg_time_stamp DESC Limit 1")
    ConversationData getChatConversationDialog(int conversationId, int conversationType);

    @Query("DELETE FROM chat_conversation")
    void deleteAll();

    @Query("DELETE FROM chat_conversation " +
            "WHERE chat_conversation_id = :conversationId " +
            "AND chat_conversation_conversation_type = :conversationType ")
    void deleteConversation(int conversationId, int conversationType);

    @Query("UPDATE chat_conversation SET chat_conversation_unread_count = chat_conversation_unread_count +1 WHERE chat_conversation_id = :conservationId AND chat_conversation_conversation_type = :conversationType ")
    void increaseUnreadCount(int conservationId, int conversationType);

    @Query("UPDATE chat_conversation SET chat_conversation_unread_count = :count WHERE chat_conversation_id = :conservationId AND chat_conversation_conversation_type = :conversationType ")
    void setUnreadCount(int count, int conversationType, int conservationId);

    @Query("UPDATE chat_conversation SET chat_conversation_unread_count = chat_conversation_unread_count +1, chat_conversation_last_message_id = :messageId WHERE chat_conversation_id = :conservationId AND chat_conversation_conversation_type = :conversationType ")
    void increaseUnreadCount(int conservationId, int conversationType, String messageId);

    @Query("UPDATE chat_conversation SET chat_conversation_unread_count = :count, chat_conversation_last_message_id = :messageId WHERE chat_conversation_id = :conservationId AND chat_conversation_conversation_type = :conversationType ")
    void setUnreadCount(int conservationId, int count, int conversationType, String messageId);

    @Query("UPDATE chat_conversation SET chat_conversation_group_role = :role WHERE chat_conversation_id = :conservationId AND chat_conversation_conversation_type = " + AppConstants.Chat.TYPE_GROUP_CHAT)
    void setGroupRole(int conservationId, String role);

    @Query("UPDATE chat_conversation " +
            "SET chat_conversation_last_message_id = :messageId " +
            "WHERE chat_conversation_id = :conservationId " +
            "AND chat_conversation_conversation_type = :conversationType ")
    long setLastMessageId(int conservationId, String messageId, int conversationType);

//    @Query("SELECT * FROM chat_conversation " +
//            "WHERE chat_conversation_unread_count != 0 " +
//            "AND chat_conversation_last_message_id IS NOT NULL " +
//            "AND chat_conversation_last_message_id != '' ")
//    List<ChatConversation> getMessageNotificationCount();

    @Query("SELECT * FROM chat_conversation " +
            "INNER JOIN chat_message ON chat_conversation.chat_conversation_last_message_id = chat_message.msg_message_id " +
            "LEFT JOIN user_info ON msg_from_to_user_id = user_info.user_id " +
            "LEFT JOIN group_chat_info ON chat_conversation.chat_conversation_conversation_id = group_chat_info.grp_id " +
            "WHERE chat_conversation_unread_count != 0 " +
            "AND chat_conversation_last_message_id IS NOT NULL " +
            "AND chat_conversation_last_message_id != '' " )
    List<ConversationData> getMessageNotificationCount();
}
