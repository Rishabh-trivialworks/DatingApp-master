package com.quintus.labs.datingapp.Utils;


import android.app.NotificationManager;
import android.content.Context;

import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.rest.Response.AddressList;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.GroupChatInfo;
import com.quintus.labs.datingapp.xmpp.utils.AppSharedPreferences;
import com.quintus.labs.datingapp.xmpp.utils.MediaModel;
import com.quintus.labs.datingapp.xmpp.utils.UserModel;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

public class EventBroadcastHelper {



    public static void sendAddressUpdate(AddressList addressList) {
        GlobalBus.getBus().post(new Events.AddressUpdate(addressList));
    }
    public static void sendPaymentUpdate(int bookingid, String status) {
        GlobalBus.getBus().post(new Events.paymentUpdated(bookingid,status));
    }

    public static  void  sendPaymentStatus(){
        GlobalBus.getBus().post(new Events.paymentUpdateRsa());
    }
    public static void sendXMPPConnecting() {
        GlobalBus.getBus().post(new Events.XMPP(Events.XMPP.Callback.CONNECTING));
    }


    public static void sendXMPPAuthenticated() {
        LogUtils.debug("getXMPPStatus " + "Authenticated");

        GlobalBus.getBus().post(new Events.XMPP(Events.XMPP.Callback.AUTHENTICATED));
    }


    public static void sendXMPPCheckingMessageStarted() {
        GlobalBus.getBus().post(new Events.XMPP(Events.XMPP.Callback.CHECKING_MESSAGES_STARED));
    }

    public static void sendXMPPCheckingMessageStarted(long message) {
        Events.XMPP xmpp = new Events.XMPP(Events.XMPP.Callback.CHECKING_MESSAGES_STARED);
        xmpp.setMessageTime(message);
        GlobalBus.getBus().post(xmpp);
    }

    public static void sendXMPPCheckingMessageCompleted() {
        GlobalBus.getBus().post(new Events.XMPP(Events.XMPP.Callback.CHECKING_MESSAGES_COMPLETED));
    }

    private static Events.XMPP xmppProgress;

    public static void sendXMPPCheckingMessageProgress(int progress) {
        if (progress == 0) {
            return;
        }

        if (xmppProgress == null) {
            xmppProgress = new Events.XMPP(Events.XMPP.Callback.CHECKING_MESSAGES_PROGRESS);
        }
        xmppProgress.progress = progress;
        GlobalBus.getBus().post(xmppProgress);
    }

    public static void sendMessageBackUpStarted() {
        GlobalBus.getBus().post(new Events.MessageBackup(Events.MessageBackup.Callback.STARTED));
    }

    public static void sendMessageBackUpCompleted() {
        GlobalBus.getBus().post(new Events.MessageBackup(Events.MessageBackup.Callback.COMPLETED));
    }

    public static void sendNewsPostingCancelEvent() {
        GlobalBus.getBus().post(new Events.PostingUpdatesCancel());
    }



    public static void sendAssignmentResponseUpdateNews(int assignemtntId, int newsMappingId) {
        GlobalBus.getBus().post(new Events.AssignmentResponseNewsModel(assignemtntId,newsMappingId));


    }

    public static void sendAddMemberCount(int count) {
        GlobalBus.getBus().post(new Events.AddMemberCount(count));


    }
    public static void sendRemoveMemberCount(int count) {
        GlobalBus.getBus().post(new Events.RemoveMemberCount(count));


    }

    public static void sendRemoveMemberNameListCount(int count) {
        GlobalBus.getBus().post(new Events.RemoveMemberNameListCount(count));


    }

    public static void sendAddNameListCount(int count) {
        GlobalBus.getBus().post(new Events.AddNameListCount(count));


    }
    public static void sendRemoveNameListCount(int count) {
        GlobalBus.getBus().post(new Events.RemoveNameListCount(count));


    }

    public static void sendNewIncomingTypingMessage(Message message, boolean isTyping) {
        GlobalBus.getBus().post(new Events.NewIncomingTypingMessage(message, isTyping));
    }

    public static void sendNewIncomingChatMessage(ChatMessage chatMessageModel) {
        GlobalBus.getBus().post(new Events.NewIncomingChatMessage(chatMessageModel));
    }

    public static void sendRefreshChatList() {
        GlobalBus.getBus().post(new Events.RefreshChatList());
    }



    public static void sendForwardMessage(Object object, List<ChatMessage> messageModels) {
        GlobalBus.getBus().post(new Events.ForwardMessageList(object, messageModels));
    }

    public static void sendNewReceiptChatMessage(ChatMessage chatMessageModel) {
        GlobalBus.getBus().post(new Events.NewReceiptChatMessage(chatMessageModel));
    }

    public static void sendNewPostNotification() {
        GlobalBus.getBus().post(new Events.NotificationNewPost());
    }

    public static void sendNewRefreshYouNotification() {
        GlobalBus.getBus().post(new Events.NotificationRefreshYouPost());
    }

    public static void sendNewRefreshFollowingNotification() {
        GlobalBus.getBus().post(new Events.NotificationRefreshFollowingPost());
    }

    public static void sendNewNotificationCount() {
        GlobalBus.getBus().post(new Events.NotificationCount());
    }

    public static void sendUserBlockedUnblocked(boolean isBlocked, int userId) {
        GlobalBus.getBus().post(new Events.UserBlockUnblocked(isBlocked, userId));
    }

    public static void sendMatchedRefresh() {
        GlobalBus.getBus().post(new Events.RefreshMatched());
    }
    public static void sendUserEvent(UserData user) {
        GlobalBus.getBus().post(new Events.UserEvent(user));
    }

    public static void sendUserOnlineStatus(int userId, boolean isAvailable) {
        GlobalBus.getBus().post(new Events.UserOnline(userId, isAvailable));
    }
    public static void sendUserUpdate() {
        GlobalBus.getBus().post(new Events.UserUpdate());
    }

    public static void sendLogout(Context context) {
        AppSharedPreferences.getInstance(context).setLogin(false);

        NotificationManager notificationManager = (NotificationManager) AppContext.getInstance().getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void sendMediaPickerDone() {
        GlobalBus.getBus().post(new Events.MediaPickerDone());
    }

    public static void sendMediaFileDeleted(MediaModel mediaModel) {
        GlobalBus.getBus().post(new Events.DeletedMedia(mediaModel));
    }



    public static void sendForwardMessageDone() {
        GlobalBus.getBus().post(new Events.ActionModeDone());
    }

    public static void groupDeleted(int groupId) {
        GlobalBus.getBus().post(new Events.ChatGroupDeleted(groupId));
    }

    public static void groupRoleUpdated(int groupId, String role) {
        GlobalBus.getBus().post(new Events.ChatGroupRoleUpdated(groupId, role));
    }

    public static void groupUpdated(GroupChatInfo groupInfo) {
        GlobalBus.getBus().post(new Events.ChatGroupUpdated(groupInfo));
    }

    public static void selectedParticipants(List<UserModel> userModels, GroupChatInfo groupInfo) {
        GlobalBus.getBus().post(new Events.ParticipantSelected(userModels, groupInfo));
    }

    public static void sendMessageSuccessful(ChatMessage message) {
        GlobalBus.getBus().post(new Events.ChatMessageSuccess(message));
    }

    public static void goOnline() {
        GlobalBus.getBus().post(new Events.Online());
    }



}
