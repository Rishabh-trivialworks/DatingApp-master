
package com.quintus.labs.datingapp.xmpp.room.models;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.quintus.labs.datingapp.xmpp.utils.MediaModel;

import java.io.Serializable;

@Entity(tableName = "group_chat_info")
public class GroupChatInfo implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "grp_id")
    @NonNull
    private int id;
    @ColumnInfo(name = "grp_name")
    private String name;
    @ColumnInfo(name = "grp_receive_private_msg")
    private boolean receivePrivateMsg;
    @ColumnInfo(name = "grp_profile_image")
    private MediaModel profileImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReceivePrivateMsg() {
        return receivePrivateMsg;
    }

    public void setReceivePrivateMsg(boolean receivePrivateMsg) {
        this.receivePrivateMsg = receivePrivateMsg;
    }

    public MediaModel getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MediaModel profileImage) {
        this.profileImage = profileImage;
    }
}
