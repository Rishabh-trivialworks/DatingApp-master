package com.quintus.labs.datingapp.xmpp.utils;

/**
 * Created by MyU10 on 4/18/2017.
 */

public class ChatHeaderModel {

    public String text;
    public Type type;

    public enum Type {
        DATE,
        PARTICIPATE_ADD,
        PARTICIPATE_DELETED
    }

    public ChatHeaderModel(Type type) {
        this.type = type;
    }

    public ChatHeaderModel(Type type, String text) {
        this.text = text;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ChatHeaderModel) {
            if (((ChatHeaderModel) obj).text.equals(text)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 39423;
    }
}
