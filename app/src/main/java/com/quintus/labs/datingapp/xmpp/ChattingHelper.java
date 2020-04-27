package com.quintus.labs.datingapp.xmpp;

import android.content.Context;

import com.google.gson.Gson;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.Base64Helper;
import com.quintus.labs.datingapp.xmpp.utils.ChatMedia;
import com.quintus.labs.datingapp.xmpp.utils.TimeUtils;


import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class ChattingHelper {

    private Context context;

    private int userId;
    private int groupId;

    private String myJid;
    private int chatType;

    private EntityBareJid userJid;
    private EntityBareJid groupJid;

    private ChattingListener listener;
    private Gson gson;

    public static Map<Integer, String> userTextMap = new HashMap<>();

    public static void storeUserMessage(int userId, String message) {
        if (message.isEmpty()) {
            userTextMap.remove(userId);
        } else {
            userTextMap.put(userId, message + " ");
        }
    }

    public static String getStoredMessage(int userId) {
        return userTextMap.get(userId);
    }

    public interface ChattingListener {
        void newMessage(MessageData message);

        void messageDeliveryReceipt(ChatMessage message);

        void messageSentSuccess(ChatMessage message);

        void messageSentFailure(ChatMessage message);

        void typingStatus(boolean isTyping);

        void chattingSetupError();
    }

    public ChattingHelper(Context context, int id, int chatType) {
        this.chatType = chatType;
        this.context = context;

        gson = new Gson();

        try {
            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                this.userId = id;
                userJid = JidCreate.entityBareFrom(AppConstants.getJID(userId + ""));

            } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                this.groupId = id;
                groupJid = (EntityBareJid) JidCreate.bareFrom(groupId + "@conference." + AppConstants.XMPP.SERVICE_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        myJid = AppConstants.getJID(TempStorage.getUser().getId() + "");
    }

    public void setup(final ChattingListener listener) {
        this.listener = listener;
        try {
            TempStorage.getXMPPHelper().addToRoster(AppConstants.getJID(userId + ""));

            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT)
                EventBroadcastHelper.sendUserOnlineStatus(userId, TempStorage.getXMPPHelper().isUserOnline(userJid));

        } catch (Exception e) {
            e.printStackTrace();
            listener.chattingSetupError();
        }
    }

    public static ChatMessage getChatMessageModel(Context context, Message message) {

        ChatMessage chatMessageModel = new ChatMessage();
        chatMessageModel.setMessageId(message.getStanzaId());

        chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT);

        int to = 0;
        int from = 0;
        int conversationId = 0;

        String messageFromJid = message.getFrom().toString();

        if (messageFromJid.contains("conference")) {
            try {
                chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT_GROUP);
                conversationId = Integer.parseInt(messageFromJid.split("@")[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                from = Integer.parseInt(messageFromJid.substring(messageFromJid.lastIndexOf("/") + 1, messageFromJid.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                from = Integer.parseInt(messageFromJid.split("@")[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String messageToJid = message.getTo().toString();

        if (messageToJid.contains("conference")) {
            try {
                chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT_GROUP);
                conversationId = Integer.parseInt(messageToJid.split("@")[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                to = Integer.parseInt(messageToJid.substring(messageToJid.lastIndexOf("/") + 1, messageToJid.length()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                to = Integer.parseInt(messageToJid.split("@")[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (from == TempStorage.getUser().getId()) {
            chatMessageModel.setMessageType(AppConstants.Chat.MESSAGE_TYPE_SENT);
            chatMessageModel.setFromToUserId(to);

            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
                conversationId = to;
                chatMessageModel.setConversationId(conversationId);
            } else {
                chatMessageModel.setConversationId(conversationId);
            }

        } else {
            chatMessageModel.setMessageType(AppConstants.Chat.MESSAGE_TYPE_RECEIVED);
            chatMessageModel.setFromToUserId(from);

            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
                conversationId = from;
                chatMessageModel.setConversationId(conversationId);
            } else {
                chatMessageModel.setConversationId(conversationId);
            }
        }

        DelayInformation delayInformation = message.getExtension(DelayInformation.ELEMENT, DelayInformation.NAMESPACE);

        if (delayInformation != null) {
            chatMessageModel.setTimestamp(delayInformation.getStamp().getTime());
        } else {
            chatMessageModel.setTimestamp(System.currentTimeMillis());
        }

        chatMessageModel.setSubject(message.getSubject());

        try {
            chatMessageModel.setBody(Base64Helper.decode(message.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT_GROUP)) {

            ExtensionElement destroyElement = message.getExtension("destroy", "http://jabber.org/protocol/muc#user");

            if (destroyElement != null) {

                chatMessageModel.setType(AppConstants.Chat.TYPE_GROUP_DELETED);

            } else {

                if (message.toXML("").toString().contains("urn:xmpp:mucsub:nodes:subscribers")) {
                    String effectedUser = null;

                    try {
                        XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                        XmlPullParser xmlParser = xmlFactoryObject.newPullParser();

                        xmlParser.setInput(new StringReader(message.toXML("").toString()));

                        int event = xmlParser.getEventType();

                        while (event != XmlPullParser.END_DOCUMENT) {

                            String name = xmlParser.getName();

                            if (event == XmlPullParser.START_TAG) {

                                if (name.equals("subscribe")) {
                                    effectedUser = xmlParser.getAttributeValue(null, "nick");
                                    chatMessageModel.setType(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED);
                                }
                                if (name.equals("unsubscribe")) {
                                    effectedUser = xmlParser.getAttributeValue(null, "nick");
                                    chatMessageModel.setType(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED);
                                }
                            }

                            event = xmlParser.next();
                        }

                        chatMessageModel.setBody(String.valueOf(effectedUser));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return chatMessageModel;
    }

    public void handleNewIncomingMessages(MessageData chatMessageModel) {

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT
                && chatMessageModel.chatMessage.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
            if (userId == chatMessageModel.chatMessage.getFromToUserId()
                    || ((TempStorage.getUser().getId() == chatMessageModel.chatMessage.getFromToUserId()))) {
            } else {
                return;
            }

        } else if (!(chatType == AppConstants.Chat.TYPE_GROUP_CHAT
                && chatMessageModel.chatMessage.getChatRoomType() == AppConstants.Chat.TYPE_GROUP_CHAT
                && groupId == chatMessageModel.chatMessage.getConversationId())) {
            return;
        }

        if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_TYPING_START)) {
            listener.typingStatus(true);
        } else if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_TYPING_STOP)) {
            listener.typingStatus(false);
        } else if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT) ||
                chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
            listener.newMessage(chatMessageModel);
        } else {
            listener.newMessage(chatMessageModel);
        }
    }

    public void typing(boolean isTyping) {
//        if (chat != null) {
//            xmppHelper.sendTypingMessage(isTyping, chat);
//        }
    }

    public ChatMessage createMediaMessage(String imagePath, int duration, String type) {

        ChatMedia chatMedia = new ChatMedia();
        chatMedia.setStoragePath(imagePath);
        chatMedia.setDuration(duration);

        Message message = new Message();
        message.setTo(userJid);

        ChatMessage chatMessageModel = new ChatMessage();
        chatMessageModel.setSubject(type);
        chatMessageModel.setMessageType(AppConstants.Chat.MESSAGE_TYPE_SENT);
        chatMessageModel.setStatus(AppConstants.Chat.STATUS_PENDING);

        chatMessageModel.setTimestamp(System.currentTimeMillis());
        chatMessageModel.setDate(TimeUtils.getChatHeaderTimeFromMillis(chatMessageModel.getTimestamp(), context));

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            chatMessageModel.setFromToUserId(userId);
            chatMessageModel.setConversationId(userId);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT);

        } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            chatMessageModel.setConversationId(groupId);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT_GROUP);
        }

        chatMessageModel.setBody(gson.toJson(chatMedia));
        chatMessageModel.setMessageId(message.getStanzaId());

        return chatMessageModel;
    }

    public void sendMediaMessage(ChatMessage chatMessageModel) {
        Message message = new Message();

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            message.setTo(userJid);
            chatMessageModel.setFromToUserId(userId);
            chatMessageModel.setConversationId(userId);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT);

        } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            message.setTo(groupJid);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT_GROUP);
            chatMessageModel.setConversationId(groupId);
        }

        message.setStanzaId(chatMessageModel.getMessageId());

        ChatMedia chatMedia = gson.fromJson(chatMessageModel.getBody(), ChatMedia.class);
        chatMedia.setStoragePath(null);
        chatMedia.setUploading(null);
        chatMedia.setDownloading(null);
        chatMedia.setSuccessfullyUploaded(null);

        if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {
            message.setSubject(AppConstants.Chat.TYPE_CHAT_AUDIO);
            chatMedia.setHeight(null);
            chatMedia.setWidth(null);
        } else if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
            message.setSubject(AppConstants.Chat.TYPE_CHAT_IMAGE);
            chatMedia.setDuration(null);
        }

        String messageText = Base64Helper.encode(gson.toJson(chatMedia));
        message.setBody(messageText);

        sendXMPPMessage(chatMessageModel, message);
    }

    private void sendXMPPMessage(ChatMessage chatMessageModel, Message message) {
        try {
            LogUtils.debug("Success" + message.toString());
            chatMessageModel.setMessageType(AppConstants.Chat.MESSAGE_TYPE_SENT);
            TempStorage.getXMPPHelper().sendMessage(message, chatMessageModel, listener);
        } catch (Exception e) {
            LogUtils.debug("Failure" + message.toString());
            listener.messageSentFailure(chatMessageModel);
            e.printStackTrace();
        }
    }

    public ChatMessage sendMessage(String messageText) {

//        for (PreventText preventText : TempStorage.getPreventTextList()) {
//            messageText = messageText.replace(preventText.getText(), preventText.getReplaceText());
//        }


        ChatMessage chatMessageModel = new ChatMessage();
        chatMessageModel.setTimestamp(System.currentTimeMillis());
        chatMessageModel.setDate(TimeUtils.getChatHeaderTimeFromMillis(chatMessageModel.getTimestamp(), context));

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            chatMessageModel.setFromToUserId(userId);
            chatMessageModel.setConversationId(userId);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT);
        } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            chatMessageModel.setConversationId(groupId);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT_GROUP);
        }

        chatMessageModel.setBody(messageText);
        chatMessageModel.setStatus(AppConstants.Chat.STATUS_PENDING);
        chatMessageModel.setSubject(AppConstants.Chat.TYPE_CHAT_TEXT);

        sendMessage(chatMessageModel);
        return chatMessageModel;
    }

    public void sendMessage(ChatMessage chatMessageModel) {
        Message message = new Message();

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            message.setTo(userJid);
            chatMessageModel.setFromToUserId(userId);
            chatMessageModel.setConversationId(userId);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT);

        } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            message.setTo(groupJid);
            chatMessageModel.setType(AppConstants.Chat.TYPE_CHAT_GROUP);
            chatMessageModel.setConversationId(groupId);
        }

        if (chatMessageModel.getMessageId() != null) {
            message.setStanzaId(chatMessageModel.getMessageId());
        } else {
            chatMessageModel.setMessageId(message.getStanzaId());
        }

        message.setSubject(AppConstants.Chat.TYPE_CHAT_TEXT);

        String messageTextBase64 = Base64Helper.encode(chatMessageModel.getBody());
        message.setBody(messageTextBase64);

        sendXMPPMessage(chatMessageModel, message);
    }

    public static Message getMessage(ChatMessage chatMessageModel) {
        Message message = new Message();

        try {
            message.setStanzaId(chatMessageModel.getMessageId());

            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
                message.setTo(JidCreate.entityBareFrom(AppConstants.getJID(chatMessageModel.getFromToUserId() + "")));

            } else if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT_GROUP)) {
                message.setTo(JidCreate.bareFrom(chatMessageModel.getConversationId() + "@conference." + AppConstants.XMPP.SERVICE_NAME));
            }

            message.setSubject(chatMessageModel.getType());

            String messageTextBase64 = Base64Helper.encode(chatMessageModel.getBody());
            message.setBody(messageTextBase64);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
