package com.quintus.labs.datingapp.xmpp.room.dao;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;

import java.util.List;

/**
 * Created by Varun John on 02,July,2019
 */

@Dao
public interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserInfo> dataList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserInfo data);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<UserInfo> dataList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserInfo data);

    @Query("SELECT * FROM user_info")
    List<UserInfo> getAll();

    @Query("SELECT * FROM user_info WHERE user_id = :userId")
    UserInfo get(int userId);

    @Query("DELETE FROM user_info")
    void deleteAll();

}
