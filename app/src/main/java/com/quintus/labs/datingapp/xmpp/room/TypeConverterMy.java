package com.quintus.labs.datingapp.xmpp.room;



import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.utils.MediaModel;
import com.quintus.labs.datingapp.xmpp.utils.UserDeviceInfoModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by Varun John on 03,July,2019
 */
public class TypeConverterMy {

    @TypeConverter
    public static MediaModel storedStringToMediaModel(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, MediaModel.class);
    }

    @TypeConverter
    public static String mediaModelToStoredString(MediaModel myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }

    @TypeConverter
    public static List<ChatMessage.MessageReceipts> storedStringToReceipts(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<ChatMessage.MessageReceipts>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String receiptsToStoredString(List<ChatMessage.MessageReceipts> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }

    @TypeConverter
    public static List<UserDeviceInfoModel> storedStringToDeviceInfo(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<UserDeviceInfoModel>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String deviceInfoToStoredString(List<UserDeviceInfoModel> myObjects) {
        Gson gson = new Gson();
        return gson.toJson(myObjects);
    }
}
