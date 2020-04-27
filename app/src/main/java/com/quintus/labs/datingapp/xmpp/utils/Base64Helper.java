package com.quintus.labs.datingapp.xmpp.utils;


import com.quintus.labs.datingapp.Utils.LogUtils;

import org.jivesoftware.smack.util.stringencoder.Base64;

public class Base64Helper {

    public static String encode(String text) {

        try {
//            text = Base64.encodeToString(text.getBytes("UTF-8"), Base64.DEFAULT);
            text = Base64.encode(text);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug("Base64Helper encode : " + e.getMessage());
        }

        return text;
    }

    public static String decode(String text) {

        if (text == null) {
            return null;
        }

        try {
//           text = new String(Base64.decode(text, Base64.DEFAULT), "UTF-8");
            text = Base64.decodeToString(text);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug("Base64Helper decode : " + e.getMessage());
        }

        return text;
    }
}
