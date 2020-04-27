package com.quintus.labs.datingapp.xmpp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;

import com.google.gson.Gson;

import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.chat.ChattingActivity;
import com.quintus.labs.datingapp.xmpp.extensions.DisplayedExtension;
import com.quintus.labs.datingapp.xmpp.room.models.ChatConversation;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.ConversationData;
import com.quintus.labs.datingapp.xmpp.room.models.GroupChatInfo;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.AppSharedPreferences;
import com.quintus.labs.datingapp.xmpp.utils.ChatNotification;
import com.quintus.labs.datingapp.xmpp.utils.NetworkUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.PresenceEventListener;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.blocking.BlockingCommandManager;
import org.jivesoftware.smackx.blocking.JidsBlockedListener;
import org.jivesoftware.smackx.blocking.JidsUnblockedListener;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Varun John on 4/6/2017.
 */

public class XMPPHelper implements JidsBlockedListener, JidsUnblockedListener, StanzaListener, PresenceEventListener, SubscribeListener {

    public static String HOST_NAME = AppConstants.XMPP.HOST_NAME_ALPHA;
    public static final String SERVICE_NAME = AppConstants.XMPP.SERVICE_NAME;
    private final int PORT = 5222;
    private long PACKET_REPLY_TIMEOUT = 10000;

    private Context context;
    private DataBase dataBase;
    private Gson gson;

    public XMPPTCPConnection connection;
    public Roster roster;

    private BlockingCommandManager blockingCommandManager;

    public boolean isConnecting;

    private Handler handlerBg;

    private static XMPPHelper xmppHelper;

    public static Boolean isCheckingFirstTime = Boolean.FALSE;
    private static String username, password;

    private Runnable runnableConnection;
    private Runnable runnableOnlineWork;
    private long timeForConnection;
    private boolean hadErrorWhileConnecting;
    private boolean isCheckingMessages;

    private XMPPHelper(Context context) {
        this.context = context;

        gson = new Gson();

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();

        handlerBg = new Handler(handlerThread.getLooper());

        dataBase = DataBase.getInstance(context);


                HOST_NAME = AppConstants.XMPP.HOST_NAME_ALPHA;


    }

    public static XMPPHelper getInstance(Context context) {
        if (xmppHelper == null) {
            xmppHelper = new XMPPHelper(context);
        }
        return xmppHelper;
    }

    public boolean isConnected() {
        if (connection != null && connection.isConnected() && connection.isAuthenticated()) {
            return true;
        }
        return false;
    }

    public void onAppForeground() {
        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();

        handlerBg = new Handler(handlerThread.getLooper());
        connect();
    }

    public void onAppBackground() {
        if (runnableConnection != null) {
            handlerBg.removeCallbacks(runnableConnection);
        }
    }

    public synchronized void connect() {

        if (runnableConnection != null) {
            handlerBg.removeCallbacks(runnableConnection);
        }

        runnableConnection = new Runnable() {
            @Override
            public void run() {

                try {

                    username = AppConstants.getJID(TempStorage.getUser().getId() + "");
                    password = TempStorage.getUser().getId() + "";

                    if (TempStorage.getUser()!=null && !isConnected()) {

                        if (!isConnecting) {
                            timeForConnection = System.currentTimeMillis();
                        }

                        isConnecting = true;
                        EventBroadcastHelper.sendXMPPConnecting();

                            SmackConfiguration.DEBUG = true;


                        if (connection == null || hadErrorWhileConnecting) {

                            if (connection != null) {
                                connection.removeAsyncStanzaListener(XMPPHelper.this);
                            }

                            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                            builder.setUsernameAndPassword(password, password);
                            builder.setHost(HOST_NAME);
                            builder.setHostAddress(InetAddress.getByName(HOST_NAME));
                            builder.setPort(PORT);
                            builder.setConnectTimeout(10 * 1000);
                            builder.setCompressionEnabled(false);
                            builder.setSendPresence(true);
                            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

                            DomainBareJid serviceName = JidCreate.domainBareFrom(SERVICE_NAME);
                            builder.setXmppDomain(serviceName);

                            connection = new XMPPTCPConnection(builder.build());

                            connection.setUseStreamManagement(true);
                            connection.setUseStreamManagementResumption(true);
                            connection.setPreferredResumptionTime(0);
                            connection.setReplyTimeout(PACKET_REPLY_TIMEOUT);
                            connection.addAsyncStanzaListener(XMPPHelper.this, new StanzaTypeFilter(Message.class));
                        }

                        LogUtils.newCheckerXMPP("ConnectionStatus: trying to connect... " + "connect " + connection.isConnected() + " auth " + connection.isAuthenticated());

                        EventBroadcastHelper.sendXMPPConnecting();

                        if (!connection.isConnected()) {
                            LogUtils.newCheckerXMPP("ConnectionStatus: calling connection.connect()");
                            connection.connect();
                        } else {
                            LogUtils.newCheckerXMPP("ConnectionStatus: connection is connected..");
                        }

                        EventBroadcastHelper.sendXMPPConnecting();

                        if (!connection.isAuthenticated()) {
                            LogUtils.newCheckerXMPP("ConnectionStatus: calling connection.login()");
                            connection.login(password, password);
                        } else {
                            LogUtils.newCheckerXMPP("ConnectionStatus: connection is Authenticated..");
                        }

                        if (isConnected()) {
                            isConnecting = false;
                            LogUtils.newCheckerXMPP("ConnectionStatus: connect " + connection.isConnected() + " auth " + connection.isAuthenticated()
                                    + " time : " + ((System.currentTimeMillis() - timeForConnection) / (1000)));
                            onAuthenticationDoneSetup();
                        }

                    } else {
                        isConnecting = false;

                        if (TempStorage.getUser()==null) {
                            disconnect(null);
                        }
                    }
                    hadErrorWhileConnecting = false;
                } catch (Exception e) {
                    LogUtils.newCheckerXMPP("ConnectionStatus: Exception " + e.getMessage());
                    e.printStackTrace();
                    disconnect(null);
                    hadErrorWhileConnecting = true;
                }

                if (TempStorage.getUser()!=null) {
                    handlerBg.postDelayed(runnableConnection, 1000);
                   // LogUtils.newCheckerXMPP("ConnectionStatus: checking later in 1 sec.......");
                } else {
                    LogUtils.newCheckerXMPP("ConnectionStatus: USER LOGGED OUT");
                }
            }
        };

        handlerBg.post(runnableConnection);
    }

    @Override
    public void processStanza(Stanza packet) {

        if (isCheckingFirstTime) {
            return;
        }

        Message message = getCorrectMessage(packet.toXML("").toString(), null);

        if (message != null) {
            processMessage(message, false);
        } else {
            processMessage((Message) packet, false);
        }
    }

    private synchronized void processMessage(Message message, final boolean isCarbonMessage) {

        try {
            if (TempStorage.getUser() == null) {
               // MyApplication.setTempStorage();
            }

            final ChatMessage chatMessageModel = ChattingHelper.getChatMessageModel(context, message);

            DisplayedExtension displayedExtension = DisplayedExtension.from(message);

//             Check if its a Receiving Receipt...
            if (displayedExtension != null) {
                onSeenReceiptReceived(displayedExtension.getId(), chatMessageModel);
                return;
            }

            // Message is not valid...
            if (chatMessageModel.getType() == null
                    || chatMessageModel.getType().isEmpty()
                    || (chatMessageModel.getSubject().contains("chat_") && chatMessageModel.getBody() == null)) {
                return;
            }

            LogUtils.newMessagesXMPP("PPPProcessMessage** " + chatMessageModel.getBody() + " Stanza id " + chatMessageModel.getMessageId() + " Type " + chatMessageModel.getSubject());

            chatMessageModel.setStatus(AppConstants.Chat.STATUS_SENT);

            dataBase.insertMessageNotify(chatMessageModel, new DataBase.OnCompleteInsertMessage<Boolean>() {
                @Override
                public void onCompleted(Boolean isSuccessful) {

                    AppSharedPreferences.getInstance(context).setLastReceivedDataTimeXMPP(chatMessageModel.getTimestamp());

                    if (isSuccessful) {
                        EventBroadcastHelper.sendNewIncomingChatMessage(chatMessageModel);
                        sendNotification(chatMessageModel);
                    }

//                    if (TempStorage.getUserDetailModel().isReceivePrivateMsg()) {
//                    sendNotification(chatMessageModel);
//                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void onSeenReceiptReceived(String receiptId, ChatMessage chatMessageModel) {

        try {
            LogUtils.newMessagesXMPP("onSeenReceiptReceived: id " + receiptId + " Stanza id " + chatMessageModel.getMessageId());

            chatMessageModel.setMessageId(receiptId);
            chatMessageModel.setStatus(AppConstants.Chat.STATUS_SEEN);

            LogUtils.debug("onSeenReceiptReceived " + chatMessageModel.getFromToUserId() + " " + chatMessageModel.getTimestamp());

            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {

                ChatMessage message = MyApplication.getChatDataBase().chatMessageDao().getChatMessage(chatMessageModel.getMessageId());
                message.setStatus(AppConstants.Chat.STATUS_SEEN);

                if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {

                    ChatConversation chatConversation = MyApplication.getChatDataBase().chatConversationDao().getChatConversation(chatMessageModel.getConversationId(), chatMessageModel.getChatRoomType());

                    if (XMPPHelper.isCheckingFirstTime) {
                        chatConversation.setUnreadCount(0);
                    }

                    chatConversation.setLastReadTimestamp(message.getTimestamp());
                    MyApplication.getChatDataBase().chatConversationDao().update(chatConversation);

                } else if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED)) {

                    ChatConversation chatConversation = MyApplication.getChatDataBase().chatConversationDao().getChatConversation(chatMessageModel.getConversationId(), chatMessageModel.getChatRoomType());

                    if (chatConversation.getLastReadTimestamp() < message.getTimestamp()) {
                        chatConversation.setLastReadTimestamp(message.getTimestamp());
                        MyApplication.getChatDataBase().chatConversationDao().update(chatConversation);
                    }
                }

                MyApplication.getChatDataBase().chatMessageDao().update(message);

            } else {

                ChatMessage.MessageReceipts messageReceipts = new ChatMessage.MessageReceipts();
                messageReceipts.setId(chatMessageModel.getFromToUserId());
                messageReceipts.setTimestamp(chatMessageModel.getTimestamp());

                ChatMessage message = MyApplication.getChatDataBase().chatMessageDao().getChatMessage(chatMessageModel.getMessageId());

                if (message.getReceipts() == null) {
                    message.setReceipts(new ArrayList<ChatMessage.MessageReceipts>());
                    message.getReceipts().add(messageReceipts);
                } else {
                    message.getReceipts().add(messageReceipts);
                }

                chatMessageModel.setReceipts(message.getReceipts());

                MyApplication.getChatDataBase().chatMessageDao().update(message);

                ChatConversation chatConversation = MyApplication.getChatDataBase().chatConversationDao().getChatConversation(chatMessageModel.getConversationId(), chatMessageModel.getChatRoomType());

                if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                    if (XMPPHelper.isCheckingFirstTime) {
                        chatConversation.setUnreadCount(0);
                        MyApplication.getChatDataBase().chatConversationDao().update(chatConversation);
                    }
                }
            }

            AppSharedPreferences.getInstance(context).setLastReceivedDataTimeXMPP(chatMessageModel.getTimestamp());

            if (!XMPPHelper.isCheckingFirstTime) {
                EventBroadcastHelper.sendNewReceiptChatMessage(chatMessageModel);
                EventBroadcastHelper.sendRefreshChatList();
            }

            ConversationData conversationData = MyApplication.getChatDataBase().chatConversationDao().haveUser(chatMessageModel.getFromToUserId());

            if (conversationData == null) {
                ChatConversation chatConversation = new ChatConversation();
                chatConversation.setConversationId(chatMessageModel.getFromToUserId());
                chatConversation.setConversationType(AppConstants.Chat.TYPE_SINGLE_CHAT);
                chatConversation.setLastMessageId("");

                MyApplication.getChatDataBase().chatConversationDao().insert(chatConversation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(final ChatMessage chatMessageModel) {
        try {
            if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                return;
            }

            if (TempStorage.getUser() == null) {
               // MyApplication.setTempStorage();
            }

            if (!TempStorage.getUser().isReceivePrivateMsgNotification() || isCheckingFirstTime) {
                return;
            }

            if ((ChattingActivity.isChatForeground && chatMessageModel.getChatRoomType() == AppConstants.Chat.TYPE_SINGLE_CHAT && ChattingActivity.userId == chatMessageModel.getFromToUserId()) ||
                    (ChattingActivity.isChatForeground && chatMessageModel.getChatRoomType() == AppConstants.Chat.TYPE_GROUP_CHAT && ChattingActivity.groupId == chatMessageModel.getConversationId())) {
                return;
            } else {
                final ChatNotification chatNotification = new ChatNotification();

                if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT))
                    chatNotification.setBody(chatMessageModel.getBody());
                else if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
                    chatNotification.setBody(context.getString(R.string.image).toUpperCase());
                } else if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {
                    chatNotification.setBody(context.getString(R.string.audio).toUpperCase());
                } else if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED)) {
                    return;
                } else if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)) {
                    return;
                } else if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_GROUP_DELETED)) {
                    return;
                } else {
                    chatNotification.setBody(context.getString(R.string.update_your_app_to_see_this_message).toUpperCase());
                }

                chatNotification.setMessageId(chatMessageModel.getMessageId());
                chatNotification.setChatMessage(chatMessageModel);


                if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
                    chatNotification.setType(AppConstants.Notification.CHAT);
                    chatNotification.setObjectDataId(chatMessageModel.getFromToUserId());

                    UserInfo userModel = MyApplication.getChatDataBase().userInfoDao().get(chatMessageModel.getFromToUserId());

                    if (userModel != null) {
                        chatNotification.setPremiumUser(userModel.isPremiumUser());
                        chatNotification.setReceivePrivateMsg(userModel.isReceivePrivateMsg());
                        chatNotification.setOnWhoseSide(userModel.getOnWhoseSide());
                        chatNotification.setTitle(userModel.getFullName());
                        chatNotification.setProfileImageURL(userModel.getProfileImageThumbnail());
                        chatNotification.setUsername(userModel.getUsername());
                        chatNotification.setDeviceInfo(userModel.getDeviceInfo());

                       // generateChatNotification(context, chatNotification);
                    }

                } else {

                    chatNotification.setType(AppConstants.Notification.CHAT_GROUP);
                    chatNotification.setObjectDataId(chatMessageModel.getConversationId());

                    GroupChatInfo model = MyApplication.getChatDataBase().groupChatInfoDao().get(chatMessageModel.getConversationId());
                    UserInfo userModel = MyApplication.getChatDataBase().userInfoDao().get(chatMessageModel.getFromToUserId());

                    if (model != null) {
                        chatNotification.setObjectDataId(chatMessageModel.getConversationId());
                        chatNotification.setTitle(userModel.getFullName() + " @ " + model.getName());
                        chatNotification.setProfileImageURL(model.getProfileImage() == null ? null : model.getProfileImage().getThumbnailPath());
                       // generateChatNotification(context, chatNotification);
                    }
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void presenceAvailable(FullJid address, Presence availablePresence) {
        LogUtils.debug("presenceAvailable " + address + " available");
        EventBroadcastHelper.sendUserOnlineStatus(AppConstants.getUserIdFromJID(address), true);
    }

    @Override
    public void presenceUnavailable(FullJid address, Presence presence) {
        LogUtils.debug("presenceUnavailable " + address + " unavailable");
        EventBroadcastHelper.sendUserOnlineStatus(AppConstants.getUserIdFromJID(address), false);
    }

    @Override
    public void presenceError(Jid address, Presence errorPresence) {
        LogUtils.debug("presenceError " + address + " error");
    }

    @Override
    public void presenceSubscribed(BareJid address, Presence subscribedPresence) {
        LogUtils.debug("presenceSubscribed " + address + " subs");
    }

    @Override
    public void presenceUnsubscribed(BareJid address, Presence unsubscribedPresence) {
        LogUtils.debug("presenceUnsubscribed " + address + " Unsubs");
    }

    @Override
    public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        return SubscribeAnswer.Approve;
    }

    public boolean isUserOnline(EntityBareJid userJid) {
        if (roster.getPresence(userJid.asBareJid()) != null && roster.getPresence(userJid.asBareJid()).isAvailable()) {
            return true;
        }
        return false;
    }

    public interface OnDisconnectListeners {
        void disconnected();

        void error();
    }

    public void disconnect(final OnDisconnectListeners onComplete) {
        try {
            long time = System.currentTimeMillis();
//            sendPresence(new Presence(Presence.Type.unavailable));

//            if (connection.isConnected()) {
            connection.disconnect();
            LogUtils.newCheckerXMPP("ConnectionStatus: disconnect successful time : " + (System.currentTimeMillis() - time) / 1000 + "sec");
//            }

            if (onComplete != null) {
                onComplete.disconnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.newCheckerXMPP("ConnectionStatus: disconnect Exception " + e.getMessage());
            if (onComplete != null) {
                onComplete.error();
            }
        }
    }

    public interface OnComplete<T> {
        void onCompleted(T t);
    }

    public void isUserBlocked(final int userId, final OnComplete<Boolean> onComplete) {
        if (!NetworkUtil.isInternetAvailable) {
            NetworkUtil.handleNoInternet(context);
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            int count = 0;
            boolean isBlocked;

            @Override
            protected Void doInBackground(Void... voids) {
                if (isConnected()) {
                    try {
                        for (Jid blockedJid : blockingCommandManager.getBlockList()) {
                            try {
                                int id = Integer.parseInt(blockedJid.toString().split("@")[0]);
                                if (userId == id) {
                                    isBlocked = true;
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onComplete.onCompleted(isBlocked);
            }
        }.execute();
    }

    public void blockUser(final int userId) {

        if (!NetworkUtil.isInternetAvailable) {
            NetworkUtil.handleNoInternet(context);
            return;
        }
        UserInfo userInfo = MyApplication.getChatDataBase().userInfoDao().get(userId);
        if (userInfo != null) {
            userInfo.setBlocked(true);
            MyApplication.getChatDataBase().userInfoDao().update(userInfo);
        }

        blockUnblock(userId, true);

    }

    public void blockUnblock(final int userId, final boolean block) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                if (block) {
                    try {
                        EntityBareJid jid = JidCreate.entityBareFrom(AppConstants.getJID(String.valueOf(userId)));
                        List<Jid> list = new ArrayList<>(1);
                        list.add(jid);
                        blockingCommandManager.blockContacts(list);
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        EntityBareJid jid = JidCreate.entityBareFrom(AppConstants.getJID(String.valueOf(userId)));
                        List<Jid> list = new ArrayList<>(1);
                        list.add(jid);
                        blockingCommandManager.unblockContacts(list);
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    public void unblockUser(final int userId) {

        if (!NetworkUtil.isInternetAvailable) {
            NetworkUtil.handleNoInternet(context);
            return;
        }
        UserInfo userInfo = MyApplication.getChatDataBase().userInfoDao().get(userId);
        if (userInfo != null) {
            userInfo.setBlocked(false);
            MyApplication.getChatDataBase().userInfoDao().update(userInfo);
        }

        blockUnblock(userId, false);


    }

    @Override
    public void onJidsBlocked(List<Jid> blockedJids) {
        LogUtils.newMessagesXMPP("Blocked contacts: " + blockedJids);

        try {
            for (Jid blockedJid : blockedJids) {
                try {
                    final int id = Integer.parseInt(blockedJid.toString().split("@")[0]);
                    EventBroadcastHelper.sendUserBlockedUnblocked(true, id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJidsUnblocked(List<Jid> unblockedJids) {
        LogUtils.newMessagesXMPP("Blocked contacts: " + unblockedJids);

        try {
            for (Jid unblockedJid : unblockedJids) {
                try {
                    final int id = Integer.parseInt(unblockedJid.toString().split("@")[0]);
                    EventBroadcastHelper.sendUserBlockedUnblocked(false, id);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDisplayMarker(final ChatMessage chatMessageModel, final ConversationData conversationData) {

        if (chatMessageModel.getFromToUserId() == TempStorage.getUser().getId()) {
            return;
        }

        if (!chatMessageModel.getSubject().contains("chat")) {
            return;
        }

        try {
            final Message message = new Message();
            message.setType(null);

            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
                message.setTo(JidCreate.entityBareFrom(AppConstants.getJID(String.valueOf(chatMessageModel.getFromToUserId()))));
            } else if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT_GROUP)) {
                message.setTo(JidCreate.fullFrom(chatMessageModel.getConversationId() + "@conference." + AppConstants.XMPP.SERVICE_NAME + "/" + chatMessageModel.getFromToUserId()));
            }

            message.setFrom(JidCreate.entityBareFrom(AppConstants.getJID(String.valueOf(TempStorage.getUser().getId()))));

            // Add Seen extension
            message.addExtension(new DisplayedExtension(chatMessageModel.getMessageId()));

            // Add an store element extension, so server will save this messsage for receipt..
            message.addExtension(new ExtensionElement() {
                @Override
                public CharSequence toXML(String enclosingNamespace) {
                    XmlStringBuilder xml = new XmlStringBuilder(this);
                    xml.closeEmptyElement();
                    return xml;
                }

                @Override
                public String getNamespace() {
                    return "urn:xmpp:hints";
                }

                @Override
                public String getElementName() {
                    return "store";
                }
            });

            connection.addStanzaIdAcknowledgedListener(message.getStanzaId(), new StanzaListener() {
                @Override
                public void processStanza(Stanza packet) {
                    LogUtils.newMessagesXMPP("SendDisplayMarkerResponse: Acknowledged Stanza id " + packet.getStanzaId());
                    chatMessageModel.setStatus(AppConstants.Chat.STATUS_SEEN);

                    MyApplication.getChatDataBase().chatMessageDao().setStatus(chatMessageModel.getMessageId(), chatMessageModel.getStatus());

                    try {
                        if (chatMessageModel.getChatRoomType() == AppConstants.Chat.TYPE_GROUP_CHAT) {
                            if (conversationData != null) {

//                                DelayInformation delayInformation = packet.getExtension(DelayInformation.ELEMENT, DelayInformation.NAMESPACE);
//
//                                if (delayInformation != null) {
//                                    conversationData.chatConversation.setLastReadTimestamp(delayInformation.getStamp().getTime());
//                                } else {
//                                    conversationData.chatConversation.setLastReadTimestamp(System.currentTimeMillis());
//                                }

                                conversationData.chatConversation.setLastReadTimestamp(chatMessageModel.getTimestamp());

                                MyApplication.getChatDataBase().chatConversationDao().update(conversationData.chatConversation);

                            } else {
                                ConversationData conversationData = MyApplication.getChatDataBase().chatConversationDao().getChatConversationDialog(chatMessageModel.getConversationId(), chatMessageModel.getChatRoomType());

//                                DelayInformation delayInformation = packet.getExtension(DelayInformation.ELEMENT, DelayInformation.NAMESPACE);
//
//                                if (delayInformation != null) {
//                                    conversationData.chatConversation.setLastReadTimestamp(delayInformation.getStamp().getTime());
//                                } else {
//                                    conversationData.chatConversation.setLastReadTimestamp(System.currentTimeMillis());
//                                }

                                conversationData.chatConversation.setLastReadTimestamp(chatMessageModel.getTimestamp());

                                MyApplication.getChatDataBase().chatConversationDao().update(conversationData.chatConversation);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    AppSharedPreferences.getInstance(context).setLastReceivedDataTimeXMPP(time);
                }
            });
            connection.sendStanza(message);
            LogUtils.newMessagesXMPP("SendDisplayMarker: " + message.getStanzaId() + " " + chatMessageModel.getMessageType() + " " + chatMessageModel.getBody());
        } catch (Exception e) {
            chatMessageModel.setStatus(AppConstants.Chat.STATUS_SENT);
            e.printStackTrace();
            LogUtils.newMessagesXMPP("SendDisplayMarker: ERROR " + e.getMessage());
        }
    }

    private void setupCarbon() {

//        IQ setIQ = new IQ("") {
//

//            @Override
//            protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
//                xml.append("<enable xmlns='urn:xmpp:carbons:2'/>");
//                return xml;
//            }
//        };

//        IQ setIQ = new IQ() {
//            public String getChildElementXML() {
//                return "<" + "enable" + " xmlns='" + "urn:xmpp:carbons:2" + "'/>";
//            }
//        };


        try {
            IQ setIQ = new IQ("enable") {
                @Override
                protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
                    xml.append(" xmlns='urn:xmpp:carbons:2'>");
                    return xml;
                }
            };

            setIQ.setType(IQ.Type.set);
            setIQ.setFrom(JidCreate.bareFrom(AppConstants.getJID(String.valueOf(TempStorage.getUser().getId()))));

            connection.sendStanza(setIQ);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    int page = 0;
    long totalTime = 0;
    int failCount = 0;

    long time;

    private void hitApiForMessageBackup(long timestamp) {


    }

    private void hitApiForFirstTimeChatMessageHistory() {


    }


    private boolean shouldCheckMessage() {
        if (!AppSharedPreferences.getInstance(context).isLogin()) {
            isCheckingMessages = false;
            return true;
        }
        return false;
    }

    private void firstTimeBackUpDone() {

        isCheckingFirstTime = false;
        AppSharedPreferences.getInstance(context).setLoadingForFirstTime(false);
        EventBroadcastHelper.sendXMPPCheckingMessageCompleted();
        EventBroadcastHelper.sendMessageBackUpCompleted();
       // hitApiInit();
    }

    private Message getCorrectMessage(String messageText, Message message) {

        if (messageText.contains("urn:xmpp:mucsub:nodes:messages") || messageText.contains("urn:xmpp:carbons:2")) {

            messageText = messageText.substring(messageText.lastIndexOf("<message"), messageText.indexOf("</message>") + "</message>".length());

            try {
                return PacketParserUtils.parseStanza(messageText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        else if (messageText.contains("urn:xmpp:carbons:2")) {
//
//        }

        return message;
    }

    public void addToRoster(String userJid) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                BareJid userEntityBareJid = null;
                try {
                    userEntityBareJid = JidCreate.entityBareFrom(userJid);
                    if (roster.getEntry(userEntityBareJid) == null) {
                        roster.createEntry(userEntityBareJid, null, null);
                    }
                    if (!roster.getEntry(userEntityBareJid).canSeeHisPresence())
                        roster.sendSubscriptionRequest(userEntityBareJid);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    public void sendMessage(Message message, final ChatMessage chatMessageModel,
                            final ChattingHelper.ChattingListener listener) {
        try {

            final long time = System.currentTimeMillis();

            if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT)) {
                message.setType(Message.Type.chat);
            } else if (chatMessageModel.getType().equals(AppConstants.Chat.TYPE_CHAT_GROUP)) {
                message.setType(Message.Type.groupchat);
            }

//            message.addExtension(new ChatMarkersElements.MarkableExtension());

            LogUtils.newCheckerXMPP("SendMessage: Sending... Stanza id " + message.getStanzaId());

            if (isConnected()) {
                connection.addStanzaIdAcknowledgedListener(message.getStanzaId(), new StanzaListener() {
                    @Override
                    public void processStanza(Stanza packet) {
                        LogUtils.newCheckerXMPP("SendMessage: Acknowledged Stanza id " + packet.getStanzaId());

                        if (chatMessageModel.getStatus() == AppConstants.Chat.STATUS_PENDING ||
                                chatMessageModel.getStatus() == AppConstants.Chat.STATUS_FAILED) {

                            chatMessageModel.setStatus(AppConstants.Chat.STATUS_SENT);
                            listener.messageSentSuccess(chatMessageModel);
                            connection.removeStanzaIdAcknowledgedListener(packet.getStanzaId());

                            MyApplication.getChatDataBase().chatMessageDao().setStatus(chatMessageModel.getMessageId(), AppConstants.Chat.STATUS_SENT);
//                            MyApplication.getChatDataBase().chatMessageDao().update(chatMessageModel);
//                            dataBase.updateChatMessageStatus(chatMessageModel);
                        }

//                        AppSharedPreferences.getInstance(context).setLastReceivedDataTimeXMPP(time);

                    }
                });

                chatMessageModel.setStatus(AppConstants.Chat.STATUS_PENDING);
                connection.sendStanza(message);
            } else {
                LogUtils.newMessagesXMPP("SendMessage: Failed Stanza id " + message.getStanzaId() + " ConnectionStatus: connect " + connection.isConnected() + " auth " + connection.isAuthenticated());
                listener.messageSentFailure(chatMessageModel);
                connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.newMessagesXMPP("SendMessage: Failed Stanza id " + message.getStanzaId() + " ConnectionStatus: connect " + connection.isConnected() + " auth " + connection.isAuthenticated() + " Exception : " + e.getMessage());
            listener.messageSentFailure(chatMessageModel);
        }
    }

    private void onAuthenticationDoneSetup() {
        try {
            getMessages();

            LogUtils.newCheckerXMPP("onAuthenticationDoneSetup Called");

            blockingCommandManager = BlockingCommandManager.getInstanceFor(connection);
            blockingCommandManager.addJidsBlockedListener(this);
            blockingCommandManager.addJidsUnblockedListener(this);

            roster = Roster.getInstanceFor(connection);
            roster.addPresenceEventListener(this);
            roster.addSubscribeListener(this);

            EventBroadcastHelper.sendXMPPAuthenticated();

            PingManager pm = PingManager.getInstanceFor(connection);
            pm.setPingInterval(25);  // secs
            pm.pingMyServer(true);
            pm.registerPingFailedListener(new PingFailedListener() {

                @Override
                public void pingFailed() {
                    LogUtils.newCheckerXMPP("PingManager: Ping Failed");
                }
            });

            goOnline();

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.newCheckerXMPP("onAuthenticationDoneSetup Error : " + e.getMessage());
        }
    }

    public void goOnline() {
        LogUtils.newCheckerXMPP("goOnline Called");

        EventBroadcastHelper.goOnline();

        handlerBg.post(new Runnable() {
            @Override
            public void run() {

                if (MyApplication.isAppForeground) {
                    sendPresence(new Presence(Presence.Type.available));
                } else {
                    sendPresence(new Presence(Presence.Type.unavailable));
                }

                // Setup Carbon we need to setup after when we go online
                setupCarbon();
                // Send pending message
                sendPendingMessages();
            }
        });
    }

    public void getMessages() {

        if (isCheckingMessages) {
            return;
        }

        if (shouldCheckMessage()) return;

        isCheckingMessages = true;

        boolean isRequiredTimeOver = AppConstants.isRequiredTimeOverInBg(context);

        if (isRequiredTimeOver) {
            // Backup messages for API as it was more than 90 days (Server Constant)
            LogUtils.newCheckerXMPP("***MAM*** Getting Messages from API as app is in background for " + AppSharedPreferences.getInstance(context).getChatHistoryRequiredAfterDays() + " days");
            AppSharedPreferences.getInstance(context).setLoadingForFirstTime(true);
        }

        if (AppSharedPreferences.getInstance(context).isLoadingForFirstTime()) {
            EventBroadcastHelper.sendMessageBackUpStarted();
            isCheckingFirstTime = true;
            page = 0;

            DataBase.getInstance(context).clearDB();
            hitApiForFirstTimeChatMessageHistory();

            return;
        }

        hitApiForMessageBackup(AppSharedPreferences.getInstance(context).getLastReceivedDataTimeXMPP());
       // hitApiInit();
    }

    private void sendPendingMessages() {

        List<MessageData> pendingMessages = MyApplication.getChatDataBase().chatMessageDao().getPendingMessages();

        if (pendingMessages != null && !pendingMessages.isEmpty()) {
            handlerBg.post(new Runnable() {
                @Override
                public void run() {

                    final ChattingHelper.ChattingListener chattingListener = new ChattingHelper.ChattingListener() {
                        @Override
                        public void newMessage(MessageData message) {

                        }

                        @Override
                        public void messageDeliveryReceipt(ChatMessage message) {
                        }

                        @Override
                        public void messageSentSuccess(ChatMessage message) {
                            EventBroadcastHelper.sendMessageSuccessful(message);
                        }

                        @Override
                        public void messageSentFailure(ChatMessage message) {
                        }

                        @Override
                        public void typingStatus(boolean isTyping) {

                        }

                        @Override
                        public void chattingSetupError() {
                        }
                    };

                    for (MessageData messageData : pendingMessages) {
                        if (messageData.chatMessage != null) {

                            ChattingHelper chattingHelperForward = null;

                            if (messageData.chatMessage.getChatRoomType() == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                                chattingHelperForward = new ChattingHelper(context, messageData.chatMessage.getConversationId(), AppConstants.Chat.TYPE_SINGLE_CHAT);

                            } else if (messageData.chatMessage.getChatRoomType() == AppConstants.Chat.TYPE_GROUP_CHAT) {
                                chattingHelperForward = new ChattingHelper(context, messageData.chatMessage.getConversationId(), AppConstants.Chat.TYPE_GROUP_CHAT);
                            }

                            if (chattingHelperForward != null) {
                                chattingHelperForward.setup(chattingListener);

                                ChatMessage chatMessage = messageData.chatMessage;

                                if (chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {

                                    chattingHelperForward.sendMessage(chatMessage);
//                                    dataBase.insertMessage(chatMessage);

                                }
                            }

                            try {
                                Thread.sleep(75);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    EventBroadcastHelper.sendRefreshChatList();
                }
            });
        }
    }

    public void sendPresence(final Presence presence) {
        try {
            this.connection.sendStanza(presence);
            LogUtils.newCheckerXMPP("***PRESENCE*** sent " + presence.getType());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.newCheckerXMPP("***PRESENCE*** sent ERROR " + presence.getType() + " ERROR : " + e.getMessage());
        }
    }
}
