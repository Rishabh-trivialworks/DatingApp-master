package com.quintus.labs.datingapp.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class ImageUtils {


    public static void setImage(Context context, String url, int placeHolder, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            GlideApp.with(context)
                    .load(url).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeHolder)
                    .into(imageView);
        } else {
            clearImage(context, imageView);
        }
    }

    public static void setImage(Context context, String url, int placeHolder, int errorPlaceHolder, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            try{
                GlideApp.with(context)
                        .load(url).transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeHolder)
                        .apply(new RequestOptions().override(600, 200))
                        .error(errorPlaceHolder)
                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            clearImage(context, imageView);
        }
    }

    public static void setImage(Context context, String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            try{
                GlideApp.with(context)
                        .load(url).transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(new RequestOptions().override(600, 200))

                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            clearImage(context, imageView);
        }
    }
    public static void setImage(Context context, String url, CircleImageView imageView) {
        if (url != null && !url.isEmpty()) {
            try{
                GlideApp.with(context)
                        .load(url).transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(new RequestOptions().override(600, 200))

                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            clearImage(context, imageView);
        }
    }
    public static void setImage(Context context, File file, ImageView imageView) {
        if (file != null) {
            try{
                GlideApp.with(context)
                        .load(file).transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(new RequestOptions().override(600, 200))

                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            clearImage(context, imageView);
        }
    }
    public static void setImage(Context context, Uri uri, ImageView imageView) {
        if (uri != null) {
            GlideApp.with(context)
                    .load(uri).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            clearImage(context, imageView);
        }
    }

    public static void setImage(Context context, Drawable drawable, ImageView imageView) {
        if (drawable != null) {
            GlideApp.with(context)
                    .load(drawable).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            clearImage(context, imageView);
        }
    }

    public static void setImage(Context context, Bitmap bitmap, ImageView imageView) {
        if (bitmap != null) {
            GlideApp.with(context)
                    .load(bitmap).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            clearImage(context, imageView);
        }
    }


    public static void setImageDelay(final Context context, String url, final ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            GlideApp.with(context)
                    .load(url)
                    .fitCenter().transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        } else {
            clearImage(context, imageView);
        }
    }

    public static void clearImage(Context context, ImageView imageView) {
        GlideApp.with(context).clear(imageView);
    }

    public static void setImage(Context context, int url, int placeHolder, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .centerCrop().transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .placeholder(placeHolder)
                .into(imageView);
    }

    public static String getImagePathFromUri(Context context, Uri uri) {

        if (uri != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                return cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return "";
    }
}
