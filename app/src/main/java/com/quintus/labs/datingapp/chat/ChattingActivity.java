package com.quintus.labs.datingapp.chat;

import android.animation.Animator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.DialogUtils;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.chat.adapter.ChatMessageAdapter;
import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.helper.AudioRecorder;
import com.quintus.labs.datingapp.helper.ChatMediaUploaderHelper;
import com.quintus.labs.datingapp.helper.Utility;
import com.quintus.labs.datingapp.mediapicker.MediaItem;
import com.quintus.labs.datingapp.mediapicker.MediaOptions;
import com.quintus.labs.datingapp.mediapicker.activities.MediaPickerActivity;
import com.quintus.labs.datingapp.receivers.NetworkChangeReceiver;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.xmpp.ChattingHelper;
import com.quintus.labs.datingapp.xmpp.DataBase;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.ConversationData;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.AppSharedPreferences;
import com.quintus.labs.datingapp.xmpp.utils.BitmapUtils;
import com.quintus.labs.datingapp.xmpp.utils.ChatHeaderModel;
import com.quintus.labs.datingapp.xmpp.utils.ChatMedia;
import com.quintus.labs.datingapp.xmpp.utils.NetworkUtil;
import com.quintus.labs.datingapp.xmpp.utils.TimeUtils;
import com.quintus.labs.datingapp.xmpp.utils.UserModel;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.quintus.labs.datingapp.Utils.AppConstants.RequestCode.MEDIA_PICKER_REQUEST;
import static com.quintus.labs.datingapp.Utils.AppConstants.RequestCode.MY_PERMISSIONS_REQUEST;


public class ChattingActivity extends AppCompatActivity implements NetworkChangeReceiver.OnNetworkChangeListener, ChatMessageAdapter.OnItemClickMessageListener, ChatMessageAdapter.OnShowLastItemListener, ChattingHelper.ChattingListener, ChatMessageAdapter.OnItemClickHeadingListener, ChatMediaUploaderHelper.Callbacks, View.OnClickListener, AudioRecorder.Callbacks {

    private Activity activity;
    private Context context;
    public static boolean isChatForeground;
    public static int userId;
    public static int groupId;

    @BindView(R.id.recyclerViewMessages)
    public RecyclerView recyclerViewMessages;
    @BindView(R.id.autoEditTextMessages)
    public AutoCompleteTextView autoEditTextMessages;
    @BindView(R.id.imageViewImage)
    public ImageView imageViewImage;
    @BindView(R.id.imageViewSend)
    public ImageView imageViewSend;
    @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.layoutChatFooter)
    public View layoutChatFooter;
    @BindView(R.id.layoutBelowList)
    public View layoutBelowList;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.activity_chat_message)
    public View root;
    @BindView(R.id.layoutChatFooterBlocked)
    public View layoutChatFooterBlocked;
    @BindView(R.id.layoutXmppInfo)
    public View layoutXmppInfo;
    @BindView(R.id.textViewXmppInfo)
    public TextView textViewXmppInfo;
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;

    @BindView(R.id.textViewInActive)
    public TextView textViewInActive;
    @BindView(R.id.layoutChatInActive)
    public View layoutChatInActive;

    @BindView(R.id.layoutRelation)
    public View layoutRelation;
    @BindView(R.id.textViewRelation)
    public TextView textViewRelation;
    @BindView(R.id.textViewRelationBlock)
    public TextView textViewRelationBlock;
    @BindView(R.id.textViewRelationAccept)
    public TextView textViewRelationAccept;

    //////////////////////////////////////Chat Audio/////////////////////////////////////////////////////

    View imageViewAudio, imageViewLockArrow, imageViewLock, imageViewMic, dustin, dustin_cover;
    View layoutDustin;
    View layoutAudioLock;
    View layoutSlideCancel, layoutForAudioBg, layoutForAudioText;
    View layoutLock;

    @BindView(R.id.audioRecordingTime)
    TextView audioRecordingTime;
    @BindView(R.id.imageViewMicLock)
    ImageView imageViewMicLock;
    @BindView(R.id.audioRecordingLockTime)
    TextView audioRecordingLockTime;
    @BindView(R.id.layoutAudioPreview)
    View layoutAudioPreview;
    @BindView(R.id.imageViewAudioPlay)
    ImageView imageViewPreviewPlay;
    @BindView(R.id.seekBarAudio)
    SeekBar seekBarAudio;
    @BindView(R.id.textViewPreviewTime)
    TextView textViewPreviewTime;
    @BindView(R.id.imageViewPreviewSend)
    ImageView imageViewPreviewSend;

    Animation animBlink, animJump, animJumpFast;

    public boolean isDeleting;
    public boolean isLocked;
    public boolean isDeleted;
    public boolean stopTrackingAction;
    private AudioRecorder audioRecorder;

    float lastX = 0;
    float lastY = 0;
    float firstX = 0;
    float firstY = 0;

    float directionOffset = 0;
    float cancelOffset = 0;
    float lockOffset = 0;
    float dp = 0;

    boolean supportsVoiceMessages;

    //////////////////////////////////////////////////////////////////////////////////////////////

    public TextView textViewUserStatus;
    public TextView textViewUserName;
    public ImageView imageViewOptions;
    public ImageView imageViewUserImage;
    public ImageView imgageViewUserType;

    private List<Object> chatMessageFailedModels = new ArrayList<>();
    private ChatMessageAdapter chatMessageAdapter;
    private ChattingHelper chattingHelper;
    private UserData userDetailModel;

    private DataBase dataBase;
    private Gson gson;
    private Handler handler;
    private HashMap<MessageData, ChatMediaUploaderHelper> mediaUploaderHelperHashMap = new HashMap<>();
   // private TaggingHelper taggingHelper;

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private boolean mFirstTym = true;
    private LinearLayoutManager linearLayoutManager;
    private boolean isUserBlocked;
    private NetworkChangeReceiver mNetWorkChangeReciver;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private TextView textViewUserOnline;
    private boolean isUserTracking;
    private boolean isRecordingCompleted;

    private ConversationData conversationData;
    public static int chatType;

    private Handler handlerBg;
    private HandlerThread handlerThread;
    private Runnable receiptRunnable;
    private boolean hasSentMsgReceipt;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBlockedUnblockedUsers(Events.UserBlockUnblocked userBlockUnblocked) {
        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            if (userBlockUnblocked.getUserId() == userId) {
                userDetailModel.setBlocked(userBlockUnblocked.isBlocked());
                checkBlockedUser();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getGroupUpdate(final Events.ChatGroupUpdated groupUpdated) {

        if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT && conversationData.groupChatInfo.getId() == groupUpdated.getGroupInfo().getId()) {

            conversationData.groupChatInfo.setName(groupUpdated.getGroupInfo().getName());
            conversationData.groupChatInfo.setProfileImage(groupUpdated.getGroupInfo().getProfileImage());

            setUserInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendForwardMessage(final Events.ForwardMessageList forwardMessageList) {
        sendForwardMessage(forwardMessageList.getObjectReceiver(), forwardMessageList.getMessageList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void groupDeleted(final Events.ChatGroupDeleted chatGroupDeleted) {
        if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT && chatGroupDeleted.getGroupId() == conversationData.groupChatInfo.getId()) {
            conversationData.chatConversation.setGroupRole(AppConstants.Chat.SERVER_ROLE_NONE);
            checkGroupActive();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void groupRoleUpdated(final Events.ChatGroupRoleUpdated chatGroupRoleUpdated) {
        if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT && chatGroupRoleUpdated.getGroupId() == conversationData.groupChatInfo.getId()) {
            conversationData.chatConversation.setGroupRole(chatGroupRoleUpdated.getRole());
            checkGroupActive();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatMessageSuccess(final Events.ChatMessageSuccess chatMessageSuccess) {
        try {
            int index = chatMessageAdapter.getList().indexOf(new MessageData(chatMessageSuccess.getChatMessageModel()));
            MessageData messageData = chatMessageAdapter.getList()
                    .get(index);
            messageData.chatMessage.setStatus(AppConstants.Chat.STATUS_SENT);
            chatMessageAdapter.notifyItemChanged(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshChatList(final Events.RefreshChatList refreshChatList) {

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

            UserInfo userModel = MyApplication.getChatDataBase().userInfoDao().get(userId);

            if (userModel != null) {
                userDetailModel = new UserData(new UserModel(userModel));
                setUserInfo();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendMessageSuccessful(final Events.ActionModeDone actionModeDone) {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void sendUserOnlineStatus(final Events.UserOnline userStatus) {
        if (userStatus.getUserID() == userId ) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userStatus.isAvailable()) {
                        textViewUserOnline.setVisibility(View.VISIBLE);
                        textViewUserOnline.setText(activity.getResources().getString(R.string.chat_online));
                    } else {
                        textViewUserOnline.setVisibility(View.VISIBLE);
                        textViewUserOnline.setText(activity.getResources().getString(R.string.tap_here_group));
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getNewReceiptMessages(Events.NewReceiptChatMessage receiptChatMessage) {

        if (chatMessageAdapter == null) {
            return;
        }

        int i = chatMessageAdapter.getList().indexOf(new MessageData(receiptChatMessage.getChatMessageModel()));
        if (i != -1) {
            MessageData chatMessageModel = chatMessageAdapter.getList().get(i);
            chatMessageModel.chatMessage.setStatus(AppConstants.Chat.STATUS_SEEN);

            if (chatMessageAdapter.getLastSeenTime() < chatMessageModel.chatMessage.getTimestamp()) {
                chatMessageAdapter.setLastSeenTime(chatMessageModel.chatMessage.getTimestamp());
            }

            chatMessageModel.chatMessage.setReceipts(receiptChatMessage.getChatMessageModel().getReceipts());

            refreshList(true);

//            doSeenAnimation(i);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getUserTypingStatus(
            final Events.NewIncomingTypingMessage newIncomingTypingMessage) {
        LogUtils.newMessagesXMPP("user is typing a message : " + newIncomingTypingMessage.isTyping());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (newIncomingTypingMessage.isTyping()) {
                    textViewUserStatus.setVisibility(View.VISIBLE);
                    textViewUserStatus.setText(context.getString(R.string.typing) + "...");
                } else {
                    textViewUserStatus.setVisibility(View.GONE);
                    textViewUserStatus.setText("");
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void getNewIncomingMessages(Events.NewIncomingChatMessage newIncomingChatMessage) {

        LogUtils.debug("getNewIncomingMessages: " + newIncomingChatMessage.getChatMessageModel().getBody());
        MessageData message = MyApplication.getChatDataBase().chatMessageDao().getMessage(newIncomingChatMessage.getChatMessageModel().getMessageId());

        if (message != null) {
            chattingHelper.handleNewIncomingMessages(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(Events.Online event) {
        LogUtils.debug("SendDisplayMarker Online");

        if (hasSentMsgReceipt) {
            onAuthCheckReceipt();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getXMPPStatus(Events.XMPP xmpp) {

        LogUtils.debug("getXMPPStatus " + xmpp.callback.toString());

        if (layoutXmppInfo == null) {
            return;
        }

        switch (xmpp.callback) {
            case AUTHENTICATED:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutXmppInfo.setVisibility(View.GONE);
                    }
                });
                chattingHelper.setup(this);

                break;
            case CONNECTING:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutXmppInfo.setVisibility(View.VISIBLE);
                        if (NetworkUtil.isInternetAvailable) {
                            textViewXmppInfo.setText(getString(R.string.connecting));
                        } else
                            textViewXmppInfo.setText(getString(R.string.no_internet));

                        textViewXmppInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_blue));
                    }
                });
                break;
            case CHECKING_MESSAGES_STARED:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutXmppInfo.setVisibility(View.VISIBLE);

                        if (NetworkUtil.isInternetAvailable) {
                            textViewXmppInfo.setText(getString(R.string.updating_conversations));
                            textViewXmppInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_orange));
                        } else {
                            textViewXmppInfo.setText(getString(R.string.no_internet));
                            textViewXmppInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_blue));
                        }
                    }
                });
                break;
            case CHECKING_MESSAGES_COMPLETED:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutXmppInfo.setVisibility(View.GONE);
                    }
                });
                break;
            default:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutXmppInfo.setVisibility(View.GONE);
                    }
                });
                break;
        }
    }

    private void onAuthCheckReceipt() {

        if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            checkGroupPendingReceipts();
        } else if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

            int conId = 0;
            try {
                conId = conversationData.chatConversation.getConversationId();
            } catch (Exception e) {
                conId = userId;
                e.printStackTrace();
            }

            MessageData messageData = MyApplication.getChatDataBase()
                    .chatMessageDao().getLastPendingReadReceiptsForSingleConversation(conId);

            if (TempStorage.getXMPPHelper() != null && messageData != null) {

                if (messageData.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED) &&
                        chatType == AppConstants.Chat.TYPE_SINGLE_CHAT &&
                        messageData.chatMessage.getStatus() != AppConstants.Chat.STATUS_SEEN) {

                    TempStorage.getXMPPHelper().sendDisplayMarker(messageData.chatMessage, null);

                    if (chatMessageAdapter != null) {
                        chatMessageAdapter.setLastSeenTime(messageData.chatMessage.getTimestamp());
                    }
                }
            }
        }
    }

    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        activity = this;
        context = this;

        time = System.currentTimeMillis();

        GlobalBus.getBus().register(this);
        dataBase = DataBase.getInstance(context);

        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();

        handlerBg = new Handler(handlerThread.getLooper());
        handler = new Handler(Looper.getMainLooper());

        if (getIntent().hasExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT)) {

            chatType = AppConstants.Chat.TYPE_SINGLE_CHAT;
            userDetailModel = (UserData) getIntent().getSerializableExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT);
            userId = userDetailModel.getId();

            conversationData = MyApplication.getChatDataBase().chatConversationDao().getChatConversationDialog(userId, AppConstants.Chat.TYPE_SINGLE_CHAT);

        } else if (getIntent().hasExtra(AppConstants.DataKey.CHAT_MODEL_OBJECT)
                || getIntent().hasExtra(AppConstants.DataKey.CONVERSATION_TYPE)
                || getIntent().hasExtra(AppConstants.DataKey.CONVERSATION_ID)) {

            conversationData = (ConversationData) getIntent().getSerializableExtra(AppConstants.DataKey.CHAT_MODEL_OBJECT);

            if (conversationData == null) {
                conversationData = MyApplication.getChatDataBase().chatConversationDao().getChatConversationDialog(getIntent().getIntExtra(AppConstants.DataKey.CONVERSATION_ID, 0), getIntent().getIntExtra(AppConstants.DataKey.CONVERSATION_TYPE, 0));
                if (conversationData == null) {
                    finish();
                    return;
                }
            }

            chatType = conversationData.chatConversation.getConversationType();

            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

                if (conversationData.userInfo == null) {
                    conversationData.userInfo = new UserInfo();
                    conversationData.userInfo.setId(conversationData.chatConversation.getUserId());
                }

                userId = conversationData.userInfo.getId();

                userDetailModel = new UserData(new UserModel(conversationData.userInfo));
            } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                if (conversationData.groupChatInfo == null) {
                    finish();
                    return;
                }
                groupId = conversationData.groupChatInfo.getId();
            }
        } else {
            finish();
            return;
        }

        mNetWorkChangeReciver = new NetworkChangeReceiver();
        NetworkChangeReceiver.register(this, mNetWorkChangeReciver);

        gson = new Gson();

        ButterKnife.bind(activity);

        recyclerViewMessages.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(false);

        recyclerViewMessages.setLayoutManager(linearLayoutManager);

        getChatMessages(System.currentTimeMillis(), 50, 0);
        setUpTypingState();

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            chattingHelper = new ChattingHelper(context, userId, chatType);
            chattingHelper.setup(this);

            checkIsUserKnown();

        } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            chattingHelper = new ChattingHelper(context, conversationData.groupChatInfo.getId(), chatType);
            chattingHelper.setup(this);
        }

        textViewEmptyLayoutText.setText(getString(R.string.empty_chat_message));
        imageViewEmptyLayoutImage.setImageResource(R.drawable.empty_messages);

       // taggingHelper = new TaggingHelper(context, autoEditTextMessages);

        imageViewImage.setOnClickListener(this);

        if (!NetworkUtil.isInternetAvailable) {
            layoutXmppInfo.setVisibility(View.VISIBLE);
            textViewXmppInfo.setText(getString(R.string.no_internet));
            textViewXmppInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_blue));
        }else{
            layoutXmppInfo.setVisibility(View.GONE);

        }

        setCustomToolBar();
        actionModeCallback = new ActionModeCallback();

        imageViewAudio = findViewById(R.id.imageViewAudio);
        imageViewLock = findViewById(R.id.imageViewLock);
        imageViewLockArrow = findViewById(R.id.imageViewLockArrow);
        layoutDustin = findViewById(R.id.layoutDustin);
        layoutSlideCancel = findViewById(R.id.layoutSlideCancel);
        layoutLock = findViewById(R.id.layoutLock);
        imageViewMic = findViewById(R.id.imageViewMic);
        layoutAudioLock = findViewById(R.id.layoutAudioLock);
        dustin = findViewById(R.id.dustin);
        dustin_cover = findViewById(R.id.dustin_cover);
        layoutForAudioText = findViewById(R.id.layoutForAudioText);
        layoutForAudioBg = findViewById(R.id.layoutForAudioBg);

        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        animBlink = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        animJump = AnimationUtils.loadAnimation(this,
                R.anim.jump);
        animJumpFast = AnimationUtils.loadAnimation(this,
                R.anim.jump_fast);

        setupRecording();
        checkPrevStoredText();

        checkGroupPendingReceipts();

        LogUtils.debug("Chatting Activity load time " + (System.currentTimeMillis() ) + " millis");

        handlerBg.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (conversationData != null) {
                    conversationData.chatConversation.setUnreadCount(0);
                    MyApplication.getChatDataBase().chatConversationDao().update(conversationData.chatConversation);
                }
            }
        }, 1000);
    }

    private void checkIsUserKnown() {
       hitApiForUserRelation();

    }

    private void hitApiForUserRelation() {
          layoutRelation.setVisibility(View.VISIBLE);
          textViewRelation.setText(context.getString(R.string.relation_default));
          textViewRelationBlock.setOnClickListener(this);
          textViewRelationAccept.setOnClickListener(this);
    }

    private void checkGroupPendingReceipts() {
        if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
            if (receiptRunnable != null) {
                handlerBg.removeCallbacks(receiptRunnable);
            }

            receiptRunnable = new Runnable() {
                @Override
                public void run() {

                    List<MessageData> messagesWithPendingReadReceipts = MyApplication.getChatDataBase().chatMessageDao().getGroupMessagesWithPendingReadReceiptsTimestamp(conversationData.chatConversation.getConversationId(), conversationData.chatConversation.getLastReadTimestamp());

                    for (MessageData chatMessageModel : messagesWithPendingReadReceipts) {
                        if (TempStorage.getXMPPHelper() != null) {
                            TempStorage.getXMPPHelper().sendDisplayMarker(chatMessageModel.chatMessage, conversationData);
                            if (chatMessageAdapter != null) {
                                chatMessageAdapter.setLastSeenTime(chatMessageModel.chatMessage.getTimestamp());
                            }
                        }
                    }
                }
            };
            handlerBg.postDelayed(receiptRunnable, 1000);
            checkGroupActive();
        }
    }

    private void checkGroupActive() {
        if (conversationData.chatConversation.getGroupRole().equals(AppConstants.Chat.SERVER_ROLE_NONE)) {
            textViewInActive.setText(getString(R.string.cannot_reply));
            layoutChatInActive.setVisibility(View.VISIBLE);
            textViewUserOnline.setVisibility(View.GONE);
        } else {
            textViewUserOnline.setVisibility(View.VISIBLE);
            layoutChatInActive.setVisibility(View.GONE);
        }
    }

    private void checkPrevStoredText() {
        final String storedMessage = ChattingHelper.getStoredMessage(userId);
        autoEditTextMessages.setText(storedMessage);

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode_chating_screen, menu);
            // disable swipe refresh if action mode is enabled
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            List<ChatMessage> copyData = chatMessageAdapter.getCopyData();

            boolean haveMediaMessages = false;

            if (copyData != null) {

                for (ChatMessage model : copyData) {
                    if (!model.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                        haveMediaMessages = true;
                        break;
                    }
                }

            } else {
                menu.getItem(0).setVisible(true);
            }

            if (haveMediaMessages) {
                menu.getItem(0).setVisible(false);
            } else {
                menu.getItem(0).setVisible(true);
            }


                menu.getItem(1).setVisible(false);

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_forward:
                    forwardMessage();
                    return true;
                case R.id.action_message_info:
                    try {
                        ChatMessage chatMessageModel = chatMessageAdapter.getCopyData().get(0);
                        if (chatMessageModel != null) {
                           // GroupMessageInfoActivity.openActivity(context, chatMessageModel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    actionMode.finish();
                    return true;
                case R.id.action_message_copy:
                    try {
                        StringBuffer stringBuffer = new StringBuffer("");

                        if (chatMessageAdapter.getSelectedList().size() == 1) {

                            ChatMessage chatMessageModel = chatMessageAdapter.getSelectedList().get(0);

                            if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                                stringBuffer.append(chatMessageModel.getBody());
                            }

                        } else {
                            for (ChatMessage chatMessageModel : chatMessageAdapter.getSelectedList()) {

                                if (chatMessageModel.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {

                                    if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {

                                        if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                                            stringBuffer.append("[" + TimeUtils.getDateForwardTime(chatMessageModel.getTimestamp()) + "] " + TempStorage.getUser().getFullName() + " : ");
                                        } else {
                                            if (chatMessageModel.operator == null) {
                                                chatMessageModel.operator = MyApplication.getChatDataBase().userInfoDao().get(chatMessageModel.getFromToUserId());
                                            }
                                            stringBuffer.append("[" + TimeUtils.getDateForwardTime(chatMessageModel.getTimestamp()) + "] " + chatMessageModel.operator.getName() + " : ");
                                        }
                                        stringBuffer.append(chatMessageModel.getBody() + "\n");

                                    } else if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

                                        if (chatMessageModel.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
                                            stringBuffer.append("[" + TimeUtils.getDateForwardTime(chatMessageModel.getTimestamp()) + "] " + TempStorage.getUser().getFullName() + " : ");
                                        } else {
                                            if (chatMessageModel.operator == null) {
                                                chatMessageModel.operator = MyApplication.getChatDataBase().userInfoDao().get(chatMessageModel.getFromToUserId());
                                            }
                                            stringBuffer.append("[" + TimeUtils.getDateForwardTime(chatMessageModel.getTimestamp()) + "] " + userDetailModel.getFullName() + " : ");
                                        }
                                        stringBuffer.append(chatMessageModel.getBody() + "\n");

                                    }
                                }
                            }
                        }


                        if (!stringBuffer.toString().trim().isEmpty()) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied text messages from Huddle", stringBuffer.toString().trim());
                            clipboard.setPrimaryClip(clip);

                            ToastUtils.show(context, R.string.text_copied_to_clipboard);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    actionMode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            chatMessageAdapter.clearSelections();
            actionMode = null;
        }
    }

    private void forwardMessage() {
        ToastUtils.show(context, "Forword Message");

//        List<ChatMessage> checkedMessageList = chatMessageAdapter.getCopyData();
//        Intent in = new Intent(this, ChatForwardActivity.class);
//        in.putExtra("MessageList", (ArrayList<ChatMessage>) checkedMessageList);
//        startActivity(in);
    }

    private void deleteMessages() {
        List<Integer> selectedItemPositions =
                chatMessageAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            chatMessageAdapter.removeData(selectedItemPositions.get(i));
        }
        chatMessageAdapter.notifyDataSetChanged();
    }



    private void checkBlockedUser() {
        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

            try {
                isUserBlocked = userDetailModel.isBlocked();
                if (isUserBlocked) {
                    layoutChatFooterBlocked.setVisibility(View.VISIBLE);
                    layoutChatFooter.setVisibility(View.INVISIBLE);
                } else {
                    layoutChatFooter.setVisibility(View.VISIBLE);
                    layoutChatFooterBlocked.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            layoutChatFooter.setVisibility(View.VISIBLE);
            layoutChatFooterBlocked.setVisibility(View.GONE);
        }
    }

    private void addListenerToCalKeyboardHeight() {
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int lastHeight;

            public void onGlobalLayout() {
                Rect r = new Rect();
                root.getWindowVisibleDisplayFrame(r);

                if (lastHeight != 0 && lastHeight > root.getHeight()) {
                    scrollToBottom(false);
                }
                lastHeight = root.getHeight();
            }
        };

        root.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    @Override
    protected void onResume() {
        isChatForeground = true;
        super.onResume();

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
         //   FirebaseMessageClass.clearChatNotification(context, userId);

        }

        setUserInfo();
        checkBlockedUser();

//        checkLastMessageReceipt();

        if (chatMessageAdapter != null)
            chatMessageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addListenerToCalKeyboardHeight();
    }

    public void callFinish() {

        if (conversationData != null) {
            try {
                ConversationData chatConversationDialog = MyApplication.getChatDataBase().chatConversationDao().getChatConversationDialog(conversationData.chatConversation.getConversationId(), conversationData.chatConversation.getConversationType());
                chatConversationDialog.chatConversation.setUnreadCount(0);
                MyApplication.getChatDataBase().chatConversationDao().update(chatConversationDialog.chatConversation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        userId = 0;
        groupId = 0;
        finish();
    }

    @Override
    protected void onStop() {
        isChatForeground = false;
        super.onStop();

        try {
            root.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.isTaskRoot()) {
            startActivity(new Intent(this, MainActivity.class));
            callFinish();
        } else {
            callFinish();
        }
    }

    public void checkEmptyData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (chatMessageAdapter == null || (chatMessageAdapter.getList() != null && chatMessageAdapter.getList().isEmpty())) {
                    layoutNoData.setVisibility(View.VISIBLE);
                } else
                    layoutNoData.setVisibility(View.GONE);
            }
        });
    }

    private void setCustomToolBar() {

        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(context,
                        R.color.header_bg_color)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        View view = LayoutInflater.from(context).inflate(R.layout.tool_bar_chatting, null);

        getSupportActionBar().setCustomView(
                view,
                new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        textViewUserName = (TextView) view.findViewById(R.id.textViewUserName);
        textViewUserStatus = (TextView) view.findViewById(R.id.textViewUserStatus);
        textViewUserOnline = (TextView) view.findViewById(R.id.textViewUserOnline);
        imageViewUserImage = (ImageView) view.findViewById(R.id.imageViewUserImage);
        imageViewOptions = (ImageView) view.findViewById(R.id.imageViewOptions);
        imgageViewUserType = (ImageView) view.findViewById(R.id.img_usertype);
        view.findViewById(R.id.imageViewBack).setOnClickListener(this);
        textViewUserName.setOnClickListener(this);
        imageViewUserImage.setOnClickListener(this);
        imageViewOptions.setOnClickListener(this);
        imgageViewUserType.setOnClickListener(this);
        textViewUserOnline.setVisibility(View.VISIBLE);
        textViewUserOnline.setOnClickListener(this);
        //textViewUserOnline.setText(R.string.tap_here_group);

    }

    private void setUserInfo() {
        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

            if (!userDetailModel.isReceivePrivateMsg()) {
                imageViewSend.setEnabled(false);
                imageViewImage.setEnabled(false);
                imageViewAudio.setEnabled(false);

                imageViewSend.setAlpha(0.6f);
                imageViewAudio.setAlpha(0.6f);
                imageViewSend.setVisibility(View.INVISIBLE);

            } else {
                imageViewSend.setEnabled(true);
                imageViewImage.setEnabled(true);
                imageViewAudio.setEnabled(true);

                imageViewSend.setAlpha(1f);
                imageViewAudio.setAlpha(1f);
                imageViewSend.setVisibility(View.VISIBLE);
            }



            supportsVoiceMessages = true;

        }


            imgageViewUserType.setVisibility(View.GONE);


        String url = null;
        int errorImageRes = 0;

        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
            if (userDetailModel.getName() != null && !userDetailModel.getName().isEmpty()) {
                textViewUserName.setText(userDetailModel.getName());
            }

            if (userDetailModel.getProfileImage() != null) {
                url = userDetailModel.getProfileImage().getThumbnailPath();
            }

            errorImageRes = R.drawable.user_default_profile_pic;

        }

        if (!this.isFinishing()) {
            Glide.with(context).asBitmap().load(url).centerCrop().placeholder(errorImageRes).error(errorImageRes)
                    .into(new BitmapImageViewTarget(imageViewUserImage) {
                        @Override
                        protected void setResource(Bitmap resource) {

                            if (!ChattingActivity.this.isFinishing()) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageViewUserImage.setImageDrawable(circularBitmapDrawable);
                            }
                        }
                    });
        }
    }

    private boolean isTyping = false;
    private int typingDelay = 2 * 1000; // 2 sec

    private Handler typingHandler = new Handler();
    private Runnable typingRunnable = new Runnable() {
        @Override
        public void run() {
            isTyping = false;
            LogUtils.newMessagesXMPP("typing " + false);
            chattingHelper.typing(false);
        }
    };

    private void setUpTypingState() {
        autoEditTextMessages.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    if (charSequence.length() >= 1500 && mFirstTym) {
                        mFirstTym = false;
                    }
                    if (!isTyping) {
                        LogUtils.newMessagesXMPP("typing " + true);
                        chattingHelper.typing(true);
                    }

                    isTyping = true;
                    typingHandler.removeCallbacks(typingRunnable);
                    typingHandler.postDelayed(typingRunnable, typingDelay);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    imageViewAudio.setVisibility(View.VISIBLE);
                    imageViewSend.setVisibility(View.INVISIBLE);
                } else {
                    imageViewAudio.setVisibility(View.GONE);
                    imageViewSend.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == MEDIA_PICKER_REQUEST) {
            ArrayList<MediaItem> mediaItemSelected = MediaPickerActivity.getMediaItemSelected(data);
            if (mediaItemSelected != null) {
                for (final MediaItem mediaItem : mediaItemSelected) {
                    if (mediaItem.isPhoto()) {
                        String image = mediaItem.getPathOrigin(context);

                        if (AppConstants.checkIsFileValid(new File(image))) {
                        } else {
                            ToastUtils.show(context, context.getString(R.string.file_corrupted));
                            return;
                        }

                        ChatMessage chatMessageModel = chattingHelper.createMediaMessage(image, 0, AppConstants.Chat.TYPE_CHAT_IMAGE);

                        MessageData messageData = new MessageData(chatMessageModel);
                        ChatMediaUploaderHelper chatMediaUploaderHelper = new ChatMediaUploaderHelper(context, messageData, this);

                        mediaUploaderHelperHashMap.put(messageData, chatMediaUploaderHelper);
                        chatMediaUploaderHelper.start();

                        dataBase.insertMessage(chatMessageModel);
                        addNewMessage(messageData);
                    }

                }
                refreshListScrollBottom();
            }
        }
    }

    private void getChatMessages(final long timestamp, final int limit, final int position) {
        LogUtils.debug("Checking Chatting getChatMessages");

        handlerBg.post(new Runnable() {
            @Override
            public void run() {
                int id = 0;

                List<MessageData> models = null;

//                long timeTookQuery = System.currentTimeMillis();

                if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                    id = userId;
                    models = MyApplication.getChatDataBase().chatMessageDao().getMessagesSingleWithLimit(id, timestamp, limit);

                } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                    id = conversationData.groupChatInfo.getId();
                    models = MyApplication.getChatDataBase().chatMessageDao().getMessagesGroupWithLimit(id, timestamp, limit);
                } else {
                    models = new ArrayList<>();
                }

//                LogUtils.debug("inBackGroundThread models size : " + models.size() + " timeTookQuery " + (System.currentTimeMillis() - timeTookQuery));

                Collections.reverse(models);

                if (!models.isEmpty()) {
                    try {
                        if (chatMessageAdapter == null) {
                            chatMessageAdapter = new ChatMessageAdapter(context, activity, chatType, AppConstants.ShownIn.CHATTING_SCREEN, ChattingActivity.this, ChattingActivity.this, ChattingActivity.this);
                            chatMessageAdapter.setOpponentUser(userDetailModel);
                            chatMessageAdapter.setConversationData(conversationData);
                            chatMessageAdapter.setLastSeenTime(conversationData.chatConversation.getLastReadTimestamp());

//                            //***********************SEEN FUNCTIONALITY (send receipt)*****************
                            try {
                                MessageData messageData = models.get(models.size() - 1);

                                if (messageData.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED) &&
                                        chatType == AppConstants.Chat.TYPE_SINGLE_CHAT &&
                                        messageData.chatMessage.getStatus() != AppConstants.Chat.STATUS_SEEN) {
                                    TempStorage.getXMPPHelper().sendDisplayMarker(messageData.chatMessage, null);
                                    if (chatMessageAdapter != null) {
                                        chatMessageAdapter.setLastSeenTime(messageData.chatMessage.getTimestamp());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            //*************************************************************************

                            hasSentMsgReceipt = true;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerViewMessages.setAdapter(chatMessageAdapter);
                                }
                            });
                        }

                        final List<MessageData> finalModels = models;

                        if (position == 0) {
                            chatMessageAdapter.addPreviousChatItems(finalModels);
//                            linearLayoutManager.scrollToPositionWithOffset(0, 0);
                        } else {
                            recyclerViewMessages.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        chatMessageAdapter.addPreviousChatItems(finalModels);
                                        chatMessageAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    checkEmptyData();
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    sendPendingMessage(true);
                    LogUtils.debug("Checking Chatting getChatMessages inBackGroundThread");

                    if (limit > models.size()) {
                        if (chatMessageAdapter != null) {
                            chatMessageAdapter.setAllMessageSet(true);
                        }
                        return;
                    }
                } else {
                    if (chatMessageAdapter != null) {
                        chatMessageAdapter.setAllMessageSet(true);
                    }

                    recyclerViewMessages.post(new Runnable() {
                        @Override
                        public void run() {
                            checkEmptyData();
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onShowLastItem() {
        LogUtils.debug("Checking Chatting onShowLastItemChatList");
    }

    @Override
    public void onShowFirstItem(int position) {
        LogUtils.debug("Checking Chatting onShowFirstItem " + position);

        try {
            getChatMessages(chatMessageAdapter.getList().get(chatMessageAdapter.getList().size() - 1).chatMessage.getTimestamp(), 200, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String currentDate;

    private void sendPendingMessage(final boolean checkAll) {
        LogUtils.debug(">>>>>>>>sendPendingMessage..." + chatMessageFailedModels.size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (checkAll) {
                        if (chatMessageAdapter != null) {
                            for (Object chatMessageFailedModel : chatMessageAdapter.getList()) {
                                ChatMessage chatMessageFailed = null;
                                if (chatMessageFailedModel instanceof ChatMessage) {
                                    chatMessageFailed = (ChatMessage) chatMessageFailedModel;
                                } else if (chatMessageFailedModel instanceof MessageData) {
                                    chatMessageFailed = ((MessageData) chatMessageFailedModel).chatMessage;
                                }
                                if (chatMessageFailed != null) {
                                    if ((chatMessageFailed.getStatus() == AppConstants.Chat.STATUS_FAILED || chatMessageFailed.getStatus() == AppConstants.Chat.STATUS_PENDING)
                                            && chatMessageFailed.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                                        chattingHelper.sendMessage(chatMessageFailed);
                                    }
                                }
                            }
                        }
                    } else {
                        for (Object chatMessageFailedModel : chatMessageFailedModels) {
                            ChatMessage chatMessageFailed = null;
                            if (chatMessageFailedModel instanceof ChatMessage) {
                                chatMessageFailed = (ChatMessage) chatMessageFailedModel;
                            } else if (chatMessageFailedModel instanceof MessageData) {
                                chatMessageFailed = ((MessageData) chatMessageFailedModel).chatMessage;
                            }
                            if (chatMessageFailed != null) {
                                if ((chatMessageFailed.getStatus() == AppConstants.Chat.STATUS_FAILED || chatMessageFailed.getStatus() == AppConstants.Chat.STATUS_PENDING)
                                        && chatMessageFailed.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                                    chattingHelper.sendMessage(chatMessageFailed);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void refreshList(boolean isRefresh) {
        if (isRefresh) {
            recyclerViewMessages.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        chatMessageAdapter.notifyDataSetChanged();
                        checkEmptyData();
                        LogUtils.debug("newMessageTesting.. refreshList " + chatMessageAdapter.getLastSeenTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void refreshListScrollBottom() {
        recyclerViewMessages.post(new Runnable() {
            @Override
            public void run() {
                try {
                    chatMessageAdapter.notifyDataSetChanged();
                    scrollToBottom(true);
                    checkEmptyData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {


            NetworkChangeReceiver.unregister(mNetWorkChangeReciver, this);
            GlobalBus.getBus().unregister(this);

            LogUtils.debug("sendcustomnotification: on destroy user id  " + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void openActivity(Context context, UserData userDetailModel) {
        context.startActivity(new Intent(context, ChattingActivity.class).putExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT, userDetailModel));
    }

//    public static Intent createIntent(Context context, ChatModel groupChatModel) {
//        return new Intent(context, ChattingActivity.class).putExtra(AppConstants.DataKey.CHAT_MODEL_OBJECT, groupChatModel);
//    }
//
//    public static Intent createIntent(Context context, UserDetailModel userDetailModel) {
//        return new Intent(context, ChattingActivity.class).putExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT, userDetailModel).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    }

    public void send(View view) {
        String message = autoEditTextMessages.getText().toString();
        if (!message.isEmpty()) {
            //Send typing done...
            if (isTyping) {
                chattingHelper.typing(false);
                isTyping = false;
            }
            //Send message...
            ChatMessage chatMessageModel = chattingHelper.sendMessage(message);

            dataBase.insertMessage(chatMessageModel);
            addNewMessage(new MessageData(chatMessageModel));
            autoEditTextMessages.setText("");
        }
    }

    public synchronized void sendForwardMessage(Object objectReceiver, final List<ChatMessage> forwardMessageList) {

        ChattingHelper chattingHelperForward = null;

        if (objectReceiver instanceof UserInfo) {
            UserInfo userModel = (UserInfo) objectReceiver;
            chattingHelperForward = new ChattingHelper(context, userModel.getId(), AppConstants.Chat.TYPE_SINGLE_CHAT);

        } else if (objectReceiver instanceof ConversationData) {
            ConversationData chatModel = (ConversationData) objectReceiver;

            if (chatModel.chatConversation.getConversationType() == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                chattingHelperForward = new ChattingHelper(context, chatModel.chatConversation.getConversationId(), AppConstants.Chat.TYPE_SINGLE_CHAT);

            } else if (chatModel.chatConversation.getConversationType() == AppConstants.Chat.TYPE_GROUP_CHAT) {
                chattingHelperForward = new ChattingHelper(context, chatModel.chatConversation.getConversationId(), AppConstants.Chat.TYPE_GROUP_CHAT);
            }
        }

        if (chattingHelperForward == null) {
            return;
        }

        chattingHelperForward.setup(new ChattingHelper.ChattingListener() {
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
        });

        for (int i = 0; i < forwardMessageList.size(); i++) {
            ChatMessage message = forwardMessageList.get(i);
            ChatMessage chatMessageModel = new ChatMessage();

            if (message.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
                chatMessageModel = chattingHelperForward.createMediaMessage("", 0, AppConstants.Chat.TYPE_CHAT_IMAGE);
                chatMessageModel.setBody(message.getBody());
                chattingHelperForward.sendMediaMessage(chatMessageModel);
                dataBase.insertMessage(chatMessageModel);

            } else if (message.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {
                chatMessageModel = chattingHelperForward.createMediaMessage("", 0, AppConstants.Chat.TYPE_CHAT_AUDIO);
                chatMessageModel.setBody(message.getBody());
                chattingHelperForward.sendMediaMessage(chatMessageModel);
                dataBase.insertMessage(chatMessageModel);

            } else if (message.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                //Send message...
                chatMessageModel = chattingHelperForward.sendMessage(message.getBody());
                dataBase.insertMessage(chatMessageModel);
            }

            if (chatType == chatMessageModel.getChatRoomType()) {
                if ((chatMessageModel.getChatRoomType() == AppConstants.Chat.TYPE_SINGLE_CHAT && userId == chatMessageModel.getConversationId()) ||
                        (chatMessageModel.getChatRoomType() == AppConstants.Chat.TYPE_GROUP_CHAT && groupId == chatMessageModel.getConversationId())) {
                    addNewMessage(new MessageData(chatMessageModel));
                }
            }
        }
    }

    private boolean isUserFirstMessage;

    private void addNewMessage(final MessageData chatMessageModel) {

        recyclerViewMessages.post(new Runnable() {
            @Override
            public void run() {
                if (chatMessageAdapter == null) {
                    chatMessageAdapter = new ChatMessageAdapter(context, activity, chatType, AppConstants.ShownIn.CHATTING_SCREEN, ChattingActivity.this, ChattingActivity.this, ChattingActivity.this);
                    chatMessageAdapter.setOpponentUser(userDetailModel);
//                    chatMessageAdapter.setLastSeenMessage(chatMessageModel);

                    try {
                        chatMessageAdapter.setLastSeenTime(conversationData.chatConversation.getLastReadTimestamp());
                    } catch (Exception e) {
                        chatMessageAdapter.setLastSeenTime(0);
                        if (conversationData == null && chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                            conversationData = MyApplication.getChatDataBase().chatConversationDao().getChatConversationDialog(userId, AppConstants.Chat.TYPE_SINGLE_CHAT);
                        }
                        e.printStackTrace();
                    }


                    recyclerViewMessages.setAdapter(chatMessageAdapter);

                    //****************************Run Animation**************************
                }

                chatMessageAdapter.addNewMessage(chatMessageModel);
//                doSeenAnimation(chatMessageModels.indexOf(chatMessageModel));

                recyclerViewMessages.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            chatMessageAdapter.notifyDataSetChanged();
                            linearLayoutManager.scrollToPositionWithOffset(0, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(View viewRoot, View view, MessageData model, int position) {
        if (chatMessageAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        }
    }

    @Override
    public void onItemLongClick(View viewRoot, View view, MessageData model, int position) {
        switch (view.getId()) {
            case R.id.textViewMessage:
            case R.id.msg_layout:
            case R.id.imageChatImage:
                //showPopUpWindowCopy(view, model);
                enableActionMode(position);
                break;
        }
    }

    @Override
    public void newMessage(MessageData chatMessageModel) {

        LogUtils.debug("newMessageTesting..");

        if (chatMessageModel.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT) && layoutRelation.getVisibility() == View.VISIBLE) {
            AppSharedPreferences.getInstance(context).saveChatInteracted(userId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layoutRelation.setVisibility(View.GONE);
                }
            });
        }

        // No need to send if message is from me, it could be carbon message..
        if (chatMessageModel.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED)) {
            if (chatMessageAdapter != null) {
//                chatMessageAdapter.setLastSeenMessage(chatMes sageModel);
                chatMessageAdapter.setLastSeenTime(chatMessageModel.chatMessage.getTimestamp());
            }

            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT && chatMessageModel.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED)) {
                if (conversationData.chatConversation != null) {
                    if (conversationData.chatConversation.getLastReadTimestamp() < chatMessageModel.chatMessage.getTimestamp()) {
                        LogUtils.debug("newMessageTesting.. time changed " + chatMessageModel.chatMessage.getTimestamp());
                        conversationData.chatConversation.setLastReadTimestamp(chatMessageModel.chatMessage.getTimestamp());
                    }
                }
            }

            if (isChatForeground) {
                TempStorage.getXMPPHelper().sendDisplayMarker(chatMessageModel.chatMessage, null);
                if (chatMessageAdapter != null) {
                    chatMessageAdapter.setLastSeenTime(chatMessageModel.chatMessage.getTimestamp());
                }
            }
        }
        addNewMessage(chatMessageModel);
    }

    private void scrollToBottom(final boolean smooth) {
        recyclerViewMessages.post(new Runnable() {
            @Override
            public void run() {
                try {
//                    recyclerViewMessages.scrollToPosition(chatMessageAdapter.getItemCount() - 1);
                    linearLayoutManager.scrollToPositionWithOffset(0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void scrollTo(final int position, final boolean smooth) {
        recyclerViewMessages.post(new Runnable() {
            @Override
            public void run() {
                try {
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void messageDeliveryReceipt(final ChatMessage message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                message.setStatus(AppConstants.Chat.STATUS_DELIVERED);
                refreshList(true);
            }
        });
    }

    @Override
    public void messageSentSuccess(final ChatMessage message) {

        chatMessageFailedModels.remove(message);
        handler.post(new Runnable() {
            @Override
            public void run() {

                AppSharedPreferences.getInstance(context).saveChatInteracted(userId);
                layoutRelation.setVisibility(View.GONE);



                message.setStatus(AppConstants.Chat.STATUS_SENT);
                refreshList(true);

                int mTextChatCount = 0;

                String attachementtype = "";

                if (message.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                    mTextChatCount = message.getBody().length();
                } else {
                    attachementtype = context.getString(R.string.mixpannal_image);
                }


            }
        });
    }

    @Override
    public void messageSentFailure(final ChatMessage message) {
        LogUtils.debug("messageSentFailure");
        chatMessageFailedModels.add(message);
        handler.post(new Runnable() {
            @Override
            public void run() {
                message.setStatus(AppConstants.Chat.STATUS_FAILED);
                refreshList(true);
                MyApplication.getChatDataBase().chatMessageDao().update(message);
            }
        });
    }

    @Override
    public void typingStatus(boolean isTyping) {
        LogUtils.debug("Chatting user typing : " + isTyping);
    }

    @Override
    public void chattingSetupError() {
    }

    @Override
    public void onItemClick(View viewRoot, View view, ChatHeaderModel model, int position) {
    }

    @Override
    public void onItemLongClick(View viewRoot, View view, ChatHeaderModel model, int position) {

    }

    @Override
    public void onItemClick(View viewRoot, View view, ChatMessageAdapter.ChatItemLoadMore model,
                            int position) {
//        try {
//            MessageData chatMessageModel = chatMessageAdapter.getMessageList().get(0);
//            getChatMessages(chatMessageModel.id, chatMessageModel);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        chatMessageAdapter.toggleSelectionMessage(position);
        int count = chatMessageAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(NumberFormat.getInstance(Locale.getDefault()).format(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void refreshList(int position) {
        refreshList(true);
    }


    @Override
    public void onChatMediaUploadingStart(MessageData chatMessageModel) {
        ChatMedia chatMedia = gson.fromJson(chatMessageModel.chatMessage.getBody(), ChatMedia.class);
        chatMedia.setUploading(true);
//        dataBase.updateChatImageMessage(chatMessageModel);
        MyApplication.getChatDataBase().chatMessageDao().update(chatMessageModel.chatMessage);
        chatMessageModel.chatMessage.setBody(gson.toJson(chatMedia));
        refreshList(true);
    }

    @Override
    public void onChatMediaUploadingFailure(MessageData chatMessageModel, String message) {
        ChatMedia chatMedia = gson.fromJson(chatMessageModel.chatMessage.getBody(), ChatMedia.class);
        chatMedia.setUploading(false);
        chatMessageModel.chatMessage.setBody(gson.toJson(chatMedia));
        chatMessageModel.chatMessage.setStatus(AppConstants.Chat.STATUS_FAILED);
//        dataBase.updateChatImageMessage(chatMessageModel);
        MyApplication.getChatDataBase().chatMessageDao().update(chatMessageModel.chatMessage);
        refreshList(true);
    }

    @Override
    public void onChatMediaUploadingSuccess(MessageData chatMessageModel, ChatMedia
            chatMedia) {
        chatMedia.setUploading(false);
        chatMessageModel.chatMessage.setBody(gson.toJson(chatMedia));
        chatMessageModel.chatMessage.setStatus(AppConstants.Chat.STATUS_PENDING);
//        dataBase.updateChatImageMessage(chatMessageModel);

        MyApplication.getChatDataBase().chatMessageDao().update(chatMessageModel.chatMessage);
        chattingHelper.sendMediaMessage(chatMessageModel.chatMessage);
    }

    @Override
    public void onChatMediaUploadingProgress(MessageData chatMessageModel, int progress) {
    }

    @Override
    public void onChatMediaUploadingCancelled(MessageData chatMessageModel) {
        ChatMedia chatMedia = gson.fromJson(chatMessageModel.chatMessage.getBody(), ChatMedia.class);
        chatMedia.setUploading(false);
        chatMessageModel.chatMessage.setBody(gson.toJson(chatMedia));
        chatMessageModel.chatMessage.setStatus(AppConstants.Chat.STATUS_PENDING);
        refreshList(true);
//        dataBase.updateChatImageMessage(chatMessageModel);
        MyApplication.getChatDataBase().chatMessageDao().update(chatMessageModel.chatMessage);
    }

    @Override
    public void chatMediaOptionsCancel(View viewRoot, View view, MessageData chatMessageModel,
                                       int position) {
        ChatMediaUploaderHelper chatMediaUploaderHelper = mediaUploaderHelperHashMap.get(chatMessageModel);
        if (chatMediaUploaderHelper != null) {
            chatMediaUploaderHelper.cancel();
        } else {
            ChatMedia chatMedia = gson.fromJson(chatMessageModel.chatMessage.getBody(), ChatMedia.class);
            chatMedia.setDownloading(false);
            chatMedia.setUploading(false);
            chatMessageModel.chatMessage.setBody(gson.toJson(chatMedia));
            refreshList(true);
//            dataBase.updateChatImageMessage(model);
            MyApplication.getChatDataBase().chatMessageDao().update(chatMessageModel.chatMessage);
        }
    }

    @Override
    public void chatMediaOptionsSend(View viewRoot, View view, MessageData model,
                                     int position) {
        model.chatMessage.setStatus(AppConstants.Chat.STATUS_PENDING);
        refreshList(true);
        ChatMedia chatMedia = gson.fromJson(model.chatMessage.getBody(), ChatMedia.class);
        if (chatMedia.getS3MediaUrl() == null) {
            ChatMediaUploaderHelper chatMediaUploaderHelper = new ChatMediaUploaderHelper(context, model, this);
            mediaUploaderHelperHashMap.put(model, chatMediaUploaderHelper);
            chatMediaUploaderHelper.start();
        } else {
            onChatMediaUploadingSuccess(model, chatMedia);
        }
    }

    @Override
    public void chatMediaOptionsError(View viewRoot, View view, MessageData model,
                                      int position) {
    }

    @Override
    public void chatMediaOptionsOpen(View viewRoot, View view, MessageData model,
                                     int position) {
   ToastUtils.show(context," chatMediaOptionsOpen");
    }

    private boolean checkPermissions() {
        ArrayList<String> arrPerm = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);

            ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void chatMediaOptionsDownload(View viewRoot, final View view,
                                         final MessageData model, final int position) {

        if (!checkPermissions() || !NetworkUtil.isInternetAvailable) {
//            ToastUtils.show(context, "The download was unable to complete please try again later.");
            return;
        }

        final ChatMedia chatMedia = gson.fromJson(model.chatMessage.getBody(), ChatMedia.class);
        final ImageView imageView = (ImageView) view;
        chatMedia.setDownloading(true);
        model.chatMessage.setBody(gson.toJson(chatMedia));
        refreshList(true);

        Call<ResponseBody> responseBodyCall = BitmapUtils.saveChatMedia(chatMedia.getS3MediaUrl(), model.chatMessage, chatMedia, new BitmapUtils.Callbacks<String>() {
            @Override
            public void onComplete(String path) {
                if (path != null) {
                    chatMedia.setStoragePath(path);
                    chatMedia.setDownloading(false);
                    model.chatMessage.setBody(gson.toJson(chatMedia));
                    refreshList(true);
//                    dataBase.updateChatImageMessage(model);
                    MyApplication.getChatDataBase().chatMessageDao().update(model.chatMessage);
                } else {
                    chatMedia.setStoragePath(null);
                    chatMedia.setDownloading(false);
                    model.chatMessage.setBody(gson.toJson(chatMedia));
                    refreshList(true);
//                    dataBase.updateChatImageMessage(model);
                    MyApplication.getChatDataBase().chatMessageDao().update(model.chatMessage);
                }
            }
        });
    }

    @Override
    public void performSeenAnimation(final int previouslySeenPosition,
                                     final int currentlySeePosition) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewUserOnline:
            case R.id.textViewUserName:
            case R.id.imageViewUserImage:
                if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                        if (userDetailModel.isBlocked()) {
                        ToastUtils.show(context, getString(R.string.dialog_unblock_Open_chat_message));
                    } else {
                     //   ChatUserCreateGroupActivity.openActivity(context, userDetailModel.getUserModel());
                    }
                } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                    if (!conversationData.chatConversation.getGroupRole().equals(AppConstants.Chat.SERVER_ROLE_NONE)) {
                     //   GroupInfoUpdate.openActivity(context, conversationData);
                    }
                }
                break;
            case R.id.imageViewImage:
                MediaOptions options = new MediaOptions.Builder()
                        .selectPhoto()
                        .setMaxImageSelectionLimit(AppConstants.Limits.POST_IMAGE_UPLOAD_CHAT_LIMIT)
                        .canSelectMultiPhoto(true)
                        .canCaptureMedia(true)
                        .setMediaListSelected(null).build();
                MediaPickerActivity.open(activity, MEDIA_PICKER_REQUEST, options);
                break;
            case R.id.imageViewBack:
                onBackPressed();
                break;
            case R.id.imageViewOptions:
                break;
            case R.id.textViewRelationAccept:
                AppSharedPreferences.getInstance(context).saveChatInteracted(userId);
                layoutRelation.setVisibility(View.GONE);
                break;
            case R.id.textViewRelationBlock:
                AppSharedPreferences.getInstance(context).saveChatInteracted(userId);
                layoutRelation.setVisibility(View.GONE);

                try {
                    TempStorage.getXMPPHelper().blockUser(userDetailModel.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.img_usertype:
//                Intent in = new Intent(context, UpgradeAcountActivity.class);
//                in.putExtra("screen", 5);
//                context.startActivity(in);
                break;
        }
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        if (!NetworkUtil.isInternetAvailable) {
            layoutXmppInfo.setVisibility(View.VISIBLE);
            textViewXmppInfo.setText(getString(R.string.no_internet));
            textViewXmppInfo.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_blue));
        } else {
            layoutXmppInfo.setVisibility(View.GONE);
        }
    }

    private void dialogUnblockUserFirst() {
        DialogUtils.showBasic(context, String.format(context.getString(R.string.dialog_unblock_first_message), userDetailModel.getFullName()), context.getString(R.string.unblock), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                unblockUser();
            }
        });
    }

    private void unblockUser() {
        try {
            TempStorage.getXMPPHelper().unblockUser(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unblockUser(View view) {
        dialogUnblockUserFirst();
    }

    private void doSeenAnimation(int position) {
    }

    ///////////////////////////////Chat Audio///////////////////////////////////////////
    public enum RecordingBehaviour {
        CANCELING,
        LOCKING,
        NONE
    }

    public enum RecordingStatus {
        PREVIEW_IF_VALID,
        SEND,
        LOCKED,
        CANCELED,
        COMPLETED
    }

    public RecordingBehaviour recordingBehaviour = RecordingBehaviour.NONE;

    private void setupRecording() {

        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserTracking = false;

                if (audioRecorder != null) {
                    audioRecorder.seekTo(seekBar.getProgress());
                }
            }
        });

        imageViewAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (isDeleting) {
                    stopRecording(RecordingStatus.COMPLETED);
                    return true;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    isLocked = false;
                    isDeleted = false;

                    cancelOffset = (float) (imageViewAudio.getX() / 2.8);
                    lockOffset = (float) (imageViewAudio.getX() / 2.5);

                    if (firstX == 0) {
                        firstX = motionEvent.getRawX();
                    }

                    if (firstY == 0) {
                        firstY = motionEvent.getRawY();
                    }

                    startRecord();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP && !isLocked)
                        stopRecording(RecordingStatus.PREVIEW_IF_VALID);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

                    if (stopTrackingAction) {
                        return true;
                    }

                    RecordingBehaviour direction = RecordingBehaviour.NONE;

                    float motionX = Math.abs(firstX - motionEvent.getRawX());
                    float motionY = Math.abs(firstY - motionEvent.getRawY());

                    if (motionX > directionOffset &&
                            motionX > directionOffset &&
                            lastX < firstX && lastY < firstY) {

                        if (motionX > motionY && lastX < firstX) {
                            direction = RecordingBehaviour.CANCELING;

                        } else if (motionY > motionX && lastY < firstY) {
                            direction = RecordingBehaviour.LOCKING;
                        }

                    } else if (motionX > motionY && motionX > directionOffset && lastX < firstX) {
                        direction = RecordingBehaviour.CANCELING;
                    } else if (motionY > motionX && motionY > directionOffset && lastY < firstY) {
                        direction = RecordingBehaviour.LOCKING;
                    }

                    if (direction == RecordingBehaviour.CANCELING) {
                        if (recordingBehaviour == RecordingBehaviour.NONE || motionEvent.getRawY() + imageViewAudio.getWidth() / 2 > firstY) {
                            recordingBehaviour = RecordingBehaviour.CANCELING;
                        }

                        if (recordingBehaviour == RecordingBehaviour.CANCELING) {
                            translateX(-(firstX - motionEvent.getRawX()));
                        }
                    } else if (direction == RecordingBehaviour.LOCKING) {
                        if (recordingBehaviour == RecordingBehaviour.NONE || motionEvent.getRawX() + imageViewAudio.getWidth() / 2 > firstX) {
                            recordingBehaviour = RecordingBehaviour.LOCKING;
                        }

                        if (recordingBehaviour == RecordingBehaviour.LOCKING) {
                            translateY(-(firstY - motionEvent.getRawY()));
                        }
                    }

                    lastX = motionEvent.getRawX();
                    lastY = motionEvent.getRawY();
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    private void translateY(float y) {
        if (y < -lockOffset) {
            locked();
            imageViewAudio.setTranslationY(0);
            return;
        }

        if (layoutLock.getVisibility() != View.VISIBLE) {
            layoutLock.setVisibility(View.VISIBLE);
        }

        imageViewAudio.setTranslationY(y);
        layoutLock.setTranslationY(y / 2);
        imageViewAudio.setTranslationX(0);
    }

    private void translateX(float x) {
        if (x < -cancelOffset) {
            canceled();
            imageViewAudio.setTranslationX(0);
            layoutSlideCancel.setTranslationX(0);
            return;
        }

        imageViewAudio.setTranslationX(x);
        layoutSlideCancel.setTranslationX(x);
        layoutLock.setTranslationY(0);
        imageViewAudio.setTranslationY(0);

        if (Math.abs(x) < imageViewMic.getWidth() / 2) {
            if (layoutLock.getVisibility() != View.VISIBLE) {
                layoutLock.setVisibility(View.VISIBLE);
            }
        } else {
            if (layoutLock.getVisibility() != View.GONE) {
                layoutLock.setVisibility(View.GONE);
            }
        }
    }

    private void locked() {
        stopTrackingAction = true;
        isLocked = true;
        stopRecording(RecordingStatus.LOCKED);
    }

    private void canceled() {
        stopTrackingAction = true;
        isDeleted = true;
        stopRecording(RecordingStatus.CANCELED);
    }

    private void stopRecording(RecordingStatus recordingStatus) {
        stopTrackingAction = true;
        firstX = 0;
        firstY = 0;
        lastX = 0;
        lastY = 0;

        recordingBehaviour = RecordingBehaviour.NONE;
        imageViewAudio.animate().scaleX(1f).scaleY(1f).translationX(0).translationY(0).setDuration(100).setInterpolator(new LinearInterpolator()).start();
        layoutSlideCancel.setTranslationX(0);
        audioRecordingTime.clearAnimation();
        audioRecordingTime.setVisibility(View.INVISIBLE);
        layoutSlideCancel.setVisibility(View.GONE);
        layoutLock.setVisibility(View.GONE);
        layoutForAudioText.setVisibility(View.GONE);
        layoutLock.setTranslationY(0);

        imageViewLockArrow.clearAnimation();
        imageViewLock.clearAnimation();

        imageViewMic.clearAnimation();

        if (recordingStatus == RecordingStatus.CANCELED) {
            imageViewMicLock.clearAnimation();
            audioRecorder.stopRecording();
            audioRecorder.release();
            performDeleteAnim();
        } else if (recordingStatus == RecordingStatus.LOCKED) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            layoutAudioLock.setVisibility(View.VISIBLE);
            layoutChatFooter.setVisibility(View.INVISIBLE);
            imageViewMic.setVisibility(View.GONE);
            layoutForAudioBg.setVisibility(View.GONE);
        } else if (recordingStatus == RecordingStatus.COMPLETED) {
            layoutForAudioBg.setVisibility(View.GONE);
            imageViewMic.setVisibility(View.GONE);
        } else {
            audioRecorder.stopRecording();
            audioRecorder.release();
            imageViewMicLock.clearAnimation();
            layoutForAudioBg.setVisibility(View.GONE);
            imageViewMic.setVisibility(View.GONE);
        }
    }

    private Handler handlerAudioStart;
    private Runnable runnableAudioStart;

    private void startRecord() {
        if (!supportsVoiceMessages) {
            String name = null;

            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                name = userDetailModel.getFullName();
            } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                name = conversationData.groupChatInfo.getName();
            }

            DialogUtils.getBasicMessage(context, String.format(getString(R.string.need_audio_upgrade), name)).show();
            imageViewAudio.setEnabled(false);
            return;
        }

        audioRecorder = new AudioRecorder(context, activity, AppConstants.Limits.AUDIO_RECORD_LIMIT_SEC, this);

        if (!checkPermissions(true, true)) {
            imageViewAudio.setEnabled(false);
            return;
        }

        Utility.vibrate(activity, 50);

        isRecordingCompleted = false;
        stopTrackingAction = false;
        imageViewAudio.animate().scaleXBy(1f).scaleYBy(1f).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
        audioRecordingTime.setVisibility(View.VISIBLE);
        layoutLock.setVisibility(View.VISIBLE);
        layoutSlideCancel.setVisibility(View.VISIBLE);
        imageViewMic.setVisibility(View.VISIBLE);

        imageViewMic.clearAnimation();
        imageViewMicLock.clearAnimation();

        imageViewLockArrow.clearAnimation();
        imageViewLock.clearAnimation();

        imageViewMic.startAnimation(animBlink);
        imageViewMicLock.startAnimation(animBlink);

        imageViewLockArrow.startAnimation(animJumpFast);
        imageViewLock.startAnimation(animJump);

        layoutForAudioText.setVisibility(View.VISIBLE);
        layoutForAudioBg.setVisibility(View.VISIBLE);

        audioRecorder.setup(null, "chat_record_" + System.currentTimeMillis() + ".aac");
        audioRecorder.startRecording();
    }

    public void audioCanceledFromLocking(View view) {
        isDeleted = true;
        audioRecorder.stopRecording();
        audioRecorder.release();
        performDeleteAnim();
        layoutAudioPreview.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void audioStopFromLocking(View view) {
        audioRecorder.stopRecording();
        audioRecorder.release();
        layoutAudioLock.setVisibility(View.GONE);
        imageViewMicLock.clearAnimation();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void previewPlay(View view) {
        audioRecorder.playPauseAudio();
    }

    public void deleteAudioFromPreview(View view) {
        layoutAudioPreview.setVisibility(View.GONE);
        layoutAudioLock.setVisibility(View.GONE);
        layoutChatFooter.setVisibility(View.VISIBLE);

        isDeleted = true;
        audioRecorder.pausePlayback();
        audioRecorder.release();
        audioRecorder = null;
//        performDeleteAnim();
    }

    public void sendAudioFromPreview(View view) {

        if (audioRecorder != null) {
            if (audioRecorder.isPlaying()) {
                audioRecorder.pausePlayback();
            }
        }

        if (audioRecorder.haveAudio() && audioRecorder.isValid()) {
            ChatMessage chatMessageModel = chattingHelper.createMediaMessage(audioRecorder.getRecordedFilePath(), audioRecorder.getAudioTotalTime(), AppConstants.Chat.TYPE_CHAT_AUDIO);

            MessageData messageData = new MessageData(chatMessageModel);

            ChatMediaUploaderHelper chatMediaUploaderHelper = new ChatMediaUploaderHelper(context, messageData, this);
            mediaUploaderHelperHashMap.put(messageData, chatMediaUploaderHelper);
            chatMediaUploaderHelper.start();

            dataBase.insertMessage(chatMessageModel);
            addNewMessage(messageData);
        }

        layoutAudioPreview.setVisibility(View.GONE);
        layoutChatFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAudioRecordingStart() {

    }

    @Override
    public void onNeedSetup() {

    }

    @Override
    public void onAudioRecordingStop() {

    }

    Handler audioPlaybackHandler;
    Runnable audioPlaybackTimerRunnable;

    @Override
    public void audioPausePlay(final boolean isPlaying) {
        if (audioPlaybackHandler == null) {
            audioPlaybackHandler = new Handler();
        }

        if (isPlaying) {
            imageViewPreviewPlay.setImageResource(R.drawable.record_pause);
            layoutChatFooter.setVisibility(View.INVISIBLE);

            if (audioPlaybackTimerRunnable == null) {
                audioPlaybackTimerRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!isUserTracking) {
                            if (audioRecorder != null) {
                                textViewPreviewTime.setText(TimeUtils.getTimeFromMillis(audioRecorder.getCurrentPosition()));
                                seekBarAudio.setProgress((int) audioRecorder.getCurrentPosition());

                                if (audioRecorder.isPlaying()) {
                                    audioPlaybackHandler.postDelayed(audioPlaybackTimerRunnable, 50);
                                } else {
                                    audioPlaybackTimerRunnable = null;
                                    audioPausePlay(false);
                                }
                            }
                        } else {
                            audioPlaybackHandler.postDelayed(audioPlaybackTimerRunnable, 50);
                        }
                    }
                };
                audioPlaybackHandler.post(audioPlaybackTimerRunnable);
            }
        } else {
            imageViewPreviewPlay.setImageResource(R.drawable.record_play);
        }
    }

    @Override
    public void onAudioRecordingCompleted(String path, final int audioTotalTime) {

        if (isDeleted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    layoutChatFooter.setVisibility(View.VISIBLE);
                    layoutAudioLock.setVisibility(View.GONE);
                }
            });
            return;
        }

        LogUtils.debug("onAudioRecordingCompleted " + path + " time " + audioTotalTime);
        isRecordingCompleted = true;

        if (audioTotalTime > 1500) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stopRecording(RecordingStatus.COMPLETED);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    seekBarAudio.setMax(audioTotalTime);
                    seekBarAudio.setProgress(0);
                    imageViewPreviewPlay.setImageResource(R.drawable.record_play);
                    previewAudio();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    layoutChatFooter.setVisibility(View.VISIBLE);
                    layoutLock.setVisibility(View.GONE);
                    stopRecording(RecordingStatus.COMPLETED);
                }
            });
        }
    }

    private void previewAudio() {
        layoutAudioLock.setVisibility(View.GONE);

        if (audioRecorder.haveAudio() && audioRecorder.isValid()) {
            ChatMessage chatMessageModel = chattingHelper.createMediaMessage(audioRecorder.getRecordedFilePath(), audioRecorder.getAudioTotalTime(), AppConstants.Chat.TYPE_CHAT_AUDIO);

            MessageData messageData = new MessageData(chatMessageModel);

            ChatMediaUploaderHelper chatMediaUploaderHelper = new ChatMediaUploaderHelper(context, messageData, this);
            mediaUploaderHelperHashMap.put(messageData, chatMediaUploaderHelper);
            chatMediaUploaderHelper.start();

            dataBase.insertMessage(chatMessageModel);
            addNewMessage(messageData);
        }

        layoutAudioPreview.setVisibility(View.GONE);
        layoutChatFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void recordingUpdates(final int timerCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                audioRecordingTime.setText(TimeUtils.getTimeFromMillis(timerCount * 1000));
                audioRecordingLockTime.setText(TimeUtils.getTimeFromMillis(timerCount * 1000));
            }
        });
    }

    private void performDeleteAnim() {
        layoutAudioPreview.setVisibility(View.GONE);
        layoutAudioLock.setVisibility(View.GONE);
        layoutChatFooter.setVisibility(View.VISIBLE);
        layoutForAudioBg.setVisibility(View.VISIBLE);
        dustin.setVisibility(View.VISIBLE);
        dustin_cover.setVisibility(View.VISIBLE);
        imageViewMic.setVisibility(View.VISIBLE);
        imageViewMic.setRotation(0);
        isDeleting = true;
        imageViewAudio.setEnabled(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isDeleting = false;
                imageViewAudio.setEnabled(true);
            }
        }, 1000);

        imageViewMic.animate().translationY(-dp * 150).rotation(180).scaleXBy(0.4f).scaleYBy(0.4f).setDuration(500).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                dustin.setTranslationX(-dp * 40);
                dustin_cover.setTranslationX(-dp * 40);

                dustin_cover.animate().translationX(0).rotation(-120).setDuration(350).setInterpolator(new DecelerateInterpolator()).start();

                dustin.animate().translationX(0).setDuration(350).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        dustin.setVisibility(View.VISIBLE);
                        dustin_cover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageViewMic.animate().translationY(0).scaleX(1).scaleY(1).setDuration(350).setInterpolator(new LinearInterpolator()).setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                imageViewMic.setVisibility(View.GONE);
                                imageViewMic.setRotation(0);

                                dustin_cover.animate().rotation(0).setDuration(150).setStartDelay(50).start();
                                dustin.animate().translationX(-dp * 40).setDuration(200).setStartDelay(250).setInterpolator(new DecelerateInterpolator()).start();
                                dustin_cover.animate().translationX(-dp * 40).setDuration(200).setStartDelay(250).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {
                                    }
                                }).start();

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutForAudioBg.setVisibility(View.GONE);
                                        dustin.setVisibility(View.GONE);
                                        dustin_cover.setVisibility(View.GONE);
                                    }
                                }, 500);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                ).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        }).start();
    }

    private boolean checkPermissions(boolean checkStorage, boolean checkAudio) {
        ArrayList<String> arrPerm = new ArrayList<>();
        if (checkAudio && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(android.Manifest.permission.RECORD_AUDIO);
        }

        if (checkStorage && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            arrPerm.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!arrPerm.isEmpty()) {
            String[] permissions = new String[arrPerm.size()];
            permissions = arrPerm.toArray(permissions);

            ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageViewAudio.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {

            if (chatMessageAdapter != null) {
                chatMessageAdapter.pauseAudio();
            }

            if (audioRecorder != null && audioRecorder.isRecording) {
                isDeleted = true;
                stopRecording(RecordingStatus.PREVIEW_IF_VALID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
}
