
package com.quintus.labs.datingapp.xmpp.room.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.quintus.labs.datingapp.xmpp.room.models.GroupChatInfo;

import java.util.List;

@Dao
public interface GroupChatInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<GroupChatInfo> dataList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GroupChatInfo data);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<GroupChatInfo> dataList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(GroupChatInfo data);

    @Query("SELECT * FROM group_chat_info")
    List<GroupChatInfo> getAll();

    @Query("SELECT * FROM group_chat_info WHERE grp_id = :conversationId")
    GroupChatInfo get(int conversationId);

    @Query("DELETE FROM group_chat_info")
    void deleteAll();
}
