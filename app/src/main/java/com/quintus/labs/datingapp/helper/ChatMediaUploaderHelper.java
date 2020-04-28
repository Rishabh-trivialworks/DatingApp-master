package com.quintus.labs.datingapp.helper;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.rest.ProgressRequestBody;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.BitmapUtils;
import com.quintus.labs.datingapp.xmpp.utils.ChatMedia;

import java.io.File;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by MyU10 on 4/28/2017.
 */

public class ChatMediaUploaderHelper {
    private Context context;
    private String mediaPath;
    private boolean enableCompression = true;
    private boolean isOriginalImageAfterCompression = false;
    private File file = null;
    private Call<ResponseModel<ImageModel>> mediaUploadCall;
    private boolean isCancelled;
    private Callbacks callbacks;
    private Gson gson;
    private ChatMedia chatMedia;
    private MessageData chatMessageModel;

    public interface Callbacks {
        void onChatMediaUploadingStart(MessageData chatMessageModel);

        void onChatMediaUploadingFailure(MessageData chatMessageModel, String message);

        void onChatMediaUploadingSuccess(MessageData chatMessageModel, ChatMedia chatMedia);

        void onChatMediaUploadingProgress(MessageData chatMessageModel, int progress);

        void onChatMediaUploadingCancelled(MessageData chatMessageModel);
    }

    public ChatMediaUploaderHelper(Context context, MessageData chatMessageModel, Callbacks callbacks) {
        this.context = context;
        this.callbacks = callbacks;
        this.chatMessageModel = chatMessageModel;
        gson = new Gson();
        chatMedia = gson.fromJson(chatMessageModel.chatMessage.getBody(), ChatMedia.class);
        this.mediaPath = chatMedia.getStoragePath();
        file = new File(mediaPath);
    }

    public void cancel() {
        isCancelled = true;
        if (mediaUploadCall != null) {
            mediaUploadCall.cancel();
        }
        callbacks.onChatMediaUploadingCancelled(chatMessageModel);
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callbacks.onChatMediaUploadingStart(chatMessageModel);
                if (enableCompression) {
                    if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
                        compressImage();
                    }
                }
                hitApiToUploadChatMedia();
            }
        }).start();
    }

    public void compressImage() {
        int[] dropBoxIMGSize = BitmapUtils.getDropBoxIMGSize(context, mediaPath);

        if (dropBoxIMGSize[0] == 0 || dropBoxIMGSize[1] == 0 ||
                dropBoxIMGSize[0] < AppConstants.Values.IMAGE_RESOLUTION_COMPRESSION * 0.75
                || dropBoxIMGSize[1] < AppConstants.Values.IMAGE_RESOLUTION_COMPRESSION * 0.75) {
            file = new File(mediaPath);
            isOriginalImageAfterCompression = true;
        } else {
            try {
                file = new Compressor.Builder(context)
                        .setMaxWidth(AppConstants.Values.IMAGE_RESOLUTION_COMPRESSION)
                        .setMaxHeight(AppConstants.Values.IMAGE_RESOLUTION_COMPRESSION)
                        .setQuality(AppConstants.Values.IMAGE_QUALITY)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(AppConstants.APP_FOLDER_IMAGE)
                        .build()
                        .compressToFile(new File(mediaPath));
                mediaPath = file.getAbsolutePath();
            } catch (Exception e) {
                file = new File(mediaPath);
                isOriginalImageAfterCompression = true;
                e.printStackTrace();
            }
        }
    }

    private void hitApiToUploadChatMedia() {

        ProgressRequestBody fileBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
//                    Log.d("varun", "onProgressUpdate " + percentage);
            }

            @Override
            public void onError() {
//                    Log.d("varun", "onError ");
            }

            @Override
            public void onFinish() {
//                    Log.d("varun", "onUploadMediaFileQueueCompleted ");
            }
        });

        String mediaType = "";

        if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
            mediaType = AppConstants.ApiParamValue.MEDIA_TYPE_IMAGE.toLowerCase();
        } else if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {
            mediaType = AppConstants.ApiParamValue.MEDIA_TYPE_AUDIO.toLowerCase();
        }
        else if (chatMessageModel.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_VIDEO)) {
            mediaType = AppConstants.ApiParamValue.MEDIA_TYPE_VIDEO.toLowerCase();
        }

        MultipartBody.Part mediaTypePart = MultipartBody.Part.createFormData(AppConstants.ApiParamKey.MEDIA_TYPE,mediaType);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(AppConstants.ApiParamKey.MEDIA, file.getName(), fileBody);
        mediaUploadCall = RestServiceFactory.createService().chatUpload(mediaTypePart,filePart);

        if (isCancelled) {
            return;
        }

        mediaUploadCall.enqueue(new RestCallBack<ResponseModel<ImageModel>>() {
            @Override
            public void onFailure(Call<ResponseModel<ImageModel>> call, String message) {
                callbacks.onChatMediaUploadingFailure(chatMessageModel, message);
            }

            @Override
            public void onResponse(Call<ResponseModel<ImageModel>> call, Response<ResponseModel<ImageModel>> restResponse, ResponseModel<ImageModel> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    if (response.data != null) {
                        chatMedia.setStoragePath(mediaPath);
                        chatMedia.setS3MediaUrl(response.data.getUrl());
                        chatMedia.setS3MediaThumbnailUrl(response.data.getUrl());
                        chatMedia.setSuccessfullyUploaded(true);
                        callbacks.onChatMediaUploadingSuccess(chatMessageModel, chatMedia);
                    }
                    EventBroadcastHelper.sendRefreshChatList();
                } else {
                    callbacks.onChatMediaUploadingFailure(chatMessageModel, response.errorMessage);
                }
            }
        });
    }
}