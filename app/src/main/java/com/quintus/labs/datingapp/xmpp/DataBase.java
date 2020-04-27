package com.quintus.labs.datingapp.xmpp;

import android.content.Context;

import com.google.gson.Gson;
import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.chat.ChattingActivity;
import com.quintus.labs.datingapp.xmpp.room.models.ChatConversation;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.ConversationData;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.AppSharedPreferences;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Helps in manipulating the database and tables.
 *
 * @author Varun John
 */
public class DataBase {

    Context context;
    private static DataBase mDatabase;
    private Gson gson = new Gson();

    private DataBase(Context context) {
        this.context = context;
    }

    public static DataBase getInstance(Context context) {
        if (mDatabase == null) {
            mDatabase = new DataBase(context);
        }
        return mDatabase;
    }

    public interface OnCompleteInsertMessage<T> {
        void onCompleted(T result);
    }

    public void insertMessage(ChatMessage chatMessageModel) {
        insertMessageNotify(chatMessageModel, null);
    }

    public synchronized void insertMessageNotify(ChatMessage chatMessageModel, final OnCompleteInsertMessage<Boolean> onComplete) {
        try {
            // Insert message..
            if (MyApplication.getChatDataBase().chatMessageDao().insert(chatMessageModel) > 0) {

                int newGroupID = -1;
                int effectedUserId = 0;

                // Check Chat Conversations..
                if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {

                    ChatConversation chatConversationSingle = MyApplication.getChatDataBase().chatConversationDao().getChatConversation(chatMessageModel.getConversationId(), AppConstants.Chat.TYPE_SINGLE_CHAT);

                    if (chatConversationSingle != null) {

                        ChatMessage chatMessageDB = MyApplication.getChatDataBase().chatMessageDao().getChatMessage(chatConversationSingle.getLastMessageId());

                        if (chatMessageDB != null && chatMessageDB.getTimestamp() < chatMessageModel.getTimestamp()) {
                            chatConversationSingle.setLastMessageId(chatMessageModel.getMessageId());
                        } else if (chatMessageDB == null) {
                            chatConversationSingle.setLastMessageId(chatMessageModel.getMessageId());
                        }

                        if ((ChattingActivity.isChatForeground && ChattingActivity.userId == chatMessageModel.getFromToUserId())) {
                            chatConversationSingle.setUnreadCount(0);
                        } else {

                            if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                                chatConversationSingle.setUnreadCount(0);
                            } else {
                                chatConversationSingle.setUnreadCount(chatConversationSingle.getUnreadCount() + 1);
                            }
                        }

                        if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED)) {
                            chatConversationSingle.setLastReadTimestamp(chatMessageModel.getTimestamp());
                        }

                        MyApplication.getChatDataBase().chatConversationDao().update(chatConversationSingle);

                    } else {

                        ChatConversation chatConversation = new ChatConversation();
                        chatConversation.setConversationId(chatMessageModel.getConversationId());
                        chatConversation.setConversationType(AppConstants.Chat.TYPE_SINGLE_CHAT);
                        chatConversation.setLastMessageId(chatMessageModel.getMessageId());

                        if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                            chatConversation.setUnreadCount(0);
                        } else {
                            chatConversation.setUnreadCount(1);
                        }

                        MyApplication.getChatDataBase().chatConversationDao().insert(chatConversation);
                    }

                } else {
                    ChatConversation chatConversationGroup = MyApplication.getChatDataBase().chatConversationDao().getChatConversation(chatMessageModel.getConversationId(), AppConstants.Chat.TYPE_GROUP_CHAT);

                    if (chatConversationGroup != null) {

                        ChatMessage chatMessageDB = MyApplication.getChatDataBase().chatMessageDao().getChatMessage(chatConversationGroup.getLastMessageId());

                        if (chatMessageDB != null && chatMessageDB.getTimestamp() < chatMessageModel.getTimestamp()) {
                            chatConversationGroup.setLastMessageId(chatMessageModel.getMessageId());
                        } else if (chatMessageDB == null) {
                            chatConversationGroup.setLastMessageId(chatMessageModel.getMessageId());
                        }

                        if ((ChattingActivity.isChatForeground && ChattingActivity.groupId == chatMessageModel.getConversationId())) {
                            // User is currently interacting with this group..
                            chatConversationGroup.setUnreadCount(0);

                            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED) ||
                                    chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED) ||
                                    chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_DELETED)) {

                                try {
                                    effectedUserId = Integer.parseInt(chatMessageModel.getBody());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if ((chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED)
                                        && effectedUserId == TempStorage.getUser().getId())) {
                                    chatConversationGroup.setGroupRole(AppConstants.Chat.SERVER_ROLE_MEMBER);
                                    EventBroadcastHelper.groupRoleUpdated(chatConversationGroup.getConversationId(), AppConstants.Chat.SERVER_ROLE_MEMBER);
                                }

                                if ((chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)
                                        && effectedUserId == TempStorage.getUser().getId())) {
                                    chatConversationGroup.setGroupRole(AppConstants.Chat.SERVER_ROLE_NONE);
                                    EventBroadcastHelper.groupRoleUpdated(chatConversationGroup.getConversationId(), AppConstants.Chat.SERVER_ROLE_NONE);
                                }

                                if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_DELETED)) {
                                    chatConversationGroup.setGroupRole(AppConstants.Chat.SERVER_ROLE_NONE);
                                    EventBroadcastHelper.groupDeleted(chatConversationGroup.getConversationId());
                                }
                            }

                        } else {

                            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED) ||
                                    chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED) ||
                                    chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_DELETED)) {

                                try {
                                    effectedUserId = Integer.parseInt(chatMessageModel.getBody());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if ((chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED)
                                        && effectedUserId == TempStorage.getUser().getId())) {
                                    chatConversationGroup.setUnreadCount(chatConversationGroup.getUnreadCount() + 1);
                                    chatConversationGroup.setGroupRole(AppConstants.Chat.SERVER_ROLE_MEMBER);
                                    EventBroadcastHelper.groupRoleUpdated(chatConversationGroup.getConversationId(), AppConstants.Chat.SERVER_ROLE_MEMBER);
                                }

                                if ((chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)
                                        && effectedUserId == TempStorage.getUser().getId())) {
                                    chatConversationGroup.setGroupRole(AppConstants.Chat.SERVER_ROLE_NONE);
                                    EventBroadcastHelper.groupRoleUpdated(chatConversationGroup.getConversationId(), AppConstants.Chat.SERVER_ROLE_NONE);
                                }

                                if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_DELETED)) {
                                    chatConversationGroup.setGroupRole(AppConstants.Chat.SERVER_ROLE_NONE);
                                    EventBroadcastHelper.groupDeleted(chatConversationGroup.getConversationId());
                                }

                            } else {

                                if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                                    chatConversationGroup.setUnreadCount(0);
                                } else {
                                    chatConversationGroup.setUnreadCount(chatConversationGroup.getUnreadCount() + 1);
                                }
                            }
                        }

                        MyApplication.getChatDataBase().chatConversationDao().update(chatConversationGroup);

                    } else {
                        // Since conversation not exists it either you created the group or someone added you..
                        ChatConversation chatConversation = new ChatConversation();
                        chatConversation.setConversationId(chatMessageModel.getConversationId());
                        chatConversation.setConversationType(AppConstants.Chat.TYPE_GROUP_CHAT);
                        chatConversation.setLastMessageId(chatMessageModel.getMessageId());

                        chatConversation.setGroupRole(AppConstants.Chat.SERVER_ROLE_MEMBER);

                        if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED)) {

                            try {
                                effectedUserId = Integer.parseInt(chatMessageModel.getBody());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            if (effectedUserId == chatMessageModel.getFromToUserId() && effectedUserId == TempStorage.getUser().getId()) {
                                // You created this group..
                                chatConversation.setGroupRole(AppConstants.Chat.SERVER_ROLE_OWNER);
                                chatConversation.setUnreadCount(0);
                            } else if (effectedUserId == TempStorage.getUser().getId()) {
                                // Someone added you..
                                chatConversation.setGroupRole(AppConstants.Chat.SERVER_ROLE_MEMBER);
                                chatConversation.setUnreadCount(1);
                            }
                        }

                        newGroupID = chatConversation.getConversationId();

                        MyApplication.getChatDataBase().chatConversationDao().insert(chatConversation);
                    }
                }

                ConversationData chatConversationUser = null;
                chatConversationUser = MyApplication.getChatDataBase().chatConversationDao().haveUser(chatMessageModel.getFromToUserId());

                if (chatConversationUser == null || chatConversationUser.userInfo == null) {

                    ChatConversation chatConversation = new ChatConversation();
                    chatConversation.setConversationId(chatMessageModel.getFromToUserId());
                    chatConversation.setConversationType(AppConstants.Chat.TYPE_SINGLE_CHAT);
                    chatConversation.setLastMessageId("");

                    MyApplication.getChatDataBase().chatConversationDao().insert(chatConversation);
                }

                boolean newEffectedUser = false;
                ConversationData effectedUser = null;

                if (effectedUserId != 0 && (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED) ||
                        chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED))) {
                    effectedUser = MyApplication.getChatDataBase().chatConversationDao().haveUser(effectedUserId);

                    if (effectedUser == null || effectedUser.chatConversation == null) {

                        newEffectedUser = true;
                        ChatConversation chatConversation = new ChatConversation();
                        chatConversation.setConversationId(effectedUserId);
                        chatConversation.setConversationType(AppConstants.Chat.TYPE_SINGLE_CHAT);
                        chatConversation.setLastMessageId("");

                        MyApplication.getChatDataBase().chatConversationDao().insert(chatConversation);
                    }
                }

                if (XMPPHelper.isCheckingFirstTime /*|| chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)*/) {
                    if (onComplete != null) {
                        onComplete.onCompleted(true);
                    }
                    return;
                }

                if (chatConversationUser == null || chatConversationUser.userInfo == null || newGroupID != -1 || newEffectedUser) {

                    List<Integer> userIds = new ArrayList<>(1);
                    List<Integer> grpIds = new ArrayList<>(1);

                    if (chatConversationUser == null || chatConversationUser.userInfo == null) {
                        if (chatMessageModel.getFromToUserId() != 0) {
                            userIds.add(chatMessageModel.getFromToUserId());
                        }
                    }

                    if (effectedUserId != 0) {
                        userIds.add(effectedUserId);
                    }

                    if (newGroupID != -1) {
                        grpIds.add(newGroupID);
                    }

                    if (!userIds.isEmpty() || !grpIds.isEmpty()) {
                        if (onComplete != null) {
                            onComplete.onCompleted(true);
                        }
                    } else {
                        if (onComplete != null) {
                            onComplete.onCompleted(true);
                        }
                    }
                } else {
                    if (onComplete != null) {
                        onComplete.onCompleted(true);
                    }
                }
            } else {

                // if we got a msg that already in db then update its timestamp..
                ChatMessage chatMessage = MyApplication.getChatDataBase().chatMessageDao().getChatMessage(chatMessageModel.getMessageId());
                chatMessage.setTimestamp(chatMessageModel.getTimestamp());
                MyApplication.getChatDataBase().chatMessageDao().update(chatMessage);

//                ChatConversation chatConversation = MyApplication.getChatDataBase().chatConversationDao().getChatConversation(chatMessageModel.getConversationId(), chatMessageModel.getChatRoomType());
//                if (chatMessageModel.getTimestamp() > chatMessage.getTimestamp()) {
//                    chatConversation.setLastMessageId(chatMessageModel.getMessageId());
//                    MyApplication.getChatDataBase().chatConversationDao().update(chatConversation);
//                }

                if (onComplete != null) {
                    onComplete.onCompleted(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug("Database insertMessageNotify Exception ");

            if (onComplete != null) {
                onComplete.onCompleted(false);
            }
        }
    }

    public boolean clearDB() {

        AppSharedPreferences.getInstance(context).setLoadingForFirstTime(true);
        AppSharedPreferences.getInstance(context).setLastReceivedDataTimeXMPP(0);
        AppSharedPreferences.getInstance(context).setErrorLastReceivedDataTimeXMPP(0);

        MyApplication.getChatDataBase().chatMessageDao().deleteAll();
        MyApplication.getChatDataBase().userInfoDao().deleteAll();
        MyApplication.getChatDataBase().chatConversationDao().deleteAll();
        MyApplication.getChatDataBase().groupChatInfoDao().deleteAll();

        return true;
    }

    public void deleteConversation(ConversationData model) {

        if (model.chatConversation.getConversationType() == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            MyApplication.getChatDataBase().chatConversationDao().deleteConversation(model.chatConversation.getConversationId(), model.chatConversation.getConversationType());
            MyApplication.getChatDataBase().chatMessageDao().deleteSingleMessages(model.chatConversation.getConversationId());
        } else if (model.chatConversation.getConversationType() == AppConstants.Chat.TYPE_GROUP_CHAT) {
            MyApplication.getChatDataBase().chatConversationDao().deleteConversation(model.chatConversation.getConversationId(), model.chatConversation.getConversationType());
            MyApplication.getChatDataBase().chatMessageDao().deleteGroupMessages(model.chatConversation.getConversationId());
        }
    }
}
