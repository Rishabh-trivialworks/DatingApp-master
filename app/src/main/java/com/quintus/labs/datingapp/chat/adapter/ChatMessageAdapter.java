package com.quintus.labs.datingapp.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;

import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.xmpp.DataBase;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.quintus.labs.datingapp.xmpp.room.models.ConversationData;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.ChatHeaderModel;
import com.quintus.labs.datingapp.xmpp.utils.ChatMedia;
import com.quintus.labs.datingapp.xmpp.utils.TimeUtils;
import com.skd.androidrecording.audio.AudioPlaybackManager;
import com.skd.androidrecording.video.PlaybackHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 12/27/2016.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PlaybackHandler {


    public static class ChatItemLoadMore {
        public boolean isLoading;

        public ChatItemLoadMore(boolean isLoading) {
            this.isLoading = isLoading;
        }
    }

    public interface OnItemClickMessageListener {
        void onItemClick(View viewRoot, View view, MessageData model, int position);

        void onItemLongClick(View viewRoot, View view, MessageData model, int position);

        void chatMediaOptionsCancel(View viewRoot, View view, MessageData model, int position);

        void chatMediaOptionsSend(View viewRoot, View view, MessageData model, int position);

        void chatMediaOptionsError(View viewRoot, View view, MessageData model, int position);

        void chatMediaOptionsOpen(View viewRoot, View view, MessageData model, int position);

        void chatMediaOptionsDownload(View viewRoot, View view, MessageData model, int position);

        void performSeenAnimation(int previouslySeenPosition, int currentlySeePosition);
    }

    private List<MessageData> list = new ArrayList<>();
    private Context context;
    private Activity activity;
    private OnItemClickMessageListener onItemClickMessageListener;
    private OnItemClickHeadingListener onItemClickHeadingListener;
    private OnShowLastItemListener onShowLastItemListener;
    private Gson gson;

    private ChatItemLoadMore chatItemLoadMore = new ChatItemLoadMore(false);
    private long lastSeenTime;
    private UserData opponentUser;

    private SparseBooleanArray selectedItems;
    private List<ChatMessage> mSelectList = new ArrayList<>();
    private List<String> mSelectListID = new ArrayList<>();

    private float dp;
    private AudioPlaybackManager playbackManager;
    private String currentlySelectedAudio;
    private ChatMessageViewHolder currentlySelectedAudioViewHolder;

    private Handler audioPlaybackHandler;
    private Runnable audioPlaybackTimerRunnable;

    private boolean isUserTracking;
    private ConversationData conversationData;

    private int chatType;
    private int shownIn;

    private DataBase database;

    public void setConversationData(ConversationData conversationData) {
        this.conversationData = conversationData;
    }

    public void setLastSeenTime(long lastSeenTime) {
        LogUtils.debug("lastSeenTimelastSeenTime: setting " + lastSeenTime);

        if (this.lastSeenTime < lastSeenTime) {
            this.lastSeenTime = lastSeenTime;
        }
    }

    public void setOpponentUser(UserData opponentUser) {
        this.opponentUser = opponentUser;
    }

    public interface OnItemClickHeadingListener {
        void onItemClick(View viewRoot, View view, ChatHeaderModel model, int position);

        void onItemLongClick(View viewRoot, View view, ChatHeaderModel model, int position);

        void onItemClick(View viewRoot, View view, ChatItemLoadMore model, int position);

        void refreshList(int position);
    }

    public interface OnShowLastItemListener {
        void onShowLastItem();

        void onShowFirstItem(int position);
    }

    public ChatMessageAdapter(Context context, Activity activity, int chatType, int shownIn, OnShowLastItemListener onShowLastItemListener,
                              OnItemClickMessageListener onItemClickMessageListener,
                              OnItemClickHeadingListener onItemClickHeadingListener) {
        this.onItemClickMessageListener = onItemClickMessageListener;
        this.onItemClickHeadingListener = onItemClickHeadingListener;
        this.context = context;
        this.activity = activity;
        this.chatType = chatType;
        this.shownIn = shownIn;
        this.onShowLastItemListener = onShowLastItemListener;
        gson = new Gson();
        selectedItems = new SparseBooleanArray();
        database = DataBase.getInstance(context);

        dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public List<MessageData> getList() {
        return list;
    }

    public void addNewMessage(MessageData model) {

        list.remove(model);
        list.add(0, model);
    }

    public void clearSelections() {
        selectedItems.clear();
        mSelectList.clear();
        mSelectListID.clear();
        notifyDataSetChanged();
    }

    public void toggleSelectionMessage(int pos) {
        if (mSelectListID.contains((list.get(pos)).chatMessage.getMessageId())) {
            for (int i = 0; i < mSelectList.size(); i++) {
                if (mSelectListID.get(i).equals((list.get(pos)).chatMessage.getMessageId())) {
                    mSelectListID.remove(i);
                    break;
                }
            }
            mSelectList.remove((list.get(pos)).chatMessage);
        } else {
            selectedItems.put(pos, true);
            mSelectList.add((list.get(pos)).chatMessage);
            mSelectListID.add((list.get(pos)).chatMessage.getMessageId());
        }
        notifyItemChanged(pos);
    }

    public List<ChatMessage> getCopyData() {
        Collections.sort(mSelectList);
        return mSelectList;
    }

    public List<ChatMessage> getSelectedList() {
        return mSelectList;
    }

    public int getSelectedItemCount() {
        return mSelectList.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        list.remove(position);
    }

    boolean allMessageSet;

    public void setAllMessageSet(boolean allMessageSet) {
        this.allMessageSet = allMessageSet;
    }

    public boolean isAllMessageSet() {
        return allMessageSet;
    }

    private RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public void addPreviousChatItems(final List<MessageData> models) {
        Collections.reverse(models);
        list.addAll(models);
    }

    public MessageData getItem(int position) {
        try {
            return list.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_me, parent, false);
            return new ChatMessageViewHolder(v);
        }
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_other, parent, false);
            return new ChatMessageViewHolder(v);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);

        MessageData model = getItem(position);

        if (model.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_SENT)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (playbackManager == null) {
            playbackManager = new AudioPlaybackManager(activity, null, this);
        }

        try {
            if (onShowLastItemListener != null) {
                if (position == getItemCount() - 1) {
                    onShowLastItemListener.onShowLastItem();
                } else if (position == (list.size() < 100 ? 30 : list.size() - 100)) { //this will depend on limit..
                    if (!isAllMessageSet()) {
                        onShowLastItemListener.onShowFirstItem(position);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (holder instanceof ChatMessageViewHolder) {
            ((ChatMessageViewHolder) holder).bind(opponentUser, lastSeenTime, this,
                    getItem(position),
                    position, onItemClickMessageListener);
        }
    }

    class ChatMessageItemLoadMore extends RecyclerView.ViewHolder {
        public View viewRoot;

        @BindView(R.id.layoutLoadMore)
        public View layoutLoadMore;
        @BindView(R.id.layoutProgress)
        public View layoutProgress;

        public ChatMessageItemLoadMore(View view) {
            super(view);
            viewRoot = view;
            ButterKnife.bind(this, view);
        }

        public void bind(final ChatItemLoadMore model, final int position, final OnItemClickHeadingListener onItemClickHeadingListener) {

            if (model.isLoading) {
                layoutProgress.setVisibility(View.VISIBLE);
                layoutLoadMore.setVisibility(View.INVISIBLE);
            } else {
                layoutProgress.setVisibility(View.INVISIBLE);
                layoutLoadMore.setVisibility(View.VISIBLE);
            }

            layoutLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickHeadingListener.onItemClick(viewRoot, layoutLoadMore, model, position);
                }
            });
        }
    }

    class ChatHeaderDateViewHolder extends RecyclerView.ViewHolder {

        public View viewRoot;

        @BindView(R.id.textView)
        public TextView textView;

        public ChatHeaderDateViewHolder(View view) {
            super(view);
            viewRoot = view;
            ButterKnife.bind(this, view);
        }

        public void bind(final ChatHeaderModel model, final int position, final OnItemClickHeadingListener onItemClickHeadingListener) {
            try {
                textView.setText(model.text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        public View viewRoot;

        @BindView(R.id.textViewMessage)
        public TextView textViewMessage;
        @BindView(R.id.textViewAssignmentTime)
        public TextView textViewTime;
        @BindView(R.id.imageViewStatus)
        public ImageView imageViewStatus;
        @BindView(R.id.imageChatImage)
        public ImageView imageChatImage;
        @BindView(R.id.layoutChatImage)
        public View layoutChatImage;
        @BindView(R.id.imageChatImageOptions)
        public ImageView imageChatImageOptions;
        @BindView(R.id.progressBarChatImage)
        public ProgressBar progressBarChatImage;
        @BindView(R.id.msg_layout)
        public View msg_layout;
        @BindView(R.id.back)
        public LinearLayout back;
        @BindView(R.id.layoutUnknown)
        public View layoutUnknown;

        @BindView(R.id.layoutAudioPlayer)
        public View layoutAudioPlayer;
        @BindView(R.id.imageViewAudioPic)
        public ImageView imageViewAudioPic;
        @BindView(R.id.imageViewAudioPlay)
        public ImageView imageViewAudioPlay;
        @BindView(R.id.imageViewAudioOptions)
        public ImageView imageViewAudioOptions;
        @BindView(R.id.seekBarAudio)
        public SeekBar seekBarAudio;
        @BindView(R.id.textViewPlayerTime)
        public TextView textViewPlayerTime;
        @BindView(R.id.progressBarAudio)
        public ProgressBar progressBarAudio;

        @BindView(R.id.layoutHeader)
        public View layoutHeader;
        @BindView(R.id.textViewHeader)
        public TextView textViewHeader;
        @BindView(R.id.textViewGroupChatUserName)
        public TextView textViewGroupChatUserName;

        @BindView(R.id.layoutGroupChatUserName)
        public View layoutGroupChatUserName;
        @BindView(R.id.layoutStatus)
        public View layoutStatus;

        @BindView(R.id.layoutDateHeader)
        public View layoutDateHeader;
        @BindView(R.id.textViewDateHeader)
        public TextView textViewDateHeader;

        private float dp;

        public ChatMessageViewHolder(View view) {
            super(view);
            viewRoot = view;
            ButterKnife.bind(this, view);

            dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, view.getContext().getResources().getDisplayMetrics());
        }

        public void bind(UserData opponentUser, long lastSeenTime, ChatMessageAdapter chatMessageAdapter, final MessageData model, final int position, final OnItemClickMessageListener onItemClickMessageListener) {

            LogUtils.debug("chat message binder: " + position + " subject: " + model.chatMessage.getSubject() + " type: " + model.chatMessage.getType());

            if (model == null) {
                return;
            }

            try {
                MessageData msg = null;

                try {
                    msg = chatMessageAdapter.list.get(position );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (msg == null || !model.chatMessage.getDate(context).equals(msg.chatMessage.getDate(context))) {
                    layoutDateHeader.setVisibility(View.VISIBLE);
                    textViewDateHeader.setText(model.chatMessage.getDate(context));
                } else {
                    layoutDateHeader.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Glide.with(context).clear(imageViewStatus);
            imageViewStatus.setVisibility(View.INVISIBLE);

            layoutUnknown.setVisibility(View.GONE);
            layoutHeader.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);

            // Other messages
            if (mSelectListID.contains(model.chatMessage.getMessageId())) {
                back.setBackground(ContextCompat.getDrawable(context, R.color.transparent_11));
            } else {
                back.setBackground(ContextCompat.getDrawable(context, R.color.transparent));
            }

            textViewTime.setText("  " + TimeUtils.getChatTimeFromMillis(model.chatMessage.getTimestamp()));
            imageViewAudioPlay.setTag(String.valueOf(model.chatMessage.getMessageId()));

            if (lastSeenTime >= model.chatMessage.getTimestamp()) {
                model.chatMessage.setStatus(AppConstants.Chat.STATUS_SEEN);
            }

            if (model.chatMessage.getMessageType().equals(AppConstants.Chat.MESSAGE_TYPE_RECEIVED)) {

                if (lastSeenTime < model.chatMessage.getTimestamp()) {
                    model.chatMessage.setStatus(AppConstants.Chat.STATUS_SEEN);
                }

                if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                    layoutGroupChatUserName.setPadding(0, 0, 0, 0);
                    textViewGroupChatUserName.setVisibility(View.GONE);
                } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                    layoutGroupChatUserName.setPadding(0, (int) (dp * 24), 0, 0);
                    textViewGroupChatUserName.setVisibility(View.VISIBLE);
                    try {
                        textViewGroupChatUserName.setText(model.userInfo.getFullName());
                        textViewGroupChatUserName.setTextColor(Color.parseColor(model.userInfo.getColorIndicator()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                    setMessage(model.chatMessage.getBody(), false);
                    layoutChatImage.setVisibility(View.GONE);
                    layoutAudioPlayer.setVisibility(View.GONE);
//                    textViewMessage.setVisibility(View.VISIBLE);
                    textViewTime.setShadowLayer(0, 0, 0, R.color.transparent);
                    textViewTime.setTextColor(ContextCompat.getColor(context, R.color.theme_text_color_grey_light_1));

                } else if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
                    textViewTime.setShadowLayer(2, 1, 1, R.color.text_shadow);
                    textViewTime.setTextColor(ContextCompat.getColor(context, R.color.white));
                    imageChatImage.setOnClickListener(null);
                    layoutChatImage.setVisibility(View.VISIBLE);
                    layoutAudioPlayer.setVisibility(View.GONE);
//                    textViewMessage.setVisibility(View.GONE);
                    textViewMessage.setText("");

                    progressBarChatImage.setVisibility(View.GONE);
                    imageChatImageOptions.setVisibility(View.GONE);

                    ChatMedia chatMedia = gson.fromJson(model.chatMessage.getBody(), ChatMedia.class);

                    if (chatMedia != null) {
                        if (chatMedia.getStoragePath() != null && !chatMedia.getStoragePath().isEmpty() && new File(chatMedia.getStoragePath()).exists()) {
//                        imageChatImage.setAlpha(1f);

                            Glide.with(context)
                                    .asBitmap()
                                    .load(chatMedia.getStoragePath())
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageChatImage);

                            imageChatImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (getSelectedItemCount() > 0) {
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    } else {
                                        onItemClickMessageListener.chatMediaOptionsOpen(viewRoot, imageChatImage, model, position);
                                    }
                                }
                            });

                            imageChatImage.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (imageChatImageOptions.getVisibility() == View.GONE)
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    return true;
                                }
                            });

                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (getSelectedItemCount() > 0) {
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    } else {
                                        onItemClickMessageListener.chatMediaOptionsOpen(viewRoot, imageChatImage, model, position);
                                    }
                                }
                            });

                            back.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (imageChatImageOptions.getVisibility() == View.GONE)
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    return true;
                                }
                            });
                        } else {
                            Glide.with(context)
                                    .load(chatMedia.getS3MediaThumbnailUrl())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .override(36, 36)
                                    .centerCrop()
                                    .into(imageChatImage);
                            imageChatImageOptions.setVisibility(View.VISIBLE);
                            imageChatImageOptions.setImageResource(R.drawable.download_chat_image);

                            if (chatMedia.isDownloading()) {
                                imageChatImageOptions.setVisibility(View.GONE);
                                progressBarChatImage.setVisibility(View.VISIBLE);
                            } else {
                                progressBarChatImage.setVisibility(View.GONE);
                            }

                            imageChatImageOptions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onItemClickMessageListener.chatMediaOptionsDownload(viewRoot, imageChatImageOptions, model, position);
                                }
                            });
                        }
                    }
                } else if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {

                    chatAudio(model, this, position, true);

                } else if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED) ||
                        model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)) {
                    //Handled Below
                } else {
                    layoutUnknown.setVisibility(View.VISIBLE);
                    layoutChatImage.setVisibility(View.GONE);
                    layoutAudioPlayer.setVisibility(View.GONE);
                    textViewMessage.setText("");
                    textViewTime.setShadowLayer(0, 0, 0, R.color.transparent);
                    textViewTime.setText("");

                    layoutUnknown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String appPackageName = context.getPackageName();
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            } else {

                textViewGroupChatUserName.setVisibility(View.GONE);
                layoutGroupChatUserName.setPadding(0, 0, 0, 0);

                if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

                        imageViewStatus.setVisibility(View.VISIBLE);

                        switch (model.chatMessage.getStatus()) {
                            case AppConstants.Chat.STATUS_PENDING:
                                imageViewStatus.setImageResource(R.drawable.msg_sending);
                                break;
                            case AppConstants.Chat.STATUS_SENT:
                                imageViewStatus.setImageResource(R.drawable.msg_sent);
                                break;
                            case AppConstants.Chat.STATUS_DELIVERED:
                                imageViewStatus.setImageResource(R.drawable.msg_sending);
                                break;
                            case AppConstants.Chat.STATUS_FAILED:
                                imageViewStatus.setImageResource(R.drawable.msg_sending);
                                break;
                            case AppConstants.Chat.STATUS_SEEN:
                                break;
                            default:
                                imageViewStatus.setImageResource(R.drawable.msg_sending);
                        }

                } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {

//                    if (shownIn == AppConstants.ShownIn.CHATTING_SCREEN) {
//                        if (TempStorage.getUserDetailModel().getPackageInfo() != null
//                        ) {
//                            imageViewStatus.setVisibility(View.VISIBLE);
//
//                            switch (model.chatMessage.getStatus()) {
//                                case AppConstants.Chat.STATUS_PENDING:
//                                    imageViewStatus.setImageResource(R.drawable.msg_sending);
//                                    break;
//                                case AppConstants.Chat.STATUS_FAILED:
//                                    imageViewStatus.setImageResource(R.drawable.msg_sending);
//                                    break;
//                                default:
//                                    imageViewStatus.setImageResource(R.drawable.msg_sent);
//                                    break;
//                            }
//
//                        } else {
//                            switch (model.chatMessage.getStatus()) {
//                                case AppConstants.Chat.STATUS_PENDING:
//                                    imageViewStatus.setImageResource(R.drawable.msg_sending);
//                                case AppConstants.Chat.STATUS_FAILED:
//                                    imageViewStatus.setImageResource(R.drawable.msg_sending);
//                                    imageViewStatus.setVisibility(View.VISIBLE);
//                                    break;
//                                default:
//                                    imageViewStatus.setVisibility(View.INVISIBLE);
//                                    break;
//                            }
//                        }
//
//                        layoutStatus.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
//                                    if (TempStorage.getUserDetailModel().getPackageInfo() != null
//                                            && TempStorage.getUserDetailModel().getPackageInfo().isAllowChatReadReceipt()) {
//                                        GroupMessageInfoActivity.openActivity(context, model.chatMessage);
//                                    }
//                                }
//                            }
//                        });
//                    }
                }

                if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
                    setMessage(model.chatMessage.getBody(), true);
                    layoutChatImage.setVisibility(View.GONE);
                    layoutAudioPlayer.setVisibility(View.GONE);
//                    textViewMessage.setVisibility(View.VISIBLE);
                    textViewTime.setShadowLayer(0, 0, 0, R.color.transparent);

                } else if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
                    textViewTime.setShadowLayer(2, 1, 1, R.color.text_shadow);
                    imageChatImage.setOnClickListener(null);
                    layoutChatImage.setVisibility(View.VISIBLE);
                    layoutAudioPlayer.setVisibility(View.GONE);
//                    textViewMessage.setVisibility(View.GONE);
                    textViewMessage.setText("");

                    progressBarChatImage.setVisibility(View.GONE);
                    imageChatImageOptions.setVisibility(View.GONE);

                    ChatMedia chatMedia = gson.fromJson(model.chatMessage.getBody(), ChatMedia.class);

                    if (chatMedia != null) {
                        if (chatMedia.getStoragePath() == null || !new File(chatMedia.getStoragePath()).exists()) {
                            progressBarChatImage.setVisibility(View.GONE);
                            imageChatImageOptions.setVisibility(View.VISIBLE);
                            imageChatImageOptions.setImageResource(R.drawable.download_chat_image);
                            Glide.with(context)
                                    .load(chatMedia.getS3MediaThumbnailUrl())
                                    .override(36, 36)
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageChatImage);

                            imageChatImageOptions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onItemClickMessageListener.chatMediaOptionsDownload(viewRoot, imageChatImageOptions, model, position);
                                }
                            });
                        } else {
                            Glide.with(context)
                                    .asBitmap()
                                    .load(chatMedia.getStoragePath())
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageChatImage);
                            imageChatImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (getSelectedItemCount() > 0) {
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    } else {
                                        onItemClickMessageListener.chatMediaOptionsOpen(viewRoot, imageChatImage, model, position);
                                    }
                                }
                            });
                            imageChatImage.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (imageChatImageOptions.getVisibility() == View.GONE)
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    return true;
                                }
                            });
                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (getSelectedItemCount() > 0) {
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    } else {
                                        onItemClickMessageListener.chatMediaOptionsOpen(viewRoot, imageChatImage, model, position);
                                    }
                                }
                            });
                            back.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (imageChatImageOptions.getVisibility() == View.GONE)
                                        onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                                    return true;
                                }
                            });
                        }
                    }

                    if (model.chatMessage.getStatus() == AppConstants.Chat.STATUS_PENDING) {
                        if (chatMedia.isUploading()) {
                            progressBarChatImage.setVisibility(View.VISIBLE);
                            imageChatImageOptions.setVisibility(View.VISIBLE);
                            imageChatImageOptions.setImageResource(R.drawable.chat_image_cancel);

                            imageChatImageOptions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onItemClickMessageListener.chatMediaOptionsCancel(viewRoot, imageChatImageOptions, model, position);
                                }
                            });
                        } else {
                            progressBarChatImage.setVisibility(View.GONE);
                            imageChatImageOptions.setVisibility(View.VISIBLE);
                            imageChatImageOptions.setImageResource(R.drawable.chat_image_retry);

                            imageChatImageOptions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onItemClickMessageListener.chatMediaOptionsSend(viewRoot, imageChatImageOptions, model, position);
                                }
                            });
                        }
                    } else if (model.chatMessage.getStatus() == AppConstants.Chat.STATUS_FAILED) {
                        progressBarChatImage.setVisibility(View.GONE);
                        imageChatImageOptions.setVisibility(View.VISIBLE);
                        imageChatImageOptions.setImageResource(R.drawable.chat_image_retry);

                        imageChatImageOptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onItemClickMessageListener.chatMediaOptionsSend(viewRoot, imageChatImageOptions, model, position);
                            }
                        });
                    }

                    if (chatMedia != null && chatMedia.isDownloading()) {
                        imageChatImageOptions.setVisibility(View.GONE);
                        progressBarChatImage.setVisibility(View.VISIBLE);
                    }
                } else if (model.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {

                    chatAudio(model, this, position, false);

                } else if (model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED) ||
                        model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)) {
                    //Handled Below
                } else {
                    layoutUnknown.setVisibility(View.VISIBLE);
                    layoutChatImage.setVisibility(View.GONE);
                    textViewMessage.setText("");
                    layoutAudioPlayer.setVisibility(View.GONE);
                    textViewTime.setShadowLayer(0, 0, 0, R.color.transparent);
                    textViewTime.setText("");

                    layoutUnknown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String appPackageName = context.getPackageName();
                            try {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (Exception e) {
                            }
                        }
                    });
                }
            }

            if (model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED) ||
                    model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)) {

                String operatorName = "";
                String subscriberName = "";
                int operatorId = 0;
                int subscriberId = 0;

                if (model.chatMessage.subscriber == null) {
                    try {
                        subscriberId = Integer.parseInt(model.chatMessage.getBody());

                        if (subscriberId != TempStorage.getUser().getId()) {
                            model.chatMessage.subscriber = MyApplication.getChatDataBase().userInfoDao().get(subscriberId);
                            subscriberName = model.chatMessage.subscriber.getFullName();
                        } else {
                            subscriberName = context.getString(R.string.you);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    subscriberName = model.chatMessage.subscriber.getFullName();
                    subscriberId = model.chatMessage.subscriber.getId();
                }

                if (model.chatMessage.operator == null) {
                    try {
                        operatorId = model.chatMessage.getFromToUserId();

                        if (operatorId != TempStorage.getUser().getId()) {
                            model.chatMessage.operator = model.userInfo;
                            operatorName = model.chatMessage.operator.getFullName();
                        } else {
                            operatorName = context.getString(R.string.you);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    operatorName = model.chatMessage.operator.getFullName();
                    operatorId = model.chatMessage.operator.getId();
                }

                if (subscriberId == operatorId && !model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)) {
                    layoutHeader.setVisibility(View.GONE);
                    back.setVisibility(View.GONE);
                } else {
                    layoutHeader.setVisibility(View.VISIBLE);
                    back.setVisibility(View.GONE);
                }

                if (model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_ADDED)) {
                    textViewHeader.setText(Html.fromHtml("<strong>" + operatorName + "</strong> " + context.getString(R.string.added) + " <strong>" + subscriberName + "</strong> "));
                } else if (model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_PARTICIPANT_DELETED)) {
                    if (subscriberId == operatorId) {
                        if (subscriberId == TempStorage.getUser().getId()) {
                            textViewHeader.setText(Html.fromHtml("<strong>" + context.getString(R.string.you) + "</strong> " + context.getString(R.string.group_left)));
                        } else {
                            textViewHeader.setText(Html.fromHtml("<strong>" + operatorName + "</strong> " + context.getString(R.string.group_left)));
                        }
                    } else {
                        textViewHeader.setText(Html.fromHtml("<strong>" + operatorName + "</strong> " + context.getString(R.string.removed) + " <strong>" + subscriberName + "</strong> "));
                    }
                }
            } else if (model.chatMessage.getType().equals(AppConstants.Chat.TYPE_GROUP_DELETED)) {

                layoutHeader.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);

                if (model.chatMessage.getFromToUserId() == TempStorage.getUser().getId()) {
                    textViewHeader.setText(context.getString(R.string.you) + " " + context.getString(R.string.message_group_deleted));
                } else {

                    if (model.chatMessage.operator == null) {

                        String name = "";
                        try {
                            model.chatMessage.operator = model.userInfo;
                            name = model.chatMessage.operator.getFullName();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        textViewHeader.setText(Html.fromHtml("<strong>" + name + "</strong> " + context.getString(R.string.message_group_deleted)));

                    } else {
                        textViewHeader.setText(Html.fromHtml("<strong>" + model.chatMessage.operator.getFullName() + "</strong> " + context.getString(R.string.message_group_deleted)));
                    }
                }
            }

            back.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (imageChatImageOptions.getVisibility() == View.GONE || !textViewMessage.getText().toString().isEmpty()) {
                        onItemClickMessageListener.onItemLongClick(viewRoot, msg_layout, model, position);
                        viewRoot.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    }
                    return true;
                }
            });

            msg_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickMessageListener.onItemLongClick(viewRoot, msg_layout, model, position);
                    return true;
                }
            });

            msg_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickMessageListener.onItemClick(viewRoot, msg_layout, model, position);
                }
            });

            textViewMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickMessageListener.onItemLongClick(viewRoot, textViewMessage, model, position);
                    return true;
                }
            });

            textViewMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageChatImageOptions.getVisibility() == View.GONE || !textViewMessage.getText().toString().isEmpty())
                        onItemClickMessageListener.onItemClick(viewRoot, textViewMessage, model, position);
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageChatImageOptions.getVisibility() == View.GONE || !textViewMessage.getText().toString().isEmpty())
                        onItemClickMessageListener.onItemClick(viewRoot, msg_layout, model, position);
                }
            });

            LogUtils.debug("lastSeenMessagelastSeenMessage : " + position + " " + lastSeenTime);

            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {

                if (lastSeenTime == model.chatMessage.getTimestamp()) {


                        imageViewStatus.setVisibility(View.VISIBLE);

                        String url = null;
                        int defaultImageRes = 0;

                        if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                            if (opponentUser.getMedia() != null&&opponentUser.getMedia().size()>0) {
                                url = opponentUser.getMedia().get(0).getUrl();
                            }
                            defaultImageRes = R.drawable.user_default_profile_pic;

                        } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                            defaultImageRes = R.drawable.grp_chat_list;
                        }

                        if (url == null) {
                            url = "";
                        }

                        Glide.with(context).asBitmap().load(url).centerCrop().placeholder(defaultImageRes).error(defaultImageRes)
                                .into(new BitmapImageViewTarget(imageViewStatus) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        imageViewStatus.setImageDrawable(circularBitmapDrawable);
                                    }
                                });

                }
            }
        }

        private void chatAudio(final MessageData model, final ChatMessageViewHolder chatMessageViewHolder, final int position, boolean isMyMessage) {
            textViewTime.setShadowLayer(0, 0, 0, R.color.transparent);
            layoutChatImage.setVisibility(View.GONE);
            layoutAudioPlayer.setVisibility(View.VISIBLE);
            textViewMessage.setText("");

            back.setOnClickListener(null);
            back.setOnLongClickListener(null);
            layoutAudioPlayer.setOnClickListener(null);
            layoutAudioPlayer.setOnLongClickListener(null);
            imageViewAudioPlay.setOnLongClickListener(null);

            String imageUrl = null;

            if (chatType == AppConstants.Chat.TYPE_SINGLE_CHAT) {
                if (isMyMessage && opponentUser.getMedia() != null&&opponentUser.getMedia().size()>0) {
                    imageUrl = opponentUser.getMedia().get(0).getUrl();
                } else if (!isMyMessage && TempStorage.getUser().getMedia() != null&&TempStorage.getUser().getMedia().size()>0) {
                    imageUrl = TempStorage.getUser().getMedia().get(0).getUrl();
                }
            } else if (chatType == AppConstants.Chat.TYPE_GROUP_CHAT) {
                if (isMyMessage && model.userInfo.getProfileImage() != null) {
                    imageUrl = model.userInfo.getProfileImageThumbnail();
                } else if (!isMyMessage && TempStorage.getUser().getMedia() != null&&TempStorage.getUser().getMedia().size()>0) {
                    imageUrl = TempStorage.getUser().getMedia().get(0).getUrl();
                }
            }

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context).asBitmap().load(imageUrl).centerCrop()
                        .placeholder(R.drawable.user_default_profile_pic).error(R.drawable.user_default_profile_pic)
                        .into(new BitmapImageViewTarget(imageViewAudioPic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imageViewAudioPic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Glide.with(context).clear(imageViewAudioPic);
                imageViewAudioPic.setImageResource(R.drawable.user_default_profile_pic);
            }

            final ChatMedia chatMedia = gson.fromJson(model.chatMessage.getBody(), ChatMedia.class);

            if (chatMedia != null) {

                textViewPlayerTime.setText(TimeUtils.getTimeFromMillis(chatMedia.getDuration() * 1000));
                seekBarAudio.setProgress(0);
                seekBarAudio.setMax(chatMedia.getDuration() * 1000);
                imageViewAudioPlay.setImageResource(R.drawable.record_play);
                imageViewAudioPlay.setTag(String.valueOf(model.chatMessage.getMessageId()));

                if (imageViewAudioPlay.getTag().toString().equals(String.valueOf(currentlySelectedAudio))) {
                    currentlySelectedAudioViewHolder = chatMessageViewHolder;
                }

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

                        if (playbackManager.isPlaying()) {
                            playbackManager.seekTo(seekBar.getProgress());
                        }
                    }
                });

                if (chatMedia.getStoragePath() == null || !new File(chatMedia.getStoragePath()).exists()) {

                    progressBarAudio.setVisibility(View.GONE);
                    imageViewAudioPlay.setVisibility(View.GONE);
                    imageViewAudioOptions.setVisibility(View.VISIBLE);
                    imageViewAudioOptions.setImageResource(R.drawable.audio_download);

                    imageViewAudioOptions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickMessageListener.chatMediaOptionsDownload(viewRoot, imageViewAudioOptions, model, position);
                        }
                    });

                } else {
                    progressBarAudio.setVisibility(View.GONE);
                    imageViewAudioOptions.setVisibility(View.GONE);
                    imageViewAudioPlay.setVisibility(View.VISIBLE);

                    imageViewAudioPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (getSelectedItemCount() > 0) {
                                onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                            } else {
                                if (currentlySelectedAudioViewHolder != null && !currentlySelectedAudioViewHolder.equals(chatMessageViewHolder)) {
                                    currentlySelectedAudioViewHolder.imageViewAudioPlay.setImageResource(R.drawable.record_play);
                                    currentlySelectedAudioViewHolder.textViewPlayerTime.setText(TimeUtils.getTimeFromMillis(playbackManager.getDuration()));
                                    currentlySelectedAudioViewHolder.seekBarAudio.setProgress(0);
                                    currentlySelectedAudioViewHolder.seekBarAudio.setMax(chatMedia.getDuration() * 1000);
                                }

                                if (model.chatMessage.getMessageId() != currentlySelectedAudio) {
                                    currentlySelectedAudio = model.chatMessage.getMessageId();
                                    currentlySelectedAudioViewHolder = chatMessageViewHolder;
                                    playbackManager.setupPlayback(chatMedia.getStoragePath());
                                    playbackManager.start();
                                    audioPausePlay();

                                    imageViewAudioPlay.setVisibility(View.VISIBLE);
                                    imageViewAudioOptions.setVisibility(View.GONE);
                                    progressBarAudio.setVisibility(View.GONE);
                                } else {
                                    if (playbackManager.isPlaying()) {
                                        playbackManager.pause();
                                    } else {
                                        playbackManager.start();
                                    }
                                    audioPausePlay();
                                }
                            }
                        }
                    });

                    imageViewAudioPlay.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                            return true;
                        }
                    });

                    layoutAudioPlayer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (getSelectedItemCount() > 0) {
                                onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                            }
//                            else {
//                                onItemClickMessageListener.chatMediaOptionsOpen(viewRoot, imageChatImage, model, position);
//                            }
                        }
                    });

                    layoutAudioPlayer.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                            return true;
                        }
                    });

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getSelectedItemCount() > 0) {
                                onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                            } else {
                                onItemClickMessageListener.chatMediaOptionsOpen(viewRoot, imageChatImage, model, position);
                            }
                        }
                    });

                    back.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            onItemClickMessageListener.onItemLongClick(viewRoot, imageChatImage, model, position);
                            return true;
                        }
                    });
                }

                if (model.chatMessage.getStatus() == AppConstants.Chat.STATUS_PENDING) {
                    imageViewAudioPlay.setVisibility(View.GONE);

                    if (chatMedia.isUploading()) {
                        progressBarAudio.setVisibility(View.VISIBLE);
                        imageViewAudioOptions.setVisibility(View.GONE);

                        imageViewAudioOptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onItemClickMessageListener.chatMediaOptionsCancel(viewRoot, imageViewAudioOptions, model, position);
                            }
                        });
                    } else {
                        progressBarAudio.setVisibility(View.GONE);
                        imageViewAudioOptions.setVisibility(View.VISIBLE);
                        imageViewAudioOptions.setImageResource(R.drawable.audio_retry);

                        imageViewAudioOptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onItemClickMessageListener.chatMediaOptionsSend(viewRoot, imageViewAudioOptions, model, position);
                            }
                        });
                    }

                } else if (model.chatMessage.getStatus() == AppConstants.Chat.STATUS_FAILED) {
                    imageViewAudioPlay.setVisibility(View.GONE);

                    progressBarAudio.setVisibility(View.GONE);
                    imageViewAudioOptions.setVisibility(View.VISIBLE);
                    imageViewAudioOptions.setImageResource(R.drawable.audio_retry);

                    imageViewAudioOptions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickMessageListener.chatMediaOptionsSend(viewRoot, imageViewAudioOptions, model, position);
                        }
                    });
                }

                if (chatMedia != null && chatMedia.isDownloading()) {
                    imageViewAudioOptions.setVisibility(View.GONE);
                    imageViewAudioPlay.setVisibility(View.GONE);
                    progressBarAudio.setVisibility(View.VISIBLE);
                }
            }
        }

        private void setMessage(String text, boolean isMyMessage) {
            textViewMessage.setText(text);
           // LinkifyHelper.setText(context, textViewMessage, text, true, LinkifyUserSpan.Theme.THEME_CHAT);
        }

    }

    @Override
    public void onPreparePlayback() {
        playbackManager.start();

        audioPausePlay();
    }

    private void audioPausePlay() {
        if (audioPlaybackHandler == null) {
            audioPlaybackHandler = new Handler();
        }

        if (playbackManager.isPlaying()) {

            if (currentlySelectedAudioViewHolder != null) {

                if (audioPlaybackTimerRunnable != null) {
                    audioPlaybackHandler.removeCallbacks(audioPlaybackTimerRunnable);
                }

                audioPlaybackTimerRunnable = new Runnable() {
                    @Override
                    public void run() {

                        LogUtils.debug("audioPausePlay audioPlaybackTimerRunnable " + currentlySelectedAudioViewHolder.imageViewAudioPlay.getTag() + " " + currentlySelectedAudio);

                        if (currentlySelectedAudioViewHolder.imageViewAudioPlay.getTag() != null
                                && currentlySelectedAudioViewHolder.imageViewAudioPlay.getTag().toString().equals(String.valueOf(currentlySelectedAudio))) {
                            if (!isUserTracking) {
                                currentlySelectedAudioViewHolder.imageViewAudioPlay.setImageResource(R.drawable.record_pause);
                                currentlySelectedAudioViewHolder.seekBarAudio.setMax(playbackManager.getDuration());
                                currentlySelectedAudioViewHolder.textViewPlayerTime.setText(TimeUtils.getTimeFromMillis(playbackManager.getCurrentPosition()));
                                currentlySelectedAudioViewHolder.seekBarAudio.setProgress((int) playbackManager.getCurrentPosition());

                                if (playbackManager.isPlaying()) {
                                    audioPlaybackHandler.postDelayed(audioPlaybackTimerRunnable, playbackManager.getDuration() / 100);
                                } else {
                                    audioPlaybackTimerRunnable = null;
                                    currentlySelectedAudioViewHolder.textViewPlayerTime.setText(TimeUtils.getTimeFromMillis(playbackManager.getDuration()));
                                    currentlySelectedAudioViewHolder.imageViewAudioPlay.setImageResource(R.drawable.record_play);
                                }
                            } else
                                audioPlaybackHandler.postDelayed(audioPlaybackTimerRunnable, playbackManager.getDuration() / 100);

                        } else
                            audioPlaybackHandler.postDelayed(audioPlaybackTimerRunnable, playbackManager.getDuration() / 100);
                    }
                };

                audioPlaybackHandler.post(audioPlaybackTimerRunnable);
            }
        } else {
            currentlySelectedAudioViewHolder.imageViewAudioPlay.setImageResource(R.drawable.record_play);
        }
    }

    public void pauseAudio() {
        if (playbackManager != null) {
            playbackManager.pause();
        }
    }
}

