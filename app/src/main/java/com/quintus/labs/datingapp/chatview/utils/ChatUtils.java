package com.quintus.labs.datingapp.chatview.utils;


import com.quintus.labs.datingapp.chatview.data.MessageBody;

import java.util.List;

public class ChatUtils {
    final public static  int  MESSAGE_TYPE_TEXT = 1;
    final public static int  MESSAGE_TYPE_IMAGE = 2;
    final public static int MESSAGE_TYPE_MULTIPLE_IMAGE = 3;
    final public static int MESSAGE_TYPE_VIDEO = 4;
    final public static int MESSAGE_TYPE_AUDIO = 5;


    public static MessageBody createMessageBody(int type, String text, List<String> urls,List<String> localUrls, String time){
        MessageBody body =  new MessageBody(text,urls,localUrls,time,type);
        return body;
    }

    public static MessageBody getMessageBody(String message){
        return JsonUtils.fromJson(message,MessageBody.class);
    }
    public static String getStringFromMessageBody(MessageBody body){
        return JsonUtils.toJson(body);
    }

}
