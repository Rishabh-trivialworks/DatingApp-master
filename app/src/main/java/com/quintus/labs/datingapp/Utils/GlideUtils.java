package com.quintus.labs.datingapp.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.quintus.labs.datingapp.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GlideUtils {


    public static void loadImage(Context context, String url, ImageView view){
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().error(R.drawable.default_man).centerCrop().placeholder(R.drawable.default_man))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       view.setImageDrawable(context.getResources().getDrawable(R.drawable.default_man));
                        LogUtils.debug(e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(view);
    }
    public static void loadImage(Context context, String url, ImageView view,int drawable ){
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().error(drawable).centerCrop().placeholder(drawable))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        view.setImageDrawable(context.getResources().getDrawable(drawable));
                        LogUtils.debug(e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade())
                .into(view);
    }
}
