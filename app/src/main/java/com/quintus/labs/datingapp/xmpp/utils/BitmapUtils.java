package com.quintus.labs.datingapp.xmpp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by MyU10 on 1/11/2017.
 */

public class BitmapUtils {

    public static int[] getDropBoxIMGSize(Context context, String path) {

        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null);

//            ExifInterface exif = new ExifInterface(path);
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//            }

          //  Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            if (rotate == 0 || rotate == 180)
                return new int[]{imageWidth, imageHeight};
            else if (rotate == 90 || rotate == 270)
                return new int[]{imageHeight, imageWidth};
            else
                return new int[]{imageWidth, imageHeight};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{0, 0};
    }

    public static int[] getDropBoxIMGSize(String path) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            return new int[]{imageWidth, imageHeight};

        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{0, 0};
        }
    }

    public static boolean isLandscape(int[] imageSize) {
        if (imageSize[1] > imageSize[0]) {
            return false;
        } else
            return true;
    }

    public interface Callbacks<T> {
        void onComplete(T t);
    }

    public static Call<ResponseBody> saveChatMedia(String url, final ChatMessage model, final ChatMedia chatMedia, final Callbacks<String> callbacks) {

        Call<ResponseBody> downloadCall = RestServiceFactory.createService().downloadFile(url);
        downloadCall.enqueue(new RestCallBack<ResponseBody>() {
            @Override
            public void onFailure(Call<ResponseBody> call, String message) {
                callbacks.onComplete(null);
            }

            @Override
            public void onResponse(final Call<ResponseBody> call, Response<ResponseBody> restResponse, final ResponseBody response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String extension = chatMedia.getS3MediaUrl().substring(chatMedia.getS3MediaUrl().lastIndexOf("."));
                        File file = null;

                        if (model.getSubject().equals(com.quintus.labs.datingapp.Utils.AppConstants.Chat.TYPE_CHAT_IMAGE)) {
                            file = new File(AppConstants.APP_FOLDER_IMAGE, model.getMessageId() + "_" + model.getConversationId() + "_" + System.currentTimeMillis() + extension);

                        }
                        else if (model.getSubject().equals(com.quintus.labs.datingapp.Utils.AppConstants.Chat.TYPE_CHAT_VIDEO)) {
                            file = new File(AppConstants.APP_FOLDER_VIDEO, model.getMessageId() + "_" + model.getConversationId() + "_" + System.currentTimeMillis() + extension);

                        }
                        else if (model.getSubject().equals(com.quintus.labs.datingapp.Utils.AppConstants.Chat.TYPE_CHAT_AUDIO)) {
                            file = new File(AppConstants.APP_FOLDER_AUDIO, model.getMessageId() + "_" + model.getConversationId() + "_" + System.currentTimeMillis() + extension);

                        }


                        saveMedia(file, response, callbacks);
                    }
                }).start();
            }
        });

        return downloadCall;
    }

    public static Call<ResponseBody> saveNewsMedia(String url, final Callbacks<String> callbacks) {

        Call<ResponseBody> downloadCall = RestServiceFactory.createService().downloadFile(url);
        downloadCall.enqueue(new RestCallBack<ResponseBody>() {
            @Override
            public void onFailure(Call<ResponseBody> call, String message) {
                callbacks.onComplete(null);
            }

            @Override
            public void onResponse(final Call<ResponseBody> call, Response<ResponseBody> restResponse, final ResponseBody response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = null;
                        file = new File(AppConstants.APP_FOLDER_IMAGE, +System.currentTimeMillis() + ".png");


                        saveMedia(file, response, callbacks);
                    }
                }).start();
            }
        });

        return downloadCall;
    }

    private static void saveMedia(File file, ResponseBody response, Callbacks<String> callbacks) {
        InputStream input = response.byteStream();
        OutputStream output = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            byte[] buffer = new byte[8 * 1024]; // or other buffer size
            int read;

            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();

            if (AppConstants.checkIsFileValid(file))
                callbacks.onComplete(file.getAbsolutePath());
            else
                saveMedia(file, response, callbacks);

            MediaScannerConnection.scanFile(AppContext.getInstance().getContext(),
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap applyOverlay(Context context, Bitmap sourceImage, int overlayDrawableResourceId) {
        Bitmap bitmap = null;
        try {
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();
            Resources r = context.getResources();
            Drawable imageAsDrawable = new BitmapDrawable(r, sourceImage);
            Drawable[] layers = new Drawable[2];
            layers[0] = imageAsDrawable;
            layers[1] = new BitmapDrawable(r, BitmapUtils.decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height));
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            bitmap = BitmapUtils.drawableToBitmap(layerDrawable);
        } catch (Exception ex) {
        }
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Compute inSampleSize
//        options.inSampleSize = computeInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
