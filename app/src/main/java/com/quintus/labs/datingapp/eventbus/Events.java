package com.quintus.labs.datingapp.eventbus;


import com.quintus.labs.datingapp.rest.Response.AddressList;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.GroupChatInfo;
import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;
import com.quintus.labs.datingapp.xmpp.utils.MediaModel;
import com.quintus.labs.datingapp.xmpp.utils.UserModel;

import org.jivesoftware.smack.packet.Message;

import java.util.List;

public class Events {
    public static class XMPP {

        public int progress;

        public enum Callback {
            CONNECTING,
            AUTHENTICATED,
            CHECKING_MESSAGES_STARED,
            CHECKING_MESSAGES_PROGRESS,
            CHECKING_MESSAGES_COMPLETED
        }

        public Callback callback;
        private long messageTime;

        public XMPP(Callback callback) {
            this.callback = callback;
        }

        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }
    }


    public static class AddressUpdate {
        public AddressList addressList;

        public AddressUpdate(AddressList addressList) {
            this.addressList = addressList;
        }

        public AddressList getAddressList() {
            return addressList;
        }
    }



    public static class paymentUpdated {
        private int bookinId;
        private String status;

        public paymentUpdated(int bookinId, String status) {
            this.bookinId = bookinId;
            this.status = status;

        }

        public int getBookinId() {
            return bookinId;
        }

        public void setBookinId(int bookinId) {
            this.bookinId = bookinId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class paymentUpdateRsa
    {

    }


    public static class AssignmentResponseNewsModel {
        int  assignmentId;
        int newMappingId;

        public AssignmentResponseNewsModel(int assignmentId,int newMappingId) {
            this.assignmentId = assignmentId;
            this.newMappingId = newMappingId;
        }

        public int getAssignmentId() {
            return assignmentId;
        }

        public int getNewMappingId() {
            return newMappingId;
        }
    }



    public static class AddMemberCount {
        int count;
        public AddMemberCount( int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    public static class RemoveMemberCount {
        int count;
        public RemoveMemberCount( int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    public static class RemoveMemberNameListCount {
        int count;
        public RemoveMemberNameListCount( int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    public static class AddNameListCount {
        int count;
        public AddNameListCount( int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    public static class RemoveNameListCount {
        int count;
        public RemoveNameListCount( int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    public static class PostingUpdatesCancel {

    }

    public static class PostingUpdatesRetry {

    }

    public static class ScanFragmentPause {

    }

    public static class ScanFragmentResume {

    }

    public static class XMPPAuthenticated {

    }

    public static class Online {

    }


    public static class MessageBackup {
        public enum Callback {
            STARTED,
            COMPLETED
        }

        public Callback callback;

        public MessageBackup(Callback callback) {
            this.callback = callback;
        }
    }

    public static class NewIncomingTypingMessage {
        private Message message;
        private boolean isTyping;

        public NewIncomingTypingMessage(Message message, boolean isTyping) {
            this.message = message;
            this.isTyping = isTyping;
        }

        public Message getMessage() {
            return message;
        }

        public boolean isTyping() {
            return isTyping;
        }
    }

    public static class NewIncomingChatMessage {
        private ChatMessage chatMessageModel;
        private UserInfo userInfo;

        public NewIncomingChatMessage(ChatMessage chatMessageModel) {
            this.chatMessageModel = chatMessageModel;
        }

        public void setChatMessageModel(ChatMessage chatMessageModel) {
            this.chatMessageModel = chatMessageModel;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public ChatMessage getChatMessageModel() {
            return chatMessageModel;
        }
    }

    public static class RefreshChatList {
    }

    public static class AttendanceSaveShowHide {
    }

    public static class RefreshHomeViewPager {
    }

    public static class RefreshNewsPostList {
    }

    public static class AttendanceCountUpdate {
    }

    public static class RefreshBoardRequestList {
        private UserModel usermodel;
        private int count;

        public RefreshBoardRequestList(UserModel usermodel, int count) {
            this.usermodel = usermodel;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public UserModel getUserModel() {
            return usermodel;
        }
    }

    public static class RefreshFollowRequestList {
        private UserModel usermodel;
        private int count;

        public RefreshFollowRequestList(UserModel usermodel, int count) {
            this.usermodel = usermodel;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public UserModel getUserModel() {
            return usermodel;
        }
    }





    public static class RemoveAllAbsent {

    }
    public static class RemoveAllUnmark
    {

    }

    public static class RemoveAllPresent {

    }

    public static class NotificationNewPost {
    }

    public static class NotificationRefreshYouPost {
    }

    public static class NotificationRefreshFollowingPost {
    }

    public static class NotificationCount {
    }

    public static class ForwardMessageList {
        private Object objectReceiver;
        private List<ChatMessage> messageList;

        public ForwardMessageList(Object objModel, List<ChatMessage> objMesage) {
            this.objectReceiver = objModel;
            this.messageList = objMesage;
        }

        public Object getObjectReceiver() {
            return objectReceiver;
        }

        public List<ChatMessage> getMessageList() {
            return messageList;
        }
    }

    public static class NewReceiptChatMessage {
        private ChatMessage chatMessageModel;

        public NewReceiptChatMessage(ChatMessage chatMessageModel) {
            this.chatMessageModel = chatMessageModel;
        }

        public ChatMessage getChatMessageModel() {
            return chatMessageModel;
        }
    }

    public static class MediaPickerDone {
    }

    public static class ActionModeDone {
    }

    public static class UserOnline {
        private boolean isAvailable;
        private int userID;

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public UserOnline(int userId, boolean isAvailable) {
            this.isAvailable = isAvailable;
            this.userID = userId;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }


    }



    public static class DeletedMedia {
        private MediaModel mediaModel;

        public DeletedMedia(MediaModel mediaModel) {
            this.mediaModel = mediaModel;
        }

        public MediaModel getMediaModel() {
            return mediaModel;
        }
    }

    public static class UserBlockUnblocked {
        private boolean isBlocked;
        private int userId;

        public UserBlockUnblocked(boolean isBlocked, int userId) {
            this.isBlocked = isBlocked;
            this.userId = userId;
        }

        public boolean isBlocked() {
            return isBlocked;
        }

        public void setBlocked(boolean blocked) {
            isBlocked = blocked;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }


    public static class BoardJoined {

    }

    public static class BoardUnJoined {

    }

    public static class ChatGroupDeleted {
        private int groupId;

        public ChatGroupDeleted(int groupId) {
            this.groupId = groupId;
        }

        public int getGroupId() {
            return groupId;
        }
    }

    public static class ChatGroupRoleUpdated {
        private int groupId;
        private String role;

        public ChatGroupRoleUpdated(int groupId, String role) {
            this.groupId = groupId;
            this.role = role;
        }

        public int getGroupId() {
            return groupId;
        }

        public String getRole() {
            return role;
        }
    }

    public static class ChatGroupUpdated {
        private GroupChatInfo groupInfo;

        public ChatGroupUpdated(GroupChatInfo groupInfo) {
            this.groupInfo = groupInfo;
        }

        public GroupChatInfo getGroupInfo() {
            return groupInfo;
        }
    }

    public static class ChatMessageSuccess {
        private ChatMessage chatMessageModel;

        public ChatMessageSuccess(ChatMessage chatMessageModel) {
            this.chatMessageModel = chatMessageModel;
        }

        public ChatMessage getChatMessageModel() {
            return chatMessageModel;
        }
    }

    public static class ParticipantSelected {
        private List<UserModel> userModels;
        private GroupChatInfo groupInfo;

        public ParticipantSelected(List<UserModel> userModels, GroupChatInfo groupInfo) {
            this.userModels = userModels;
            this.groupInfo = groupInfo;
        }

        public List<UserModel> getUserModels() {
            return userModels;
        }

        public GroupChatInfo getGroupInfo() {
            return groupInfo;
        }
    }



}
