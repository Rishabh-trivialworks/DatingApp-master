package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.quintus.labs.datingapp.xmpp.room.TypeConverterMy;
import com.quintus.labs.datingapp.xmpp.room.dao.ChatConversationDao;
import com.quintus.labs.datingapp.xmpp.room.dao.ChatMessageDao;
import com.quintus.labs.datingapp.xmpp.room.dao.GroupChatInfoDao;
import com.quintus.labs.datingapp.xmpp.room.dao.UserInfoDao;


/**
 * Created by Varun John on 02,July,2019
 */
@Database(entities = {ChatConversation.class, ChatMessage.class, UserInfo.class, GroupChatInfo.class}, version = 9, exportSchema = false)
@TypeConverters({TypeConverterMy.class})
public abstract class ChatDataBase extends RoomDatabase {

    public abstract ChatConversationDao chatConversationDao();

    public abstract ChatMessageDao chatMessageDao();

    public abstract UserInfoDao userInfoDao();

    public abstract GroupChatInfoDao groupChatInfoDao();

}
